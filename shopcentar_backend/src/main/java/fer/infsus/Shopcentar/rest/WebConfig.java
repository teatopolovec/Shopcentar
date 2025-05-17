package fer.infsus.Shopcentar.rest;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path uploadDir = Paths.get("slike");
        String uploadPath = uploadDir.toFile().getAbsolutePath();

        registry.addResourceHandler("/slike/**")
                .addResourceLocations("file:" + uploadPath + "/");
    }
}
