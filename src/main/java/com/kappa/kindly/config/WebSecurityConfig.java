package com.kappa.kindly.config;

import java.util.Arrays;

import com.kappa.kindly.security.JwtAuthenticationEntryPoint;
import com.kappa.kindly.security.JwtRequestFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private JwtRequestFilter jwtRequestFilter;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {

		authenticationManagerBuilder
			.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity
			.cors().and()
			.csrf().disable()
			.authorizeRequests()
				.antMatchers("/signin", "/signup").permitAll()
				.antMatchers(HttpMethod.GET, "/institution", "/institution/**").permitAll()
				.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
			.anyRequest().authenticated().and()
			.exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
	}

	// @Bean
	// CorsConfigurationSource corsConfigurationSource() {
	// 	CorsConfiguration config = new CorsConfiguration();

	// 	config.setAllowedOrigins(Arrays.asList("*"));
	// 	config.setAllowedMethods(Arrays.asList("GET", "POST", "OPTIONS"));
	// 	config.setAllowCredentials(true);
	// 	config.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));

	// 	UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	// 	source.registerCorsConfiguration("/**", config);

	// 	return source;
	// }
}
