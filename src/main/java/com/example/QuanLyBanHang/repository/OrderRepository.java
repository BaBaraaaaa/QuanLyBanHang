package com.example.QuanLyBanHang.repository;

import com.example.QuanLyBanHang.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Integer> {

    @Query(value="Select * From `order` o ORDER BY o.id DESC LIMIT 5;",nativeQuery = true)
    List<String> findTop5RecentCustomer();
}
