package com.example.QuanLyBanHang.repository;

import com.example.QuanLyBanHang.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    // lấy product bán chạy nhất
    @Query(value = "Select * from product p Order by p.quantity Desc limit 12" , nativeQuery = true)
    List<Product> findTop12ProductBestSellers();
    //định nghĩa lấy tất cả trong csdl có tên và có category
    @Query(value="select * from `QLStore`.product where `QLStore`.product.product_name like %?1% and `QLStore`.product.category_id= ?2",nativeQuery = true)
    Page<Product> findByProduct_NameAndCategory_idContaining(String name, int category_id, Pageable pageable);

    //lấy product có ngày mới nhất
    @Query(value="Select * From product p ORDER BY p.created_at DESC LIMIT 12",nativeQuery = true)
    List<Product> findTop12ProductNewArrivals();

}
