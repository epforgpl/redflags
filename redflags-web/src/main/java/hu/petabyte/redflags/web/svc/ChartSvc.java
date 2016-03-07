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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * @author Zsolt Jurányi
 */
@Service
public class ChartSvc {

	// TODO mi lenne ha mégis initben csinálná, aztán scheduled módon update?

	public static class FlagCounts {
		private final Map<Long, Long> flagCountFrequency = new HashMap<Long, Long>();
		private Long flaggedCount = 0L;
		private Long notFlaggedCount = 0L;

		public Map<Long, Long> getFlagCountFrequency() {
			return flagCountFrequency;
		}

		public Long getFlaggedCount() {
			return flaggedCount;
		}

		public Long getNotFlaggedCount() {
			return notFlaggedCount;
		}

	}

	public static class FlaggedNotices {
		private final List<String> categories = new ArrayList<String>();
		private final List<Integer> noticeCounts = new ArrayList<Integer>();
		private final List<Integer> flaggedNoticeCounts = new ArrayList<Integer>();

		public List<String> getCategories() {
			return categories;
		}

		public List<Integer> getFlaggedNoticeCounts() {
			return flaggedNoticeCounts;
		}

		public List<Integer> getNoticeCounts() {
			return noticeCounts;
		}

	}

	private @Autowired JdbcTemplate jdbc;

	public List<Integer> flaggedNoticeCountsPerQuarter() {
		return jdbc
				.queryForList(
				// "select count(*) from rfwl_notices where flagCount is not null or flagCount > 0 group by year(date), quarter(date)",
						"select c.c from (select year(date) y, quarter(date) q from rfwl_notices n group by y, q order by date) q"
								+ " left join (select year(date) y, quarter(date) q, count(*) c from rfwl_notices where flagCount is not null or flagCount > 0 group by y, q order by date) c"
								+ "	on q.y = c.y and q.q = c.q", Integer.class);
	}

	public FlagCounts getResult() {
		FlagCounts result = new FlagCounts();

		// flag count donut

		Map<Long, Long> fcfq = result.getFlagCountFrequency();
		List<Map<String, Object>> r = jdbc
				.queryForList("select flagCount, count(id) as 'frequency' from rfwl_notices group by flagCount");
		long max = 0;
		for (Map<String, Object> m : r) {
			Long fc = (Long) m.get("flagCount");
			Long fq = (Long) m.get("frequency");
			if (null == fc) {
				fc = 0L;
				result.notFlaggedCount += fq;

			} else {
				max = Math.max(max, fc);
				result.flaggedCount += fq;
			}
			fcfq.put(fc, fq);
		}
		for (long i = 0L; i <= max; i++) {
			if (!fcfq.containsKey(i)) {
				fcfq.put(i, 0L);
			}
		}

		// flag count frequency per quarter
		// r =
		// jdbc.queryForList("select concat(year(n.date), ' - ', quarter(n.date)) 'ym', flagCount, count(*) 'c' from rfwl_notices n group by year(n.date), quarter(n.date), flagCount");
		// result.quarters = jdbc
		// .queryForList(
		// "select distinct concat(year(date), ' - ', quarter(date)) from rfwl_notices order by date",
		// String.class);

		// series per flag count
		// serie data element: freq (by quarter, but fill nulls!)
		return result;
	}

	public List<Integer> noticeCountsPerQuarter() {
		return jdbc
				.queryForList(
						"select count(*) from rfwl_notices group by year(date), quarter(date)",
						Integer.class);
	}

	public List<String> quarters() {
		return jdbc
				.queryForList(
						"select distinct concat(year(date), ' - ', quarter(date)) from rfwl_notices order by date",
						String.class);
	}

	public String sumValueCSV() {
		StringBuilder sb = new StringBuilder();
		sb.append("Categories,NC-1,NC-2,NC-4\n");
		try {
			List<Map<String, Object>> list = jdbc
					.queryForList("select y, group_concat(v) v from rfwl_barplot group by y");
			for (Map<String, Object> m : list) {
				sb.append(m.get("y").toString());
				sb.append(",");
				sb.append(m.get("v").toString());
				sb.append("\n");
			}
		} catch (Exception e) {
		}
		return sb.toString();
	}
}
