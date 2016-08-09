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

import java.util.Locale;

/**
 * @author Péter Szűcs
 */
public class UrlParameters {
	
	String urlRoot;
	Integer count;
	Integer page;
	String order;
	String fromDate;
	String toDate;
	String filterDoc;
	String accessToken;
	Filters filters;
	Locale locale;
	
	public UrlParameters(String urlRoot, Integer count, Integer page, String order, String fromDate, String toDate, String filterDoc, String accessToken, Filters filters, Locale locale) {
		super();
		this.urlRoot = urlRoot;
		this.count = count;
		this.page = page;
		this.order = order;
		this.fromDate = fromDate;
		this.toDate = toDate;
		this.accessToken = accessToken;
		this.filterDoc = filterDoc;
		this.filters = filters;
		this.locale = locale;
	}

	public String getUrlRoot() {
		return urlRoot;
	}

	public void setUrlRoot(String urlRoot) {
		this.urlRoot = urlRoot;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	
	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getFilterDoc() {
		return filterDoc;
	}

	public void setFilterDoc(String filterDoc) {
		this.filterDoc = filterDoc;
	}

	public Filters getFilters() {
		return filters;
	}

	public void setFilters(Filters filters) {
		this.filters = filters;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}
	
}
