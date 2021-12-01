package com.example.SafeBlurrinder;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class WebConfig implements WebMvcConfigurer {
        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry){
            registry.addResourceHandler("/video/**")
//                    .addResourceLocations("file:///C:/Users/user/GaoriVideos/");
                    .addResourceLocations("file:///"+System.getProperty("user.home")+"/GaoriVideos/");
        }
}
