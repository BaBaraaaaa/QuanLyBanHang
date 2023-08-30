package com.example.QuanLyBanHang.Controller;

import com.example.QuanLyBanHang.entity.Cart;
import com.example.QuanLyBanHang.entity.Product;
import com.example.QuanLyBanHang.entity.User;
import com.example.QuanLyBanHang.service.CartService;
import com.example.QuanLyBanHang.service.ProductService;
import com.example.QuanLyBanHang.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class CartController {
    @Autowired
    CartService cartService;
    @Autowired
    ProductService productService;
    @Autowired
    UserService userService;
    @Autowired
    HttpSession session;
    @GetMapping("/cart")
    public String CartView(Model model)
    {
        User user = (User) session.getAttribute("acc");
        if (user == null)
        {
            session.setAttribute("NoSignIn" ,"Vui lòng Đăng nhập trước !");
            return "redirect:/home";
        }
        else
        {
            List<Cart> list = cartService.GetAllCartByUser_id(user.getId());
            int ToTal = 0;
            for (Cart cart : list )
            {
                ToTal += cart.getCount() + cart.getProduct().getPrice();
            }

            if (list.size() != 0)
            {
                model.addAttribute("Total",ToTal);
                model.addAttribute("listCart",list);
                session.setAttribute("listCart",list);
                session.setAttribute("Total",ToTal);
            }
            return  "shopping-cart";
        }
    }
    @GetMapping("/addToCart/{id}")
    public String AddToCart(@PathVariable int id, Model model, HttpServletRequest request)
    {
        String referer = request.getHeader("Referer");
        User user = (User) session.getAttribute("acc");
        if (user == null)
        {
            session.setAttribute("AddToCartErr","Vui lòng đăng nhập trước khi thực hiện");
            return "redirect:" + referer;

        }
        else
        {
            List<Cart>list = userService.getUserById(user.getId()).getCart();
            Product product = productService.getProductById(id);
            int cartCount = 0;
            for (Cart cart : list)
            {
                if (cart.getProduct().getId() == id)
                {
                    cartCount++;
                }
            }
            if (cartCount >0)
            {
                for (Cart cart : list)
                {
                    if (cart.getProduct().getId() == id)
                    {
                        cart.setCount(cart.getCount()+ 1);
                        cartService.saveCart(cart);
                    }
                }
            }
            else
            {
                Cart newCart = new Cart();
                newCart.setCount(1);
                newCart.setProduct(product);
                newCart.setUser(user);
                cartService.saveCart(newCart);
            }
            list =cartService.GetAllCartByUser_id(user.getId());
            session.setAttribute("countCart",list.size());
            return "redirect:" +referer;

        }

    }
    @PostMapping("/addToCart")
    public String AddToCartPost(@ModelAttribute("product_id") int product_id,@ModelAttribute("count") int a,
                                Model model, HttpServletRequest request)
    {
        int count = a;
        String referer = request.getHeader("Referer");
        User user = (User) session.getAttribute("acc");
        if (user == null)
        {
            session.setAttribute("AddToCartErr","Vui lòng đăng nhập trước khi thực hiện");
            return "redirect:"+ referer;
        }
        else
        {
            List<Cart>list = userService.getUserById(user.getId()).getCart();
            Product product = productService.getProductById(product_id);
            int Countcart = 0;
            for (Cart cart: list)
            {
                if (cart.getProduct().getId() == product_id)
                {
                    cart.setCount(cart.getCount() + count);
                    cartService.saveCart(cart);
                    Countcart++;
                }
            }
            if (Countcart == 0 )
            {
                Cart newCart = new Cart();
                newCart.setCount(Countcart);
                newCart.setProduct(product);
                newCart.setUser(user);
                cartService.saveCart(newCart);
            }
            list = cartService.GetAllCartByUser_id(user.getId());
            session.setAttribute("countCart",list.size());
            return  "redirect:" +referer;
        }
    }
    @GetMapping("/deleteCart/{id}")
    public String GetDeleteCart(@PathVariable int id, Model model, HttpServletRequest request) throws Exception {
        String referer = request.getHeader("Referer");
        User user = (User) session.getAttribute("acc");
        if (user == null) {
            return "redirect:" +referer;
        } else {
            System.out.println(id);
            cartService.deleteById(id);
            session.setAttribute("CartDelete", id);
            List<Cart> listCart = cartService.GetAllCartByUser_id(user.getId());
            session.setAttribute("countCart", listCart.size());
            return "redirect:/cart";
        }
    }
    @PostMapping("/updateCart")
    public String UpdateCart(HttpServletRequest request, Model model) throws Exception {
        @SuppressWarnings("unchecked")
        List<Cart> listCart = (List<Cart>) session.getAttribute("listCart");
        int i = 0;
        for (Cart o : listCart) {

            String a = request.getParameter("count" + i);
            int count = Integer.parseInt(a);
            System.out.println(count);
            o.setCount(count);
            i++;
        }
        for (Cart o : listCart) {
            cartService.saveCart(o);
        }
        return "redirect:/cart";
    }
}
