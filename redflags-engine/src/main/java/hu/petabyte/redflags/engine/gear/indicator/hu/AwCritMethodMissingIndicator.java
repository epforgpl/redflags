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

import java.util.regex.Pattern;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Zsolt Jurányi
 */
@Component
@ConfigurationProperties(prefix = "awCritMethodMissingIndicator")
public class AwCritMethodMissingIndicator extends AbstractTD3CIndicator {

	private Pattern methodPattern = Pattern
			.compile(
					"(módszer|pont(ozás|szám|ok).*(kiosztás|megadás|mód|skála)|pontkiosztás|pontskála|számítási metódus|számítási mód|ponthatár.*értékel|(relatív|abszolút) értékelés|arányosan (kevesebbet|többet)|arányosítás|sorba ?rendezés|pontozás|hasznossági függvény| pontszám)",
					Pattern.CASE_INSENSITIVE);

	@Override
	protected IndicatorResult flagImpl(Notice notice) {
		if (!fetchAwardCriteria(notice).equals("AC-2")) {
			return irrelevantData();
		}

		if (!methodPattern.matcher(fetchAdditionalInfo(notice)).find()) {
			return returnFlag();
		}
		return null;
	}

	public Pattern getMethodPattern() {
		return methodPattern;
	}

	public void setMethodPattern(Pattern methodPattern) {
		this.methodPattern = methodPattern;
	}

}
