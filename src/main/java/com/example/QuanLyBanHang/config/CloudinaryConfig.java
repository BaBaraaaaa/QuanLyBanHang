package com.example.QuanLyBanHang.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
//https://javawhizz.com/2023/02/uploading-and-serving-static-files-in-spring-boot-using-cloudinary
// hướng dãn sử dụng Cloudinary
@Configuration
public class CloudinaryConfig {
    private final String CLOUD_NAME =null;
    private final String API_KEY  = null;
    private final String API_SECRET =null;
    @Bean
    public Cloudinary cloudinary(){
        Map<String, String> config = new HashMap<>();
//        config.put("cloud_name",CLOUD_NAME);
        config.put("dyxo0cjnu",CLOUD_NAME);

//        config.put("api_key",API_KEY);
        config.put("494587416745948",API_KEY);
//        config.put("api_secret",API_SECRET);
        config.put("Sh6vbrgLkJ2JTV-2ZCdc3KeEK3c",API_SECRET);
        Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dyxo0cjnu",
                "api_key", "494587416745948",
                "api_secret", "Sh6vbrgLkJ2JTV-2ZCdc3KeEK3c"));
        return cloudinary;
    }
}
