package com.example.pos_backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // --- KRİTİK AYAR ---
        // Yolu "C:/ndm-proje/uploads/" olarak SABİTLİYORUZ.
        // Böylece proje nerede çalışırsa çalışsın resimler hep burada aranacak.
        // Sondaki "/" işaretine ve "file:///" yapısına dikkat et.
        String myExternalFilePath = "file:///C:/ndm-proje/uploads/";

        System.out.println("RESİM YOLU SABİTLENDİ: " + myExternalFilePath);

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(myExternalFilePath);
    }
}