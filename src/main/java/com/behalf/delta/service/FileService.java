package com.behalf.delta.service;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

@Service
public class FileService {

    @Autowired
    private MinioClient minioClient;

    private final String defaultBucketName = "images";

    public void uploadFile(String name, byte[] content, String bucketName) {
        try {
            // Create temporary file
            File file = new File("/tmp/" + name);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(content);
            fos.close();

            // Get file input stream and size for upload
            FileInputStream fis = new FileInputStream(file);
            long fileSize = file.length();

            // Upload to MinIO using the correct parameters
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(name)
                            .stream(fis, fileSize, -1)
                            .contentType(getContentType(name))
                            .build()
            );

            // Close the stream and delete temp file
            fis.close();
            file.delete();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private String getContentType(String fileName) {
        // Simple content type detection based on file extension
        if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (fileName.endsWith(".png")) {
            return "image/png";
        } else if (fileName.endsWith(".gif")) {
            return "image/gif";
        } else {
            return "application/octet-stream";
        }
    }

    public byte[] getFile(String key, String bucketName) {
        try {
            InputStream obj = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(key)
                            .build()
            );
            byte[] content = IOUtils.toByteArray(obj);
            obj.close();
            return content;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public String getFileUrl(String bucketName, String fileName) {
        return String.format("https://bucket-production-9ee7.up.railway.app/%s/%s", bucketName, fileName); // Adjust URL if MinIO is running on a different host or port
    }
}
