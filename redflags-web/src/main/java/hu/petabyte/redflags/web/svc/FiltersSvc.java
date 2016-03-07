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

import hu.petabyte.redflags.web.model.UserFilter;
import hu.petabyte.redflags.web.model.UserFilterRepo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * @author Zsolt Jurányi
 */
@Service
public class FiltersSvc {

	private @Autowired AccountSvc accounts;
	private @Autowired FilterSvc filter;
	private @Autowired UserFilterRepo filters;

	public List<UserFilter> getFilters() {
		List<UserFilter> r = filters.findByUserId(accounts.getCurrentUserId(),
				new Sort(Sort.Direction.DESC, "id"));
		for (UserFilter f : r) {
			filter.fill(f);
		}
		return r;
	}
}
