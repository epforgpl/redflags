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
package hu.petabyte.redflags.web.svc;

import hu.petabyte.redflags.web.model.Account;
import hu.petabyte.redflags.web.model.AccountRepo;
import hu.petabyte.redflags.web.model.Filter;
import hu.petabyte.redflags.web.model.UserFilter;
import hu.petabyte.redflags.web.model.UserFilterRepo;
import hu.petabyte.redflags.web.util.Filters;

import java.text.SimpleDateFormat;
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
import org.springframework.stereotype.Service;

/**
 * @author Zsolt Jurányi
 */
@Service
public class FilterEmailSvc {

	private static final SimpleDateFormat df = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	private @Autowired UserFilterRepo userFilterRepo;
	private @Autowired NoticesSvc noticesSvc;
	private @Autowired AccountRepo accountRepo;
	private @Autowired MessageSource msg;
	private @Autowired EmailSvc email;
	private @Autowired FilterSvc filterSvc;
	private @Value("${redflags.url}") String urlPrefix;
	private @Value("${redflags.contact}") String contactAddress;
	private @Value("${redflags.triggerSecret}") String triggerSecret;

	public boolean sendFilterLetters(HttpServletRequest request,
			HttpServletResponse response) {

		List<UserFilter> userFilters = userFilterRepo
				.findBySubscribeTrueOrderByUserIdAsc();
		for (UserFilter userFilter : userFilters) {
			// System.out.println(userFilter);

			// get user:
			Account a = accountRepo.findOne(userFilter.getUserId());
			if (null == a) {
				continue;
			}
			Locale loc = new Locale(a.getLang());

			// date filter is irrelevant:
			String filter = userFilter.getFilter();
			filter = filter.replaceAll("date:[\\d\\-]+(\\||$)", "");

			List<Filter> filters = new Filters(filter).asList();

			// injecting after filter to get only fresh ones:
			filters.add(new Filter("after", userFilter.getLastSentNumber()
					+ "-" + userFilter.getLastSentYear()));

			// query:
			long count = noticesSvc.count(filters);
			// System.out.println("COUNT: " + count);
			if (0 == count) {
				continue;
			}
			List<Map<String, Object>> query = noticesSvc.query(10, 0, false,
					filters);

			Map<String, Object> root = new HashMap<String, Object>();
			root.put("user", a.getName());
			root.put("count", count);
			root.put("notices", query);
			root.put("date", df.format(new Date()));
			root.put("filters", filters);
			root.put("resultsUrl", urlPrefix + "/notices/10/1/?filter="
					+ userFilter.getFilter());
			root.put("unsubscribeUrl",
					urlPrefix + "/filter/" + userFilter.getId());
			root.put("urlPrefix", urlPrefix);
			root.put("contactAddress", contactAddress);

			String text = email.text("filter-email", root, request, response,
					loc);
			if (null != text) {
				String subject = msg.getMessage("filter.email.subject", null,
						loc)
						+ (null == userFilter.getName()
						|| userFilter.getName().trim().isEmpty() ? "#"
						+ userFilter.getId() : userFilter.getName());
				String to = a.getEmailAddress();
				if (email.send(to, subject, text)) {
					// success
					filterSvc.fillLastNoticeId(userFilter);
					userFilterRepo.save(userFilter);
					// here we save the last notice ID
				}
			}

		} // filters
		return true;
	}

	public boolean validateSecret(String secret) {
		return triggerSecret.equals(secret);
	}
}
