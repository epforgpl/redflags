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

import hu.petabyte.redflags.web.model.Filter;
import hu.petabyte.redflags.web.svc.AccountSvc;
import hu.petabyte.redflags.web.svc.FilterSvc;
import hu.petabyte.redflags.web.svc.IndicatorsSvc;
import hu.petabyte.redflags.web.svc.NoticesSvc;
import hu.petabyte.redflags.web.util.Filters;
import hu.petabyte.redflags.web.util.PagerCalculator;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Zsolt Jurányi
 */
@Controller
public class NoticesCtrl {

	private static List<Integer> counts = new ArrayList<Integer>();

	static {
		counts.addAll(Arrays.asList(5, 10, 25, 50));
	}

	private @Autowired AccountSvc accounts;
	private @Autowired FilterSvc filterSvc;
	private @Autowired NoticesSvc notices;
	private @Autowired IndicatorsSvc indicators;

	@RequestMapping("/notices/{count}/{page}")
	public String notices(
			Map<String, Object> m,
			@PathVariable Integer count,
			@PathVariable Integer page,
			@RequestParam(value = "filter", required = false, defaultValue = "") String filter) {
		return String.format("redirect:/notices/%d/%d/by-date%s", count, page,
				(filter.isEmpty() ? "" : "?filter=" + filter));
	}

	@RequestMapping("/notices/{count}/{page}/{order}")
	public String notices(
			Map<String, Object> m,
			@PathVariable Integer count,
			@PathVariable Integer page,
			@PathVariable String order,
			@RequestParam(value = "saveFilter", required = false) Object saveFilter,
			@RequestParam(value = "filter", required = false) String filter,
			@RequestParam(value = "contr", required = false) String filterContr,
			@RequestParam(value = "cpv", required = false) String filterCpv,
			@RequestParam(value = "date", required = false) String filterDate,
			@RequestParam(value = "doc", required = false) String filterDoc,
			@RequestParam(value = "flags", required = false) String filterFlags,
			@RequestParam(value = "indicators", required = false) String[] filterIndicators,
			@RequestParam(value = "text", required = false) String filterText,
			@RequestParam(value = "value", required = false) String filterValue,
			@RequestParam(value = "winner", required = false) String filterWinner)
			throws UnsupportedEncodingException {

		// check arguments
		boolean orderByFlags = "by-flags".equals(order);
		if (!orderByFlags && !"by-date".equals(order)) {
			return String
					.format("redirect:/notices/%d/%d/by-date", count, page);
		}

		if (null == count || !counts.contains(count)) {
			return String.format("redirect:/notices/%d/%d/%s", 10, 1, order);
		}

		if (null == page || page < 1) {
			return String.format("redirect:/notices/%d/%d/%s", count, 1, order);
		}

		// map filters

		if (null == filter) {
			filter = "";
		}
		String rawFilter = filter;
		Filters filters = new Filters();
		filters.add("contr", filterContr).add("cpv", filterCpv)
				.add("date", filterDate).add("doc", filterDoc)
		.add("flags", filterFlags)
				.add("indicators", Arrays.toString(filterIndicators))
				.add("text", filterText).add("value", filterValue)
				.add("winner", filterWinner);
		String parFilter = filters.asString();
		if (parFilter.isEmpty()) {
			filters = new Filters(rawFilter);
			filter = filters.asString();
		} else {
			filter = parFilter;
		}

		if (null != saveFilter) {
			return saveFilter(filters.asString());
		}

		if (!filter.equals(rawFilter)) {
			String ef = URLEncoder.encode(filter, "UTF-8");
			return String.format("redirect:/notices/%d/%d/%s?filter=%s", count,
					1, order, ef);
		}

		Map<String, String> filterMap = new HashMap<String, String>();
		m.put("filters", filterMap);
		for (Filter f : filters.asList()) {
			filterMap.put(f.getType(), f.getParameter());
		}
		List<Filter> filtersList = filters.asList();
		filterSvc.fillOrgNames(filtersList);
		m.put("filtersList", filtersList);

		// count
		long time = -System.currentTimeMillis();
		long filteredCount = notices.count(filters.asList());
		long allCount = (null != filter) ? notices.count(null) : filteredCount;
		List<Map<String, Object>> docTypes = notices.docTypes();
		time += System.currentTimeMillis();
		PagerCalculator pager = new PagerCalculator(filteredCount, count,
				page - 1);
		List<Map<String, Object>> objs;
		if (0 == filteredCount) {
			objs = new ArrayList<Map<String, Object>>();
		} else {

			// check page
			if (page > pager.getPageCount()) {
				return String.format("redirect:/notices/%d/%d%s", count,
						pager.getPageCount(), null == filter ? "" : "?filter="
								+ filter);
			}

			// query
			long time2 = -System.currentTimeMillis();
			objs = notices.query(pager.getPerPage(), pager.getOffset(),
					orderByFlags, filters.asList());
			time2 += System.currentTimeMillis();
			time += time2;
			if (null == objs) {
				return "redirect:/list/error";
			}
		}

		m.put("allCount", allCount);
		m.put("counts", counts);
		m.put("order", order);
		m.put("docTypes", docTypes);
		m.put("indicators", indicators.getIndicators());
		m.put("filter", filter);
		m.put("filteredCount", filteredCount);
		m.put("objs", objs);
		m.put("page", pager.getPageIndex() + 1);
		m.put("pages", pager.getPageCount());
		m.put("pageTitleLabel", "notices.title");
		m.put("queryTime", time);
		return "notices";
	}

	@RequestMapping({ "/notices/{count}", "/notices/{count}/" })
	public String notices(
			Map<String, Object> m,
			@PathVariable Integer count,
			@RequestParam(value = "filter", required = false, defaultValue = "") String filter) {
		return String.format("redirect:/notices/%d/1/by-date%s", count,
				(filter.isEmpty() ? "" : "?filter=" + filter));
	}

	@RequestMapping({ "/notices", "/notices/" })
	public String notices(
			Map<String, Object> m,
			@RequestParam(value = "filter", required = false, defaultValue = "") String filter) {
		return "redirect:/notices/10/1/by-date"
				+ (filter.isEmpty() ? "" : "?filter=" + filter);
	}

	private String saveFilter(String filter) {
		long id = filterSvc.saveNew(accounts.getCurrentUserId(), filter);
		return "redirect:/filter/" + id;
	}
}
