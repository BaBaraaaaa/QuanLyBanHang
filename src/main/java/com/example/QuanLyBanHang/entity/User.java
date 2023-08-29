package com.example.QuanLyBanHang.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data // lombok giúp generate các hàm constructor, get, set v.v.
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "`user`")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "login_Type", columnDefinition = "nvarchar(250)")
    private String login_Type;

    @Column(name = "role", columnDefinition = "nvarchar(250)")
    private String role;

    @Column(name = "password",columnDefinition = "nvarchar(250)")
    private String password;

    @Column(name = "user_Name", columnDefinition = "nvarchar(250)")
    private String user_Name;

    @Column(name = "avatar", columnDefinition = "nvarchar(250)")
    private String avatar;

    @Column(name = "email", columnDefinition = "nvarchar(250)")
    private String email;

    @Column(name = "phone_Number", columnDefinition = "nvarchar(250)")
    private String phone_Number;

    @Column(name = "full_name" ,columnDefinition = "nvarchar(250)" , nullable = false)
    private  String full_name;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Order> order;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Cart> cart;
}
