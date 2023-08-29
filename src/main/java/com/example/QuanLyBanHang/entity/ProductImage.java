package com.example.QuanLyBanHang.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data // lombok giúp generate các hàm constructor, get, set v.v.
@AllArgsConstructor
@NoArgsConstructor
public class ProductImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "url_Image", columnDefinition = "nvarchar(1111)")
    private String url_Image;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
