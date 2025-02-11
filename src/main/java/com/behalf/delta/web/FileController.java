package com.behalf.delta.web;

import com.behalf.delta.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/document")
public class FileController {

    @Autowired
    private FileService fileService;


    @PostMapping("/{bucketName}/file")
    public ResponseEntity<String> uploadFile(
            @PathVariable String bucketName,
            @RequestParam("file") MultipartFile file) {
        try {
            String uniqueFileName = UUID.randomUUID().toString();
            fileService.uploadFile(uniqueFileName, file.getBytes(), bucketName);
            String fileUrl = fileService.getFileUrl(bucketName, uniqueFileName);
            return ResponseEntity.ok(fileUrl);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload file: " + e.getMessage());
        }
    }


    @GetMapping("/{bucketName}/file/{fileName}")
    public ResponseEntity<byte[]> downloadFile(
            @PathVariable String bucketName,
            @PathVariable String fileName) {
        byte[] data = fileService.getFile(fileName, bucketName);
        if (data != null) {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + fileName + "\"")
                    .body(data);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
