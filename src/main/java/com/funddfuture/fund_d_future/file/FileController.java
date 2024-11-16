package com.funddfuture.fund_d_future.file;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/files")
public class FileController {
    @Autowired
    private FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<File> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        File uploadedFile = fileService.uploadPublicFile(file);
        return ResponseEntity.ok(uploadedFile);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteFile(@PathVariable String id) {
        fileService.deletePublicFile(UUID.fromString(id));
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/upload-url")
    public ResponseEntity<File> uploadGoogleUrl(@RequestParam String url) {
        File uploadedFile = fileService.uploadGoogleUrl(url);
        return ResponseEntity.ok(uploadedFile);
    }
}