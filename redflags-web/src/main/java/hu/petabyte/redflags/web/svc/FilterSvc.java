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

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

/**
 * @author Zsolt Jurányi
 */
@Service
public class FilterSvc {

	private @Autowired AccountSvc accounts;
	private @Autowired AccountRepo accountRepo;
	private @Autowired UserFilterRepo filters;
	private @Autowired OrganizationSvc org;
	private @Autowired NoticesSvc notices;

	public void delete(long id) {
		UserFilter f = filters.findByIdAndUserId(id,
				accounts.getCurrentUserId());
		if (null != f) {
			filters.delete(id);
		}
	}

	public void fill(UserFilter filter) {
		if (null != filter) {
			filter.setFilters(new Filters(filter.getFilter()).asList());
			fillOrgNames(filter.getFilters());
		}
	}

	public void fillLastNoticeId(UserFilter f) {
		Map<String, Object> lastNotice = notices.lastNotice();
		if (null != lastNotice) {
			f.setLastSentNumber(Integer.valueOf(((BigDecimal) lastNotice
					.get("noticeNumber")).intValue()));
			f.setLastSentYear(Integer.valueOf(((BigDecimal) lastNotice
					.get("noticeYear")).intValue()));
		}
	}

	public void fillOrgNames(List<Filter> filterList) {
		for (Filter f : filterList) {
			if (f.getParameter().matches("ORG-[0-9abcdef]{32}")) {
				if ("contr".equals(f.getType())) {
					f.setInfo((String) org.basic(f.getParameter()).get("name"));
				} else if ("winner".equals(f.getType())) {
					f.setInfo((String) org.basic(f.getParameter()).get("name"));
				}
			}
		}
	}

	public UserFilter get(long id) {
		UserFilter filter = filters.findByIdAndUserId(id,
				accounts.getCurrentUserId());
		fill(filter);
		return filter;
	}

	public long saveNew(long userId, String filter) {
		long currentUserId = accounts.getCurrentUserId();
		if (currentUserId == userId) {
			List<UserFilter> fs = filters.findByUserIdAndFilter(currentUserId,
					filter);
			UserFilter f = null;
			if (null == fs || fs.isEmpty()) {
				f = new UserFilter(userId, filter, false);
				fillLastNoticeId(f);
				filters.save(f);
			} else {
				f = fs.get(0);
			}
			return f.getId();
		}
		return 0;
	}

	public boolean saveSettings(long id, String name, boolean subscribe) {
		UserFilter f = filters.findByIdAndUserId(id,
				accounts.getCurrentUserId());
		if (null != f) {
			f.setName(name);
			f.setSubscribe(subscribe);
			fillLastNoticeId(f);
			filters.save(f);

			// save user language for use in email
			Account a = accounts.getCurrent();
			a.setLang(LocaleContextHolder.getLocale().getLanguage());
			accountRepo.save(a);

			return true;
		} else {
			return false;
		}
	}
}
