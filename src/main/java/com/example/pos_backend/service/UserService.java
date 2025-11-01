package com.example.pos_backend.service; // Paket adınızın bu olduğundan emin olun

import com.example.pos_backend.model.User;
import com.example.pos_backend.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service // Bu sınıfın bir "Servis" (Beyin) katmanı olduğunu Spring'e bildirir.
public class UserService {

    // --- 'Müteahhit' katmanını (Repository) enjekte etme ---

    private final UserRepository userRepository;

    // Tek constructor (Yapıcı Metot)
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * YENİ METOT (Arkadaşınızın İhtiyacı İçin: "Giriş Yap")
     * Kullanıcı girişi mantığını yönetir.
     * @param username Gelen kullanıcı adı
     * @param password Gelen şifre (bizim projede bu, "hash_garson123" gibi sahte hash)
     * @return Şifre doğruysa 'User' nesnesini döndürür.
     * @throws EntityNotFoundException Kullanıcı bulunamazsa.
     * @throws IllegalStateException Şifre yanlışsa veya hesap pasifse.
     */
    public User loginUser(String username, String password) {

        // 1. Kullanıcıyı 'username' ile veritabanında bul.
        // (Bu 'findByUsername' metodunu Adım 53.1'de 'UserRepository'ye eklemiştik)
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Kullanıcı bulunamadı: " + username));

        // 2. Şifre Kontrolü (GÜVENLİK NOTU)
        // Gerçek bir projede, burada Spring Security ve BCrypt ile şifre hash'leri
        // (örn: BCrypt.check(password, user.getPassword())) karşılaştırılır.
        //
        // Bizim projemizde (DatabaseInitializerService) şifreleri "hash_garson123"
        // olarak 'düz metin-hash' şeklinde kaydettiğimiz için, basit bir 'equals' karşılaştırması yapıyoruz.
        // Arkadaşınız Android'den 'password' olarak "hash_garson123" gönderecek.

        if (!user.getPassword().equals(password)) {
            // Şifreler eşleşmiyorsa
            throw new IllegalStateException("Yanlış şifre");
        }

        // 3. İŞ KURALI (Arkadaşınızın 'isActive' alanı için)
        // 'deneme_pasif' (isActive=false) kullanıcısı giriş yapamasın.
        if (!user.isActive()) {
            throw new IllegalStateException("Kullanıcı hesabı pasif: " + username);
        }

        // 4. Başarılı. 'User' nesnesini döndür.
        // (Adım 55'te 'User.java'daki 'password' alanına @JsonIgnore koyduğumuz için,
        // bu nesne JSON'a döndüğünde şifre alanı görünmeyecektir. GÜVENLİ.)
        return user;
    }
}