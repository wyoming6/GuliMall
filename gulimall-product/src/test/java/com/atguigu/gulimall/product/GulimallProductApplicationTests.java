package com.atguigu.gulimall.product;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.atguigu.gulimall.product.entity.BrandEntity;
import com.atguigu.gulimall.product.service.BrandService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GulimallProductApplicationTests {
    @Autowired
    BrandService brandService;

    @Autowired
    OSS ossClient;

    @Test
    public void testUpload() throws FileNotFoundException {
        String endpoint = "oss-us-west-1.aliyuncs.com";
        String accessKeyId = "LTAI5tK9tVP3BHjM2VaaqSzB";
        String accessKeySecret = "I4EEPKUiCKPYuqjKNfD3LaynbUu3cD";

        String bucketName = "gulimall-dev-1";
        String objectName = "banff.jpeg";

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        InputStream inputStream = new FileInputStream("/Users/catherine_sun/Pictures/banff.jpeg");
        // 创建PutObject请求。
        ossClient.putObject(bucketName, objectName, inputStream);
        ossClient.shutdown();
        System.out.println("File uploaded.");


    }
    @Test
    public void testUpload1() throws FileNotFoundException {
        String bucketName = "gulimall-dev-1";
        String objectName = "Meng5.jpg";

        InputStream inputStream = new FileInputStream("/Users/catherine_sun/Pictures/Meng1.jpg");
        // 创建PutObject请求。
        ossClient.putObject(bucketName, objectName, inputStream);
        ossClient.shutdown();
        System.out.println("File uploaded.");
    }

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
