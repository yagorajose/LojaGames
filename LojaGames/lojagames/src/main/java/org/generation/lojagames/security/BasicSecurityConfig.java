package org.generation.lojagames.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class BasicSecurityConfig extends WebSecurityConfigurerAdapter {


	@Autowired
	private UserDetailsService userDetailsService;

	@Override // sobrescrita de método
	// alias
	protected void configure(AuthenticationManagerBuilder auth) throws Exception { // tratativa de erro

		auth.userDetailsService(userDetailsService); 

		auth.inMemoryAuthentication().withUser("root").password(passwordEncoder().encode("root"))
				.authorities("ROLE_USER");

	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();

	}

	@Override
	protected void configure(HttpSecurity http) throws Exception { 
			http.authorizeRequests()
			.antMatchers("/usuarios/logar").permitAll() // libera endpoints para o cliente conseguir fazer requisições dentro da api sem precisar de token
			.antMatchers("/usuarios/cadastrar").permitAll()
			.antMatchers("/usuarios/atualizar").permitAll()
			.antMatchers(HttpMethod.OPTIONS).permitAll()
			.anyRequest().authenticated() // todas as outras requisições devem ser autenticadas
			.and().httpBasic()
			.and().sessionManagement()/* indica qual o tipo de sessão */.sessionCreationPolicy(SessionCreationPolicy.STATELESS)// não guarda nenhuma sessão
			.and().cors() //
			.and().csrf().disable(); 
	}

}

