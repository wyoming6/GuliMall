package com.atguigu.gulimall.thirdparty;


import com.aliyun.oss.OSSClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GulimallThirdPartyApplicationTests {
    @Autowired
    OSSClient ossClient;

    @Test
    public void contextLoads() {
    }

    @Test
    public void testUpload() throws FileNotFoundException {
        String bucketName = "gulimall-dev-1";
        String objectName = "Meng2.jpg";

        InputStream inputStream = new FileInputStream("/Users/catherine_sun/Pictures/Meng1.jpg");
        // 创建PutObject请求。
        ossClient.putObject(bucketName, objectName, inputStream);
        ossClient.shutdown();
        System.out.println("File uploaded.");
    }

}
