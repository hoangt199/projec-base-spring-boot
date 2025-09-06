package com.example.file.service;

import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * Service for interacting with MinIO storage
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MinioService {

    private final MinioClient minioClient;

    @Value("${minio.bucket-name}")
    private String bucketName;

    /**
     * Initialize MinIO bucket if it doesn't exist
     */
    public void initializeBucket() {
        try {
            boolean bucketExists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!bucketExists) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
                log.info("Created MinIO bucket: {}", bucketName);
            }
        } catch (Exception e) {
            log.error("Error initializing MinIO bucket: {}", e.getMessage(), e);
            throw new RuntimeException("Error initializing MinIO bucket", e);
        }
    }

    /**
     * Upload a file to MinIO
     *
     * @param file the file to upload
     * @param objectName the object name in MinIO
     * @return the object name
     */
    public String uploadFile(MultipartFile file, String objectName) {
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build());
            return objectName;
        } catch (Exception e) {
            log.error("Error uploading file to MinIO: {}", e.getMessage(), e);
            throw new RuntimeException("Error uploading file to MinIO", e);
        }
    }

    /**
     * Upload a byte array to MinIO
     *
     * @param data the byte array to upload
     * @param objectName the object name in MinIO
     * @param contentType the content type
     * @return the object name
     */
    public String uploadBytes(byte[] data, String objectName, String contentType) {
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(data);
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(bais, data.length, -1)
                            .contentType(contentType)
                            .build());
            return objectName;
        } catch (Exception e) {
            log.error("Error uploading bytes to MinIO: {}", e.getMessage(), e);
            throw new RuntimeException("Error uploading bytes to MinIO", e);
        }
    }

    /**
     * Download a file from MinIO
     *
     * @param objectName the object name in MinIO
     * @return the input stream
     */
    public InputStream downloadFile(String objectName) {
        try {
            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build());
        } catch (Exception e) {
            log.error("Error downloading file from MinIO: {}", e.getMessage(), e);
            throw new RuntimeException("Error downloading file from MinIO", e);
        }
    }

    /**
     * Delete a file from MinIO
     *
     * @param objectName the object name in MinIO
     */
    public void deleteFile(String objectName) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build());
        } catch (Exception e) {
            log.error("Error deleting file from MinIO: {}", e.getMessage(), e);
            throw new RuntimeException("Error deleting file from MinIO", e);
        }
    }

    /**
     * Delete multiple files from MinIO
     *
     * @param objectNames the object names in MinIO
     */
    public void deleteFiles(List<String> objectNames) {
        try {
            for (String objectName : objectNames) {
                minioClient.removeObject(
                        RemoveObjectArgs.builder()
                                .bucket(bucketName)
                                .object(objectName)
                                .build());
            }
        } catch (Exception e) {
            log.error("Error deleting files from MinIO: {}", e.getMessage(), e);
            throw new RuntimeException("Error deleting files from MinIO", e);
        }
    }

    /**
     * List files in a directory
     *
     * @param prefix the directory prefix
     * @return list of object names
     */
    public List<String> listFiles(String prefix) {
        List<String> objects = new ArrayList<>();
        try {
            Iterable<Result<Item>> results = minioClient.listObjects(
                    ListObjectsArgs.builder()
                            .bucket(bucketName)
                            .prefix(prefix)
                            .recursive(true)
                            .build());

            for (Result<Item> result : results) {
                Item item = result.get();
                objects.add(item.objectName());
            }
            return objects;
        } catch (Exception e) {
            log.error("Error listing files from MinIO: {}", e.getMessage(), e);
            throw new RuntimeException("Error listing files from MinIO", e);
        }
    }

    /**
     * Check if a file exists in MinIO
     *
     * @param objectName the object name in MinIO
     * @return true if the file exists
     */
    public boolean fileExists(String objectName) {
        try {
            minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build());
            return true;
        } catch (ErrorResponseException e) {
            if (e.errorResponse().code().equals("NoSuchKey")) {
                return false;
            }
            log.error("Error checking if file exists in MinIO: {}", e.getMessage(), e);
            throw new RuntimeException("Error checking if file exists in MinIO", e);
        } catch (Exception e) {
            log.error("Error checking if file exists in MinIO: {}", e.getMessage(), e);
            throw new RuntimeException("Error checking if file exists in MinIO", e);
        }
    }
}