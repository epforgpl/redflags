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

import hu.petabyte.redflags.web.svc.SecuritySvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 *
 * @author Zsolt Jurányi
 *
 */
@ControllerAdvice
public class SiteAdvice {

	private @Autowired SecuritySvc security;

	@Value("${site.message:}")
	private String message;

	@ModelAttribute("message")
	public String getMessage() {
		return message;
	}

	@ModelAttribute("printCaptcha")
	public boolean getPrintCaptcha() {
		return security.isUseCaptcha();
	}

}
