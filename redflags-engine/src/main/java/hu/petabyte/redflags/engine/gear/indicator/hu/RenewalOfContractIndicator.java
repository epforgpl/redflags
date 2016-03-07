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
@ConfigurationProperties(prefix = "renewalOfContractIndicator")
public class RenewalOfContractIndicator extends AbstractTD3CIndicator {

	private byte maxCount = 2;
	private int maxMonths = 48;

	@Override
	protected IndicatorResult flagImpl(Notice notice) {
		boolean found = false;
		for (ObjOfTheContract obj : notice.getObjs()) {
			if (null != obj.getRawRenewable()) {
				found = true;
			}
			if (obj.isRenewable()) {

				if (obj.getRenewalCount() > maxCount) {
					return returnFlag("infoCount",
							"count=" + obj.getRawRenewalCount(), "max="
									+ maxCount);
				}

				if (null != obj.getRenewalDuration()
						&& obj.getRenewalDuration().getInMonths() > maxMonths) {
					return returnFlag("infoDur", "months="
							+ obj.getRenewalDuration().getInMonths(), "max="
							+ maxMonths);
				}

				if (0 == obj.getRenewalCount()
						&& null == obj.getRenewalDuration()) {
					return returnFlag("infoMiss");
				}
			}
		}
		if (!found) {
			return missingData();
		}
		return null;
	}

	public byte getMaxCount() {
		return maxCount;
	}

	public int getMaxMonths() {
		return maxMonths;
	}

	public void setMaxCount(byte maxCount) {
		this.maxCount = maxCount;
	}

	public void setMaxMonths(int maxMonths) {
		this.maxMonths = maxMonths;
	}

}
