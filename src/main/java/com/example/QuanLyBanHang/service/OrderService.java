package com.example.QuanLyBanHang.service;



import com.example.QuanLyBanHang.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {

	public void saveOrder(Order order);
	
	List<Order> getAllOrderByUser_Id(String id);

	Order findById(int id);

	List<Order> findAll();

	List<Order> findTop5RecentOrder();

	List<String> findTop5RecentCustomer();

	Page<Order> findAll(Pageable pageable);

	void deleteById(int id);

	List<Order> findAllByPayment_Method(String payment_Method);

	List<Order> findTop5OrderByPaymentMethod(String payment_method);
}
