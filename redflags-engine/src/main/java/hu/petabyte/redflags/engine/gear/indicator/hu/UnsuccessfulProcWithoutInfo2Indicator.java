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
package hu.petabyte.redflags.engine.gear.indicator.hu;

import hu.petabyte.redflags.engine.gear.indicator.AbstractTD7Indicator;
import hu.petabyte.redflags.engine.model.IndicatorResult;
import hu.petabyte.redflags.engine.model.Notice;
import hu.petabyte.redflags.engine.model.noticeparts.Award;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Zsolt Jurányi
 */
@Component
@ConfigurationProperties(prefix = "unsuccessfulProcWithoutInfo2Indicator")
public class UnsuccessfulProcWithoutInfo2Indicator extends AbstractTD7Indicator {

	@Override
	protected IndicatorResult flagImpl(Notice notice) {
		String s = fetchAdditionalInfo(notice).trim();
		if (successful(notice)) { // mindenhol van nyertes
			for (String line : s.split("\n")) {
				if (line.matches(".*124.*?\\(9\\).*")//
						|| (line.matches(".*szerződés.*((meg)?kötés|teljesítés).*") && line
								.matches(".*(nem képes|képtelen).*"))) {
					return returnFlag();
				}
			}
			return null;
		} else {
			return irrelevantData();
		}
	}

	private boolean successful(Notice notice) {
		if (null == notice.getAwards() || notice.getAwards().isEmpty()) {
			return false;
		}

		for (Award a : notice.getAwards()) {
			if (null == a.getWinnerOrg()) {
				return false;
			}
		}
		return true;
	}

}
