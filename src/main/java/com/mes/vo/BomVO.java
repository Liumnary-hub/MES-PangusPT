package com.mes.vo;// BOM 视图对象


import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class BomVO {
    private Long id;
    private String bomCode;
    private Long productId;
    private String productCode;
    private String productName;
    private String version;
    private String status;
    private String statusDesc;
    private LocalDateTime effectiveDate;
    private LocalDateTime expiryDate;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String createUser;
    private String updateUser;

    // 添加物料相关字段
    private Long materialId;       // 添加
    private String materialCode;   // 添加
    private String materialName;   // 添加
    private Double currentQuantity; // 添加（用于存储需求数量）

    // 添加分页参数
    private Integer pageNum = 1;
    private Integer pageSize = 10;

    // BOM 明细列表
    private List<BomDetailVO> bomDetails;
}