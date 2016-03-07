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
@ConfigurationProperties(prefix = "offerGuaranteeIsHighIndicator")
public class OfferGuaranteeIsHighIndicator extends AbstractTD3CIndicator {

	private double maxPercent = 2.0;

	@Override
	protected IndicatorResult flagImpl(Notice notice) {
		parseOfferGuarantee(notice);
		long g = 0;
		String gCurr = null;
		if (null != notice.getCompl()) {
			g = notice.getCompl().getGuaranteeValue();
			gCurr = notice.getCompl().getGuaranteeValueCurr();
		}
		if (0 == g || null == gCurr) {
			return irrelevantData();
		}
		if (gCurr.matches("Ft|forint")) {
			gCurr = "HUF";
		}

		long estimVal = fetchEstimatedValue(notice);
		String estimCurr = fetchEstimatedValueCurrency(notice);
		if (0 == estimVal || null == estimCurr) {
			return missingData();
		}
		if (estimCurr.matches("Ft|forint")) {
			estimCurr = "HUF";
		}

		// do magic

		double p = ((double) g / (double) estimVal) * 100;

		if (gCurr.equals(estimCurr) && p > maxPercent) {
			return returnFlag("info", "percent=" + String.format("%.1f", p),
					"max=" + String.format("%.1f", maxPercent));
		}
		return null;
	}

	public double getMaxPercent() {
		return maxPercent;
	}

	private void parseOfferGuarantee(Notice notice) {
		if (null != notice.getCompl()
				&& null != notice.getCompl().getAdditionalInfo()) {
			for (String line : notice.getCompl().getAdditionalInfo()
					.split("\n")) {
				if (line.matches(".*ajánlati biztosíték.*")) {
					Pattern p = Pattern
							.compile(".*[^0-9\\- ]+ (?<g>\\d{1,3}( ?\\d{3}){2,10} )(?<c>[A-Za-z]+).*");
					Matcher m = p.matcher(line);
					while (m.find()) {
						long gv = Long.parseLong("0"
								+ m.group("g").replaceAll("[^0-9]", ""));
						if (gv > notice.getCompl().getGuaranteeValue()) {
							notice.getCompl().setGuaranteeValue(gv);
							notice.getCompl().setGuaranteeValueCurr(
									m.group("c"));
						}
					}
				}
			}
		}
	}

	public void setMaxPercent(double maxPercent) {
		this.maxPercent = maxPercent;
	}
}
