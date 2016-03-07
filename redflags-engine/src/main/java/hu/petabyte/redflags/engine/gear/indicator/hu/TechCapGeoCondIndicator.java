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
import hu.petabyte.redflags.engine.gear.indicator.helper.HuIndicatorHelper;
import hu.petabyte.redflags.engine.model.IndicatorResult;
import hu.petabyte.redflags.engine.model.Notice;

import java.util.regex.Pattern;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Zsolt Jurányi
 */
@Component
@ConfigurationProperties(prefix = "techCapGeoCondIndicator")
public class TechCapGeoCondIndicator extends AbstractTD3CIndicator {

	private Pattern geoCondPattern = Pattern
			.compile("( km(-en belül| .*távolság)|(ajánlatkérő székhelye szerinti|megvalósítással érintett) (megye|város|település|régió).*( közigazgatási | bel)területén| távolság).*(iroda|fiók|fióktelep|telep|telephell?y|raktár|ügyfélszolgálat|beváltóhely|helyi?ség|illetékességi terület|telek|rakodótér|ügyféltér|szervíz)");
	private Pattern exceptionPattern = Pattern
			.compile("távolság(ok)? rövidítés");

	@Override
	protected IndicatorResult flagImpl(Notice notice) {
		String s = String.format("%s\n%s", //
				fetchTechnicalCapacity(notice), //
				fetchAdditionalInfo(notice) //
				).trim();

		for (String line : s.toString().split("\n")) {
			line = HuIndicatorHelper.words2Digits(s);
			if (geoCondPattern.matcher(line).find()
					&& !exceptionPattern.matcher(line).find()) {
				return returnFlag();
			}
		}
		return null;
	}

	public Pattern getExceptionPattern() {
		return exceptionPattern;
	}

	public Pattern getGeoCondPattern() {
		return geoCondPattern;
	}

	public void setExceptionPattern(Pattern exceptionPattern) {
		this.exceptionPattern = exceptionPattern;
	}

	public void setGeoCondPattern(Pattern geoCondPattern) {
		this.geoCondPattern = geoCondPattern;
	}

}
