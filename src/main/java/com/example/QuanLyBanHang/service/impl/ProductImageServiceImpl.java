package com.example.QuanLyBanHang.service.impl;


import com.example.QuanLyBanHang.entity.ProductImage;
import com.example.QuanLyBanHang.repository.ProdcutImageRepository;
import com.example.QuanLyBanHang.service.ProductImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductImageServiceImpl implements ProductImageService {
	@Autowired
	ProdcutImageRepository prodcutImageRepository;

	@Override
	public void save(ProductImage productImage) {
	prodcutImageRepository.save(productImage);
	}

	@Override
	public void deleteById(int id) {
		prodcutImageRepository.deleteById(id);

	}
}
