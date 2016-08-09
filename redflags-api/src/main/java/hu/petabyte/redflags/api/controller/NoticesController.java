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

import hu.petabyte.redflags.api.service.ApiUserService;
import hu.petabyte.redflags.api.util.ApiHelper;
import hu.petabyte.redflags.api.util.ErrorResponse;
import hu.petabyte.redflags.api.util.UrlParameters;
import hu.petabyte.redflags.web.svc.NoticesSvc;
import hu.petabyte.redflags.api.util.Filters;
import hu.petabyte.redflags.web.util.PagerCalculator;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
public class NoticesController {
	
	@Autowired
	private NoticesSvc notices;
	
	@Autowired
	private ApiUserService apiUserService;
	
	@Autowired
	private MessageSource msg;
	
	@Autowired
	private ApiHelper apiHelper;
	
	@Value("${fromDateDefault}")
	private String fromDateDefault;
	 
	@Value("${defaultItemCount}")
	private int defaultItemCount;
	 
	@Value("${maxItemCount}")
	private int maxItemCount;
	
	Filters filters;

	@SuppressWarnings("unchecked")
	@RequestMapping("/notices")
	public Map<String, Object> notices (
			@RequestParam(value = "count", required = true, defaultValue = "0") Integer count,
			@RequestParam(value = "page", required = true, defaultValue = "0") Integer page,
			@RequestParam(value = "access_token", required = true) String accessToken,
			@RequestParam(value = "order", required = false) String order,
			@RequestParam(value = "contractingAuthorityNameLike", required = false) String filterContr,
			@RequestParam(value = "cpvCodes", required = false) String filterCpv,
			@RequestParam(value = "fromDate", required = false) String fromDate,
			@RequestParam(value = "toDate", required = false) String toDate,
			@RequestParam(value = "doc", required = false) String filterDoc,
			@RequestParam(value = "flagCountRange", required = false) String filterFlags,
			@RequestParam(value = "indicators", required = false) String[] filterIndicators,
			@RequestParam(value = "textLike", required = false) String filterText,
			@RequestParam(value = "valueRange", required = false) String filterValue,
			@RequestParam(value = "winnerNameLike", required = false) String filterWinner,
			HttpServletRequest request,
			HttpServletResponse response,
			Locale locale,
			Model model) throws UnsupportedEncodingException {
		
		Map<String, Object> result = new HashMap<String, Object>();
		if(apiUserService.findByAccessToken(accessToken) != null) {
			boolean orderByFlags = "by-flags".equals(order);
			if (!orderByFlags && !"by-date".equals(order) && order != null) {
				order = "by-date";
			}
			Map<String, Object> errors = checkArguments(order, fromDate, toDate, filterDoc, filterCpv, 
						filterFlags, filterValue, filterContr, filterIndicators, filterText, filterWinner, locale);
			if(errors.size() > 0) {
				return errors;
			}
			
			UrlParameters params = new UrlParameters(request.getRequestURI(), count, page, order, fromDate, toDate, filterDoc, accessToken, filters, locale);
			
			// check page
			if (0 == params.getCount()) {
				if (params.getPage() < 1) {
					params.setPage(1);
				}
				params.setCount(defaultItemCount);
				apiHelper.redirectTo(apiHelper.getUrl(params), response);
			} else if(params.getCount() > maxItemCount) {
				if (params.getPage() < 1) {
					params.setPage(1);
				}
				params.setCount(maxItemCount);
				apiHelper.redirectTo(apiHelper.getUrl(params), response);
			}
			if (params.getPage() < 1) {
				params.setPage(1);
				apiHelper.redirectTo(apiHelper.getUrl(params), response);
			}
					
			// count
			long filteredCount = notices.count(filters.asListForQuery());
			
			PagerCalculator pager = new PagerCalculator(filteredCount, count, page - 1);
			List<Map<String, Object>> objs = new ArrayList<Map<String, Object>>();
			if (0 == filteredCount) {
				return apiHelper.getMessageObject("message.noSuchElement", locale);
			} else {
				// check page
				if (params.getPage() > pager.getPageCount()) {
					params.setPage(pager.getPageCount());
					apiHelper.redirectTo(apiHelper.getUrl(params), response);
				}
				// query
				objs = notices.query(pager.getPerPage(), pager.getOffset(), orderByFlags, filters.asListForQuery());
				result.put("count", objs.size());
				for (int i = 0; i < objs.size(); i++) {
					//document type names
					objs.get(i).put("type", msg.getMessage("notice.data.documentType." + objs.get(i).get("typeId").toString().split("-")[1], null, Locale.ENGLISH));
					
					//flag names
					List<Map<String, Object>> flagList = ((List<Map<String, Object>>) objs.get(i).get("flags"));
					for (int j = 0; j < flagList.size(); j++) {
						String[] split = flagList.get(j).get("information").toString().split("\\|");
						boolean first = true;
						String label = "";
						try {
							for (int k = 0; k < split.length; k++) {
								if(first) {
									label = msg.getMessage(split[k], null, Locale.ENGLISH);
									first = false;
								} else {
									if(split[k].contains("=")) {
										String attribute = split[k].split("=")[0];
										if(split[k].split("=")[1] != null && !split[k].split("=")[1].equals("")) {
											String value = split[k].split("=")[1];
											label = label.replace("{" + attribute + "}", value);
										}
									}
								}
							}
						} catch (Exception e) {
							label = flagList.get(j).get("information").toString();
						}
						flagList.get(j).put("information", label);
					}
				}
				
				params.setUrlRoot(request.getRequestURL().toString());
				result.put("links", apiHelper.getLinksObject(params, pager));
				result.put("result", objs);
				return result;
			}
		} else {
			List<ErrorResponse> errors = new ArrayList<ErrorResponse>();
			errors.add(new ErrorResponse("access_token","message.error.title.invalidParameter","message.noSuchAccessToken", null, "/notices", true));
			return apiHelper.getErrorObject(errors, locale);
		}
		
	}
	
