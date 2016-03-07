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

import hu.petabyte.redflags.engine.gear.indicator.AbstractTD3CIndicator;
import hu.petabyte.redflags.engine.gear.indicator.helper.HuIndicatorHelper;
import hu.petabyte.redflags.engine.model.IndicatorResult;
import hu.petabyte.redflags.engine.model.Notice;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Zsolt Jurányi
 */
@Component
@ConfigurationProperties(prefix = "finAbRevenueCondManyYearsIndicator")
public class FinAbRevenueCondManyYearsIndicator extends AbstractTD3CIndicator {

	private long maxYears = 3;

	@Override
	protected IndicatorResult flagImpl(Notice notice) {
		String s = String.format("%s\n%s", //
				fetchFinancialAbility(notice), //
				fetchAdditionalInfo(notice) //
				).trim();

		s = HuIndicatorHelper.words2Digits(s);

		Pattern p = Pattern.compile(" (?<y>\\d+) ([^ ]+ )?év(re|ben)");
		for (String line : s.split("\n")) {
			if (line.contains("árbevétel")) {
				Matcher m = p.matcher(line);
				if (m.find()) {
					int y = Integer.parseInt(m.group("y"));
					if (y > maxYears) {
						return returnFlag("info", "years=" + y, "max="
								+ maxYears);
					}
				}
			}
		}
		return null;
	}

	public long getMaxYears() {
		return maxYears;
	}

	public void setMaxYears(long maxYears) {
		this.maxYears = maxYears;
	}

}
