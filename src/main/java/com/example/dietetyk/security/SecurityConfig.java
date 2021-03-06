package com.example.dietetyk.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.dietetyk.security.jwt.filters.JwtRequestFilter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	UserDetailsService customUserDetailsService;

	@Autowired
	private JwtRequestFilter jwtRequestFilter;

	@Bean
	public PasswordEncoder passwordEncoder() {
		PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
		return passwordEncoder;
	}

	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(customUserDetailsService);
	}

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.headers().frameOptions().disable();
		httpSecurity.csrf().disable()
			.authorizeRequests()
				.antMatchers("/", "/account/login").permitAll()
				.antMatchers("/admin/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
				.and()
			.exceptionHandling().and()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
	}
}
