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
import hu.petabyte.redflags.web.util.PagerCalculator;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Component;

/**
 * @author Péter Szűcs
 */
@Component
public class ApiHelper {

	@Autowired
	private MessageSource msg;
	
	public void redirectTo(String url, HttpServletResponse response) {
		try {
			response.sendRedirect(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Map<String, Object> getMessageObject(String messagePropertyName, Locale locale) {
		Map<String, Object> rootObject = new HashMap<String, Object>();
		List<Map<String, Object>> messages = new ArrayList<Map<String, Object>>();
		
		Map<String, Object> message = new HashMap<String, Object>();
		message.put("title", msg.getMessage(messagePropertyName, null, locale));
		messages.add(message);
		rootObject.put("messages", messages);
		return rootObject;
	}
	
	public Map<String, Object> getErrorObject(List<ErrorResponse> errors, Locale locale) {
		Map<String, Object> rootObject = new HashMap<String, Object>();
		List<Map<String, Object>> errorsMap = new ArrayList<Map<String, Object>>();
		
		for (ErrorResponse error : errors) {
			Map<String, Object> errorMap = new HashMap<String, Object>();
			
			if(error.getParameter() != null || error.getPointer() != null) {
				Map<String, Object> source = new HashMap<String, Object>();
				if(error.getParameter() != null) {
					source.put("parameter", error.getParameter());
				}
				if(error.getPointer() != null) {
					source.put("pointer", error.getPointer());
				}
				errorMap.put("source", source);
			}
			if(error.getTitle() != null) {
				errorMap.put("title", (error.isMessageFromProperty()) ? msg.getMessage(error.getTitle(), null, locale) : error.getTitle());
			}
			if(error.getDetail() != null) {
				errorMap.put("detail", (error.isMessageFromProperty()) ? msg.getMessage(error.getDetail(), null, locale) : error.getDetail());
			}
			if(error.getStatus() != null) {
				errorMap.put("status", error.getStatus());
			}
			errorsMap.add(errorMap);
		}
		
		rootObject.put("errors", errorsMap);
		return rootObject;
	}
	
	public Map<String, Object> getLinksObject(UrlParameters urlParams, PagerCalculator pager) {
		Map<String, Object> links = new HashMap<String, Object>();
		
		urlParams.setPage(1);
		links.put("first", decodeUrl(getUrl(urlParams)));
		
		urlParams.setPage(pager.getPageCount());
		links.put("last", decodeUrl(getUrl(urlParams)));
		
		if((pager.getPageIndex()) > 0){
			urlParams.setPage(pager.getPageIndex());
			links.put("prev", decodeUrl(getUrl(urlParams)));
		} else {
			links.put("prev", null);
		}
		
		if((pager.getPageIndex() + 2) <= pager.getPageCount()){
			urlParams.setPage(pager.getPageIndex() + 2);
			links.put("next", decodeUrl(getUrl(urlParams)));
		} else {
			links.put("next", null);
		}

		return links;
	}
	
	public String getUrl(UrlParameters params) {
		
		String url = params.getUrlRoot();
		if(params.getCount() != null) {
			url += "?count=" + params.getCount();
		}
		if(params.getPage() != null) {
			url += "&page=" + params.getPage();
		}
		if(params.getOrder() != null) {
			url += "&order=" + params.getOrder();
		}
		
		if(params.getAccessToken() != null) {
			url += "&access_token=" + params.getAccessToken();
		}
		
		for (Filter filter : params.getFilters().asList()) {
			if(filter.getParameter() != null && !filter.getType().equals("date") && !filter.getType().equals("doc")) {
				try {
					url += "&" + msg.getMessage("noticesFilter.parameterName." + filter.getType(), null, params.getLocale()) + "=";
					url += URLEncoder.encode(filter.getParameter(), "utf-8");
				} catch (NoSuchMessageException e) {
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		}
		return url;
	}
	
	public String decodeUrl(String url) {
		try {
			url = URLDecoder.decode(url, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return url;
	}
}
