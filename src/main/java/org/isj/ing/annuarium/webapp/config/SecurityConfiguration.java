package org.isj.ing.annuarium.webapp.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration// toujours ajouter les deux ci
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {//pour configurer sa securite a spring security{

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder; // ne jamais garder le mot de passe en clair et ceci pour encoder
	// on utilise

	@Autowired
	private DataSource dataSource; // va recuperer toutes les donnees utilisees dans applicationproperties

	@Value("${spring.queries.users-query}")
	private String usersQuery; // gestion des users et leurs roles

	@Value("${spring.queries.roles-query}")
	private String rolesQuery;

	@Override
	protected void configure(AuthenticationManagerBuilder auth)
			throws Exception {
		auth.
		jdbcAuthentication()
		.usersByUsernameQuery(usersQuery)
		.authoritiesByUsernameQuery(rolesQuery)
		.dataSource(dataSource)
		.passwordEncoder(bCryptPasswordEncoder);// pour indiquer que les mdp sont encodes en BD
	}//la gestion avec sping security ce sont juste les regles de securites

	@Override
	protected void configure(HttpSecurity http) throws Exception {
//permit.All() l'acces a la route /api est autorisee a tout le monde //.antMatchers("/api/**").permitAll()//pour permettre l'acces a toutes les routes des endpoints
		http.
		authorizeRequests()
		.antMatchers("/login").permitAll()
				.antMatchers("/registration").permitAll()
				.antMatchers("/","/rechercherform","/listactes","/detail").permitAll()
		.antMatchers("/api/**").permitAll()
		.antMatchers("/admin/**").hasAuthority("ADMIN")// pour renvoyer les users non autorises a l'interieur des pages html
		.anyRequest().authenticated().and().csrf().disable().formLogin()
		.loginPage("/login").failureUrl("/login?error=true")//pour indiquer la page de login htm que le controller doit appeler
		.defaultSuccessUrl("/")//pour indiquer quelle page va s'ouvrir quand l'user a reussi a se loguer
		.usernameParameter("email")// email la cest le name dans le input dans le formulaire
		.passwordParameter("password")
		.and().logout()
		.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))//la route pour se deconnecter
		.logoutSuccessUrl("/login").and().exceptionHandling()
		.accessDeniedPage("/access-denied");
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web
		.ignoring()
		.antMatchers("/assets/**","/forms/**","/resources/**", "/static/**");
	}

}
