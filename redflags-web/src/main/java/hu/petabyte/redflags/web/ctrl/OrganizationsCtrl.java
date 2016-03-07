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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import hu.petabyte.redflags.web.svc.OrganizationsSvc;
import hu.petabyte.redflags.web.util.PagerCalculator;

/**
 * @author Zsolt Jurányi
 */
@Controller
public class OrganizationsCtrl {

	private static List<Integer> counts = new ArrayList<Integer>();

	static {
		counts.addAll(Arrays.asList(5, 10, 25, 50));
	}

	private @Autowired OrganizationsSvc organizations;

	@RequestMapping("/organizations/{count}/{page}")
	public String organizations(Map<String, Object> m, @PathVariable Integer count, @PathVariable Integer page,
			String filter) {

		// check arguments
		if (null != filter) {
			filter = filter.trim();
			if (filter.isEmpty()) {
				return String.format("redirect:/organizations/%d/%d", 10, page);
			}
		}

		if (null == count || !counts.contains(count)) {
			return String.format("redirect:/organizations/%d/%d%s", 10, page,
					null == filter ? "" : "?filter=" + filter);
		}

		if (null == page || page < 1) {
			return String.format("redirect:/organizations/%d/%d%s", count, 1,
					null == filter ? "" : "?filter=" + filter);
		}

		// count
		long time = -System.currentTimeMillis();
		long filteredCount = organizations.count(filter);
		long allCount = (null != filter) ? organizations.count(null) : filteredCount;
		time += System.currentTimeMillis();
		PagerCalculator pager = new PagerCalculator(filteredCount, count, page - 1);
		List<Map<String, Object>> objs;
		if (0 == filteredCount) {
			objs = new ArrayList<Map<String, Object>>();
		} else {

			// check page
			if (page > pager.getPageCount()) {
				return String.format("redirect:/organizations/%d/%d%s", count, pager.getPageCount(),
						null == filter ? "" : "?filter=" + filter);
			}

			// query
			long time2 = -System.currentTimeMillis();
			objs = organizations.query(pager.getPerPage(), pager.getOffset(), filter);
			time2 += System.currentTimeMillis();
			time += time2;
			if (null == objs) {
				return "redirect:/list/error";
			}
		}

		m.put("allCount", allCount);
		m.put("counts", counts);
		m.put("filter", filter);
		m.put("filteredCount", filteredCount);
		m.put("objs", objs);
		m.put("page", pager.getPageIndex() + 1);
		m.put("pages", pager.getPageCount());
		m.put("pageTitleLabel", "organizations.title");
		m.put("queryTime", time);
		return "organizations";
	}

	@RequestMapping({ "/organizations/{count}", "/organizations/{count}/" })
	public String organizations(Map<String, Object> m, @PathVariable Integer count,
			@RequestParam(value = "filter", required = false, defaultValue = "") String filter) {
		return String.format("redirect:/organizations/%d/1%s", count, (filter.isEmpty() ? "" : "?filter=" + filter));
	}

	@RequestMapping({ "/organizations", "/organizations/" })
	public String organizations(Map<String, Object> m,
			@RequestParam(value = "filter", required = false, defaultValue = "") String filter) {
		return "redirect:/organizations/10/1" + (filter.isEmpty() ? "" : "?filter=" + filter);
	}

}
