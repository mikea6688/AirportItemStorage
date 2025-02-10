package org.code.airportitemstorage.service;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Service
public class OssService {
    @Autowired
    private OSS ossClient;

    @Value("${aliyun.oss.bucketName}")
    private String bucketName;

    @Value("${aliyun.oss.endpoint}")
    private String endpoint;

    public String uploadImage(MultipartFile file) {
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        try (InputStream inputStream = file.getInputStream()) {
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileName, inputStream);
            ossClient.putObject(putObjectRequest);
            return fileName;  // 返回存储在 OSS 上的文件名，或者可以根据需要返回文件的 URL
        } catch (Exception e) {
            throw new RuntimeException("上传文件失败", e);
        }
    }

    public String getImageUrl(String fileName) {
        if(fileName == null || fileName.isEmpty())return null;

        return  "https://" + bucketName + "." + endpoint + "/" + fileName;  // 图片的URL
    }

}
