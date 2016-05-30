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

import java.util.Locale;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * @author Zsolt Jurányi
 */
@ControllerAdvice
public class LangAdvice {

	@Value("${site.languages:en,hu}")
	private String languages;

	@ModelAttribute("lang")
	public String lang(Locale loc) {
		return loc.getLanguage();
	}

	@ModelAttribute("language")
	public String language(Locale loc) {
		return loc.getDisplayLanguage();
	}

	@ModelAttribute("languages")
	public String[] languages() {
		return languages.split(",");
	}
}
