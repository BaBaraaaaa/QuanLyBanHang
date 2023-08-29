package com.example.QuanLyBanHang.service.impl;



import com.example.QuanLyBanHang.entity.Category;
import com.example.QuanLyBanHang.repository.CategoryRepository;
import com.example.QuanLyBanHang.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
	@Autowired
	CategoryRepository categoryRepository;

	@Override
	public Category saveCategory(Category category) {
		return null;
	}

	@Override
	public Category getCategoryById(int id) {
		return categoryRepository.getReferenceById(id);
	}

	@Override
	public Category updateCategory(Category category) {
		return null;
	}

	@Override
	public List<Category> findAll() {
		return categoryRepository.findAll();
	}

	@Override
	public void deleteCategoryId(int id) {

	}
}
