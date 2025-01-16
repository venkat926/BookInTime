package org.kvn.BookInTime.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Value("${user.authority}")
    private String userAuthority;

    @Value("${admin.authority}")
    private String adminAuthority;

    // User Authorization
    // TODO: update requestMatchers after adding APIs
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/user/addUser/**").permitAll()
                        .requestMatchers("/user/addAdmin/**").permitAll()
                        .requestMatchers("/user/getAllReviews/**").hasAuthority(userAuthority)
                        .requestMatchers("/user/getAllTickets/**").hasAuthority(userAuthority)
                        .requestMatchers("/movie/addMovie/**").hasAuthority(adminAuthority)
                        .requestMatchers("/movie/filter/**").permitAll()
                        .requestMatchers("/movie/getAllReviews/**").permitAll()
                        .requestMatchers("/movie/gatAllShows/**").permitAll()
                        .requestMatchers("movie/top5ByGenre/**").permitAll()
                        .requestMatchers("/review/addReview/**").hasAuthority(userAuthority)
                        .requestMatchers("/review/getReview/**").permitAll()
                        .requestMatchers("/review/deleteReview/**").hasAuthority(userAuthority)
                        .requestMatchers("/theater/addTheater/**").hasAuthority(adminAuthority)
                        .requestMatchers("/theater/getTheater").permitAll()
                        .requestMatchers("/theater/getAllShows/**").permitAll()
                        .requestMatchers("/show/addShow/**").hasAuthority(adminAuthority)
                        .requestMatchers("/show/getShow/**").permitAll()
                        .requestMatchers("/show/getShowsInCity/**").permitAll()
                        .requestMatchers("/ticket/bookTickets/**").hasAuthority(userAuthority)
                        .requestMatchers("/ticket/id/**").hasAuthority(userAuthority)
                        .requestMatchers("/ticket/cancelTicket/**").hasAuthority(userAuthority)
                        .anyRequest().authenticated()
        ).formLogin(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable());

        return http.build();
    }

    // Password Encoder
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
