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
@ConfigurationProperties(prefix = "highEstimatedValueIndicator")
public class HighEstimatedValueIndicator extends AbstractTD3CIndicator {

	private long maxValueInWorks = 1500 * 1000000;
	private long maxValueInSupply = 1000 * 1000000;
	private long maxValueInService = 1000 * 1000000;

	@Override
	protected IndicatorResult flagImpl(Notice notice) {
		String contractType = notice.getData().getContractType().getId();

		boolean foundEstimValue = false;
		for (ObjOfTheContract obj : notice.getObjs()) {
			if (null == obj.getEstimatedValueCurr()
					|| !obj.getEstimatedValueCurr().matches("HUF|Ft|forint")) {
				continue;
			}
			if (null != obj.getFrameworkAgreement()) {
				return null; // not flagging fw ag
			}
			if (obj.getEstimatedValue() > 0) {
				foundEstimValue = true;
			}

			if ("NC-1".equals(contractType)
					&& obj.getEstimatedValue() > maxValueInWorks) {
				return returnFlag("infoWorks",
						"estim=" + (obj.getEstimatedValue() / 1000000), "max="
								+ (maxValueInWorks / 1000000));
			}

			if ("NC-2".equals(contractType)
					&& obj.getEstimatedValue() > maxValueInSupply) {
				return returnFlag("infoSupply",
						"estim=" + (obj.getEstimatedValue() / 1000000), "max="
								+ (maxValueInSupply / 1000000));
			}

			if ("NC-4".equals(contractType)
					&& obj.getEstimatedValue() > maxValueInService) {
				return returnFlag("infoService",
						"estim=" + (obj.getEstimatedValue() / 1000000), "max="
								+ (maxValueInService / 1000000));
			}
		}

		if (!foundEstimValue) {
			return missingData();
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
