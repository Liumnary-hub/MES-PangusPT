# MesDemo — 离散制造执行系统（MES）后端

基于 Spring Boot 3 的 MES 后端实训项目，覆盖「工单 → 任务 → 工序 → 领料 → 库存 → 质量 → 设备 → 排产」主业务链路，提供 163 个 REST 接口。

## 技术栈

| 类别 | 选型 |
| --- | --- |
| 语言 / 运行时 | Java 17 |
| 框架 | Spring Boot 3.5.6、Spring Security |
| 持久层 | MyBatis-Plus 3.5.6、MyBatis XML、Spring Data JPA（用户/角色/登录日志） |
| 数据库 | MySQL 8 |
| 鉴权 | JWT（jjwt 0.11.5）+ RBAC |
| 对象映射 | MapStruct、BeanUtils |
| 接口文档 | Knife4j 4.5 / OpenAPI 3 |
| 构建 | Maven |

## 功能模块

| 模块 | 主要内容 |
| --- | --- |
| 生产工单 | 工单 CRUD、状态流转（draft → planned → released → in_progress → completed / cancelled）、优先级 |
| 生产任务 | 按工单生成任务、任务派工、开工/完工/暂停、进度与工时回写 |
| 工单-任务联动 | 任务进度汇总回写工单完成数量，任务状态变化驱动工单状态 |
| 工艺路线 / 工序 | 路线与工序维护、标准工时、OEE 计算 |
| BOM / 物料 | BOM 明细、物料主数据、批量导入 |
| 库存 | 库位库存、批次库存、锁定/解锁、调拨、调整、库存流水 |
| 领料 | 领料单创建、执行拣料（锁定库存）、完成出库（扣减库存）、取消释放 |
| 质量 | 检验计划/任务、缺陷代码/明细、合格率、Pareto、SPC |
| 设备 | 设备台账、点检、保养、故障记录、状态日志 |
| 排产 | 排产计划、排产明细、班次日历 |
| 系统 | 登录鉴权、用户/角色、登录日志 |

## 核心实现

### 1. 工单状态机与任务联动

`ProductionCoordinatorServiceImpl` 在任务进度更新后汇总工单下所有任务状态，推导工单状态并回写完成数量、实际起止时间，避免任务与工单状态不一致。

### 2. OEE 计算

`ProcessStepServiceImpl#calculateOEE` 按 `可用率 × 表现性 × 质量指数` 计算设备综合效率，输出各项分值与等级。

### 3. 并发安全的库存扣减

所有库存数量变更走数据库条件更新，不再使用 `selectById → set → updateById`：

```sql
-- 锁定
UPDATE mes_inventory
SET locked_quantity = locked_quantity + #{quantity}
WHERE id = #{id} AND current_quantity - locked_quantity >= #{quantity};

-- 领料出库（现存量与锁定量同时扣减）
UPDATE mes_inventory
SET current_quantity = current_quantity - #{quantity},
    locked_quantity = locked_quantity - #{quantity}
WHERE id = #{id}
  AND current_quantity >= #{quantity}
  AND locked_quantity >= #{quantity};
```

影响行数为 0 即判定库存不足，由数据库行锁保证并发下不会超卖 / 超锁。

### 4. 批次全流程追溯

`GET /api/trace/batch/{batchNo}` 以批次号为入口，串联：

```
批次 → 质量检验任务 → 生产工单 → 工序任务 → 领料明细 → 库存批次
```

返回结构见 `BatchTraceVO`，空结果返回 404。

## 快速开始

1. 创建数据库：

```sql
CREATE DATABASE mes DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
```

2. 修改 `src/main/resources/application.yml` 中的数据库账号密码。

3. 启动：

```bash
mvn spring-boot:run
```

4. 默认账号（首次启动由 `DataInitializer` 创建）：

```
用户名：admin
密码：admin123
```

5. 登录获取 Token：

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'
```

后续请求携带 `Authorization: Bearer <token>`。

6. 接口文档：

- Knife4j UI：`http://localhost:8080/doc.html`
- OpenAPI JSON：`http://localhost:8080/api-docs`

## 追溯接口示例

```bash
curl http://localhost:8080/api/trace/batch/BATCH20240101 \
  -H "Authorization: Bearer <token>"
```

返回 `BatchTraceVO`：

```json
{
  "code": 200,
  "data": {
    "batchNo": "BATCH20240101",
    "inspectionTasks": [{ "taskNo": "QT...", "batchNo": "BATCH20240101", "orderNo": "WO..." }],
    "orders": [{ "orderCode": "WO...", "status": "in_progress" }],
    "processTasks": [{ "taskCode": "TASK...", "operationSequence": 1 }],
    "pickLists": [{ "pickListCode": "PL...", "workOrderCode": "WO..." }],
    "pickedMaterials": [{ "materialCode": "M001", "batchNumber": "BATCH20240101" }],
    "inventoryLots": [{ "materialCode": "M001", "currentQuantity": 100 }]
  }
}
```

批次不存在或没有任何关联记录时返回 `code = 404`。

## 项目结构

```
src/main/java/com/mes
├── config          # 安全、Jackson、数据初始化
├── controller      # REST 接口层
├── service         # 业务接口与实现（含 BatchTraceService）
├── mapper          # MyBatis-Plus / 注解 SQL
├── repository      # Spring Data JPA（用户、角色、登录日志）
├── entity          # 数据库实体
├── dto / vo        # 入参 / 出参
├── security        # JWT 过滤器、认证入口
└── util
src/main/resources
├── application.yml
└── mapper          # MyBatis XML
```

## 说明

- 设备数据目前通过 `ProductionDataService#batchInsertData` 批量写入 `mes_production_data`，尚未接入 Modbus / OPC UA / MQTT 等工业协议适配层。
- 用户/角色/登录日志使用 JPA，业务表使用 MyBatis-Plus，复杂查询使用 XML；三种方式按历史模块划分，新增业务统一使用 MyBatis-Plus。
- 当前未引入 Redis、消息队列等中间件。
