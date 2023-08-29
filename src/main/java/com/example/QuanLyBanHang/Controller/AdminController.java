package com.example.QuanLyBanHang.Controller;

import com.cloudinary.Cloudinary;
import com.example.QuanLyBanHang.entity.*;
import com.example.QuanLyBanHang.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Controller
public class AdminController {
    @Autowired
    OrderService orderService;

    @Autowired
    UserService userService;

    @Autowired
    ProductService productService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    Order_ItemService order_ItemService;

    @Autowired
    ProductImageService productImageService;

    @Autowired
    HttpSession session;
    @Autowired
    FileUpload cloudinary;

    @GetMapping("signin-admin")
    public String SignInAdminView(Model model) {
        // lấy thông tin lỗi từ session
        String err_sign_admin = (String) session.getAttribute("err_sign_admin");
        // trả lỗi về  err_sign_admin
        model.addAttribute("err_sign_admin", err_sign_admin);
        // xóa thông tin lỗi khỏi session để tránh hiển thị lại lỗi ở lần truy cập tiếp theo
        session.setAttribute("err_sign_admin", null);
        return "signin-admin";
    }

    @PostMapping(value = "signin-admin")
    public String SignInAdminView(@ModelAttribute("login_username") String user_Name,
                                  @ModelAttribute("login_password") String password, Model model)
    {
        List<User> list = userService.getAllUser();
        if (list == null)
        {
            session.setAttribute("err_sign_admin", "Bạn không có quyền truy cập !");
            return "redirect:signin-admin";
        }
        for (User user : list)
        {

            if (!user.getUser_Name().equals(user_Name))
            {
                System.out.println(user.getUser_Name() + user.getRole()
                 + user.getFull_name() +user.getPassword());
                session.setAttribute("err_sign_admin", "Bạn không có quyền truy cập !");
                return "redirect:signin-admin";
            }
            else
            {
                String decodedValue = new String(Base64.getDecoder().decode(user.getPassword()));
                if (!decodedValue.equals(password))
                {
                    session.setAttribute("err_sign_admin", "User Name or Password is not correct!");
                    return "redirect:signin-admin";
                } else
                {
                    //session tạm thời lưu thông tin người dùng là quản trị viên
                    session.setAttribute("admin", user);
                    return "redirect:dashboard";



                }
            }
        }
        return "redirect:signin-admin";
    }
    @GetMapping("dashboard")
    public String DashboardView(Model model) {
        User admin = (User) session.getAttribute("admin");
        if (admin == null) {
            return "redirect:/signin-admin";
        } else {
            List<Order> listOrder = orderService.findAll();
            List<Product> listProduct = productService.getAllProduct();
            List<User> listUser = userService.findAll();
            List<Category> listCategory = categoryService.findAll();

            List<Order> recentOrders = orderService.findTop5RecentOrder();
            List<String> recentUser = orderService.findTop5RecentCustomer();
            List<User> recentCustomer = new ArrayList<>();
            for (String y : recentUser) {
                recentCustomer.add(userService.getUserByUser_nameandRole(y, "user"));
            }
            model.addAttribute("Total_Order", listOrder.size());


            model.addAttribute("Total_Order", listOrder.size());
            model.addAttribute("Total_Product", listProduct.size());
            model.addAttribute("Total_User", listUser.size());
            model.addAttribute("Total_Category", listCategory.size());
            model.addAttribute("recentOrders", recentOrders);
            return "dashboard";
        }
    }

    @GetMapping("logout-admin")
    public String LogOutAdmin(Model model) {
        session.setAttribute("admin", null);
        return "redirect:/signin-admin";
    }
    // kiểm tra hóa đơn
    @GetMapping("/dashboard-invoice/{id}")
    public String InvoiceView(@PathVariable int id, Model model, HttpServletRequest request) {
        Order order = orderService.findById(id);
        List<Order_Item> listOrder_Item = order_ItemService.getAllByOrder_Id(order.getId());
        model.addAttribute("listOrder_Item", listOrder_Item);
        model.addAttribute("order", order);
        return "dashboard-invoice";
    }
    // order
    @GetMapping("/dashboard-orders")
    public String DashboardOrderView(Model model) {
        User admin = (User) session.getAttribute("admin");
        if (admin == null) {
            return "redirect:/signin-admin";
        } else {
            Pageable pageable = PageRequest.of(0, 3);
            Page<Order> pageOrder = orderService.findAll(pageable);
            model.addAttribute("pageOrder", pageOrder);
            return "dashboard-orders";
        }
    }
    // list product đã thêm
    @GetMapping("dashboard-myproducts")
    public String DashboardMyProductView(Model model) {
        User admin = (User) session.getAttribute("admin");
        if (admin == null) {
            return "redirect:/signin-admin";
        } else {
            List<Category> listCategories = categoryService.findAll();
            Pageable pageable = PageRequest.of(0, 3);
            Page<Product> pageProduct = productService.findAll(pageable);
            List<Product> productList = pageProduct.getContent();
            model.addAttribute("pageProduct", pageProduct);
            model.addAttribute("listProduct",productList);
            model.addAttribute("listCategories", listCategories);
            return "dashboard-myproducts";
        }
    }
    //

