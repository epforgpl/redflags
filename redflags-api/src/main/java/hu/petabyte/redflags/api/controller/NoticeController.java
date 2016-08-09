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
import hu.petabyte.redflags.web.svc.NoticeSvc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
public class NoticeController {

	@Autowired
	private NoticeSvc notice;
	
	@Autowired
	private ApiUserService apiUserService;
	
	@Autowired
	private MessageSource msg;

	@Autowired
	private ApiHelper apiHelper;
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/notice")
	public Map<String, Object> notice (
			@RequestParam(value = "id", required = true) String id,
			@RequestParam(value = "access_token", required = true) String accessToken,
			Model model,
			Locale locale) {
		Map<String, Object> result = new HashMap<String, Object>();
		
		if(apiUserService.findByAccessToken(accessToken) != null) {
			Map<String, Object> obj = new HashMap<String, Object>();
			Map<String, Object> basic = notice.basic(id);
			
			if (null != basic) {
				obj.put("basic", basic);
				obj.put("data", notice.data(id));
				
				//flag names
				List<Map<String, Object>> flagList =  notice.flags(id);
				setFlagNames(flagList);
				
				obj.put("flags", flagList);
				obj.put("cnFlags", notice.cnFlags(id));
				obj.put("contractingAuthority", notice.contr(id));
				obj.put("objectOfTheContract", notice.objs(id));
				obj.put("awards", notice.awards(id));
				obj.put("left", notice.left(id));
				obj.put("lots", notice.lots(id));
				obj.put("procedure", notice.proc(id));
				
				List<Map<String, Object>> relList = notice.related(id);
				for (int i = 0; i < relList.size(); i++) {
					relList.get(i).put("type", msg.getMessage("notice.data.documentType." + relList.get(i).get("typeId").toString().split("-")[1], null, Locale.ENGLISH));
					
					//flag names
					List<Map<String, Object>> relFlagList = ((List<Map<String, Object>>) relList.get(i).get("flags"));
					setFlagNames(relFlagList);
				}
				obj.put("familyMembers", relList);
				obj.put("complementaryInformation", notice.compl(id));
				result.put("result", obj);
			} else {
				return apiHelper.getMessageObject("message.noSuchElement", locale);
			}
		} else {
			List<ErrorResponse> errors = new ArrayList<ErrorResponse>();
			errors.add(new ErrorResponse("access_token","message.error.title.invalidParameter","message.noSuchAccessToken", null, "/notice", true));
			return apiHelper.getErrorObject(errors, locale);
		}
		return result;
	}
	
	public void setFlagNames(List<Map<String, Object>> flagList) {
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
				e.printStackTrace();
				label = flagList.get(j).get("information").toString();
			}
			flagList.get(j).put("information", label);
		}
	}
}
