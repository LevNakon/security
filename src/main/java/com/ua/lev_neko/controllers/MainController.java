package com.ua.lev_neko.controllers;

import com.ua.lev_neko.models.Customer;
import com.ua.lev_neko.service.CustomerService;
import com.ua.lev_neko.utils.CustomerEditor;
import com.ua.lev_neko.utils.CustomerValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;

@Controller
@PropertySource("classpath:validation.properties")
public class MainController {

    @Autowired
    private Environment environment;

    @Autowired
    private CustomerService customerService;

    @GetMapping("/")
    public String index(){
//        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//
//        if (principal instanceof UserDetails) {
//            boolean empty = ((UserDetails) principal).getAuthorities().isEmpty();
//            if(empty){
//                return "index";
//            }
//            System.out.println("----------");
//        } else {
//            String username = principal.toString();
//            if(username.equals("anonymousUser")){
//                return "index";
//            }
//            System.out.println(username);
//        }
//        return "success";
        if (SecurityContextHolder.getContext().getAuthentication() != null &&
                SecurityContextHolder.getContext().getAuthentication().isAuthenticated() &&
                //when Anonymous Authentication is enabled
                !(SecurityContextHolder.getContext().getAuthentication()
                        instanceof AnonymousAuthenticationToken) ){
            Customer user = (Customer)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            System.out.println(user.toString());
            return "success";
        }else {
            return "index";
        }
    }

    @PostMapping("/success")
    public String success(){
        return "success";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CustomerEditor customerEditor;
    @Autowired
    private CustomerValidator customerValidator;

    @PostMapping("/save")
    public String save(Customer customer , BindingResult result , Model model){
        customerValidator.validate(customer,result);
        if (result.hasErrors()) {
            String errors = "";
            List<ObjectError> allErrors = result.getAllErrors();
            for (ObjectError error : allErrors) {
                errors += " " + environment.getProperty(error.getCode());
            }
            model.addAttribute("error", errors);
            return "index";
        }
        customerEditor.setValue(customer);
        customerService.save(customer);
        return "login";
    }

}
