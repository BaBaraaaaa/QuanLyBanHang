package com.example.QuanLyBanHang.repository;

import com.example.QuanLyBanHang.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository  extends JpaRepository<Cart,Integer> {
    List<Cart> findAllByUser_id(int user_id);
}
