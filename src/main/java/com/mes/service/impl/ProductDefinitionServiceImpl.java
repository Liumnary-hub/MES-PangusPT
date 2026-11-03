package com.mes.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mes.entity.ProductDefinition;
import com.mes.mapper.ProductDefinitionMapper;
import com.mes.service.ProductDefinitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductDefinitionServiceImpl extends ServiceImpl<ProductDefinitionMapper, ProductDefinition>  implements ProductDefinitionService {

    @Autowired
    private ProductDefinitionMapper productMapper;

    @Override
    public IPage<ProductDefinition> getProductsByCondition(
            Page<ProductDefinition> page,
            String productCode,
            String productName,
            String productType,
            String status
    ) {
        return productMapper.selectByCondition(page,productCode,productName,productType,status);
    }

    @Override
    public boolean saveProduct(ProductDefinition product) {

        int count =productMapper.countByProductCode(product.getProductCode());
        if(count>0){
            throw new RuntimeException("产品编码已经存在");
        }
        return this.save(product);
    }



    @Override
    public boolean updateProduct(ProductDefinition product) {
        ProductDefinition existing = this.getById(product.getId());
        if(existing !=null && !existing.getProductCode().equals(product.getProductCode()))
        {
            int count = productMapper.countByProductCode(product.getProductCode());
            if (count > 0) {
                throw new RuntimeException("产品编码已存在");
            }
        }


        return this.updateById(product);
    }

    @Override
    public boolean toggleStatus(Long id, String status) {
        ProductDefinition product = this.getById(id);
        if (product == null) {
            throw new RuntimeException("产品不存在");
        }
        product.setStatus(status);
        return this.updateById(product);
    }


}
