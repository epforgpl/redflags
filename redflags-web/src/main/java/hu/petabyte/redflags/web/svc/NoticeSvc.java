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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * @author Zsolt Jurányi
 */
@Service
public class NoticeSvc {

	private @Autowired JdbcTemplate jdbc;

	public List<Map<String, Object>> winners(String id) {
		try {
			return jdbc
					.queryForList(
							"select distinct winnerOrgId id, winnerOrgName name from rfwl_winners where noticeId = ? order by winnerOrgName;",
							id);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public List<Map<String, Object>> awards(String id) {
		try {
			return jdbc
					.queryForList(
							"SELECT a.*,"
									+ "(select o.id from te_organization o inner join te_relationdescriptor r on r.relationRightId = o.id where r.relationLeftId = a.id limit 1) 'winner_id',"
									+ "(select o.name from te_organization o inner join te_relationdescriptor r on r.relationRightId = o.id where r.relationLeftId = a.id limit 1) 'winner_name'"
									+ " from te_award a WHERE id LIKE ?", id
									+ "-A-%");
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public Map<String, Object> basic(String id) {
		try {
			return jdbc.queryForMap("select * from te_notice where id = ?", id);
		} catch (Exception ex) {
			// ex.printStackTrace();
			return null;
		}
	}

	public Map<String, Object> compl(String id) {
		try {
			return jdbc.queryForMap(
					"select * from te_complementaryinfo where id = ?", id);
		} catch (Exception ex) {
			// ex.printStackTrace();
			return null;
		}
	}

	public Map<String, Object> contr(String id) {
		try {
			return jdbc
					.queryForMap(
							"select * from te_organization o where id = (select contractingOrgId from rfwl_notices where id = ?)",
							id);
		} catch (Exception ex) {
			// ex.printStackTrace();
			return null;
		}
	}

	public Map<String, Object> data(String id) {
		try {
			Map<String, Object> m = jdbc.queryForMap(
					"select * from te_data where noticeId = ?", id);
			List<Map<String, Object>> dts = jdbc
					.queryForList(
							"select r.additonalInfo prop, r.relationRightId, t.id, t.name from te_relationdescriptor r inner join te_datatype t on t.id=r.relationRightId where r.relationLeftId=?",
							id);
			for (Map<String, Object> dt : dts) {
				m.put(dt.get("prop").toString(), dt.get("name"));
				m.put(dt.get("prop").toString() + "Id", dt.get("id"));
			}
			try {
				m.put("cpvCodes",
						jdbc.queryForList(
								"select distinct c.* from te_cpv c inner join te_relationdescriptor r on r.relationRightId = c.id where r.relationLeftId=? order by c.id",
								id));
			} catch (Exception ex) {
			}
			return m;
		} catch (Exception ex) {
			// ex.printStackTrace();
			return null;
		}
	}

	public List<Map<String, Object>> flags(String id) {
		try {
			return jdbc.queryForList("select * from te_flag where id like ?",
					id + "-%");
		} catch (Exception ex) {
			return new ArrayList<Map<String, Object>>();
		}
	}

	public List<Map<String, Object>> cnFlags(String id) {
		try {
			List<Map<String, Object>> m;
			m = jdbc.queryForList(
					"select typeID t, documentFamilyId d from rfwl_notices where id = ?",
					id);
			String typeId = (String) m.get(0).get("t");
			String docFamilyId = (String) m.get(0).get("d");
			if ("TD-7".equals(typeId)) {
				m = jdbc.queryForList(
						"select id from rfwl_notices where documentFamilyId = ? and typeID = ?",
						docFamilyId, "TD-3");
				return flags((String) m.get(0).get("id"));
			}
		} catch (Exception ex) {
			// ex.printStackTrace();
		}
		return null;
	}

	public Map<String, Object> left(String id) {
		try {
			return jdbc.queryForMap("select * from te_leftinfo where id = ?",
					id);
		} catch (Exception ex) {
			// ex.printStackTrace();
			return null;
		}
	}

	public List<Map<String, Object>> lots(String id) {
		try {
			List<Map<String, Object>> lots = jdbc.queryForList(
					"SELECT * FROM te_lot WHERE noticeId = ?", id);
			for (Map<String, Object> lot : lots) {
				try {
					lot.put("differentDuration",
							jdbc.queryForMap(
									"select d.* from te_duration d inner join te_relationdescriptor r on r.relationRightId = d.id where r.relationLeftId=?",
									lot.get("id").toString()));
				} catch (Exception e) {
				}
				try {
					lot.put("cpvCodes",
							jdbc.queryForList(
									"select c.* from te_cpv c inner join te_relationdescriptor r on r.relationRightId = c.id where r.relationLeftId=? order by c.id",
									lot.get("id").toString()));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return lots;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public List<Map<String, Object>> objs(String id) {
		try {
			List<Map<String, Object>> objs = jdbc.queryForList(
					"select * from te_objofthecontract where noticeId = ?", id);
			for (Map<String, Object> obj : objs) {
				String oid = obj.get("id").toString();
				try {
					obj.put("duration", jdbc.queryForMap(
							"select * from te_duration d where id = ?", oid
									+ "-CONTRACT-DUR"));
				} catch (Exception ex) {
				}

				try {
					obj.put("renewalDuration", jdbc.queryForMap(
							"select * from te_duration d where id = ?", oid
									+ "-RENEWAL-DUR"));
				} catch (Exception ex) {
				}
			}
			return objs;
		} catch (Exception ex) {
			// ex.printStackTrace();
			return null;
		}
	}

	public Map<String, Object> proc(String id) {
		try {
			return jdbc.queryForMap("select * from te_procedure where id = ?",
					id);
		} catch (Exception ex) {
			// ex.printStackTrace();
			return null;
		}
	}

	public List<Map<String, Object>> related(String id) {
		try {
			List<Map<String, Object>> lm = jdbc
					.queryForList(
							"select * from rfwl_notices where documentFamilyId = (select documentFamilyId from te_notice where id = ?)",
							id);
			for (Map<String, Object> m : lm) {
				String rid = (String) m.get("id");
				List<Map<String, Object>> flags = flags(rid);
				m.put("flags", flags);
			}
			return lm;
		} catch (Exception ex) {
			// ex.printStackTrace();
			return null;
		}
	}
}
