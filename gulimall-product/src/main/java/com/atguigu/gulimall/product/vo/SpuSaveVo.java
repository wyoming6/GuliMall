/**
  * Copyright 2022 bejson.com 
  */
package com.atguigu.gulimall.product.vo;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Auto-generated: 2022-08-27 14:22:50
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
@Data
public class SpuSaveVo {
    //SPU基本信息，对应pms_spu_info
    private String spuName;
    private String spuDescription;
    private Long catalogId;
    private Long brandId;
    private BigDecimal weight;
    private int publishStatus;
    private Bounds bounds;

    //SPU商品描述（图），对应pms_spu_info_desc
    private List<String> decript;

    //SPU图片集，对应pms_spu_images
    private List<String> images;

    //SPU的规格参数（基本属性），对应pms_product_attr_value
    private List<BaseAttrs> baseAttrs;

    //保存当前SPU对应的所有SKU信息，对应pms_sku_info, pms_sku_images, pms_sku_sale_attr_value
    private List<Skus> skus;

}