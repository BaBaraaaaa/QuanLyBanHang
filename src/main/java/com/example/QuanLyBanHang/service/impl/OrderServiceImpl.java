package com.example.QuanLyBanHang.service.impl;


import com.example.QuanLyBanHang.entity.Order;
import com.example.QuanLyBanHang.repository.OrderRepository;
import com.example.QuanLyBanHang.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	OrderRepository orderRepository;

	@Override
	public void saveOrder(Order order) {

	}

	@Override
	public List<Order> getAllOrderByUser_Id(String id) {
		return null;
	}

	@Override
	public Order findById(int id) {
		return null;
	}

	@Override
	public List<Order> findAll() {
		return orderRepository.findAll();
	}

	@Override
	public List<Order> findTop5RecentOrder() {
		return null;
	}

	@Override
	public List<String> findTop5RecentCustomer() {
		return orderRepository.findTop5RecentCustomer();
	}

	@Override
	public Page<Order> findAll(Pageable pageable) {
		return orderRepository.findAll(pageable);
	}

	@Override
	public void deleteById(int id) {

	}

	@Override
	public List<Order> findAllByPayment_Method(String payment_Method) {
		return null;
	}

	@Override
	public List<Order> findTop5OrderByPaymentMethod(String payment_method) {
		return null;
	}
}
