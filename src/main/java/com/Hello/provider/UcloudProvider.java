package com.Hello.provider;


import cn.ucloud.ufile.UfileClient;
import cn.ucloud.ufile.api.object.ObjectConfig;
import cn.ucloud.ufile.auth.ObjectAuthorization;
import cn.ucloud.ufile.auth.UfileObjectLocalAuthorization;
import cn.ucloud.ufile.bean.PutObjectResultBean;
import cn.ucloud.ufile.exception.UfileClientException;
import cn.ucloud.ufile.exception.UfileServerException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.InputStream;
import java.util.UUID;

@Service
public class UcloudProvider {

    @Value("${ufile.public-key}")
    private String publicKey;

    @Value("${ufile.private-key}")
    private String privateKey;




    public String upload(InputStream inputStream, String mimeType, String fileName){
        // 起名字
        String generaterFileName = "";
        String[] filePaths = fileName.split("\\.");
        if (filePaths.length > 1){
            generaterFileName = UUID.randomUUID().toString()+"."+filePaths[filePaths.length - 1];
            System.out.println(generaterFileName);
        }else {
            return null;
        }


        try {
            // Bucket相关API的授权器
            ObjectAuthorization objectAuthorization = new UfileObjectLocalAuthorization(
                    publicKey, privateKey);
            // 配置
            ObjectConfig config = new ObjectConfig("cn-bj", "ufileos.com");

            PutObjectResultBean response = UfileClient.object(objectAuthorization, config)
                    .putObject(inputStream, mimeType)
                    .nameAs(generaterFileName)
                    .toBucket("aimyon")
                    .setOnProgressListener((bytesWritten, contentLength) -> {
                    })
                    .execute();
        } catch (UfileClientException e) {
            e.printStackTrace();
            return null;
        } catch (UfileServerException e) {
            e.printStackTrace();
            return null;
        }

        return "";
    }
}
