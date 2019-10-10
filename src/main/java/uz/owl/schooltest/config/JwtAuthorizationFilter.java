package uz.owl.schooltest.config;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import uz.owl.schooltest.dao.UserDao;
import uz.owl.schooltest.entity.Role;
import uz.owl.schooltest.entity.User;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.MappingMatch;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final UserDao userDao;
    private final Logger log = LoggerFactory.getLogger(getClass());

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserDao userDao) {
        super(authenticationManager);
        this.userDao = userDao;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = request.getHeader(SecurityConstants.TOKEN_HEADER);
        try {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = getUsernamePasswordAuthenticationToken(request, response);
            if (usernamePasswordAuthenticationToken != null) {
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        } catch (ExpiredJwtException exception) {
            log.warn("Request to parse expired JWT : {} failed : {}", token, exception.getMessage());
        } catch (UnsupportedJwtException exception) {
            log.warn("Request to parse unsupported JWT : {} failed : {}", token, exception.getMessage());
        } catch (MalformedJwtException exception) {
            log.warn("Request to parse invalid JWT : {} failed : {}", token, exception.getMessage());
        } catch (SignatureException exception) {
            log.warn("Request to parse JWT with invalid signature : {} failed : {}", token, exception.getMessage());
        } catch (IllegalArgumentException exception) {
            log.warn("Request to parse empty or null JWT : {} failed : {}", token, exception.getMessage());
        } catch (NullPointerException e) {
            log.warn("Karochi qorqish kerakman ok!");
        } finally {
            chain.doFilter(request, response);
        }
    }

    private UsernamePasswordAuthenticationToken getUsernamePasswordAuthenticationToken(HttpServletRequest request, HttpServletResponse response) {
        String header = request.getHeader(SecurityConstants.TOKEN_HEADER);
        String tokenString = header.substring(SecurityConstants.TOKEN_PREFIX.length());
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(SecurityConstants.JWT_SECRET.getBytes()).parseClaimsJws(tokenString);
        String username = claimsJws.getBody().getSubject();
        User user = userDao.findByUsername(username);
        if (!user.isEnable()) {
            return null;
        }
        List<Role> roles = user.getRoles();
        List<SimpleGrantedAuthority> authority = roles.stream().map(r -> new SimpleGrantedAuthority(r.getRolename())).collect(Collectors.toList());
        if (!authority.contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            LocalDateTime paymentExpiredDate = user.getPaymentExpiredDate();
            LocalDateTime currentDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(System.currentTimeMillis()), ZoneId.systemDefault());

            int i = currentDateTime.compareTo(paymentExpiredDate);
            if (i > 0) {
                /**
                 * if account payment time was expired deny some endpoints (
                 * allow only http get method
                 * )
                 */
                authority = Arrays.asList(
                        new SimpleGrantedAuthority("GET_ONLY"),
                        new SimpleGrantedAuthority("ROLE_USER")
                );
            }
        }

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, null, authority);
        try {
            doFilter(request, response, user, roles);
        } catch (Exception e) {
            return null;
        }
        return usernamePasswordAuthenticationToken;
    }

    /**
     * Business logikaga asosan filter qildi agar filterda otmasa exception qaytaradi
     *
     * @param request
     * @param response
     * @param user
     * @param roles
     */
    void doFilter(HttpServletRequest request, HttpServletResponse response, User user, List<Role> roles) throws Exception {
    }
}