	private Map<String, Object> checkArguments(String order, String fromDate, String toDate, String filterDoc, String filterCpv, 
			String filterFlags, String filterValue, String filterContr, String[] filterIndicators, String filterText, String filterWinner, Locale locale) {
		
		List<ErrorResponse> errors = new ArrayList<ErrorResponse>();
		
		//date
		String newFromDate = fromDate;
		if(fromDate == null){
			newFromDate = fromDateDefault;
		}
		String newToDate = toDate;
		if(toDate == null){
			Date date = new Date();
			newToDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
		}
		Date from = null;
		Date to = null;
		try {
			from = new SimpleDateFormat("yyyy-MM-dd").parse(newFromDate);
		} catch (ParseException e) {
			errors.add(new ErrorResponse("fromDate","message.error.title.invalidParameter","message.error.invalidDateFormat", null, "/notices", true));
		}
		
		try {
			to = new SimpleDateFormat("yyyy-MM-dd").parse(newToDate);
		} catch (ParseException e) {
			errors.add(new ErrorResponse("toDate","message.error.title.invalidParameter","message.error.invalidDateFormat", null, "/notices", true));
		}
		
		if(errors.size() == 0) {
			if(from.after(to)){
				errors.add(new ErrorResponse("toDate","message.error.title.invalidParameter","message.error.invalidDateParameters", null, "/notices", true));
			}
		}
		String filterDate = newFromDate + ":" + newToDate;
		
		//doc
		if(filterDoc != null && !filterDoc.equals("")){
			try {
				msg.getMessage("notice.data.documentType." + filterDoc, null, locale);
			} catch (Exception e) {
				errors.add(new ErrorResponse("documentType","message.error.title.invalidParameter","message.error.noSuchDocumentType", null, "/notices", true));
			}
		}
		
		//cpv
		if(filterCpv != null && !filterCpv.equals("")){
			String test = filterCpv.replaceAll("[^0-9,]", "");
			if (!test.matches("\\d{8}(,\\d{8})*")) {
				errors.add(new ErrorResponse("cpvCodes","message.error.title.invalidParameter","message.error.invalidCpvCode", null, "/notices", true));
			}
		}
		
		//filterCount range
		if(filterFlags != null && !filterFlags.equals("")){
			String test = filterFlags.replaceAll("[^0-9\\-]", "");
			if (!test.matches("\\d+-\\d+") && !test.matches("\\d+")) {
				errors.add(new ErrorResponse("flagCountRange","message.error.title.invalidParameter","message.error.invalidFlagCountRange", null, "/notices", true));
			}
		}
		
		//value range
		if(filterValue != null && !filterValue.equals("")){
			String test = filterValue.replaceAll("[\\[\\] ]", "");
			if (!test.matches("[A-Za-z0-9]+(,[A-Za-z0-9]+)*")) {
				errors.add(new ErrorResponse("valueRange","message.error.title.invalidParameter","message.error.invalidValueRange", null, "/notices", true));
			}
		}
		
		if(errors.size() > 0) {
			return apiHelper.getErrorObject(errors, locale);
		}
		
		// map filters
		filters = new Filters();
		filters.add("contr", filterContr).add("cpv", filterCpv).add("date", filterDate).add("doc", filterDoc)
				.add("flags", filterFlags).add("indicators", Arrays.toString(filterIndicators)).add("text", filterText)
				.add("value", filterValue).add("winner", filterWinner).add("fromDate", newFromDate).add("toDate", newToDate);
		return new HashMap<String, Object>();
	}
}
