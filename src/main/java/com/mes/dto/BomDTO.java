package com.mes.dto;// BOM 数据传输对象


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class BomDTO {
    private Long id;
    @NotBlank(message = "BOM编码不能为空")
    private String bomCode;

    @NotNull(message = "产品ID不能为空")
    private Long productId;

    @NotBlank(message = "产品编码不能为空")
    private String productCode;

    private String version;
    private String status;
    private LocalDateTime effectiveDate;
    private LocalDateTime expiryDate;
    // 添加 BOM 明细列表

    private List<BomDetailDTO> bomDetails;

    // 添加分页参数
    private Integer pageNum = 1;
    private Integer pageSize = 10;



}