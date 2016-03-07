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

import hu.petabyte.redflags.web.svc.OrganizationSvc;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Zsolt Jurányi
 */
@Controller
public class OrganizationCtrl {

	private @Autowired OrganizationSvc org;
	private @Autowired MessageSource msg;

	@RequestMapping({ "/organization", "/organization/" })
	public String organization() {
		return "redirect:/organizations";
	}

	@RequestMapping("/organization/{id}")
	public String organization(Map<String, Object> m, Locale loc,
			@PathVariable String id) {

		long time = -System.currentTimeMillis();
		Map<String, Object> basic = org.basic(id);
		String name = id;
		if (null != basic) {
			if (null != basic.get("name")) {
				name = basic.get("name").toString();
			}
			m.put("basic", basic);
			m.put("callCount", org.callCount(id));
			m.put("calls", org.calls(id));
			m.put("winCount", org.winCount(id));
			List<Map<String, Object>> wins = org.wins(id);
			// int awardCount = 0;
			// if (null != wins) {
			// for (Map<String, Object> w : wins) {
			// try {
			// awardCount += Integer.parseInt(w.get("awardCount")
			// .toString());
			// } catch (Exception e) {
			// }
			// }
			// }
			m.put("wins", wins);
			// m.put("awardCount", awardCount);
		}
		time += System.currentTimeMillis();
		m.put("pageTitle", name);
		m.put("prevPageTitleLabel", "organizations.title");
		m.put("prevPageUrl", "/organizations");
		m.put("queryTime", time);
		return "organization";
	}
}
