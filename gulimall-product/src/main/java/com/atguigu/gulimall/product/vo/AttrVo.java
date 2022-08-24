package com.atguigu.gulimall.product.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
//内部不需要写任何数据库相关的注解
@Data
public class AttrVo {

    /**
     *
     */
    private Long attrId;
    /**
     *
     */
    private String attrName;
    /**
     * 是否需要被检索（0-不需要；1-需要）
     */
    private Integer searchType;
    /**
     *
     */
    private String icon;
    /**
     *
     */
    private String valueSelect;
    /**
     *
     */
    private Integer attrType;
    /**
     *
     */
    private Long enable;
    /**
     *
     */
    private Long catelogId;
    /**
     *
     */
    private Integer showDesc;

    //分组信息
	private Long attrGroupId;
}
