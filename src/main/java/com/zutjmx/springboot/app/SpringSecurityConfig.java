package com.zutjmx.springboot.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers("/","/css/**","/js/**","/images/**","/listar").permitAll()
			.antMatchers("/ver/**").hasAnyRole("USER")
			.antMatchers("/uploads/**").hasAnyRole("USER")
			.antMatchers("/formulario-cliente/**").hasAnyRole("ADMIN")
			.antMatchers("/eliminar/**").hasAnyRole("ADMIN")
			.antMatchers("/factura/**").hasAnyRole("ADMIN")
		.anyRequest().authenticated()
		.and()
		.formLogin().loginPage("/login")
					.permitAll()
		.and()
		.logout().permitAll();
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {		
		return new BCryptPasswordEncoder();
	}
	
	@Autowired
	public void configurerGlobal(AuthenticationManagerBuilder builder) throws Exception {		
		PasswordEncoder encoder = passwordEncoder(); 		
		UserBuilder usuarios = User.builder().passwordEncoder(encoder::encode);
		
		builder.inMemoryAuthentication()
		       .withUser(usuarios.username("admin")
		    		             .password("123456")
		    		             .roles("ADMIN","USER"))
		       .withUser(usuarios.username("zutjmx")
  		             			 .password("123456")
  		             			 .roles("USER"));
	}
}
