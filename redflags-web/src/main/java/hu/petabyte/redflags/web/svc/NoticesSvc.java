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

import hu.petabyte.redflags.web.App;
import hu.petabyte.redflags.web.model.Filter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

/**
 * @author Zsolt Jurányi
 */
@Service
public class NoticesSvc {

	private static final String FTL = "notices-sql.ftl";

	private Configuration cfg;
	private Template tpl;
	private @Autowired JdbcTemplate jdbc;
	private @Autowired NoticeSvc notice;

	public long count(List<Filter> filters) {
		try {
			return jdbc.queryForObject(sql(filters, true, false), Long.class);
		} catch (Exception ex) {
			ex.printStackTrace();
			return 0L;
		}
	}

	public List<Map<String, Object>> docTypes() {
		try {
			return jdbc
					.queryForList("select distinct substring(typeId, 4, 1) as id, type as name from rfwl_notices order by id");
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<Map<String, Object>>();
		}
	}

	@PostConstruct
	public void init() throws IOException {
		cfg = new Configuration(Configuration.VERSION_2_3_21);
		cfg.setClassForTemplateLoading(App.class, "/queries/");
		cfg.setDefaultEncoding("UTF-8");
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
		tpl = cfg.getTemplate(FTL);
	}

	public Map<String, Object> lastNotice() {
		try {
			return jdbc
					.queryForMap("select noticeNumber, noticeYear from te_notice order by noticeYear desc, noticeNumber desc limit 1");
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public List<Map<String, Object>> query(int perPage, long offset,
			boolean orderByFlags, List<Filter> filters) {
		try {
			List<Map<String, Object>> lm = jdbc.queryForList(
					sql(filters, false, orderByFlags), perPage, offset);
			for (Map<String, Object> m : lm) {
				String id = (String) m.get("id");
				List<Map<String, Object>> flags = notice.flags(id);
				m.put("flags", flags);
				List<Map<String, Object>> winners = notice.winners(id);
				m.put("winners", winners);
			}
			return lm;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private String sql(List<Filter> filters, boolean counting,
			boolean orderByFlags) throws TemplateException, IOException {
		Map<String, Object> root = new HashMap<String, Object>();
		root.put("filters", filters);
		if (counting) {
			root.put("counting", true);
		}
		if (orderByFlags) {
			root.put("orderByFlags", true);
		}
		OutputStream os = new ByteArrayOutputStream();
		Writer out = new OutputStreamWriter(os);
		tpl.process(root, out);
		out.close();
		StringBuilder sb = new StringBuilder();
		for (String line : os.toString().split("\n")) {
			line = line.replaceAll("\\s+$", "");
			if (!line.isEmpty()) {
				sb.append(line);
				sb.append("\n");
			}
		}
		return sb.toString();
	}
}
