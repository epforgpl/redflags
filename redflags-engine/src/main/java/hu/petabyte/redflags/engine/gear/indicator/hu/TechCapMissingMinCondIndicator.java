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
@ConfigurationProperties(prefix = "techCapMissingMinCondIndicator")
public class TechCapMissingMinCondIndicator extends AbstractTD3CIndicator {

	private Pattern mustContainPattern = Pattern
			.compile("Az alkalmasság minimumkövetelménye.*?:"
					+ "|Alkalmatlan.*? a részvételre jelentkező.*?ha:"
					+ "|Alkalmatlan.*? az [Aa]jánlattevő.*?ha:");

	@Override
	protected IndicatorResult flagImpl(Notice notice) {
		String s = String.format("%s", //
				fetchTechnicalCapacity(notice) //
				).trim();

		if (!mustContainPattern.matcher(s).find()) {
			return returnFlag();
		}
		return null;
	}

	public Pattern getMustContainPattern() {
		return mustContainPattern;
	}

	public void setMustContainPattern(Pattern mustContainPattern) {
		this.mustContainPattern = mustContainPattern;
	}
}
