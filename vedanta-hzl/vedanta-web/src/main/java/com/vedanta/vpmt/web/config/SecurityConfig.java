package com.vedanta.vpmt.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	@Qualifier("vedantaAuthenticationProvider")
	private AuthenticationProvider authenticationProvider;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().headers().frameOptions().disable().and().authorizeRequests()
				.antMatchers("/", "/favicon.ico", "/resources/**").permitAll()
				.antMatchers("/admin/**", "/support/support-list").hasAnyRole("ADMIN").antMatchers("/user/**")
				.hasAnyRole("ADMIN", "USER_MANAGER").antMatchers(("/form-save/**"))
				.hasAnyRole("ADMIN", "USER_MANAGER", "PLANT_HEAD", "BUSINESS_UNIT_HEAD", "CONTRACT_MANAGER","HR")
				.antMatchers(("/scorecard/**"), ("/support"))
				.hasAnyRole("ADMIN", "USER_MANAGER", "PLANT_HEAD", "BUSINESS_UNIT_HEAD", "CONTRACT_MANAGER","HR").and()
				.formLogin().loginPage("/").permitAll().loginProcessingUrl("/authenticate")
				.usernameParameter("j_username").passwordParameter("j_password").defaultSuccessUrl("/index").and()
				.logout().logoutUrl("/logout").logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.clearAuthentication(true).invalidateHttpSession(true).logoutSuccessUrl("/logoutsuccessfully")
				.permitAll().and().exceptionHandling().accessDeniedPage("/403");
	}

	@Override
	@Bean(name = "authenticationManager")
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

}
