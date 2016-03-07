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

import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * @author Zsolt Jurányi
 */
@Service
public class OrganizationsSvc {
	private @Autowired JdbcTemplate jdbc;
	private @Value("classpath:/queries/organizations-query.txt") Resource queryRes;
	private @Value("classpath:/queries/organizations-count.txt") Resource countRes;
	private String countSql, querySql;

	@PostConstruct
	public void init() {
		try {
			countSql = new Scanner(countRes.getInputStream()).useDelimiter(
					"\\Z").next();
		} catch (Exception e) {
			e.printStackTrace();
			countSql = "SELECT COUNT(*) FROM rfwl_organizations";
		}
		try {
			querySql = new Scanner(queryRes.getInputStream()).useDelimiter(
					"\\Z").next();
		} catch (Exception e) {
			e.printStackTrace();
			querySql = "SELECT * FROM rfwl_organizations LIMIT 0";
		}
	}

	private String addFilter(String sql, String filter) {
		if (null != filter && !(filter = filter.trim()).isEmpty()) {
			sql = sql.replace("-- FILTER", "").replaceAll("LIKE",
					filter.replaceAll("^| +|$", "%"));
		} else {
			sql = sql.replace("-- NOFILTER", "");
		}
		return sql;
	}

	public long count(String filter) {
		try {
			return jdbc.queryForObject(addFilter(countSql, filter), Long.class);
		} catch (Exception ex) {
			ex.printStackTrace();
			return 0L;
		}
	}

	public List<Map<String, Object>> query(int perPage, long offset,
			String filter) {
		try {
			return jdbc.queryForList(addFilter(querySql, filter), perPage,
					offset);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
}
