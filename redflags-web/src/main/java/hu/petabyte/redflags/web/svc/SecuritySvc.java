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

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

/**
 * @author Zsolt Jurányi
 */
@Service
public class SecuritySvc implements CaptchaValidator {

	private @Autowired AccountRepo accounts;
	private @Autowired AccountSvc accountService;
	private @Autowired CaptchaValidatorSvc captchaValidator;
	private @Autowired EmailSvc email;
	private @Autowired MessageSource msg;
	private @Value("${site.useCaptcha:true}") boolean useCaptcha;

	public boolean activate(long id) {
		Account a = accounts.findOne(id);
		if (null == a) {
			return false;
		} else {
			a.setActive(true);
			a.setRememberToken(null);
			a.setRememberTokenExpiresAt(null);
			accounts.save(a);
			return true;
		}
	}

	public boolean changePassword(long id, String password) {
		Account a = accounts.findOne(id);
		if (null != a && null != password || !password.isEmpty()) {
			a.setCryptedPassword(password);
			a.setRememberToken(null);
			a.setRememberTokenExpiresAt(null);
			accountService.saveAccountAndCryptPassword(a);
			return true;
		} else {
			return false;
		}
	}

	public String initiatePasswordChange(String email, String urlFormat,
			Locale loc) {
		Account a = accounts.findByEmailAddress(email);
		if (null == a || null == a.getActive() || !a.getActive()) {
			return "forgot.notfound";
		} else {
			return sendEmailConfirmation(a, urlFormat, "forgot", loc);
		}
	}

	public String initiateRegistration(String email, String password,
			String name, String urlFormat, Locale loc) {
		if (null == email || null == password || null == name
				|| email.isEmpty() || password.isEmpty() || name.isEmpty()) {
			return "register.empty";
		}
		if (null != accounts.findByEmailAddress(email)) {
			return "register.taken";
		}
		Account a = new Account(password, null, null, name, email, new Date(),
				false);
		accountService.saveAccountAndCryptPassword(a);
		return sendEmailConfirmation(a, urlFormat, "register", loc);
	}

	public boolean isUseCaptcha() {
		return useCaptcha;
	}

	private String sendEmailConfirmation(Account a, String urlFormat,
			String labelPrefix, Locale loc) {
		String token = UUID.randomUUID().toString();
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.DATE, 1);
		a.setRememberToken(token);
		a.setRememberTokenExpiresAt(c.getTime());
		accounts.save(a);

		String to = a.getEmailAddress();
		String subject = String
				.format(msg.getMessage(
						String.format("%s.%s", labelPrefix, "mail.subject"),
						null, loc));
		String text = String.format(msg.getMessage(
				String.format("%s.%s", labelPrefix, "mail"), null, loc), a
				.getName(), String.format(urlFormat, a.getId(), token));

		this.validateToken(a.getId(), token); // TODO remove it - temporary autoconfirm
		this.activate(a.getId()); // ... and this
		if (false/*email.send(to, subject, text)*/) {
			return String.format("%s.%s", labelPrefix, "sent");
		} else {
			return String.format("%s.%s", labelPrefix, "failed");
		}
	}

	@Override
	public boolean validateCaptcha(String response) {
		// site.useCaptcha property
		return useCaptcha ? captchaValidator.validateCaptcha(response) : true;
	}

	public boolean validateToken(long id, String token) {
		if (null == token || token.isEmpty()) {
			return false;
		}
		Account a = accounts.findOne(id);
		if (null == a || null == a.getRememberToken()
				|| null == a.getRememberTokenExpiresAt()
				|| a.getRememberToken().isEmpty()) {
			return false;
		}
		Calendar now = Calendar.getInstance();
		now.setTime(new Date());
		Calendar rememberDate = Calendar.getInstance();
		rememberDate.setTime(a.getRememberTokenExpiresAt());
		if (!token.equals(a.getRememberToken()) || now.after(rememberDate)) {
			return false;
		} else {
			return true;
		}
	}
}
