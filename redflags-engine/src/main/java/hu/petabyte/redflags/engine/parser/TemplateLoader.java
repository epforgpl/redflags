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
package hu.petabyte.redflags.engine.parser;

import hu.petabyte.redflags.engine.model.Notice;
import hu.petabyte.redflags.engine.util.IOUtils;
import hu.petabyte.redflags.engine.util.MappingUtils;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Zsolt Jurányi
 *
 */
public class TemplateLoader {
	private static final Logger LOG = LoggerFactory
			.getLogger(TemplateLoader.class);

	public static String getTemplate(String name) {
		try {
			String t = IOUtils.loadUTF8Resource("templates/" + name + ".tpl");
			if (t.trim().matches("SAME AS .*")) {
				String name2 = t.trim().substring(8);
				LOG.debug("Template redirection {} -> {}", name, name2);
				t = IOUtils.loadUTF8Resource("templates/" + name2 + ".tpl");
			}
			return t;
		} catch (IOException e) {
			LOG.error("Could not load template: {}", name);
			return null;
		}
	}

	public static String getTemplateFor(Notice notice, String language) {
		String td = (String) MappingUtils.getDeepPropertyIfExists(notice,
				"data.documentType.id");
		String di = (String) MappingUtils.getDeepPropertyIfExists(notice,
				"data.directive");
		if (null != di) {
			di = di.replaceAll(".*\\(", "").replaceAll("\\).*", "")
					.replaceAll("/", "");
		}
		if (null != td && td.matches("^TD-.$")) {
			String name = String.format("%s-%s", td, language);
			String template = null;
			if (null != di) {
				template = getTemplate(name + "-" + di);
			}
			if (null == template) { // if no DI or no template for DI
				template = getTemplate(name);
			}
			return template;
		} else {
			LOG.error("Invalid TD field: {}", td);
			return null;
		}
	}
}
