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
package hu.petabyte.redflags.api.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hu.petabyte.redflags.api.service.ApiUserService;
import hu.petabyte.redflags.api.util.ApiHelper;
import hu.petabyte.redflags.api.util.ErrorResponse;
import hu.petabyte.redflags.api.util.UrlParameters;
import hu.petabyte.redflags.web.model.Filter;
import hu.petabyte.redflags.web.svc.OrganizationsSvc;
import hu.petabyte.redflags.api.util.Filters;
import hu.petabyte.redflags.web.util.PagerCalculator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Péter Szűcs
 */
@RestController
public class OrganizationsController {
	
	@Autowired
	private OrganizationsSvc organizations;
	
	@Autowired
	private ApiUserService apiUserService;
	
	@Autowired
	private MessageSource msg;
	
	@Value("${defaultItemCount}")
	private int defaultItemCount;
	
	@Value("${maxItemCount}")
	private int maxItemCount;
	
	@Autowired
	private ApiHelper apiHelper;
	
	PagerCalculator pager;
			
	@RequestMapping("/organizations")
	public Map<String, Object> organizations (
			@RequestParam(value = "count", required = true, defaultValue = "0") Integer count,
			@RequestParam(value = "page", required = true, defaultValue = "0") Integer page,
			@RequestParam(value = "access_token", required = true) String accessToken,
			@RequestParam(value = "nameLike", required = false) String filter,
			Model model,
			HttpServletRequest request,
			HttpServletResponse response,
			Locale locale) throws UnsupportedEncodingException {
		
		if(apiUserService.findByAccessToken(accessToken) != null) {
			long filteredCount = organizations.count(filter);
			Filters filters = new Filters();
			if(filter != null && !filter.equals("")) {
				filters.add(new Filter("nameLike", filter));
			}
			
			UrlParameters params = new UrlParameters(request.getRequestURI(), count, page, null, null, null, null, accessToken, filters, locale);
			checkArguments(params, filteredCount, response);
	
			Map<String, Object> result = new HashMap<String, Object>();
			List<Map<String, Object>> objs = new ArrayList<Map<String, Object>>();
			if (0 == filteredCount) {
				return apiHelper.getMessageObject("message.noSuchElement", locale);
			} else {
				objs = organizations.query(pager.getPerPage(), pager.getOffset(), filter);
				params.setUrlRoot(request.getRequestURL().toString());
				result.put("links", apiHelper.getLinksObject(params, pager));
				result.put("count", objs.size());
				result.put("result", objs);
				return result;
			}
		} else {
			List<ErrorResponse> errors = new ArrayList<ErrorResponse>();
			errors.add(new ErrorResponse("access_token","message.error.title.invalidParameter","message.noSuchAccessToken", null, "/organizations", true));
			return apiHelper.getErrorObject(errors, locale);
		}
	}
	
	private void checkArguments(UrlParameters params, long filteredCount, HttpServletResponse response) {
		boolean needRedirect = false;
		
		if (0 == params.getCount()) {
			if (params.getPage() < 1) {
				params.setPage(1);
			}
			params.setCount(defaultItemCount);
			needRedirect = true;
		} else if(params.getCount() > maxItemCount) {
			if (params.getPage() < 1) {
				params.setPage(1);
			}
			params.setCount(maxItemCount);
			needRedirect = true;
		}
		if (params.getPage() < 1) {
			params.setPage(1);
			needRedirect = true;
		}
		pager = new PagerCalculator(filteredCount, params.getCount(), params.getPage() - 1);
		
		if (params.getPage() > pager.getPageCount() && pager.getPageCount() != 0) {
			params.setPage(pager.getPageCount());
			needRedirect = true;
		}
		
		if(needRedirect) {
			apiHelper.redirectTo(apiHelper.getUrl(params), response);
		}
	}
}
