package com.kenProject.oss.utils;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@SuppressWarnings({"all"})
public class ConstantPropertiesUtils implements InitializingBean {
    //读取配置文件的内容
    @Value("${aliyun.oss.file.endpoint}")
    private String endpoint;

    @Value("${aliyun.oss.file.keyid}")
    private String keyId;

    @Value("${aliyun.oss.file.keysecret}")
    private String keySecret;

    @Value("${aliyun.oss.file.bucketname}")
    private String bucketName;


    public static String END_POINT;
    public static String ACCES_KEY_ID;
    public static String ACCES_KEY_SECRET;
    public static String BUCKET_NAME;

    @Override
    public void afterPropertiesSet() throws Exception {
        END_POINT=endpoint;
        ACCES_KEY_ID=keyId;
        ACCES_KEY_SECRET=keySecret;
        BUCKET_NAME=bucketName;

    }
}
