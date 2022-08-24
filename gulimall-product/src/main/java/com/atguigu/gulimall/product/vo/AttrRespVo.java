package com.atguigu.gulimall.product.vo;

import lombok.Data;

@Data
public class AttrRespVo extends AttrVo{
    /**
     * catelogName: 例如 手机/数码/手机
     * groupName: 例如 主体
     */
    private String catelogName;
    private String groupName;

    private Long[] catelogPath;
}
