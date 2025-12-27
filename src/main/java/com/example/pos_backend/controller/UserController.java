package com.example.pos_backend.controller;

import com.example.pos_backend.config.JwtUtils;
import com.example.pos_backend.dto.LoginRequest;
import com.example.pos_backend.dto.LoginResponse;
import com.example.pos_backend.model.User;
import com.example.pos_backend.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin("*")
@RestController
@RequestMapping("")
public class UserController {

    private final UserService userService;
    private final JwtUtils jwtUtils;

    public UserController(UserService userService, JwtUtils jwtUtils) {
        this.userService = userService;
        this.jwtUtils = jwtUtils;
    }

    // ========== GİRİŞ VE GÜVENLİK İŞLEMLERİ ==========

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest request) {
        try {
            User user = userService.loginUser(request.getUsername(), request.getPassword());
            String token = jwtUtils.generateToken(user.getUsername());
            LoginResponse response = new LoginResponse(token, user);
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Kullanıcı adı veya şifre hatalı");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Sunucu hatası oluştu");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * ŞİFRE SIFIRLAMA KODU İSTE (YENİ)
     * URL: POST /forgot-password?username=admin
     */
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String username) {
        try {
            userService.forgotPassword(username);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Sıfırlama kodu e-posta adresinize gönderildi.");
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Kullanıcı bulunamadı.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "E-posta gönderilemedi: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * YENİ ŞİFREYİ ONAYLA (YENİ)
     * URL: POST /reset-password?username=admin&code=123456&newPassword=yeniSifre123
     */
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String username,
                                           @RequestParam String code,
                                           @RequestParam String newPassword) {
        try {
            userService.resetPassword(username, code, newPassword);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Şifreniz başarıyla güncellendi ve hesabınız açıldı.");
            return ResponseEntity.ok(response);
        } catch (IllegalStateException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Şifre sıfırlanırken bir hata oluştu.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // ========== PERSONEL YÖNETİMİ (CRUD) ==========

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        try {
            List<User> users = userService.getAllUsers();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Hata: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        try {
            User user = userService.getUserById(id);
            return ResponseEntity.ok(user);
        } catch (EntityNotFoundException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/users")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        try {
            User createdUser = userService.createUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User user) {
        try {
            User updatedUser = userService.updateUser(id, user);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            Map<String, String> success = new HashMap<>();
            success.put("message", "Silindi");
            return ResponseEntity.ok(success);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/users/{id}/unlock")
    public ResponseEntity<?> unlockUserAccount(@PathVariable Long id) {
        try {
            userService.unlockUserAccount(id);
            Map<String, String> success = new HashMap<>();
            success.put("message", "Kilit açıldı");
            return ResponseEntity.ok(success);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}