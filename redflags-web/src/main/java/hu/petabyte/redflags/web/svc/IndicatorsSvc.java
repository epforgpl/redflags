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

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * @author Zsolt Jurányi
 */
@Service
public class IndicatorsSvc {

	private static final String QUERY = "select distinct substring(id from position(\"-F-\" in id) + 3) i from te_flag;";
	private final List<String> indicators = new ArrayList<String>();
	private @Autowired JdbcTemplate jdbc;

	public synchronized List<String> getIndicators() {
		return indicators;
	}

	@Scheduled(fixedDelay = 60 * 60 * 1000)
	public synchronized void refresh() {
		try {
			List<String> newIndicators = jdbc.queryForList(QUERY, String.class);
			indicators.clear();
			indicators.addAll(newIndicators);
		} catch (Exception e) {
			// log
		}
	}

}
