package com.behalf.delta.service;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    public List<String> uploadMultiple(String bucketName, List<MultipartFile> files) {
        List<String> urls = new ArrayList<>();
        for (MultipartFile file : files) {
            try {
                String uniqueFileName = UUID.randomUUID().toString();
                uploadFile(uniqueFileName, file.getBytes(), bucketName);
                String url = getFileUrl(bucketName, uniqueFileName);
                urls.add(url);
            } catch (Exception e) {
                throw new RuntimeException("Failed to upload file: " + e.getMessage(), e);
            }
        }
        return urls;
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
        return String.format("%s/%s", bucketName, fileName); // Adjust URL if MinIO is running on a different host or port
    }
}
