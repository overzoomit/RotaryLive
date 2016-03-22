/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
				String providedPassword = (String) authentication.getCredentials();
				UserDetails user = userDetailsService.loadUserByUsername(email);
				if (user == null || !user.getPassword().equals(providedPassword)) {
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
