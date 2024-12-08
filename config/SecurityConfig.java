package com.example.hardlab5.config;

//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/auth/register", "/auth/login").permitAll() // доступ без аутентификации
//                        .anyRequest().authenticated() // все остальные запросы требуют аутентификации
//                )
//                .formLogin(form -> form
//                        .loginPage("/auth/login")
//                        .defaultSuccessUrl("/main", true)
//                        .permitAll()
//                )
//                .logout(logout -> logout
//                        .logoutUrl("/logout")
//                        .logoutSuccessUrl("/auth/login?logout")
//                )
//                .sessionManagement(session -> session
//                        .invalidSessionUrl("/auth/login") // при неактивной сессии перенаправление на логин
//                        .maximumSessions(1)
//                        .expiredUrl("/auth/login?expired")
//                );
//
//
//        return http.build();
//    }
//
//}


//
import com.example.hardlab5.CustomUserDetailsService;
import com.example.hardlab5.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Autowired
    private CustomUserDetailsService customUserDetailsService;




    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return customUserDetailsService;  // Используем кастомную реализацию UserDetailsService
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(auth -> auth
                        .requestMatchers("/auth/register", "/auth/login").permitAll() // доступ без аутентификации
                        .anyRequest().authenticated() // все остальные запросы требуют аутентификации
                )
                .formLogin(form -> form
                        .loginPage("/auth/login")
                        .defaultSuccessUrl("/main", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/auth/login?logout")
                )
                .sessionManagement(session -> session
                        .invalidSessionUrl("/auth/login") // при неактивной сессии перенаправление на логин
                        .maximumSessions(1)
                        .expiredUrl("/auth/login?expired")
                );

        return http.build();
    }
}


