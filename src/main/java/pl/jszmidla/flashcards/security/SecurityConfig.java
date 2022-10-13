package pl.jszmidla.flashcards.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeRequests()
                .mvcMatchers("/sets/create").authenticated()
                .mvcMatchers("/sets/delete/*").authenticated()

                .mvcMatchers("/").permitAll()
                .mvcMatchers("/register").permitAll()
                .mvcMatchers("/js/**", "/css/**", "/webjars/**").permitAll()
                .mvcMatchers("/sets/**").permitAll()

                .anyRequest().authenticated()
            .and()
            .formLogin( form -> form
//                .loginPage("/login")
                .permitAll());

        return http.build();
    }
}
