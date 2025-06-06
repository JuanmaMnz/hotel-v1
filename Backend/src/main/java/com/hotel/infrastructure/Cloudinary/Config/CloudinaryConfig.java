package com.hotel.infrastructure.Cloudinary.Config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "cloudinary")
public class CloudinaryConfig {

    @Value("${CLOUDINARY_NAME}")
    private String CLOUD_NAME;

    @Value("${CLOUDINARY_API_KEY}")
    private String API_KEY;

    @Value("${CLOUDINARY_API_SECRET}")
    private String API_SECRET;

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", CLOUD_NAME,
                "api_key", API_KEY,
                "api_secret", API_SECRET
        ));
    }

}