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
package hu.petabyte.redflags.filter;

import hu.petabyte.redflags.web.util.Filters;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

/**
 * @author Zsolt Jurányi
 */
public class FiltersDev {

	@Test
	public void test() throws IOException, TemplateException {
		Filters filterList = new Filters(
				"cpv:90000000,91000000|value:0-10000000000|contr:a|date:2011-01-01:2015-03-31");
		// List<Filter> filters = FilterString
		// .toFilters("cpv:90000000,91000000|value:0-10000000000|contr:a|date:2011-01-01:2015-03-31");
		// System.out.println(FilterString.fromFilters(filters));
		System.out.println(filterList.asString());

		// freemarker test
		Configuration cfg = new Configuration(Configuration.VERSION_2_3_21);
		cfg.setClassForTemplateLoading(FiltersDev.class, "/queries/");
		cfg.setDefaultEncoding("UTF-8");
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

		Template temp = cfg.getTemplate("notices-sql.ftl");

		Map<String, Object> root = new HashMap<String, Object>();
		root.put("filters", filterList.asList());

		OutputStream os = new ByteArrayOutputStream();
		Writer out = new OutputStreamWriter(os);
		temp.process(root, out);
		out.close();
		StringBuilder sb = new StringBuilder();
		for (String line : os.toString().split("\n")) {
			line = line.replaceAll("\\s+$", "");
			if (!line.isEmpty()) {
				sb.append(line);
				sb.append("\n");
			}
		}
		System.out.println(sb.toString());
	}
}
