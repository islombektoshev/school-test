package uz.owl.schooltest.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import uz.owl.schooltest.dao.UserDao;
import uz.owl.schooltest.service.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;
    private final ObjectMapper objectMapper;
    private final UserDao userDao;

    private static final String[] AUTH_WHITELIST = {
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/v2/api-docs",
            "/webjars/**",
            "/api/v1/has",
            "/api/v1/signup",
            "/api/v1/test"
//            "/api/public"
    };

    public SecurityConfig(UserService userService, ObjectMapper objectMapper, UserDao userDao) {
        this.userService = userService;
        this.objectMapper = objectMapper;
        this.userDao = userDao;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(AUTH_WHITELIST);
    }


    /**
     * Bu yerrda asosan access lar qoyilgan security business logic {@link JwtAuthorizationFilter} da ham yozilgan
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().and()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/api/public").permitAll()
                .antMatchers("/api/v1/admin/**").hasRole("ADMIN")
                .antMatchers("/api/v1/has", "/api/v1/signup", "/api/v1/test").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/").hasAnyAuthority("GET_ONLY", "FULL_USER_ACCESS")
                .antMatchers(HttpMethod.GET, "/api/v1/centers/**").hasAnyAuthority("GET_ONLY", "FULL_USER_ACCESS")
                .antMatchers(HttpMethod.POST, "/api/v1/centers/**").hasAnyAuthority("POST_ONLY", "FULL_USER_ACCESS")
                .antMatchers(HttpMethod.PUT, "/api/v1/centers/**").hasAnyAuthority("PUT_ONLY", "FULL_USER_ACCESS")
                .antMatchers(HttpMethod.DELETE, "/api/v1/centers/**").hasAnyAuthority("DELETE_ONLY", "FULL_USER_ACCESS")
                .antMatchers(HttpMethod.PATCH, "/api/v1/centers/**").hasAnyAuthority("PATCH_ONLY", "FULL_USER_ACCESS")
//                .anyRequest().authenticated()
                .and()
                .addFilter(new JwtAuthentiocationFilter(authenticationManager(), objectMapper))
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), userDao))
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        ;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    public CorsConfigurationSource getCorsConfigurationSource() {
        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return urlBasedCorsConfigurationSource;
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("OPTIONS");
        config.addAllowedMethod("GET");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("DELETE");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
