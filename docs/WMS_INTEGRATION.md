# MES → WMS 库存集成

两个方向：

- 领料完成 → WMS 出库
- 工单完工 → WMS 入库

WMS 是库存主账；MES 本地库存是车间执行镜像。

## 代码位置

| 文件 | 作用 |
| --- | --- |
| `config/WmsProperties.java` | `mes.wms.*` 配置绑定 |
| `config/WmsRestTemplateConfig.java` | 带超时的 `RestTemplate` |
| `integration/wms/WmsInventoryClient.java` | 调用 WMS 出库/入库接口 |
| `integration/wms/WmsOutboundRequest.java` | 出库请求体 |
| `integration/wms/WmsInboundRequest.java` | 入库请求体 |
| `integration/wms/WmsApiResponse.java` | WMS 统一响应解析 |
| `service/impl/PickListServiceImpl.java` | `completePickList` 中触发领料出库 |
| `service/impl/ProductionCoordinatorServiceImpl.java` | 工单首次完成时触发完工入库 |

## 配置

```yaml
mes:
  wms:
    enabled: true
    base-url: http://localhost:8088
    api-key: mes-wms-dev-key
    finished-goods-warehouse-code: FG01
    connect-timeout-ms: 3000
    read-timeout-ms: 5000
```

环境变量：

| 变量 | 默认值 |
| --- | --- |
| `WMS_ENABLED` | `true` |
| `WMS_BASE_URL` | `http://localhost:8088` |
| `WMS_API_KEY` | `mes-wms-dev-key` |
| `WMS_FG_WAREHOUSE_CODE` | `FG01` |
| `WMS_CONNECT_TIMEOUT_MS` | `3000` |
| `WMS_READ_TIMEOUT_MS` | `5000` |

`WMS_ENABLED=false` 时领料完成只扣 MES 本地库存，不调用 WMS，便于单独跑 MES 测试。

请求头与响应：

- 请求头：`Content-Type: application/json`、`X-Integration-Key: ${mes.wms.api-key}`
- 响应体：`{code, status, msg, data}`；`code == 200` 且 `status == true` 视为成功
- 连接超时默认 3 秒，读取超时默认 5 秒

## 字段映射

| MES | WMS |
| --- | --- |
| `pick_list_detail.material_code` | `commodity.code`；为空时从物料主数据补 `material_code`，仍为空则报错 |
| `warehouse_location.warehouse_code` | `warehouse.code`；为空直接报错，`warehouseName` 仅用于展示 |
| `pick_list_detail.picked_quantity` | `quantity`，必须为整数 |
| `pick_list_detail.batch_number` | `batch_no` |
| `pick_list.pick_list_code` | `source_no` |
| `pick_list_detail.id` | `source_item_no` |
| `MES-PICKLIST-{pickListId}-ITEM-{detailId}` | 出库 `requestId`（幂等键） |
| `MES-ORDER-{orderId}-FINISHED-IN` | 入库 `requestId`（幂等键） |

MES 库位必须配置 `warehouse_code`，否则同步时抛业务异常。

完工入库映射：

| MES | WMS |
| --- | --- |
| `production_order.product_code` | `commodity.code` |
| `mes.wms.finished-goods-warehouse-code` | `warehouse.code` |
| `production_order.completed_quantity` | `quantity` |
| `production_order.order_code` | `batch_no` |
| `production_order.order_code` | `source_no` |
| `production_order.id` | `source_item_no` |

## 事务与重试

### 领料出库

- WMS 调用在 `completePickList` 的 `@Transactional` 内。任一明细调用失败 → 本地库存扣减、领料单状态全部回滚。
- WMS 已成功但 MES 回滚时，重新调用 `complete` 会带相同 `requestId`，WMS 返回幂等结果，不会重复扣减。
- 先按明细逐条同步；失败后整单重试即可，已成功的明细是幂等的。

### 完工入库

- 只在工单状态从非 `completed` 首次变为 `completed` 时触发，重复同步不会重复入库。
- WMS 调用在 `syncOrderStatus` 所在事务内；WMS 失败 → 任务完成、工单状态回滚。
- 重试完成任务会带相同 `requestId`，WMS 返回幂等结果。

## 验证

1. 启动 WMS（8088）和 MES（8080）。
2. 在 WMS 中准备商品、仓库、库存，并设置 `code` 与 MES 的 `material_code` / `warehouse_code` 一致。
3. 在 MES 创建领料单并执行拣料：

```bash
# 登录
TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}' | jq -r '.token')

# 执行拣料（示例 ID 按实际数据替换）
curl -X POST http://localhost:8080/api/picklist/execute \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"pickListId":9001,"pickerId":"U1","pickerName":"张三","executeItems":[{"pickListDetailId":9001,"pickedQuantity":5}]}'

# 完成领料，触发 WMS 出库
curl -X POST http://localhost:8080/api/picklist/9001/complete \
  -H "Authorization: Bearer $TOKEN"
```

4. 查 WMS 出库流水：

```sql
SELECT request_id, source_no, source_item_no, cid, wid, count, batch_no
FROM inventory_record
WHERE source_type = 'MES_PICK_LIST'
ORDER BY create_at DESC;
```

## 注意

- MES 本地扣减与 WMS 扣减不在同一数据库事务内，属于最终一致的同步调用；当前用「MES 回滚 + WMS 幂等重试」保证可恢复。
- WMS 库存按仓库+商品聚合，不按批次。批次仅落流水，不参与 WMS 库存计算。
- 未实现 WMS → MES 的库存回写或对账任务。
