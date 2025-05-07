package com.jobapp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Configure le routage pour le frontend React
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // Route toutes les URLs non-API vers index.html
        registry.addViewController("/")
                .setViewName("forward:/index.html");

        // Gère les routes React (client-side routing)
        registry.addViewController("/{path:^(?!api$|static$|favicon\\.ico$).*}/**")
                .setViewName("forward:/index.html");
    }

    /**
     * Configure la gestion des ressources statiques
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/")
                .setCachePeriod(0); // Désactive le cache en développement
    }
}