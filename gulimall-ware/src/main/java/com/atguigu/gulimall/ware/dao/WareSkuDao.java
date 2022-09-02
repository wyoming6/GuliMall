package com.atguigu.gulimall.ware.dao;

import com.atguigu.gulimall.ware.entity.WareSkuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 
 * 
 * @author catherine sun
 * @email catherine.sun20@gmail.com
 * @date 2022-07-12 13:25:19
 */
@Mapper
public interface WareSkuDao extends BaseMapper<WareSkuEntity> {
    void addStock(@Param("skuId")Long skuId, @Param("wareId") Long wareId, @Param("skuNum") Integer skuNum);
}
