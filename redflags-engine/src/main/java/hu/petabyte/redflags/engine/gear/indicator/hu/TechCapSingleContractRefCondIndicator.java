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
@ConfigurationProperties(prefix = "techCapSingleContractRefCondIndicator")
public class TechCapSingleContractRefCondIndicator extends
		AbstractTD3CIndicator {

	@Override
	protected IndicatorResult flagImpl(Notice notice) {
		String s = String.format("%s", //
				fetchTechnicalCapacity(notice) //
				).trim();

		Pattern p = Pattern
				.compile("teljesíthető.*?külön (munk|szerződés).*?is");
		if (p.matcher(s).find()) {
			return null;
		}

		for (String line : s.split("\n")) {
			if (HuIndicatorHelper.isRef(line)
					&& (line.matches(".* egy szerződés keretében .*") || line
							.matches(".*Az előírt alkalmassági követelmény.*? (maximum|legfeljebb) (1|egy).*?(referenci|szerződés).*?igazolható"))
							&& !line.matches(".* (több|tetszőleges számú) (rész|szerződés|referenci).*")
							&& !line.matches(".* (is teljesíthető|egy referenciaként|amennyiben[^,]+egy szerződés|nem feltétel, hogy[^,]+egy szerződés).*")
							&& !line.matches(".* egy szerződés keretében[^,.]+is.*")
							&& !line.matches(".* folyamatosan egy szerződés keretében.*")) {
				return returnFlag();
			}
		}
		return null;
	}
}
