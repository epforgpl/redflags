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
import hu.petabyte.redflags.engine.gear.indicator.helper.KMonitorInstitutions;
import hu.petabyte.redflags.engine.model.IndicatorResult;
import hu.petabyte.redflags.engine.model.Notice;
import hu.petabyte.redflags.engine.model.noticeparts.Award;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Zsolt Jurányi
 */
@Component
@ConfigurationProperties(prefix = "winnerOrgInKMDBIndicator")
public class WinnerOrgInKMDBIndicator extends AbstractTD7Indicator {

	private @Autowired KMonitorInstitutions kmdb;

	@Override
	public void beforeSession() throws Exception {
		kmdb.init();
		setWeight(0.5);
		super.beforeSession();
	}

	@Override
	protected IndicatorResult flagImpl(Notice notice) {
		for (Award a : notice.getAwards()) {
			if (null != a.getWinnerOrg()) {
				String name = a.getWinnerOrg().getName();
				String found = kmdb.findOrgName(name);
				if (null != found) {
					return returnFlag("info", "tedOrg=" + name, "kmdbOrg="
							+ found);
				}
			}
		}
		return null;
	}
}
