package com.Hello.provider;


import cn.ucloud.ufile.UfileClient;
import cn.ucloud.ufile.api.object.ObjectConfig;
import cn.ucloud.ufile.auth.ObjectAuthorization;
import cn.ucloud.ufile.auth.UfileObjectLocalAuthorization;
import cn.ucloud.ufile.bean.PutObjectResultBean;
import cn.ucloud.ufile.exception.UfileClientException;
import cn.ucloud.ufile.exception.UfileServerException;
import com.Hello.exception.CustomizeErrorCode;
import com.Hello.exception.CustomizeException;
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

    @Value("${ufile.bucket-name}")
    private String bucketName;

    @Value("${ufile.region}")
    private String region;

    @Value("${ufile.suffix}")
    private String suffix;

    @Value("${ufile.expires}")
    private int expires;

    public String upload(InputStream inputStream, String mimeType, String fileName){
        // 起名字
        String generaterFileName = "";
        String[] filePaths = fileName.split("\\.");
        if (filePaths.length > 1){
            generaterFileName = UUID.randomUUID().toString()+"."+filePaths[filePaths.length - 1];
            System.out.println(generaterFileName);
        }else {
            throw new CustomizeException(CustomizeErrorCode.UPLOAD_FAIL);
        }


        try {
            // Bucket相关API的授权器
            ObjectAuthorization objectAuthorization = new UfileObjectLocalAuthorization(
                    publicKey, privateKey);
            // 配置
            ObjectConfig config = new ObjectConfig(region, suffix);

            PutObjectResultBean response = UfileClient.object(objectAuthorization, config)
                    .putObject(inputStream, mimeType)
                    .nameAs(generaterFileName)
                    .toBucket(bucketName)
                    .setOnProgressListener((bytesWritten, contentLength) -> {
                    })
                    .execute();
            if (response != null && response.getRetCode() == 0){
                String url = UfileClient.object(objectAuthorization, config)
                        .getDownloadUrlFromPrivateBucket(generaterFileName, bucketName, expires)
                        .createUrl();
                return url;
            }else {
                throw new CustomizeException(CustomizeErrorCode.UPLOAD_FAIL);
            }
        } catch (UfileClientException e) {
            e.printStackTrace();
            throw new CustomizeException(CustomizeErrorCode.UPLOAD_FAIL);
        } catch (UfileServerException e) {
            e.printStackTrace();
            throw new CustomizeException(CustomizeErrorCode.UPLOAD_FAIL);
        }

    }
}
