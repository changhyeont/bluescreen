package com.example.bluescreen.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileService {
    private final String uploadDir = "uploads";

    public FileService() {
        // 업로드 디렉토리 생성
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    public String saveFile(MultipartFile file) {
        try {
            // 파일 확장자 추출
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            
            // 고유한 파일명 생성
            String filename = UUID.randomUUID().toString() + extension;
            
            // 파일 저장 경로 생성
            Path filePath = Paths.get(uploadDir, filename);
            
            // 파일 저장
            Files.copy(file.getInputStream(), filePath);
            
            // 저장된 파일의 URL 반환
            return "/uploads/" + filename;
        } catch (IOException e) {
            throw new RuntimeException("파일 저장에 실패했습니다.", e);
        }
    }

    public void deleteFile(String fileUrl) {
        if (fileUrl != null && !fileUrl.isEmpty()) {
            try {
                // URL에서 파일명 추출
                String filename = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
                
                // 파일 삭제
                Path filePath = Paths.get(uploadDir, filename);
                Files.deleteIfExists(filePath);
            } catch (IOException e) {
                throw new RuntimeException("파일 삭제에 실패했습니다.", e);
            }
        }
    }
} 