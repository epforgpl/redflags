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
@ConfigurationProperties(prefix = "unsuccessfulProcWithoutInfo1Indicator")
public class UnsuccessfulProcWithoutInfo1Indicator extends AbstractTD7Indicator {

	@Override
	protected IndicatorResult flagImpl(Notice notice) {
		String s = fetchAdditionalInfo(notice).trim();
		if (unsuccessful(notice)) {
			for (String line : s.split("\n")) {
				if (line.matches(".*76.*?[\\( ]1[\\) ].*(bek\\. )?[a-h\\-]{1,3}\\)?.*")
						|| lineMatchesKbt76Text(line)) {
					return null;
				}
			}
			return returnFlag();
		} else {
			return irrelevantData();
		}
	}

	private boolean lineMatchesKbt76Text(String line) {
		line = line.toLowerCase();
		return // line.contains("eredménytelen") &&
		(
		// a)
		(line.matches(".*(nem|nincs).*(nyújt|érkez|tett).*") && line
								.matches(".*(ajánlat|részvételi jelentkezés).*"))//
				// b)
				|| (line.matches(".*(kizárólag|csak).*érvénytelen.*(ajánlat|részvételi jelentkezés).*(nyújt|érkez|tett).*"))
				|| (line.matches(".*(mindegyik|minden|összes|valamennyi).*(ajánlat|részvételi jelentkezés).*érvénytelen.*"))
				|| (line.matches(".*74.*\\([123\\-]+\\).*"))
				// c)
				|| (line.matches(".*ajánlatkérő.*fedezet.*mérték.*")//

				&& line.matches(".*(nem|sem) (nyújt|érkez|tett).*megfelelő ajánlat"))
				|| line.matches(".*ajánlat.*fedezetnél magasabb.*")//

										// d)
				|| (line.matches(".*szerződés.*(kötés|teljesítés).*")//
				&& line.matches(".*(nem (lenne )?képes|képtelen|lehetetlen).*"))
				// e)
				|| (line.matches(".*(eljárás tisztaság|érdek(é|ei)t súlyosan sért).*"))
				// f)
				|| (line.matches(".*lejár.*ajánlati kötöttség.*")//
				&& line.contains("egyetlen ajánlattevő sem tartja"))
		// g)
		|| (line.matches(".*(új.*?eljárás|eláll.*eljárás (lefolytatás|lebonyolítás)).*"))
		//
		);
	}

	private boolean unsuccessful(Notice notice) {
		if (null == notice.getAwards() || notice.getAwards().isEmpty()) {
			return true;
		}

		for (Award a : notice.getAwards()) {
			if (null == a.getWinnerOrg()) {
				return true;
			}
		}
		return false;
	}

}
