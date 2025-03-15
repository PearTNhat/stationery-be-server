package com.project.stationery_be_server.configuration;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {

    @Bean
    Cloudinary configKey() {
        Map config = new HashMap();
        config.put("cloud_name", "dfz51ssyj");
        config.put("api_key", "863829725281893");
        config.put("api_secret", "saK_hIfL5si8X9fcna6ImuVdgIs");
        return new Cloudinary(config);
    }
}
