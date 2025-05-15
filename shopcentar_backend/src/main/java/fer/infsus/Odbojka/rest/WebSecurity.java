package fer.infsus.Odbojka.rest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity(
        securedEnabled = true,
        prePostEnabled = false
)
public class WebSecurity {
    public WebSecurity() {
    }
    @Bean
    public SecurityFilterChain spaFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorize -> authorize
                .anyRequest().permitAll()
        )
                .csrf(AbstractHttpConfigurer::disable);
        http.csrf(AbstractHttpConfigurer::disable);
        return (SecurityFilterChain)http.build();
    }
}
