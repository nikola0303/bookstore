package bookstore.config;

import com.fasterxml.jackson.databind.ObjectMapper;

import bookstore.model.MyUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.stream.Collectors;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    
	private AuthenticationManager authenticationManager;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) throws AuthenticationException {
        try {
        	MyUser credentials = new ObjectMapper().readValue(req.getInputStream(), MyUser.class);            
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(credentials.getEmail(), credentials.getPassword(), Collections.emptyList()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain, Authentication auth) throws IOException, ServletException {
    	Claims claims = Jwts.claims().setSubject(((User) auth.getPrincipal()).getUsername());
        claims.put("scopes", ((User) auth.getPrincipal()).getAuthorities().stream().map(s -> s.toString()).collect(Collectors.toList()));

        String token = Jwts.builder()
                //.setSubject(((User) auth.getPrincipal()).getUsername())
        		.setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + 864_000_000)) //10 dana
                .signWith(SignatureAlgorithm.HS512, "SecretKeyToGenJWTs".getBytes())
                .compact();
        res.addHeader("Authorization", "Bearer " + token);
        res.addHeader("Access-Control-Expose-Headers", "Authorization");
    }
    
}
