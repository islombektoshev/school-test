package uz.owl.schooltest.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import uz.owl.schooltest.config.model.SecurityDto;
import uz.owl.schooltest.config.model.SecurityPayload;
import uz.owl.schooltest.entity.Role;
import uz.owl.schooltest.entity.User;
import uz.owl.schooltest.web.Message;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class JwtAuthentiocationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;

    private final ObjectMapper objectMapper;

    public JwtAuthentiocationFilter(AuthenticationManager authenticationManager, ObjectMapper objectMapper) {
        this.authenticationManager = authenticationManager;
        this.objectMapper = objectMapper;
        setFilterProcessesUrl(SecurityConstants.AUTH_LOGIN_URL);
        setAuthenticationFailureHandler(((httpServletRequest, httpServletResponse, e) -> {
            String un_authenticated = objectMapper.writeValueAsString(new Message(403, "Un Authenticated"));
            httpServletResponse.getWriter().println(un_authenticated);
            httpServletResponse.setStatus(403);
            httpServletResponse.setContentType("application/json");
        }));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        SecurityPayload securityPayload = securityPayload(request);
        return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(securityPayload.getUsername(), securityPayload.getPassword()));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        User user = (User) authResult.getPrincipal();
        String username = user.getUsername();
        List<Role> roles = user.getRoles();
        String token = Jwts.builder().signWith(Keys.hmacShaKeyFor(SecurityConstants.JWT_SECRET.getBytes()), SignatureAlgorithm.HS256)
                .setHeaderParam("typ", SecurityConstants.TOKEN_TYPE)
                .setExpiration(new Date(System.currentTimeMillis() + 86400000L))
                .setSubject(username)
                .setAudience(SecurityConstants.TOKEN_AUDIENCE)
                .setIssuer(SecurityConstants.TOKEN_ISSUER)
                .claim("rol", roles.stream().map(Role::getRolename).collect(Collectors.toList()))
                .compact();
        response.addHeader(SecurityConstants.TOKEN_HEADER, SecurityConstants.TOKEN_PREFIX + token);
        response.setContentType("application/json");
        response.getWriter().println(
                new SecurityDto(
                        SecurityConstants.TOKEN_HEADER,
                        SecurityConstants.TOKEN_PREFIX + token
                ).json()
        );
    }

    private SecurityPayload securityPayload(HttpServletRequest httpServletRequest){
        try {
            return objectMapper.readValue(httpServletRequest.getInputStream(), SecurityPayload.class);
        } catch (Exception ignored) { }
        return new SecurityPayload();

    }
}
