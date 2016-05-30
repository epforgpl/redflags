/*
   Copyright 2014-2016 PetaByte Research Ltd.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package hu.petabyte.redflags.web.cfg;

import hu.petabyte.redflags.web.svc.LoginCaptchaFilter;
import hu.petabyte.redflags.web.svc.SecuritySvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.CharacterEncodingFilter;

/**
 * @author Zsolt Jurányi
 */
@Configuration
@EnableWebSecurity
public class SecurityRoles extends WebSecurityConfigurerAdapter {

	private @Autowired JdbcTemplate jdbc;
	private @Autowired SecuritySvc security;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		CharacterEncodingFilter filter = new CharacterEncodingFilter();
		filter.setEncoding("UTF-8");
		filter.setForceEncoding(true);

		http.addFilterBefore(new LoginCaptchaFilter(security), CsrfFilter.class)
				//
		.authorizeRequests()
				//
		.antMatchers(//
				// resources
				"/css/**", //
				"/doc/**", //
				"/img/**", //
				"/js/**", //
				"/favicon.ico", //
				"/robots.txt", //
				// public pages
				"/", //
				"/activate/**", //
				"/change-password/**", //
				"/chart/**", //
				"/forgot", //
				"/login", //
				"/register", //
				"/send-filter-emails", //
				"/version"// "/send-test-email"
				)
				.permitAll()
				//
				.anyRequest()
				.authenticated()
				.and()
				//
				.formLogin()
				.loginPage("/login")
				.permitAll()
				.and()
				//
				.logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.deleteCookies("remember-me").logoutSuccessUrl("/login")
				.permitAll().and()
				//
				.rememberMe().tokenValiditySeconds(60 * 60 * 24 * 30)
				.tokenRepository(persistentTokenRepository());
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth)
			throws Exception {
		auth.userDetailsService(userDetailsService()).passwordEncoder(
				new BCryptPasswordEncoder());
	}

	private PersistentTokenRepository persistentTokenRepository() {
		JdbcTokenRepositoryImpl tr = new JdbcTokenRepositoryImpl();
		tr.setJdbcTemplate(jdbc);
		return tr;
	}
}
