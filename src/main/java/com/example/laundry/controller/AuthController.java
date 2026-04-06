// src/main/java/com/example/laundry/controller/AuthController.java
package com.example.laundry.controller;

import com.example.laundry.entity.Customer;
import com.example.laundry.repository.CustomerRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {
    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/customer/login")
    public String customerLoginPage() {
        return "customer_login";
    }

    @GetMapping("/customer/register")
    public String customerRegisterPage() {
        return "customer_register";
    }

    @PostMapping("/customer/register")
    public String registerCustomer(@RequestParam String firstName, @RequestParam String lastName, @RequestParam String nic,
                                   @RequestParam String phone, @RequestParam String address, @RequestParam String email,
                                   @RequestParam String password) {
        Customer customer = new Customer();
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setNic(nic);
        customer.setPhone(phone);
        customer.setAddress(address);
        customer.setEmail(email);
        customer.setPassword(password);
        customerRepository.save(customer);
        return "redirect:/customer/login";
    }

    @PostMapping("/customer/login")
    public String loginCustomer(@RequestParam String email, @RequestParam String password, HttpSession session) {
        Customer customer = customerRepository.findByEmail(email);
        if (customer != null && customer.getPassword().equals(password)) {
            session.setAttribute("userType", "customer");
            session.setAttribute("userId", customer.getId());
            return "redirect:/customer/dashboard";
        } else {
            return "redirect:/customer/login?error=true";
        }
    }

    @GetMapping("/staff/login")
    public String staffLoginPage() {
        return "staff_login";
    }

    @PostMapping("/staff/login")
    public String loginStaff(@RequestParam String username, @RequestParam String password, HttpSession session) {
        if ("Admin".equals(username) && "Admin123".equals(password)) {
            session.setAttribute("userType", "admin");
            return "redirect:/admin/dashboard";
        }
        if ("InManager".equals(username) && "InManager123".equals(password)) {
            session.setAttribute("userType", "inventory");
            return "redirect:/inventory/dashboard";
        }
        if ("Supervisor".equals(username) && "Supervisor123".equals(password)) {
            session.setAttribute("userType", "supervisor");
            return "redirect:/supervisor/dashboard";
        }
        if ("CustSupport".equals(username) && "CustSupport123".equals(password)) {
            session.setAttribute("userType", "support");
            return "redirect:/support/dashboard";
        }
        if ("DelCoord".equals(username) && "DelCoord123".equals(password)) {
            session.setAttribute("userType", "delivery");
            return "redirect:/delivery/dashboard";
        }
        return "redirect:/staff/login?error=true";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}