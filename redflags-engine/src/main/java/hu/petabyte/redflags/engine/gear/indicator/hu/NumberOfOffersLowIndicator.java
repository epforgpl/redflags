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
@ConfigurationProperties(prefix = "numberOfOffersLowIndicator")
public class NumberOfOffersLowIndicator extends AbstractTD7Indicator {

	private int minOffers = 3;

	public int getMinOffers() {
		return minOffers;
	}

	public void setMinOffers(int minOffers) {
		this.minOffers = minOffers;
	}

	@Override
	protected IndicatorResult flagImpl(Notice notice) {
		if (fetchProcedureType(notice).matches("PR-[TV]")) {
			return irrelevantData();
		}

		int minNOO = Integer.MAX_VALUE;
		for (Award a : notice.getAwards()) {
			if (a.getNumberOfOffers() > 0) {
				minNOO = Math.min(minNOO, a.getNumberOfOffers());
			}
		}

		if (minNOO < minOffers) {
			if (1 == minNOO) {
				return returnFlag("info1");
			}
			if (2 == minNOO) {
				return returnFlag("info2");
			}
		}

		return null;
	}
}
