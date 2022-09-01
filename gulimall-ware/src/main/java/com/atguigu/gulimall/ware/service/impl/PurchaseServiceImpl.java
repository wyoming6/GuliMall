package com.atguigu.gulimall.ware.service.impl;

import com.atguigu.common.constant.WareConstant;
import com.atguigu.gulimall.ware.entity.PurchaseDetailEntity;
import com.atguigu.gulimall.ware.service.PurchaseDetailService;
import com.atguigu.gulimall.ware.vo.MergeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.common.utils.PageUtils;
import com.atguigu.common.utils.Query;

import com.atguigu.gulimall.ware.dao.PurchaseDao;
import com.atguigu.gulimall.ware.entity.PurchaseEntity;
import com.atguigu.gulimall.ware.service.PurchaseService;
import org.springframework.transaction.annotation.Transactional;


@Service("purchaseService")
public class PurchaseServiceImpl extends ServiceImpl<PurchaseDao, PurchaseEntity> implements PurchaseService {
    @Autowired
    PurchaseDetailService purchaseDetailService;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<PurchaseEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageUnreceivePurchase(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<PurchaseEntity>().eq("status",0).or().eq("status",1)
        );

        return new PageUtils(page);
    }
    @Transactional
    @Override
    public void mergePurchase(MergeVo mergeVo) {
        Long purchaseId = mergeVo.getPurchaseId();
        if (purchaseId == null) {
            PurchaseEntity entity = new PurchaseEntity();
            entity.setCreateTime(new Date());
            entity.setUpdateTime(new Date());
            entity.setStatus(WareConstant.PurchaseStatusEnum.CREATED.getCode());
            this.save(entity);
            purchaseId = entity.getId();
        }
        List<Long> items = mergeVo.getItems();
        Long finalPurchaseId = purchaseId;
        List<PurchaseDetailEntity> collect = items.stream().map(item -> {
            PurchaseDetailEntity purchaseDetailEntity = new PurchaseDetailEntity();
            purchaseDetailEntity.setId(item);
            purchaseDetailEntity.setPurchaseId(finalPurchaseId);
            purchaseDetailEntity.setStatus(WareConstant.PurchaseDetailStatusEnum.ASSIGNED.getCode());
            return purchaseDetailEntity;
        }).collect(Collectors.toList());
        purchaseDetailService.updateBatchById(collect);

        PurchaseEntity purchaseEntity = new PurchaseEntity();
        purchaseEntity.setId(purchaseId);
        purchaseEntity.setUpdateTime(new Date());
        this.updateById(purchaseEntity);
    }

    //receive purchase order
    @Override
    public void received(List<Long> ids) {
        //更新采购单的状态及时间
        List<PurchaseEntity> collect = ids.stream().map(id -> {
            PurchaseEntity byId = this.getById(id);
            return byId;
        }).filter(entity -> {
            return entity.getStatus() == WareConstant.PurchaseStatusEnum.CREATED.getCode() || entity.getStatus() == WareConstant.PurchaseStatusEnum.ASSIGNED.getCode();
        }).map(entity -> {
            entity.setStatus(WareConstant.PurchaseStatusEnum.RECEIVED.getCode());
            entity.setUpdateTime(new Date());
            return entity;
        }).collect(Collectors.toList());

        this.updateBatchById(collect);
        //更新采购需求的状态
        collect.forEach((item) -> {
            List<PurchaseDetailEntity> detailEntities = purchaseDetailService.list(new QueryWrapper<PurchaseDetailEntity>().eq("purchase_id", item.getId()));
            List<PurchaseDetailEntity> newDetailEntities = detailEntities.stream().map(entity -> {
                entity.setStatus(WareConstant.PurchaseDetailStatusEnum.BUYING.getCode());
                return entity;
            }).collect(Collectors.toList());
            purchaseDetailService.updateBatchById(newDetailEntities);
        });

    }

}