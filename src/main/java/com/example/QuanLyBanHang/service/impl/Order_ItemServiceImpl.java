package com.example.QuanLyBanHang.service.impl;


import com.example.QuanLyBanHang.entity.Order_Item;
import com.example.QuanLyBanHang.repository.Order_ItemRepository;
import com.example.QuanLyBanHang.service.Order_ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Order_ItemServiceImpl implements Order_ItemService {

	@Autowired
	Order_ItemRepository order_ItemRepository;

	@Override
	public List<Order_Item> getAllByOrder_Id(int id) {
		return null;
	}

	@Override
	public void saveOrder_Item(Order_Item order_Item) {

	}

	@Override
	public void deleteById(int id) {

	}
}
