package com.example.pos_backend.service;

import com.example.pos_backend.model.Role;
import com.example.pos_backend.model.User;
import com.example.pos_backend.repository.RoleRepository;
import com.example.pos_backend.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final EmailService emailService; // Yeni enjekte edildi
    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       BCryptPasswordEncoder passwordEncoder,EmailService emailService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    /**
     * LOGIN İŞLEMİ
     * Transaction kullanmıyoruz çünkü her repository metodu kendi transaction'ını yönetiyor
     */
    public User loginUser(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Kullanıcı bulunamadı"));

        // Hesap kilitli mi kontrol et
        if (user.isAccountLocked()) {
            throw new RuntimeException("Hesabınız kilitlenmiştir. Lütfen yönetici ile iletişime geçin.");
        }

        // Şifre kontrolü
        if (!passwordEncoder.matches(password, user.getPassword())) {
            // Başarısız deneme sayısını artır
            userRepository.incrementFailedAttempts(user.getUserID());

            // Güncel kullanıcıyı tekrar çek
            User updatedUser = userRepository.findById(user.getUserID())
                    .orElseThrow(() -> new EntityNotFoundException("Kullanıcı bulunamadı"));

            // 3 başarısız denemeden sonra hesabı kilitle
            if (updatedUser.getFailedAttempts() >= 3) {
                userRepository.lockAccount(user.getUserID());
                throw new RuntimeException("Hesabınız 3 başarısız giriş denemesi nedeniyle kilitlendi!");
            }

            int kalanHak = 2 - updatedUser.getFailedAttempts();
            throw new RuntimeException("Hatalı şifre! Kalan deneme hakkı: " + kalanHak);
        }

        // Giriş başarılı - başarısız deneme sayısını sıfırla
        userRepository.resetFailedAttempts(user.getUserID());
        return user;
    }

    /**
     * TÜM KULLANICILARI LİSTELE
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * ID'YE GÖRE KULLANICI GETIR
     */
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Kullanıcı bulunamadı: " + id));
    }

    /**
     * YENİ KULLANICI OLUŞTUR
     */
    @Transactional
    public User createUser(User user) {
        // Kullanıcı adı kontrolü
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("Bu kullanıcı adı zaten kullanılıyor: " + user.getUsername());
        }

        // Rol kontrolü ve atama
        if (user.getRole() != null) {
            Role dbRole = roleRepository.findByRoleName(user.getRole().getRoleName())
                    .orElseThrow(() -> new RuntimeException("Rol bulunamadı: " + user.getRole().getRoleName()));
            user.setRole(dbRole);
        } else {
            throw new RuntimeException("Kullanıcı için rol belirtilmelidir");
        }

        // Şifre kontrolü ve hashleme
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new RuntimeException("Şifre boş olamaz");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Varsayılan değerler
        user.setActive(true);
        user.setFailedAttempts(0);
        user.setAccountLocked(false);

        return userRepository.save(user);
    }

    /**
     * KULLANICI GÜNCELLE
     */
    @Transactional
    public User updateUser(Long id, User userUpdates) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Kullanıcı bulunamadı: " + id));

        // Temel bilgileri güncelle
        if (userUpdates.getFullName() != null) {
            existingUser.setFullName(userUpdates.getFullName());
        }

        // Kullanıcı adı değişikliği
        if (userUpdates.getUsername() != null && !userUpdates.getUsername().equals(existingUser.getUsername())) {
            // Yeni kullanıcı adı başkası tarafından kullanılıyor mu kontrol et
            if (userRepository.findByUsername(userUpdates.getUsername()).isPresent()) {
                throw new RuntimeException("Bu kullanıcı adı zaten kullanılıyor: " + userUpdates.getUsername());
            }
            existingUser.setUsername(userUpdates.getUsername());
        }

        // Aktiflik durumu
        existingUser.setActive(userUpdates.isActive());

        // Şifre güncelleme (eğer yeni şifre gönderildiyse)
        if (userUpdates.getPassword() != null && !userUpdates.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(userUpdates.getPassword()));
        }

        // Rol güncelleme
        if (userUpdates.getRole() != null) {
            Role dbRole = roleRepository.findByRoleName(userUpdates.getRole().getRoleName())
                    .orElseThrow(() -> new RuntimeException("Rol bulunamadı: " + userUpdates.getRole().getRoleName()));
            existingUser.setRole(dbRole);
        }

        return userRepository.save(existingUser);
    }

    /**
     * KULLANICI SİL
     */
    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("Silinecek kullanıcı bulunamadı: " + id);
        }
        userRepository.deleteById(id);
    }

    /**
     * HESAP KİLİDİNİ AÇ
     * Admin tarafından kilitli hesapların kilidini açmak için
     */
    @Transactional
    public void unlockUserAccount(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Kullanıcı bulunamadı: " + userId));

        userRepository.resetFailedAttempts(userId);
        userRepository.unlockAccount(userId);
    }
    public void forgotPassword(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Kullanıcı bulunamadı."));

        // Kullanıcının kayıtlı e-postası var mı kontrol et
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new IllegalStateException("Kullanıcıya ait bir e-posta adresi bulunamadı.");
        }

        // 6 haneli rastgele kod üret
        String code = String.format("%06d", new Random().nextInt(999999));
        user.setResetCode(code);
        user.setResetCodeExpiresAt(LocalDateTime.now().plusMinutes(15));
        userRepository.save(user);

        // --- ARTIK KONSOLA DEĞİL, EMAİLE GÖNDERİYORUZ ---
        emailService.sendResetCode(user.getEmail(), code);
    }

    @Transactional
    public void resetPassword(String username, String code, String newPassword) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Kullanıcı bulunamadı."));

        if (user.getResetCode() == null || !user.getResetCode().equals(code)) {
            throw new IllegalStateException("Sıfırlama kodu hatalı.");
        }

        if (user.getResetCodeExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Sıfırlama kodunun süresi dolmuş.");
        }

        // Şifreyi güncelle ve hesabı tertemiz yap
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetCode(null); // Kodu temizle
        user.setResetCodeExpiresAt(null);
        user.setFailedAttempts(0); // Hata sayısını sıfırla
        user.setAccountLocked(false); // Kilidi kaldır!

        userRepository.save(user);
    }
}