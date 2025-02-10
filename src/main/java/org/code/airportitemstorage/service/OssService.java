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
            return fileName;  // ���ش洢�� OSS �ϵ��ļ��������߿��Ը�����Ҫ�����ļ��� URL
        } catch (Exception e) {
            throw new RuntimeException("�ϴ��ļ�ʧ��", e);
        }
    }

    public String getImageUrl(String fileName) {
        if(fileName == null || fileName.isEmpty())return null;

        return  "https://" + bucketName + "." + endpoint + "/" + fileName;  // ͼƬ��URL
    }

}
