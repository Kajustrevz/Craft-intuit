package com.craft.craftapp.auth;

import com.craft.craftapp.common.AppConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserRepository userRepository;
    private final JwtTokenFilter jwtTokenFilter;
    private final AppConfiguration appConfiguration;

    public SecurityConfig(UserRepository otpRepository,
                          JwtTokenFilter jwtTokenFilter, AppConfiguration appConfiguration) {
        this.userRepository = otpRepository;
        this.jwtTokenFilter = jwtTokenFilter;
        this.appConfiguration = appConfiguration;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(username -> userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException(String.format("username: %s, not exist", username))));
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Enable CORS and disable CSRF
        http = http.cors().and().csrf().disable();

        // Set session management to stateless
        http = http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and();

        // Set unauthorized requests exception handler
        http = http
                .exceptionHandling()
                .authenticationEntryPoint(
                        (request, response, ex) -> {
                            response.sendError(
                                    HttpServletResponse.SC_UNAUTHORIZED,
                                    ex.getMessage());
                        })
                .and();

        // Set permissions on endpoints

        if(appConfiguration.getEnvironment().equalsIgnoreCase("X")){
            http.authorizeRequests()
                    // Our public endpoints
                    //.antMatchers("/api/**").permitAll()
                    .antMatchers(HttpMethod.GET, "/error/**").permitAll()
                    .antMatchers("/api/auth/**").permitAll()
                    .antMatchers(HttpMethod.GET, "/api/update").permitAll()
                    .antMatchers("/api/leads").permitAll()
                    .antMatchers("/api/leads/").permitAll()
                    .antMatchers(HttpMethod.GET, "/swagger-ui.html").permitAll()
                    .antMatchers(HttpMethod.GET, "/swagger-ui/**").permitAll()
                    .antMatchers(HttpMethod.GET, "/v3/**").permitAll()
                    // Our private endpoints
                    .anyRequest().authenticated();
        } else{
            http.authorizeRequests()
                    // Our public endpoints
                    //.antMatchers("/api/**").permitAll()
                    .antMatchers(HttpMethod.GET, "/error/**").permitAll()
                    .antMatchers("/auth/signup").permitAll()
                    .antMatchers("/auth/login").permitAll()
                    .antMatchers(HttpMethod.GET, "/swagger-ui.html").permitAll()
                    .antMatchers(HttpMethod.GET, "/swagger-ui/**").permitAll()
                    .antMatchers(HttpMethod.GET, "/v3/**").permitAll()
                    // Our private endpoints
                    .anyRequest().authenticated();
        }

        // Add JWT token filter
        http.addFilterBefore(
                jwtTokenFilter,
                UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Used by spring security if CORS is enabled.
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("*");
        //config.setAllowedOriginPattern("*");
        //config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        String hierarchy = "ROLE_ADMIN > ROLE_STAFF\nROLE_STAFF > ROLE_USER";
        roleHierarchy.setHierarchy(hierarchy);
        return roleHierarchy;
    }

}
