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
package hu.petabyte.redflags.web.svc;

import hu.petabyte.redflags.web.model.Account;
import hu.petabyte.redflags.web.model.AccountRepo;
import hu.petabyte.redflags.web.model.WebUser;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author Zsolt Jurányi
 */
@Service
public class AccountSvc {

	private @Autowired AccountRepo accounts;

	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

	public List<Account> findAll() {
		return (List<Account>) accounts.findAll();
	}

	public long getCurrentUserId() {
		Account a = getCurrent();
		return (null == a) ? 0 : a.getId();
	}

	public Account getCurrent() {
		try {
			WebUser user = (WebUser) SecurityContextHolder.getContext()
					.getAuthentication().getPrincipal();
			return user.getAccount();
		} catch (Exception e) {
			return null;
		}
	}

	public void saveAccountAndCryptPassword(Account account) {
		account.setCryptedPassword(encoder.encode(account.getCryptedPassword()));
		accounts.save(account);
	}

}
