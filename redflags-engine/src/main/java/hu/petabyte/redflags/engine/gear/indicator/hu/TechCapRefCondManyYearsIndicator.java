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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Zsolt Jurányi
 */
@Component
@ConfigurationProperties(prefix = "techCapRefCondManyYearsIndicator")
public class TechCapRefCondManyYearsIndicator extends AbstractTD3CIndicator {

	private long minRefYearsWorks = 5;
	private long minRefYearsSupply = 3;
	private long minRefYearsService = 3;
	private long maxRefYearsWorks = 8;
	private long maxRefYearsSupply = 6;
	private long maxRefYearsService = 6;

	@Override
	protected IndicatorResult flagImpl(Notice notice) {
		String s = fetchTechnicalCapacity(notice).trim();
		String contractType = fetchContractType(notice);
		if (s.isEmpty() || contractType.isEmpty()) {
			return null;
		}

		Pattern p = Pattern
				.compile(" (elmúlt|(meg)?előző|számított) (?<v>\\d+).*?(?<u>év|hónap)");
		Pattern p3 = Pattern
				.compile("(?<a>20\\d{2})-(?<b>20\\d{2})\\. években");

		int v = 0;
		for (String line : s.split("\n")) {
			if (HuIndicatorHelper.isRef(line)) {
				int vv = 0;
				Matcher m = p.matcher(line);
				if (m.find()) {
					vv = Integer.parseInt(m.group("v"));
					String u = m.group("u");
					if ("év".equals(u)) {
						vv *= 12;
					}
				} else {
					Matcher m3 = p3.matcher(line);
					if (m3.find()) {
						int a = Integer.parseInt(m3.group("a"));
						int b = Integer.parseInt(m3.group("b"));
						vv += (b - a + 1) * 12;
					}
				}
				v = Math.max(v, vv);
			}
		}

		if (0 == v) {
			return null;
		}

		v /= 12;

		if ("NC-1".equals(contractType)
				&& (v < minRefYearsWorks || v > maxRefYearsWorks)) {
			return returnFlag("infoWorks", "years=" + v, "min="
					+ minRefYearsWorks, "max=" + maxRefYearsWorks);
		}

		if ("NC-2".equals(contractType)
				&& (v < minRefYearsSupply || v > maxRefYearsSupply)) {
			return returnFlag("infoSupply", "years=" + v, "min="
					+ minRefYearsSupply, "max=" + maxRefYearsSupply);
		}

		if ("NC-4".equals(contractType)
				&& (v < minRefYearsService || v > maxRefYearsService)) {
			return returnFlag("infoService", "years=" + v, "min="
					+ minRefYearsService, "max=" + maxRefYearsService);
		}

		return null;
	}

	public long getMaxRefYearsService() {
		return maxRefYearsService;
	}

	public long getMaxRefYearsSupply() {
		return maxRefYearsSupply;
	}

	public long getMaxRefYearsWorks() {
		return maxRefYearsWorks;
	}

	public long getMinRefYearsService() {
		return minRefYearsService;
	}

	public long getMinRefYearsSupply() {
		return minRefYearsSupply;
	}

	public long getMinRefYearsWorks() {
		return minRefYearsWorks;
	}

	public void setMaxRefYearsService(long maxRefYearsService) {
		this.maxRefYearsService = maxRefYearsService;
	}

	public void setMaxRefYearsSupply(long maxRefYearsSupply) {
		this.maxRefYearsSupply = maxRefYearsSupply;
	}

	public void setMaxRefYearsWorks(long maxRefYearsWorks) {
		this.maxRefYearsWorks = maxRefYearsWorks;
	}

	public void setMinRefYearsService(long minRefYearsService) {
		this.minRefYearsService = minRefYearsService;
	}

	public void setMinRefYearsSupply(long minRefYearsSupply) {
		this.minRefYearsSupply = minRefYearsSupply;
	}

	public void setMinRefYearsWorks(long minRefYearsWorks) {
		this.minRefYearsWorks = minRefYearsWorks;
	}

}
