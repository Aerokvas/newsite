package artas.newsite.config;

import artas.newsite.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    private final CustomUserDetailsService userDetailsService;

    @Autowired
    public WebSecurityConfig(@Qualifier("customPersonDetails") CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService)
                .passwordEncoder(bCryptPasswordEncoder());
        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

        http
                .csrf((csrf) -> csrf
                            .ignoringRequestMatchers(AntPathRequestMatcher.antMatcher("/console/**"))
                            .disable()
                )
                .authorizeHttpRequests((requests) -> {
                            try {
                                requests
                                        .requestMatchers("/", "/registration/**").permitAll()
                                        .requestMatchers(AntPathRequestMatcher.antMatcher("/console/**")).permitAll()
                                        .requestMatchers("/admin/**").hasRole("ADMIN")
                                        .requestMatchers("/home/**", "/profile_account/**").hasAnyRole("USER", "ADMIN")
                                        .anyRequest().authenticated()
                                        .and()
                                        .headers().frameOptions().sameOrigin();
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                )
                .formLogin((form) -> form
                        .loginPage("/login")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/home", true)
                        .permitAll()
                )
                .logout(LogoutConfigurer::permitAll)
                .authenticationManager(authenticationManager);
        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}