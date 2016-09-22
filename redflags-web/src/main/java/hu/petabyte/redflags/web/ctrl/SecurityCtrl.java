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
package hu.petabyte.redflags.web.ctrl;

import hu.petabyte.redflags.web.model.Account;
import hu.petabyte.redflags.web.model.AccountRepo;
import hu.petabyte.redflags.web.svc.SecuritySvc;

import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Zsolt Jurányi
 */
@Controller
public class SecurityCtrl {

	private @Autowired AccountRepo accounts;
	private @Autowired SecuritySvc svc;
	private @Value("${redflags.url}") String urlPrefix;

	@RequestMapping(value = "/activate/{id}/{token}", method = RequestMethod.GET)
	public String activate(Map<String, Object> m, @PathVariable int id,
			@PathVariable String token) {
		if (!svc.validateToken(id, token)) {
			m.put("messageLabel", "activate.invalid");
			m.put("messageLevel", -1);
		} else if (svc.activate(id)) {
			m.put("messageLabel", "activate.success");
			m.put("messageLevel", 1);
		} else {
			m.put("messageLabel", "activate.error");
			m.put("messageLevel", -1);
		}
		return "login";
	}

	@RequestMapping(value = "/change-password", method = RequestMethod.POST)
	public String changePassword(
			Map<String, Object> m,
			@RequestParam(value = "id", required = true) int id,
			@RequestParam(value = "token", required = true) String token,
			@RequestParam(value = "password", required = true) String password,
			@RequestParam(value = "g-recaptcha-response", required = true) String captcha) {
		if (svc.validateCaptcha(captcha)) {
			if (!svc.validateToken(id, token)) {
				m.put("messageLabel", "change.invalid");
				m.put("messageLevel", -1);
			} else if (svc.changePassword(id, password)) {
				m.put("messageLabel", "change.success");
				m.put("messageLevel", 1);
			} else {
				m.put("messageLabel", "change.error");
				m.put("messageLevel", -1);
			}
			return "login";
		} else {
			return "redirect:/change-password/" + id + "/" + token;
		}
	}

	@RequestMapping(value = "/change-password/{id}/{token}", method = RequestMethod.GET)
	public String changePasswordForm(Map<String, Object> m,
			@PathVariable long id, @PathVariable String token) {
		Account a = accounts.findOne(id);
		m.put("id", id);
		m.put("email", a.getEmailAddress());
		m.put("name", a.getName());
		m.put("token", token);
		if (!svc.validateToken(id, token)) {
			m.put("messageLabel", "change.invalid");
			m.put("messageLevel", -1);
		}
		return "change-password";
	}

	@RequestMapping(value = "/forgot", method = RequestMethod.GET)
	public String forgotForm(Map<String, Object> m) {
		m.put("mode", "forgot");
		return "login";
	}

	@RequestMapping(value = "/forgot", method = RequestMethod.POST)
	public String forgotSend(
			Map<String, Object> m,
			@RequestParam(required = true) String email,
			@RequestParam(value = "g-recaptcha-response", required = false) String captcha,
			Locale loc) {

		if (svc.validateCaptcha(captcha)) {
			String urlFormat = urlPrefix + "/change-password/%d/%s";

			String messageLabel = svc.initiatePasswordChange(email, urlFormat,
					loc);
			m.put("messageLabel", messageLabel);
			m.put("messageLevel", ("forgot.sent".equals(messageLabel) ? 1 : -1));
			m.put("email", email);
		} else {
			m.put("messageLabel", "captcha.error");
			m.put("messageLevel", -1);
		}
		m.put("mode", "forgot");
		return "login";
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String loginForm(Map<String, Object> m) {
		m.put("mode", "login");
		return "login";
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String register(
			Map<String, Object> m,
			@RequestParam(required = true) String email,
			@RequestParam(required = true) String password,
			@RequestParam(required = true) String name,
			@RequestParam(value = "g-recaptcha-response", required = false) String captcha,
			Locale loc) {

		email = email.toLowerCase();
		if (!email.matches("^[a-z0-9\\-.+]+@([a-z0-9\\-]+\\.)+[a-z]{2,}$")) {
			m.put("messageLabel", "register.invalid");
			m.put("messageLevel", -1);
		} else if (svc.validateCaptcha(captcha)) {
			String urlFormat = urlPrefix + "/activate/%d/%s";
			String messageLabel = svc.initiateRegistration(email, password,
					name, urlFormat, loc);
			m.put("messageLabel", messageLabel);
			m.put("messageLevel", ("register.sent".equals(messageLabel) ? 1
					: -1));
			m.put("email", email);
		} else {
			m.put("messageLabel", "captcha.error");
			m.put("messageLevel", -1);
		}
		m.put("mode", "register");
		return "login";
	}

	@RequestMapping("/register")
	public String registerForm(Map<String, Object> m) {
		m.put("mode", "register");
		return "login";
	}
}
