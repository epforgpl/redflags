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

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Zsolt Jurányi
 */
@Component
@ConfigurationProperties(prefix = "fwAgHighEstimatedValueIndicator")
public class FwAgHighEstimatedValueIndicator extends AbstractTD3CIndicator {

	private long maxValueInWorks = 1500 * 1000000;
	private long maxValueInSupply = 1000 * 1000000;
	private long maxValueInService = 1000 * 1000000;

	@Override
	protected IndicatorResult flagImpl(Notice notice) {
		if (fetchFrameworkAgreement(notice).trim().isEmpty()) {
			return irrelevantData();
		}
		String contractType = fetchContractType(notice);
		long estimVal = fetchEstimatedValue(notice);
		String estimCurr = fetchEstimatedValueCurrency(notice);
		if (contractType.isEmpty() || 0L == estimVal || null == estimCurr) {
			return missingData();
		}

		if ("NC-1".equals(contractType) && estimVal > maxValueInWorks) {
			return returnFlag("infoWorks", "estim=" + (estimVal / 1000000),
					"max=" + (maxValueInWorks / 1000000));
		}

		if ("NC-2".equals(contractType) && estimVal > maxValueInSupply) {
			return returnFlag("infoSupply", "estim=" + (estimVal / 1000000),
					"max=" + (maxValueInSupply / 1000000));
		}

		if ("NC-4".equals(contractType) && estimVal > maxValueInService) {
			return returnFlag("infoService", "estim=" + (estimVal / 1000000),
					"max=" + (maxValueInService / 1000000));
		}

		return null;
	}

	public long getMaxValueInService() {
		return maxValueInService;
	}

	public long getMaxValueInSupply() {
		return maxValueInSupply;
	}

	public long getMaxValueInWorks() {
		return maxValueInWorks;
	}

	public void setMaxValueInService(long maxValueInService) {
		this.maxValueInService = maxValueInService;
	}

	public void setMaxValueInSupply(long maxValueInSupply) {
		this.maxValueInSupply = maxValueInSupply;
	}

	public void setMaxValueInWorks(long maxValueInWorks) {
		this.maxValueInWorks = maxValueInWorks;
	}

}
