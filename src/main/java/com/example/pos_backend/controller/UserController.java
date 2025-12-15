package com.example.pos_backend.controller;

import com.example.pos_backend.dto.LoginRequest;
import com.example.pos_backend.model.User;
import com.example.pos_backend.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("") // DÜZELTİLDİ: Burası boş bırakıldı, 'api' YOK.
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // URL: http://...:8080/login
    @PostMapping("/login")
    public User loginUser(@RequestBody LoginRequest request) {
        return userService.loginUser(request.getUsername(), request.getPassword());
    }

    // --- PERSONEL YÖNETİMİ (CRUD) ---

    // 1. LİSTELEME
    // URL: http://...:8080/users
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // 2. EKLEME
    // URL: http://...:8080/users
    @PostMapping("/users")
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    // 3. GÜNCELLEME
    // URL: http://...:8080/users/{id}
    @PutMapping("/users/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        return userService.updateUser(id, user);
    }

    // 4. SİLME
    // URL: http://...:8080/users/{id}
    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}