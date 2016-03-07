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

import java.util.regex.Pattern;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import hu.petabyte.redflags.engine.gear.indicator.AbstractTD3CIndicator;
import hu.petabyte.redflags.engine.gear.indicator.helper.HuIndicatorHelper;
import hu.petabyte.redflags.engine.model.IndicatorResult;
import hu.petabyte.redflags.engine.model.Notice;

/**
 * @author Zsolt Jurányi
 */
@Component
@ConfigurationProperties(prefix = "techCapEURefCondIndicator")
public class TechCapEURefCondIndicator extends AbstractTD3CIndicator {

	private Pattern euRefPattern = Pattern.compile(
			"társfinanszírozott|(EU-?|Európai Unió|közösségi?)(s|ból)?( származó)?( pénzügyi)? (által finanszírozott|finanszírozású|forrás|támogatás)");
	private Pattern excPattern = Pattern.compile(
			"(projektmenedzs|vezet|tanácsadó).*?(feladat|szolgáltatás)|dokumentáció (összeállítás|elkészítés)|(projekt|program).*?(ellenőrzés|értékelés|minősítés)|kapcsolódó.*?(projektmenedzseri|támogatás.*?szakértői)| EU .*támogatási kérelem|forrás elnyerésére vonatkozó.*projekt.*előkészítés|támogatások felhasználásának.*előkészítéséhez kapcsolódó");

	@Override
	protected IndicatorResult flagImpl(Notice notice) {
		String s = String.format("%s", //
				fetchTechnicalCapacity(notice) //
		).trim();

		for (String line : s.split("\n")) {
			if (HuIndicatorHelper.isRef(line) //
					&& !excPattern.matcher(line).find() //
					&& euRefPattern.matcher(line).find()) {
				System.out.println("*** EUREF: " + line);
				return returnFlag();
			}
		}
		return null;
	}

	public Pattern getEuRefPattern() {
		return euRefPattern;
	}

	public Pattern getExcPattern() {
		return excPattern;
	}

	public void setEuRefPattern(Pattern euRefPattern) {
		this.euRefPattern = euRefPattern;
	}

	public void setExcPattern(Pattern excPattern) {
		this.excPattern = excPattern;
	}

}