    @GetMapping("/dashboard-addproduct")
    public String DashboardAddProductView(Model model) {
        User admin = (User) session.getAttribute("admin");
        if (admin == null) {
            return "redirect:/signin-admin";
        } else {
            String addProduct = (String) session.getAttribute("addProduct");
            model.addAttribute("addProduct", addProduct);
            session.setAttribute("addProduct", null);
            List<Category> listCategories = categoryService.findAll();
            model.addAttribute("listCategories", listCategories);
            return "dashboard-addproduct";
        }
    }
    @PostMapping("dashboard-addproduct")
    public String DashboardAddProductHandel
            (Model model, @ModelAttribute("product_name") String product_name,
            @ModelAttribute("price") String price, @ModelAttribute("availability") String availability,
            @ModelAttribute("category") int category, @ModelAttribute("description") String description,
            @ModelAttribute("listImage") MultipartFile[] listImage) throws Exception {
        User admin = (User) session.getAttribute("admin");
        if (admin == null) {
            return "redirect:/signin-admin";
        } else {
            if (listImage != null) {
                Category cate = categoryService.getCategoryById(category);
                System.out.println(cate);
                long millis = System.currentTimeMillis();
                Date create_at = new java.sql.Date(millis);
                Product newPro = new Product();
                newPro.setCreated_At(create_at);
                newPro.setDescription(description);
                newPro.setIs_Active(1);
                newPro.setIs_Selling(1);
                newPro.setPrice(Integer.parseInt(price));
                newPro.setProduct_Name(product_name);
                newPro.setQuantity(Integer.parseInt(availability));
                newPro.setSold(0);
                newPro.setCategory(cate);
                //save lại trước sau đó mới lưu ảnh
                productService.saveProduct(newPro);
               newPro = productService.getProductById(newPro.getId());
               //search trong MultipartFile
                for (MultipartFile y : listImage) {
                    String urlImg = cloudinary.uploadFile(y);
                    ProductImage img = new ProductImage();
                    img.setProduct(newPro);
                    img.setUrl_Image(urlImg);
                    productImageService.save(img);
                }
                session.setAttribute("addProduct", "addProductSuccess");
                return "redirect:/dashboard-addproduct";
            } else {
                return "redirect:/dashboard-addproduct";
            }

        }
    }
    @GetMapping("/dashboard-myproducts/edit/{id}")
    public String DashboardMyProductEditView(@PathVariable int id, Model model) {
        User admin = (User) session.getAttribute("admin");
        if (admin == null) {
            return "redirect:/signin-admin";
        } else {
            List<Category> listCategories = categoryService.findAll();
            Product product = productService.getProductById(id);
            model.addAttribute("product", product);
            model.addAttribute("listCategories", listCategories);
            String editProduct = (String) session.getAttribute("editProduct");
            model.addAttribute("editProduct", editProduct);
            session.setAttribute("editProduct", null);
            return "dashboard-my-products-edit";
        }
    }
    @PostMapping("/dashboard-myproducts/edit")
    public String DashboardMyProductEditHandel(Model model,
                                               @ModelAttribute("product_id") int product_id,
                                               @ModelAttribute("product_name") String product_name,
                                               @ModelAttribute("price") String price,
                                               @ModelAttribute("availability") String availability,
                                               @ModelAttribute("category") int category,
                                               @ModelAttribute("description") String description,
                                               @ModelAttribute("listImage") MultipartFile[] listImage)
            throws Exception
    {
        User admin = (User) session.getAttribute("admin");
        if (admin == null) {
            return "redirect:/signin-admin";
        } else {
            if (listImage != null) {
                Category cate = categoryService.getCategoryById(category);
                Product product = productService.getProductById(product_id);
                product.setProduct_Name(product_name);
                product.setPrice(Integer.parseInt(price));
                product.setQuantity(Integer.parseInt(availability));
                product.setCategory(cate);
                product.setDescription(description);
                productService.saveProduct(product);
                for (MultipartFile y : listImage) {
                    if (!y.isEmpty()) {
                        String urlImg = cloudinary.uploadFile(y);
                        ProductImage img = new ProductImage();
                        img.setProduct(product);
                        img.setUrl_Image(urlImg);
                        productImageService.save(img);
                    }
                }
                session.setAttribute("editProduct", "editProductSuccess");
                return "redirect:/dashboard-myproducts/edit/" + product_id;
            } else {
                return "redirect:/dashboard-myproducts/edit/" + product_id;
            }

        }
    }
    @GetMapping("/dashboard-myproducts/delete/{id}")
    public String DeleteProduct(@PathVariable int id, HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        productService.deleteProductById(id);
        return "redirect:"+referer;
    }

}
