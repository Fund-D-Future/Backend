package com.funddfuture.fund_d_future.file;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
public class FileService {

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    public File uploadPublicFile(MultipartFile file) throws IOException {
        String key = UUID.randomUUID() + "-" + file.getOriginalFilename();
        String url = uploadToS3(file.getBytes(), key);

        File newFile = File.builder()
                .key(key)
                .url(url)
                .build();
        return fileRepository.save(newFile);
    }

    public void deletePublicFile(UUID fileId) {
        File file = fileRepository.findById(UUID.fromString(String.valueOf(fileId))).orElse(null);
        if (file != null) {
            deleteFromS3(file.getKey());
            fileRepository.delete(file);
        }
    }

    public File uploadGoogleUrl(String url) {
        String filename = url.substring(url.lastIndexOf('/') + 1);
        File newFile = File.builder()
                .key(filename)
                .url(url)
                .build();
        return fileRepository.save(newFile);
    }

    private String uploadToS3(byte[] data, String filename) {
        InputStream inputStream = new ByteArrayInputStream(data);
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(data.length);
        amazonS3.putObject(bucketName, filename, inputStream, metadata);
        return amazonS3.getUrl(bucketName, filename).toString();
    }

    private void deleteFromS3(String fileName) {
        amazonS3.deleteObject(new DeleteObjectRequest(bucketName, fileName));
    }
}