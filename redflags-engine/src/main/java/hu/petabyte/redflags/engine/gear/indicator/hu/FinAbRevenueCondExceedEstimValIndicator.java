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
import hu.petabyte.redflags.engine.model.CPV;
import hu.petabyte.redflags.engine.model.IndicatorResult;
import hu.petabyte.redflags.engine.model.Notice;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Zsolt Jurányi
 */
@Component
@ConfigurationProperties(prefix = "finAbRevenueCondExceedEstimValIndicator")
public class FinAbRevenueCondExceedEstimValIndicator extends
		AbstractTD3CIndicator {

	private static final Logger LOG = LoggerFactory
			.getLogger(FinAbRevenueCondExceedEstimValIndicator.class);

	private Pattern excludePattern = Pattern
			.compile(
					"működés(i|ének) ideje alatt|megkezdett működés időszakában|teljes időszak tekintetében|éven belül jött létre|működése megkezdésétől számított időszakban|tevékenység(e|ének) megkezdése óta|Kormányrendelet 14\\.|(310/2011.*?14\\.|később létrejött gazdasági szereplő|újonnan piacra lépő szervezetek|rövidebb ideje működik|időszak.*?( kezdete)? után( nem)? (jött létre|kezdte meg( a)? működését))",
					Pattern.CASE_INSENSITIVE);

	public Pattern getExcludePattern() {
		return excludePattern;
	}

	public void setExcludePattern(Pattern excludePattern) {
		this.excludePattern = excludePattern;
	}

	@Override
	protected IndicatorResult flagImpl(Notice notice) {
		String s = String.format("%s\n%s", //
				fetchFinancialAbility(notice), //
				fetchAdditionalInfo(notice) //
				).trim();
		long estimVal = fetchEstimatedValue(notice);
		String estimCurr = fetchEstimatedValueCurrency(notice);
		if (s.isEmpty() || 0L == estimVal || null == estimCurr) {
			return missingData();
		}

		s = s.replaceAll("\\([^\\)]+\\)", "");
		s = HuIndicatorHelper.words2Digits(s);

		if (estimCurr.matches("Ft|forint")) {
			estimCurr = "HUF";
		}

		StringBuilder concrObjs = new StringBuilder();
		for (CPV c : notice.getData().getCpvCodes()) {
			concrObjs.append("|").append(c.getName().toLowerCase());
		}

		// TODO + shortDescr tisztítva...

		Pattern p = Pattern
				.compile("(beszerzés tárgy"
						+ concrObjs
						+ "|származó).+?árbevétel.+?[^0-9] (?<v>\\d{1,3}( ?\\d{3}){2,10})(?<c> [A-Za-z]+)[^A-Za-z]");

		long sum = 0;
		for (String line : s.split("\n")) {
			if (excludePattern.matcher(line).find()) {
				continue;
			}

			Matcher m = p.matcher(line);
			if (m.find()) {
				System.out.println("*** " + line);
				long v = Long.parseLong("0" + m.group("v").replaceAll(" ", ""));
				String c = m.group("c").trim();
				if (c.matches("Ft|forint.*")) {
					c = "HUF";
				}
				if ("eFt".equals(c)) {
					v *= 1000;
					c = "HUF";
				}
				if (c.equals(estimCurr)) {
					sum += v;
				}
			}
		}

		LOG.trace("{}, revenue condition value = {}, estimated value = {}",
				notice.getId(), sum, estimVal);
		if (sum > estimVal) {
			return returnFlag("info", "revenue=" + (sum / 1000000), "estim="
					+ (estimVal / 1000000), "curr=" + estimCurr);
		}
		return null;
	}
}
