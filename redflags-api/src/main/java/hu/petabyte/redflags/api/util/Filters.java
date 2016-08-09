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
package hu.petabyte.redflags.api.util;

import hu.petabyte.redflags.web.model.Filter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Zsolt Jurányi
 */
public class Filters {

	private static final String FILTER_TYPES = "after|cpv|contr|date|doc|flags|indicators|text|value|winner|namelike|fromdate|todate";

	private final List<Filter> FILTERS = new ArrayList<Filter>();

	public Filters() {
	}

	public Filters(List<Filter> f) {
		add(f);
	}

	public Filters(String s) {
		add(s);
	}

	public Filters add(Filter filter) {
		addOne(asString(filter));
		return this;
	}

	public Filters add(List<Filter> filters) {
		for (Filter filter : filters) {
			add(filter);
		}
		return this;
	}

	public Filters add(String... s) {
		int n = s.length - s.length % 2;
		for (int i = 0; i < n; i += 2) {
			add(s[i], s[i + 1]);
		}
		return this;
	}

	public Filters add(String s) {
		for (String filter : s.split("\\|")) {
			addOne(filter);
		}
		return this;
	}

	public Filters add(String t, String p) {
		if (null != t && null != p) {
			addOne(String.format("%s:%s", t, p));
		}
		return this;
	}

	private void addOne(String s) {
		Filter filter = parse(s);
		if (null != filter) {
			FILTERS.add(filter);
		}
	}

	public List<Filter> asList() {
		return FILTERS;
	}
	
	public List<Filter> asListForQuery() {
		List<Filter> result = new ArrayList<Filter>();
		for (Filter filter : FILTERS) {
			if(!filter.getType().equals("fromDate") && !filter.getType().equals("toDate")){
				result.add(filter);
			}
		}
		return result;
	}

	public String asString() {
		StringBuilder sb = new StringBuilder();
		for (Filter filter : FILTERS) {
			sb.append(asString(filter));
			sb.append("|");
		}
		return sb.toString().replaceAll("\\|$", "");
	}

	private String asString(Filter filter) {
		return String.format("%s:%s", filter.getType(), filter.getParameter());
	}

	private Filter parse(String s) {
		String[] p = s.split(":", 2);
		// System.out.println("PARSE: " + s + " > " + p[0] + ", " + p[1]);
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

			} else if ("flags".equals(type) || "value".equals(type)) {
				parameter = parameter.replaceAll("[^0-9\\-]", "");
				if (parameter.matches("\\d+")) {
					parameter = String.format("%s-%s", parameter, parameter);
				}
				if (!parameter.matches("\\d+-\\d+")) {
					return null;
				}

			} else if ("indicators".equals(type)) {
				parameter = parameter.replaceAll("[\\[\\] ]", "");
				if ("null".equals(parameter)
						|| !parameter.matches("[A-Za-z0-9]+(,[A-Za-z0-9]+)*")) {
					return null;
				}

			} else if ("after".equals(type)) {
				if (!parameter.matches("\\d{1,7}-2\\d{3}")) {
					return null;
				}

			}

			if (!parameter.isEmpty()) {
				return new Filter(type, parameter);
			}
		}
		return null;
	}

	private boolean validFilterType(String t) {
		return FILTER_TYPES.contains(t);
	}

}
