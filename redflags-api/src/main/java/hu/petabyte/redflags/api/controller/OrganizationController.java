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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import hu.petabyte.redflags.api.service.ApiUserService;
import hu.petabyte.redflags.api.util.ApiHelper;
import hu.petabyte.redflags.api.util.ErrorResponse;
import hu.petabyte.redflags.web.svc.OrganizationSvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Péter Szűcs
 */
@RestController
public class OrganizationController {

	@Autowired
	private OrganizationSvc organization;
	
	@Autowired
	private ApiUserService apiUserService;
	
	@Autowired
	private MessageSource msg;
	
	@Autowired
	private ApiHelper apiHelper;
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/organization")
	public Map<String, Object> organization (
			@RequestParam(value = "id", required = true) String id,
			@RequestParam(value = "access_token", required = true) String accessToken,
			Model model,
			Locale locale) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> obj = new HashMap<String, Object>();
		Map<String, Object> basic = null;
		
		if(apiUserService.findByAccessToken(accessToken) != null) {
			try {
				basic = organization.basic(id);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (null != basic) {
				obj.put("basic", basic);
				obj.put("callCount", organization.callCount(id));
	
				List<Map<String, Object>> callList = organization.calls(id);
				//flag names
				for (int i = 0; i < callList.size(); i++) {
					
					//document type names
					callList.get(i).put("type", msg.getMessage("notice.data.documentType." + callList.get(i).get("typeId").toString().split("-")[1], null, Locale.ENGLISH));
					
					//flag names
					List<Map<String, Object>> flagList = ((List<Map<String, Object>>) callList.get(i).get("flags"));
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
				obj.put("calls",callList);
				
				obj.put("winCount", organization.winCount(id));
				List<Map<String, Object>> wins = organization.wins(id);
				obj.put("wins", wins);
				result.put("result", obj);
			} else {
				return apiHelper.getMessageObject("message.noSuchElement", locale);
			}
			return result;
		} else {
			List<ErrorResponse> errors = new ArrayList<ErrorResponse>();
			errors.add(new ErrorResponse("access_token","message.error.title.invalidParameter","message.noSuchAccessToken", null, "/organization", true));
			return apiHelper.getErrorObject(errors, locale);
		}
	}
}
