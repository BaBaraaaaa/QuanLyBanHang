package com.example.QuanLyBanHang.service;


import com.example.QuanLyBanHang.entity.ProductImage;

public interface ProductImageService {

	void save(ProductImage productImage);

	void deleteById(int id);

}
