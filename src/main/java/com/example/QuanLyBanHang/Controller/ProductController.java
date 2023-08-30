package com.example.QuanLyBanHang.Controller;


import com.example.QuanLyBanHang.entity.Cart;
import com.example.QuanLyBanHang.entity.Category;
import com.example.QuanLyBanHang.entity.Product;
import com.example.QuanLyBanHang.entity.User;
import com.example.QuanLyBanHang.repository.ProductRepository;
import com.example.QuanLyBanHang.service.CartService;
import com.example.QuanLyBanHang.service.CategoryService;
import com.example.QuanLyBanHang.service.ProductService;
import com.example.QuanLyBanHang.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class ProductController {
    @Autowired
    ProductService productService;

    @Autowired
    UserService userService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CartService cartService;
    @Autowired
    HttpSession session;

    @GetMapping(value = {"","/home"})
    public String listProduct(Model model)
    {

        String error_momo = (String) session.getAttribute("error_momo");
        String NoSignIn = (String) session.getAttribute("NoSignIn");
        session.setAttribute("NoSignIn", null);
        session.setAttribute("error_momo", null);

        model.addAttribute("error_momo", error_momo);
        model.addAttribute("NoSignIn", NoSignIn);


        System.out.println(NoSignIn);
        User acc = (User) session.getAttribute("acc");
        if (acc!= null)
        {
            List<Cart> listCart = cartService.GetAllCartByUser_id(acc.getId());
            session.setAttribute("countCart", listCart.size());
            session.setAttribute("AddToCartErr", null);

        }
        if (session.getAttribute("acc") == null)
        {
            session.setAttribute("countCart", 0);
        }
        //trả về 12 best Seller
        List<Product> Top12ProductBestSellers = productService.findTop12ProductBestSellers();
        model.addAttribute("Top12ProductBestSellers", Top12ProductBestSellers);

        //trả về 12 sản phẩm mới
        List<Product> Top12ProductNewArrivals = productService.findTop12ProductNewArrivals();

        model.addAttribute("Top12ProductNewArrivals", Top12ProductNewArrivals);
        return "index";
    }
    @GetMapping("/shop")
    public String shop(Model model) throws Exception {
        List<Product> productList = productService.getAllProduct();
        int TotalPro = productList.size();
        model.addAttribute("TotalPro",TotalPro);
        // mỗi trang 12 sản phẩm
        Pageable pageable = PageRequest.of(0, 12);
        Page<Product> page = productRepository.findAll(pageable);
        List<Category> listCategory = categoryService.findAll();
        String search_input = (String) session.getAttribute("search_input");
        model.addAttribute("listProduct", page);
        model.addAttribute("listCategory", listCategory);
        model.addAttribute("search_input", search_input);
        return "shop";
    }
    @GetMapping("/shop/{id}")
    public String shopPage(Model model, @PathVariable int id)
    {
        List<Product> productList = productService.getAllProduct();
        int ToTalProduct = productList.size();
        model.addAttribute("TotalPro",ToTalProduct);
        Pageable pageable = PageRequest.of(id,12);
        Page<Product> page = productRepository.findAll(pageable);
        model.addAttribute("listProduct",page);
        List<Category> listCategory = categoryService.findAll();
        String search_input = (String) session.getAttribute("search_input");
        User user = (User) session.getAttribute("acc");
        if (user!= null)
        {
            model.addAttribute("user_Name",user.getUser_Name());
        }
        if (listCategory != null)
        {
            model.addAttribute("listCategory",listCategory);
        }
        else
        {
            model.addAttribute("listCategory",null);
        }
        model.addAttribute("search_input",search_input);
        return "shop";
    }
    @GetMapping("/productDetail/{id}")
    public String ProductDetailId(@PathVariable int id, Model model) {
        Product product = productService.getProductById(id);
        if(product !=null){
            List<Product> relatedProduct = productService.findTop4ProductByCategory_id(product.getCategory().getId());
            model.addAttribute("relatedProduct", relatedProduct);
            model.addAttribute(product);
            return "shop-details";
        }
        else {
            return "redirect:/home";
        }


    }
    @PostMapping("/search")
    public String Search(@ModelAttribute("search-input") String search_input, Model model) throws Exception {
        session.setAttribute("search_input", search_input);
        return "redirect:/search/0";
    }


}
