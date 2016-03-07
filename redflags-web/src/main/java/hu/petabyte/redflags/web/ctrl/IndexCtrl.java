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

import java.io.IOException;
import java.nio.charset.Charset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Zsolt Jurányi
 */
@Controller
public class IndexCtrl {

	private @Value("classpath:/robots.txt") Resource robotsTxt;
	private @Autowired VersionAdvice ver;

	@RequestMapping("/*")
	public String defaultRedirect() {
		return "redirect:/";
	}

	@RequestMapping("/")
	public String index() {
		return "index";
	}

	@RequestMapping("/version")
	@ResponseBody
	public String version() {
		StringBuilder s = new StringBuilder();
		s.append("<pre>");
		s.append("\nVersion = " + ver.getVersion());
		s.append("\nStarted = " + ver.getBuilt());
		s.append("</pre>");
		return s.toString();
	}

	@RequestMapping("/robots.txt")
	@ResponseBody
	public String robots() {
		try {
			return StreamUtils.copyToString(robotsTxt.getInputStream(),
					Charset.forName("UTF-8"));
		} catch (IOException e) {
			return "";
		}
	}

}
