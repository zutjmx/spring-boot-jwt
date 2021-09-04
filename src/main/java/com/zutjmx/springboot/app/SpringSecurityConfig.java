package com.zutjmx.springboot.app;

/*import javax.sql.DataSource;*/

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;

import com.zutjmx.springboot.app.auth.handler.LoginSuccessHandler;
import com.zutjmx.springboot.app.models.service.JpaUserDetailsService;

@EnableGlobalMethodSecurity(securedEnabled=true, prePostEnabled=true)
@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder; 
	
	@Autowired
	private LoginSuccessHandler successHandler;
	
	/*@Autowired
	private DataSource dataSource;*/
	
	@Autowired
	private JpaUserDetailsService userDetailsService;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers("/","/css/**","/js/**","/images/**","/listar**","/locale","/api/clientes/**").permitAll()
			/*.antMatchers("/ver/**").hasAnyRole("USER")*/
			/*.antMatchers("/uploads/**").hasAnyRole("USER")*/
			/*.antMatchers("/formulario-cliente/**").hasAnyRole("ADMIN")*/
			/*.antMatchers("/eliminar/**").hasAnyRole("ADMIN")*/
			/*.antMatchers("/factura/**").hasAnyRole("ADMIN")*/
		.anyRequest().authenticated()
		.and()
			.formLogin()
				.successHandler(successHandler)
				.loginPage("/login")
			.permitAll()
		.and()
		.logout().permitAll()
		.and()
		.exceptionHandling().accessDeniedPage("/error_403");
	}

	/*@Bean
	public BCryptPasswordEncoder passwordEncoder() {		
		return new BCryptPasswordEncoder();
	}*/
	
	@Autowired
	public void configurerGlobal(AuthenticationManagerBuilder builder) throws Exception {		
		
		builder.userDetailsService(userDetailsService)
			/*.dataSource(dataSource)*/
			.passwordEncoder(passwordEncoder)/*
			.usersByUsernameQuery("select username, password, enabled from users where username = ?")
			.authoritiesByUsernameQuery("select u.username, a.authority from authorities a inner join users u on (a.user_id = u.id) where u.username = ?")*/
			;
		
		/*PasswordEncoder encoder = this.passwordEncoder; 		
		UserBuilder usuarios = User.builder().passwordEncoder(encoder::encode);
		
		builder.inMemoryAuthentication()
		       .withUser(usuarios.username("admin")
		    		             .password("123456")
		    		             .roles("ADMIN","USER"))
		       .withUser(usuarios.username("zutjmx")
  		             			 .password("123456")
  		             			 .roles("USER"));*/
		
	}
}
