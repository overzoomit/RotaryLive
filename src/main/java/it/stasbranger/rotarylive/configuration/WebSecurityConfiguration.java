package it.stasbranger.rotarylive.configuration;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;

import it.stasbranger.rotarylive.service.CustomUserDetailsService;


@Configuration
@EnableAuthorizationServer
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private CustomUserDetailsService userDetailsService;

	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
		.antMatchers("/login").permitAll()
		.antMatchers("/signup").permitAll()
		.antMatchers("/club").permitAll()
		.anyRequest().authenticated()
		.and()
		.formLogin().permitAll();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService);
		auth.authenticationProvider(new AuthenticationProvider() {
			@Override
			public Authentication authenticate(Authentication authentication) throws AuthenticationException {
				String email = (String) authentication.getPrincipal();
				
				PasswordEncoder encoder = new BCryptPasswordEncoder();
				
				String providedPassword = (String) authentication.getCredentials();
				UserDetails user = userDetailsService.loadUserByUsername(email);
				if (user == null || !encoder.matches(providedPassword, user.getPassword())) {
					throw new BadCredentialsException("Username/Password does not match for " + authentication.getPrincipal());
				}

				return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
			}

			@Override
			public boolean supports(Class<?> authentication) {
				return true;
			}
		});
	}
}
