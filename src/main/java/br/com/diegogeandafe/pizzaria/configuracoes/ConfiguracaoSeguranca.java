package br.com.diegogeandafe.pizzaria.configuracoes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import br.com.diegogeandafe.pizzaria.modelo.servicos.ServicoAutenticacao;


@Configuration
@EnableWebSecurity
public class ConfiguracaoSeguranca extends WebSecurityConfigurerAdapter {
	
	
	@Autowired private ServicoAutenticacao servicoAutenticacao;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth
			.userDetailsService(servicoAutenticacao)
			.passwordEncoder(encoder());
			//.inMemoryAuthentication().withUser("admin").password("admin").roles("PIZZARIA");
	}
	
	
	//configuracao de autenticacao
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
				//autoriza o acesso apos tudo que vier em seguida de /app/pizzas/** para permisoes do tipo PIZZARIA
				.antMatchers("/app/pizzas/**", "/app/ingredientes/**").hasRole("PIZZARIA")
				.anyRequest().permitAll()
			.and()
				.formLogin()
				.loginPage("/login.jsp")
				.loginProcessingUrl("/autenticar")
				.defaultSuccessUrl("/app/pizzas")
				//autenticacao caso o usuario ou senha seja incorreta
				.failureUrl("/login.jsp?semacesso=true")
				//passando parametro de usuario
				.usernameParameter("usuario")
				//passando parametro de senha
				.passwordParameter("senha")
			.and()
				.logout()
					.logoutUrl("/sair")
					.logoutSuccessUrl("/login.jsp?saiu=true");
				
	}
	
	@Bean
	public BCryptPasswordEncoder encoder(){
		return new BCryptPasswordEncoder();
	}
	
	public static void main(String[] args) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		System.out.println(encoder.encode("admin"));
	}

}