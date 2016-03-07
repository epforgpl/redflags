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

import hu.petabyte.redflags.web.model.Account;
import hu.petabyte.redflags.web.model.AccountRepo;
import hu.petabyte.redflags.web.model.WebUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @author Zsolt Jurányi
 */
@Configuration
public class SecurityCfg extends
		GlobalAuthenticationConfigurerAdapter {

	@Autowired
	AccountRepo accountRepository;

	@Override
	public void init(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService());
	}

	@Bean
	UserDetailsService userDetailsService() {
		return new UserDetailsService() {

			@Override
			public UserDetails loadUserByUsername(String email)
					throws UsernameNotFoundException {
				Account account = (Account) accountRepository
						.findByEmailAddress(email);
				if (account != null) {
					return new WebUser(account.getEmailAddress(),
							account.getCryptedPassword(), account.getActive(),
							true, true, true,
							AuthorityUtils.createAuthorityList("USER"), account);
				} else {
					throw new UsernameNotFoundException(
							"could not find the user '" + email + "'");
				}
			}

		};
	}
}
