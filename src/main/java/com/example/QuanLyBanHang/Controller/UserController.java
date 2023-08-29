package com.example.QuanLyBanHang.Controller;

import com.example.QuanLyBanHang.Dto.UserCreateForm;
import com.example.QuanLyBanHang.entity.Cart;
import com.example.QuanLyBanHang.entity.User;
import com.example.QuanLyBanHang.service.CartService;
import com.example.QuanLyBanHang.service.FileUpload;
import com.example.QuanLyBanHang.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Controller
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    CartService cartService;


    private final  FileUpload fileUpload;


    @Autowired
    HttpSession session;

    public UserController(FileUpload fileUpload) {
        this.fileUpload = fileUpload;
    }

    @GetMapping("/signin")
    public String SigInView(Model model){

        return "signin";
    }
    @PostMapping("/signin")
    public  String Signin(@ModelAttribute("login-name") String loginname,
                          @ModelAttribute("password") String password,
                          @RequestParam(value = "remember" ,defaultValue = "false") boolean remember,
                          Model model)
    {
        //tìm  user có name và role là user
        User user = userService.getUserByUser_nameandRole(loginname,"user");
        System.out.println(user.getUser_Name() +" và"+ user.getPassword());
        if (user.getUser_Name() == null)
        {
            model.addAttribute("errorLogin", "Tên đăng nhập hoặc mật khẩu không chính xác!");
            return "signin";
        }
        else {
            if (!user.getPassword().equals(password)) {
                model.addAttribute("errorLogin", "Tên đăng nhập hoặc mật khẩu không chính xác!");
                return "signin";

            } else {
                if (remember == true) {
                    session.setAttribute("acc", user);
                    List<Cart> listCart = cartService.GetAllCartByUser_id(user.getId());
                    session.setAttribute("countCart", listCart.size());
                    return "redirect:/home";
                }
                return "redirect:/home";
            }
        }

    }
    @GetMapping("/signup")
    public String SignUpView(Model model) {
        return "signup";
    }
    @PostMapping("/signup")
    public  String SignUp(@ModelAttribute("username") String username,@ModelAttribute("your_email") String email,
                          @ModelAttribute("fullname") String fullname, @ModelAttribute("password") String password,
                          @ModelAttribute("comfirm_password") String comfirm_password, Model model)
    {
        UserCreateForm form = new UserCreateForm();
        User user = userService.getUserByUser_nameandRole(username,"user");
        if (user.getUser_Name()!= null)
        {
            model.addAttribute("errorSignUp", "Tài khoản đã tồn tại!");
            return "/signup";
        }
            form.setUserName(username);
            form.setEmail(email);
            form.setFullName(fullname);
            form.setPassword(password);
            form.setAvatar(null);
            form.setLoginType("default");
            form.setPhoneNumber(null);
            form.setRole("user");
            userService.createUser(form);
            return "redirect:/signin";


    }
    @GetMapping("/signout")
    public String SignOut(Model model) {
        session.setAttribute("acc", null);
        return "redirect:/home";
    }
    @GetMapping("/myprofile")
    public  String Myprofile(Model model, HttpServletRequest request )
    {
        User user = (User) session.getAttribute("acc");
        //sử dụng HTTP request header về trang trước đó
        String backpage = request.getHeader("Referer");
        String messChangeProfile = (String) session.getAttribute("messChangeProfile");
        model.addAttribute("messChangeProfile",messChangeProfile);
        session.setAttribute("messChangeProfile",null);
        if (user == null)
        {
            return "redirect:" + backpage;
        }
        else
        {
            String error_change_pass = (String) session.getAttribute("error_change_pass");
            String ChangePassSuccess = (String) session.getAttribute("ChangePassSuccess");
            model.addAttribute("error_change_pass", error_change_pass);
            model.addAttribute("ChangePassSuccess", ChangePassSuccess);
            //sao khi lưu tb vào biến error_change_pass và ChangePassSuccess
            //thì set session trở về thành null
            session.setAttribute("error_change_pass", null);
            session.setAttribute("ChangePassSuccess", null);
            model.addAttribute("user", user);
            return "/myprofile";
        }



    }
    @PostMapping("/changeProfile")
    public String ChangeProfile(Model model, @ModelAttribute("avatar") MultipartFile avatar,
                                @ModelAttribute("fullname") String fullname, @ModelAttribute("phone") String phone,
                                @ModelAttribute("email") String email) throws IOException {
        User user = (User) session.getAttribute("acc");
        if (user != null) {
            if (!avatar.isEmpty()) {
                String url = fileUpload.uploadFile(avatar);
                user.setAvatar(url);
            }
            user.setUser_Name(fullname);
            user.setEmail(email);
            user.setPhone_Number(phone);
            userService.saveUser(user);
            session.setAttribute("acc", user);
            session.setAttribute("messageChangeProfile", "Change Success.");
            return "redirect:/myprofile";
        } else {
            return "rediect:/home";
        }
    }
    @PostMapping("/changepassword")
    public String ChangePassword(Model model, @ModelAttribute("current_password") String current_password,
                                 @ModelAttribute("new_password") String new_password,
                                 @ModelAttribute("confirm_password") String confirm_password,
                                 HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        User user = (User) session.getAttribute("acc");

        if (user.getPassword().equals(current_password)) {
            session.setAttribute("error_change_pass", "Current Password not correct!");
            return "redirect:/myprofile";
        } else {
            if (!new_password.equals(confirm_password)) {
                session.setAttribute("error_change_pass", "Confirm New Password not valid!");
                return "redirect:/myprofile";
            } else {

                user.setPassword(new_password);
                userService.saveUser(user);
                session.setAttribute("acc", user);
            }
        }
        session.setAttribute("ChangePassSuccess", "ChangePassSuccess");
        return "redirect:" + referer;
    }
    @GetMapping("/forgot")
    public String forGotView(Model model) {
        String error_forgot = (String) session.getAttribute("error_forgot");
        model.addAttribute("error_forgot", error_forgot);
        session.setAttribute("error_forgot", null);
        model.addAttribute("forgot", "Forgot Password");
        return "signin";
    }
    @PostMapping("/forgot")
    public String forGotHandel(@ModelAttribute("login-name") String login_name, Model model) throws Exception {
        User user = userService.getUserByUser_nameandRole(login_name, "user");
        if (user == null) {
            session.setAttribute("error_forgot", "Tài khoản không tồn tại!");
            return "redirect:/forgot";
        } else {
            session.setAttribute("userForgot", user);
            return "redirect:/code";
        }
    }


}
