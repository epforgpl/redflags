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
public class OrganizationSvc {

	private @Autowired JdbcTemplate jdbc;
	private @Autowired NoticeSvc notice;
	// private @Autowired NoticesSvc notices; // a bit slower
	private @Value("classpath:/queries/organization-wins.txt") Resource winsQueryRes;
	private String winsQuerySql;

	public Map<String, Object> basic(String id) {
		try {
			return jdbc.queryForMap(
					"select * from te_organization where id = ?", id);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public long callCount(String id) {
		try {
			return jdbc
					.queryForObject(
							"select count(distinct id) from rfwl_notices where contractingOrgId = ?",
							new Object[] { id }, Long.class);
		} catch (Exception ex) {
			ex.printStackTrace();
			return 0L;
		}
	}

	public List<Map<String, Object>> calls(String id) {
		try {
			List<Map<String, Object>> lm = jdbc
					.queryForList(
							"select * from rfwl_notices where contractingOrgId = ? limit 10;",
							id);
			for (Map<String, Object> m : lm) {
				String nid = (String) m.get("id");
				List<Map<String, Object>> flags = notice.flags(nid);
				m.put("flags", flags);
			}
			return lm;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	@PostConstruct
	public void init() {
		try {
			winsQuerySql = new Scanner(winsQueryRes.getInputStream())
					.useDelimiter("\\Z").next();
		} catch (Exception e) {
			e.printStackTrace();
			winsQuerySql = "SELECT * FROM te_notice LIMIT 0";
		}
	}

	public long winCount(String id) {
		try {
			return jdbc
					.queryForObject(
							"select count(distinct substring(relationLeftId, 1, position(\"-A-\" in relationLeftId)-1))\r\n"
									+ "		from te_relationdescriptor\r\n"
									+ "		where additonalInfo = \"winnerOrg\" and relationRightId = ?;",
							new Object[] { id }, Long.class);
		} catch (Exception ex) {
			ex.printStackTrace();
			return 0L;
		}
	}

	public List<Map<String, Object>> wins(String id) {
		try {
			List<Map<String, Object>> lm = jdbc.queryForList(winsQuerySql, id);
			for (Map<String, Object> m : lm) {
				String nid = (String) m.get("id");
				List<Map<String, Object>> flags = notice.flags(nid);
				m.put("flags", flags);
				List<Map<String, Object>> winners = notice.winners(nid);
				m.put("winners", winners);
			}
			return lm;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

}
