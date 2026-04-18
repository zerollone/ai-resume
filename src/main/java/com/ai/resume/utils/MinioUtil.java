package com.ai.resume.utils;

import com.ai.resume.config.properties.MinioProperties;
import com.ai.resume.exception.CommonException;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * @author ws
 * @date 2026/4/18 14:48
 */
@Component
public class MinioUtil {

    @Autowired
    private MinioClient minioClient;

    @Autowired
    private MinioProperties minioProperties;

    public String uploadFile(MultipartFile file, String filename) {
        return uploadFile(file, minioProperties.getBucketName(), filename);
    }

    public String uploadFile(MultipartFile file, String bucketName, String filename) {
        if (file == null || file.isEmpty()) {
            throw new CommonException("文件不能为空");
        }

        try {
            createBucketIfNotExists(bucketName);
            // 获取文件输入流
            try (InputStream inputStream = file.getInputStream()) {
                PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(filename)
                        .stream(inputStream, file.getSize(), -1)
                        .contentType(file.getContentType())
                        .build();

                minioClient.putObject(putObjectArgs);

                return getFileUrl(bucketName, filename);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new CommonException("文件上传失败");
        }
    }

    /**
     * 创建桶（如果不存在）
     */
    private void createBucketIfNotExists(String bucketName) throws Exception {
        boolean found = minioClient.bucketExists(
                BucketExistsArgs.builder().bucket(bucketName).build()
        );

        if (!found) {
            minioClient.makeBucket(
                    MakeBucketArgs.builder().bucket(bucketName).build()
            );
        }
    }

    /**
     * 获取文件访问URL
     */
    private String getFileUrl(String bucketName, String objectName) {
        return String.format("%s/%s", bucketName, objectName);
    }
}
