package bookstore.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true, jsr250Enabled = true)
@EnableWebSecurity
@EnableTransactionManagement
public class WebConfiguration extends WebSecurityConfigurerAdapter {
	
	@Override
    protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests().antMatchers("/").permitAll().and()
			.authorizeRequests().antMatchers("/h2-console/**").permitAll();
      
		http
			.formLogin().disable()
			.httpBasic().disable()
			.rememberMe()
			.and()
			.cors().and().csrf().disable(); // Disable CSRF (cross site request forgery)
		
		http.headers().frameOptions().disable();
		
		http
			.addFilter(new JWTAuthenticationFilter(authenticationManager()))
	        .addFilter(new JWTAuthorizationFilter(authenticationManager()))           
	        .logout().clearAuthentication(true)
	        .logoutSuccessHandler((new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK)))
	        .deleteCookies("JSESSIONID")
	        .invalidateHttpSession(true);
    }
    
}

