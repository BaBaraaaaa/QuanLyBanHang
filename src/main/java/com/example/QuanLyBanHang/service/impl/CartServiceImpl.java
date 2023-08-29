package com.example.QuanLyBanHang.service.impl;

import com.example.QuanLyBanHang.entity.Cart;
import com.example.QuanLyBanHang.repository.CartRepository;
import com.example.QuanLyBanHang.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CartServiceImpl implements CartService {
    @Autowired
    CartRepository cartRepository;
    @Override
    public void deleteById(int id) {

    }

    @Override
    public List<Cart> GetAllCartByUser_id(int user_id) {
        return cartRepository.findAllByUser_id(user_id);
    }

    @Override
    public void saveCart(Cart cart) {

    }
}
