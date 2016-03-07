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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Zsolt Jurányi
 */
@Component
@ConfigurationProperties(prefix = "techCapExpertsExpCondManyYearsIndicator")
public class TechCapExpertsExpCondManyYearsIndicator extends
		AbstractTD3CIndicator {

	private byte maxYears = 4;

	@Override
	protected IndicatorResult flagImpl(Notice notice) {
		String s = String.format("%s\n%s", //
				fetchTechnicalCapacity(notice), //
				fetchAdditionalInfo(notice) //
				).trim();

		Pattern p = Pattern
				.compile("(legalább|minimum)? (?<v>\\d+) (?<u>év|hónap)(es|os)? ");
		int v = 0;
		String r = "(szakmai|szakértői)?([^ ]+)?( szerzett)? (tapasztalat|gyakorlat)(tal)?( rendelkező szakember)?";
		for (String line : s.split("\n")) {
			if (line.matches(".*" + r + ".*")) {
				Matcher m = p.matcher(line);
				if (m.find()) {
					int vv = Integer.parseInt(m.group("v"));
					String u = m.group("u");
					if ("év".equals(u)) {
						vv *= 12;
					}
					v = Math.max(v, vv);
				}
			}
		}

		v /= 12;
		if (v > maxYears) {
			return returnFlag("info", "years=" + v, "max=" + maxYears);
		}

		return null;
	}

	public byte getMaxYears() {
		return maxYears;
	}

	public void setMaxYears(byte maxYears) {
		this.maxYears = maxYears;
	}

}
