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
import hu.petabyte.redflags.engine.model.noticeparts.ObjOfTheContract;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Zsolt Jurányi
 */
@Component
@ConfigurationProperties(prefix = "fwAgLongDurationIndicator")
public class FwAgLongDurationIndicator extends AbstractTD3CIndicator {

	private byte maxYears = 4;
	private String justificationSentence = "Indokolás arra az esetre vonatkozóan, ha a keretmegállapodás időtartama meghaladja a négy évet:";

	@Override
	protected IndicatorResult flagImpl(Notice notice) {
		boolean found = false;
		for (ObjOfTheContract obj : notice.getObjs()) {
			if (null != obj.getFrameworkAgreement()) {
				found = true;

				int m = obj.getFrameworkDuration().getInMonths();
				long max = maxYears * 12;
				if (max < m) {

					String label = "info";

					// check justification
					String[] p = obj.getFrameworkAgreement().split(
							justificationSentence, 2);
					if (p.length < 2 || p[1].trim().isEmpty()) {
						label = "info2";
					}

					return returnFlag(label, "months=" + m, "max=" + max);
				}
			}
		}
		if (!found) {
			return irrelevantData();
		}
		return null;
	}

	public String getJustificationSentence() {
		return justificationSentence;
	}

	public byte getMaxYears() {
		return maxYears;
	}

	public void setJustificationSentence(String justificationSentence) {
		this.justificationSentence = justificationSentence;
	}

	public void setMaxYears(byte maxYears) {
		this.maxYears = maxYears;
	}

}
