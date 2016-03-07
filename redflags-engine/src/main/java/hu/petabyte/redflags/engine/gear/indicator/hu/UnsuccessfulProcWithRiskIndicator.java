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

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Zsolt Jurányi
 */
@Component
@ConfigurationProperties(prefix = "unsuccessfulProcWithRiskIndicator")
public class UnsuccessfulProcWithRiskIndicator extends AbstractTD7Indicator {

	@Override
	protected IndicatorResult flagImpl(Notice notice) {
		String s = fetchAdditionalInfo(notice).trim();
		for (String line : s.split("\n")) {
			if (line.matches(".*76.{0,15}\\(1\\).{0,15} [bde]\\).*")
					|| /* b) */line.matches(".*kizárólag érvénytelen.*")
					|| /* d) */line
							.matches(".*ajánlat(ot )?tevő.*(megkötés|teljesítés).*képtelen.*")
					|| /* e) */line
							.matches(".*ajánlat(ot )?tevő.*sértő cselekmény.*")) {
				return returnFlag();
			}
		}
		return null;
	}
}