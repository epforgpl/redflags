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
package hu.petabyte.redflags.engine.scope;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import hu.petabyte.redflags.engine.epforgpl.EPFScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hu.petabyte.redflags.engine.boot.RedflagsEngineConfig;
import hu.petabyte.redflags.engine.model.NoticeID;
import hu.petabyte.redflags.engine.tedintf.MaxNumberDeterminer;

/**
 * @author Zsolt Jurányi
 */
@Service
public class ScopeProvider {

	private static final String NID = "\\d{1,7}-\\d{4}"; // notice id pattern

	private @Autowired RedflagsEngineConfig config;
	private @Autowired MaxNumberDeterminer mnd;

	public RedflagsEngineConfig getConfig() {
		return config;
	}

	public MaxNumberDeterminer getMnd() {
		return mnd;
	}

	public AbstractScope scope(String scopeStr) {
		checkNotNull(scopeStr, "scopeStr should not be null.");
		checkArgument(!scopeStr.isEmpty(), "scopeStr should not be blank.");

		// EPF
		if (scopeStr.equalsIgnoreCase("epf")) {
			return new EPFScope();
		}

		// DIRECTORY SCOPE: "directory"

		if (scopeStr.equalsIgnoreCase("directory")) {
			return new DirectoryScope(config.getCacheDirectory());
		}

		// SINGLE NOTICE SCOPE: "number-year"

		if (scopeStr.matches(NID)) {
			return new SingleNoticeScope(new NoticeID(scopeStr));
		}

		// NOTICE LIST SCOPE: "number-year,number-year,..."

		if (scopeStr.matches(String.format("%s(,%s)+", NID, NID))) {
			List<NoticeID> ids = new ArrayList<NoticeID>();
			for (String id : scopeStr.split(",")) {
				ids.add(new NoticeID(id));
			}
			return new NoticeListScope(ids);
		}

		// NOTICE RANGE SCOPE: "number-year..[number-year]"

		if (scopeStr.matches(NID + "..")) {
			int year = Calendar.getInstance().get(Calendar.YEAR);
			scopeStr = scopeStr + "999999-" + year;
			// -> number-year..999999-YYYY
		}

		if (scopeStr.matches(String.format("%s..%s", NID, NID))) {
			String[] parts = scopeStr.split("\\.\\.");
			NoticeID first = new NoticeID(parts[0]);
			NoticeID last = new NoticeID(parts[1]);
			return new NoticeRangeScope(first, last, mnd);
		}

		// SINGLE YEAR SCOPE: "yyyy"

		if (scopeStr.matches("\\d{4}")) {
			return new SingleYearScope(Integer.valueOf(scopeStr), mnd);
		}

		// YEAR LIST SCOPE: "yyyy,yyyy,..."

		if (scopeStr.matches("\\d{4}(,\\d{4})+")) {
			List<Integer> ys = new ArrayList<Integer>();
			for (String y : scopeStr.split(",")) {
				ys.add(Integer.valueOf(y));
			}
			return new YearListScope(ys, mnd);
		}

		// YEAR RANGE SCOPE: "yyyy..[yyyy]"

		if (scopeStr.matches("\\d{4}\\.\\.")) {
			scopeStr += Integer.toString(Calendar.getInstance().get(Calendar.YEAR));
			// -> "year..year"
		}

		if (scopeStr.matches("\\d{4}\\.\\.\\d{4}")) {
			int y1 = Integer.valueOf(scopeStr.substring(0, 4));
			int y2 = Integer.valueOf(scopeStr.substring(6, 10));
			List<Integer> ys = new ArrayList<Integer>();
			for (int y = y1; y <= y2; y++) {
				ys.add(y);
			}
			return new YearListScope(ys, mnd);
		}

		// AUTO SCOPE: "auto"
		if (scopeStr.equalsIgnoreCase("auto")) {
			return new AutoScope(mnd);
		}

		throw new IllegalArgumentException("Invalid scope format: " + scopeStr);
	}

	public void setConfig(RedflagsEngineConfig config) {
		this.config = config;
	}

	public void setMnd(MaxNumberDeterminer mnd) {
		this.mnd = mnd;
	}

}
