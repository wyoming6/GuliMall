package com.atguigu.gulimall.product;

import com.atguigu.gulimall.product.entity.BrandEntity;
import com.atguigu.gulimall.product.service.BrandService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GulimallProductApplicationTests {
    @Autowired
    BrandService brandService;
    @Test
    public void contextLoads() {
        BrandEntity brandEntity = new BrandEntity();

        //insert data
//        brandEntity.setName("Huawei");
//        brandService.save(brandEntity);
//        System.out.println("Saved successfully.");

        //update data
//        brandEntity.setBrandId(1L); //we want to update the data whose id=1
//        brandEntity.setDescript("Huawei Technologies Canada Co., Ltd");//set the description to be...
//        brandService.updateById(brandEntity);

        List<BrandEntity> list = brandService.list(new QueryWrapper<BrandEntity>().eq("brand_id", 1L));
        list.forEach((item) -> {
            System.out.println(item);
        });
    }

}
