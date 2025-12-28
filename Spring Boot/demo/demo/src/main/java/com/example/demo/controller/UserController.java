package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*") // অ্যান্ড্রয়েড রিকোয়েস্ট এলাউ করার জন্য এটি বাধ্যতামূলক
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/save")
    public ResponseEntity<Map<String, Object>> saveUser(@RequestBody User user) {
        User savedUser = userService.saveUser(user);

        // অ্যান্ড্রয়েড অ্যাপের সুবিধার জন্য একটি রেসপন্স মেসেজ পাঠানো
        Map<String, Object> response = new HashMap<>();
        response.put("id", savedUser.getId());
        response.put("message", "User saved successfully!");

        return ResponseEntity.ok(response);
    }
}