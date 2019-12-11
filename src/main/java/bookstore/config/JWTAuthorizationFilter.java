package bookstore.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    public JWTAuthorizationFilter(AuthenticationManager authManager) {
        super(authManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        String header = req.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            chain.doFilter(req, res);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(req);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(req, res);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        
        if (token != null) {
            // parse the token
        	Jws<Claims> jwsClaims = Jwts.parser().setSigningKey("SecretKeyToGenJWTs".getBytes()).parseClaimsJws(token.replace("Bearer ", ""));
        	
        	String username = jwsClaims.getBody().getSubject();
        	
        	@SuppressWarnings("unchecked")
			List<String> scopes = jwsClaims.getBody().get("scopes", List.class);
        	
        	if(scopes.isEmpty()) {
        		return null;
        	}
        	
        	List<GrantedAuthority> authorities = scopes.stream().map(authority -> new SimpleGrantedAuthority(authority)).collect(Collectors.toList());
        	
            if (username != null) {
                return new UsernamePasswordAuthenticationToken(username, null, authorities);
            }
            
            return null;
        }
        
        return null;
    }
    
}
