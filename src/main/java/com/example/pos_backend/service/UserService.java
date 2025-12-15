package com.example.pos_backend.service;

import com.example.pos_backend.model.Role;
import com.example.pos_backend.model.User;
import com.example.pos_backend.repository.RoleRepository;
import com.example.pos_backend.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    // --- DÜZELTME BURADA YAPILDI ---
    // Parantez içine 'RoleRepository roleRepository' eklendi.
    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }
    // --- DÜZELTME BİTTİ ---

    /**
     * YENİ METOT (Arkadaşınızın İhtiyacı İçin: "Giriş Yap")
     */
    public User loginUser(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Kullanıcı bulunamadı: " + username));

        if (!user.getPassword().equals(password)) {
            throw new IllegalStateException("Yanlış şifre");
        }

        if (!user.isActive()) {
            throw new IllegalStateException("Kullanıcı hesabı pasif: " + username);
        }

        return user;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void updateUserStatus(Long userId, boolean isActive) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Kullanıcı bulunamadı"));
        user.setActive(isActive);
        userRepository.save(user);
    }

    // --- YENİ: KULLANICI EKLE ---
    public User createUser(User user) {
        // Gelen rolu kontrol et ve DB'den gerçeğini bul
        if (user.getRole() != null) {
            Role dbRole = roleRepository.findByRoleName(user.getRole().getRoleName())
                    .orElseThrow(() -> new RuntimeException("Rol bulunamadı"));
            user.setRole(dbRole);
        }

        return userRepository.save(user);
    }

    // --- YENİ: KULLANICI GÜNCELLE ---
    public User updateUser(Long id, User userUpdates) {
        // 1. Veritabanındaki mevcut kullanıcıyı bul
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        // 2. Basit alanları güncelle
        existingUser.setFullName(userUpdates.getFullName());
        existingUser.setUsername(userUpdates.getUsername());
        existingUser.setActive(userUpdates.isActive());

        // Şifre boş değilse güncelle
        if (userUpdates.getPassword() != null && !userUpdates.getPassword().isEmpty()) {
            existingUser.setPassword(userUpdates.getPassword());
        }

        // --- KRİTİK KISIM: ROL GÜNCELLEME ---
        if (userUpdates.getRole() != null) {
            String roleName = userUpdates.getRole().getRoleName();

            Role dbRole = roleRepository.findByRoleName(roleName)
                    .orElseThrow(() -> new RuntimeException("Hata: " + roleName + " rolü veritabanında bulunamadı!"));

            existingUser.setRole(dbRole);
        }

        // 3. Kaydet
        return userRepository.save(existingUser);
    }

    // --- YENİ: KULLANICI SİL ---
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("Silinecek kullanıcı bulunamadı id: " + id);
        }
        userRepository.deleteById(id);
    }
}