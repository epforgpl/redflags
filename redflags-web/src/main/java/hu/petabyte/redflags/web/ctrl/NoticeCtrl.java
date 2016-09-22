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

import hu.petabyte.redflags.web.svc.NoticeSvc;

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
public class NoticeCtrl {

	private @Autowired NoticeSvc notice;
	private @Autowired MessageSource msg;

	@RequestMapping({ "/notice", "/notice/" })
	public String notice() {
		return "redirect:/notices";
	}

	@SuppressWarnings("unchecked")
	@RequestMapping("/notice/{id}")
	public String notice(Map<String, Object> m, Locale loc,
			@PathVariable String id) {

		long time = -System.currentTimeMillis();
		Map<String, Object> basic = notice.basic(id);
		String dt = null;
		if (null != basic) {
			m.put("basic", basic);
			m.put("awards", notice.awards(id)); // list
			m.put("contr", notice.contr(id));
			m.put("compl", notice.compl(id));
			m.put("data", notice.data(id));
			if (null != m.get("data")) {
				Map<String, Object> d = (Map<String, Object>) m.get("data");
				// dt = (String) d.get("documentType");
				String dtid = (String) d.get("documentTypeId");
				String msgKey = dtid!=null ? "notice.data.documentType." + dtid.split("-")[1] : "notice.data.documentType";
				dt = msg.getMessage(
						msgKey, null,
						loc);
			}
			m.put("flags", notice.flags(id)); // list
			m.put("cnFlags", notice.cnFlags(id)); // list
			m.put("left", notice.left(id));
			m.put("lots", notice.lots(id)); // list
			m.put("objs", notice.objs(id)); // list
			m.put("proc", notice.proc(id));
			m.put("rels", notice.related(id));
		}
		time += System.currentTimeMillis();
		m.put("pageTitle", id + (null != dt ? " (" + dt + ")" : ""));
		m.put("prevPageTitleLabel", "notices.title");
		m.put("prevPageUrl", "/notices");
		m.put("queryTime", time);
		return "notice";
	}
}
