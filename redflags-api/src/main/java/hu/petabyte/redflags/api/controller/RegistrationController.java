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
package hu.petabyte.redflags.api.controller;

import hu.petabyte.redflags.api.model.ApiUser;
import hu.petabyte.redflags.api.service.ApiUserService;
import hu.petabyte.redflags.api.service.CaptchaValidatorSvcProxy;
import hu.petabyte.redflags.api.validator.ApiUserValidator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Péter Szűcs
 * @author Zsolt Jurányi
 */
@Controller
public class RegistrationController {

	@Autowired
	private MessageSource msg;

	@Autowired
	private ApiUserService apiUserService;

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private ApiUserValidator apiUserValidator;

	@Autowired
	private CaptchaValidatorSvcProxy captchaValidator;

	@InitBinder("apiUser")
	public void initBinder(WebDataBinder binder) {
		binder.addValidators(apiUserValidator);
	}

	@RequestMapping("/registration")
	public String registration(Model model, Locale locale) {
		model.addAttribute("apiUser", new ApiUser());
		return "registration";
	}

	@RequestMapping("/registration/save")
	public String saveRegistration(
			@Valid @ModelAttribute("apiUser") ApiUser apiUser,
			@RequestParam(value = "g-recaptcha-response", required = true) String captcha,
			BindingResult result, Model model, HttpServletRequest request,
			Locale locale) {
		if (!captchaValidator.validateCaptcha(captcha)) {
			model.addAttribute("apiUser", apiUser);
			model.addAttribute("errorList", Arrays.asList("Invalid captcha."));
			return "/registration";
		}
		if (result.hasErrors()) {
			List<String> errorList = new ArrayList<String>();
			for (ObjectError oe : result.getGlobalErrors()) {
				errorList.add((msg.getMessage(oe.getDefaultMessage(), null,
						locale)));
			}
			model.addAttribute("apiUser", apiUser);
			model.addAttribute("errorList", errorList);
			return "/registration";
		}
		try {
			String token = UUID.randomUUID().toString();

			int cnt = 0;
			while (apiUserService.findByAccessToken(token) != null) {
				token = UUID.randomUUID().toString();
				cnt++;
				if (cnt > 10) {
					model.addAttribute("error", msg.getMessage(
							"registration.errorMessage", null, locale));
					return "/registration";
				}
			}

			apiUser.setAccessToken(token);
			apiUserService.save(apiUser);

			SimpleMailMessage email = new SimpleMailMessage();
			email.setFrom("registration@api.redflags.eu");
			email.setTo(apiUser.getEmail());
			email.setSubject(msg.getMessage("registrationEmail.subject", null,
					locale));
			email.setText("Hello " + apiUser.getName() + ",\n\n"
					+ msg.getMessage("registrationEmail.text", null, locale)
					+ " " + token + ".");
			mailSender.send(email);

			model.addAttribute("success",
					msg.getMessage("registration.successMessage", null, locale));
			return "/registration";
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("error",
					msg.getMessage("registration.errorMessage", null, locale));
			return "/registration";
		}
	}
}
