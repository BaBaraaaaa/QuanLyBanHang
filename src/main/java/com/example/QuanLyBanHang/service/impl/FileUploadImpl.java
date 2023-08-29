package com.example.QuanLyBanHang.service.impl;

import com.cloudinary.Cloudinary;
import com.example.QuanLyBanHang.service.FileUpload;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
@Service
@RequiredArgsConstructor
public class FileUploadImpl implements FileUpload {
    private final  Cloudinary cloudinary;
    @Override
    public File FileUpload(MultipartFile multipartFile) throws IOException {
        File converFile = new File(multipartFile.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(converFile);
        fos.write(multipartFile.getBytes());
        fos.close();
        return converFile;


    }
    @Override
    public String uploadFile(MultipartFile multipartFile) throws IOException {

        return cloudinary.uploader().upload(multipartFile.getBytes(),
                Map.of("public_id", UUID.randomUUID().toString())).get("url").toString();
    }

}
