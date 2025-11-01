package com.example.pos_backend.controller; // Paket adınızın bu olduğundan emin olun

import com.example.pos_backend.dto.LoginRequest; // Az önce oluşturduğumuz DTO'yu import et
import com.example.pos_backend.model.User;
import com.example.pos_backend.service.UserService; // 'Kullanıcı Beyni'ni import et
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*") // Android (veya Postman) tarafının erişebilmesi için
@RestController
@RequestMapping("") // Tüm kullanıcı endpoint'lerinin başı '/api'
public class UserController {

    // --- 'Beyin' katmanını (Service) enjekte etme ---

    private final UserService userService;

    // Tek constructor (Yapıcı Metot)
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // --- YENİ ENDPOINT (Arkadaşınızın İhtiyacı İçin: "Giriş Yap") ---
    // Bu, "Giriş Yap" senaryosunu gerçekleştiren API kapısıdır.
    // URL: POST http://localhost:8080/api/login
    // BODY: { "username": "garson01", "password": "hash_garson123" }

    /**
     * Kullanıcı girişi yapmak için kullanılır.
     * @param request JSON gövdesinden gelen 'username' ve 'password'ü tutan DTO.
     * @return Başarılı giriş yapılırsa 'User' nesnesi (şifre alanı @JsonIgnore ile gizlenmiş).
     */
    @PostMapping("/login")
    public User loginUser(@RequestBody LoginRequest request) {
        // KAPI (Controller), İŞİ BEYNE (Service) PASLAR
        return userService.loginUser(request.getUsername(), request.getPassword());
    }

    // TODO (İleride Gerekirse):
    // Arkadaşınızın 'Tüm Kullanıcıları Listeleme' (örn: Admin paneli)
    // ekranına ihtiyacı olursa, buraya bir @GetMapping("/users") eklenebilir.
}