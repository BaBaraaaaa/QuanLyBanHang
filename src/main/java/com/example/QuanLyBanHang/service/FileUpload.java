package com.example.QuanLyBanHang.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public interface FileUpload {
    String uploadFile(MultipartFile multipartFile) throws IOException;
     File FileUpload(MultipartFile multipartFile) throws IOException;
}
