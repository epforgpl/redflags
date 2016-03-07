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

import hu.petabyte.redflags.web.model.Filter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author Zsolt Jurányi
 */
@Deprecated
public class FilterString {

	private static final String FILTER_TYPES = "cpv|contr|date|doc|text|value|winner";
	private static final String REPEATABLE_FILTER_TYPES = "";

	public static String fromFilters(List<Filter> filters) {
		StringBuilder sb = new StringBuilder();
		for (Filter f : filters) {
			sb.append(f.toString());
			sb.append("|");
		}
		return sb.toString().replaceAll("\\|$", "");
	}

	private static Filter parseFilter(String filter) {
		String[] p = filter.split(":", 2);
		if (p.length == 2) {
			String type = p[0].toLowerCase();
			if (!validFilterType(type)) {
				return null;
			}
			String parameter = p[1].replaceAll("[\"'`;]+|--", " ")
					.replaceAll(" +", " ").trim();

			// validation, cleaning where needed
			if ("cpv".equals(type)) {
				parameter = parameter.replaceAll("[^0-9,]", "");
				if (!parameter.matches("\\d{8}(,\\d{8})*")) {
					return null;
				}

			} else if ("date".equals(type)) {
				if (parameter.matches("\\d{4}-\\d{2}-\\d{2}")) {
					parameter = String.format("%s:%s", parameter, parameter);
				}
				if (!parameter
						.matches("\\d{4}-\\d{2}-\\d{2}:\\d{4}-\\d{2}-\\d{2}")) {
					return null;
				}

			} else if ("doc".equals(type)) {
				parameter = parameter.toUpperCase();
				if (!parameter.matches("[A-Z0-9]")) {
					return null;
				}

			} else if ("value".equals(type)) {
				parameter = parameter.replaceAll("[^0-9\\-]", "");
				if (parameter.matches("\\d+")) {
					parameter = String.format("%s-%s", parameter, parameter);
				}
				if (!parameter.matches("\\d+-\\d+")) {
					return null;
				}

			}
			return new Filter(type, parameter);
		}
		return null;
	}

	private static boolean repeatableFilter(Filter f) {
		return REPEATABLE_FILTER_TYPES.contains(f.getType());
	}

	public static List<Filter> toFilters(String filterList) {
		List<Filter> filters = new ArrayList<Filter>();
		String currentFilters = "";
		for (String filter : filterList.split("\\|")) {
			Filter f = parseFilter(filter);
			if (null != f
					&& (repeatableFilter(f) || !currentFilters.contains(f
							.getType()))) {
				currentFilters += f.getType() + "|";
				filters.add(f);
			}
		}
		Collections.sort(filters, new Comparator<Filter>() {

			@Override
			public int compare(Filter arg0, Filter arg1) {
				if (arg0.getType().equalsIgnoreCase(arg1.getType())) {
					return arg0.getParameter().compareTo(arg1.getParameter());
				} else {
					return arg0.getType().compareTo(arg1.getType());
				}
			}
		});
		return filters;
	}

	public static String validate(String filters) {
		return fromFilters(toFilters(filters));
	}

	private static boolean validFilterType(String t) {
		return FILTER_TYPES.contains(t);
	}
}
