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
@ConfigurationProperties(prefix = "persSitMissingCondIndicator")
public class PersSitMissingCondIndicator extends AbstractTD3CIndicator {

	private Pattern mustContainPattern = Pattern
			.compile("(Kbt\\. ?)?56\\.? ?§(-ának)?\\.? ?\\([12]\\)");
	private String excSentence = "228/2004";

	@Override
	protected IndicatorResult flagImpl(Notice notice) {
		String s = String.format("%s\n%s", //
				fetchPersonalSituation(notice), //
				fetchAdditionalInfo(notice) //
				).trim();

		// védelmi beszerzés
		if (s.contains(excSentence)) {
			return null;
		}

		// kötelező kizáró okok
		if (!mustContainPattern.matcher(s).find()) {
			return returnFlag();
		}

		return null;
	}

	public String getExcSentence() {
		return excSentence;
	}

	public Pattern getMustContainPattern() {
		return mustContainPattern;
	}

	public void setExcSentence(String excSentence) {
		this.excSentence = excSentence;
	}

	public void setMustContainPattern(Pattern mustContainPattern) {
		this.mustContainPattern = mustContainPattern;
	}

}
