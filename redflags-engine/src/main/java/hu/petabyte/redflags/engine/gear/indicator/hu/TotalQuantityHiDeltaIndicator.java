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
import hu.petabyte.redflags.engine.model.IndicatorResult.IndicatorResultType;
import hu.petabyte.redflags.engine.model.noticeparts.ObjOfTheContract;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Zsolt Jurányi
 */
@Component
@ConfigurationProperties(prefix = "totalQuantityHiDeltaIndicator")
public class TotalQuantityHiDeltaIndicator extends AbstractTD3CIndicator {

	private double maxPercent = 49.999;
	private Pattern posPattern = Pattern
			.compile(
					"\\(?(\\+\\/?-|\\+|-|–|tételenként|(pozitív|negatív) irányba|legalább|legfeljebb|minimum|maximum|mennyiség|plusz|mínusz)\\)? ?(?<p>[0-9.,]+) ?(%|százalék)",
					Pattern.CASE_INSENSITIVE);
	private Pattern negPattern = Pattern
			.compile(
					"(rendelkezésre állás|számla|megtakarítás|képvisel|osztás|rendelkező|biztonság|fővonal|figyelem|arány|közcél|nyomvonal|mutató|amelynek.*%|ügyf[eé]l|nedvesség|létszám|hatásfok|üzemidő|töménység|célcsoport|közel.*?%|telítettség|%-át|valamennyi rész|teljesítmény|adag|megoszlás|anyagmennyiség|időráfordítás|\\w+nap| nap|%-a erejéig)",
					Pattern.CASE_INSENSITIVE);

	@Override
	protected IndicatorResult flagImpl(Notice notice) {
		StringBuilder sb = new StringBuilder();
		for (ObjOfTheContract obj : notice.getObjs()) {
			sb.append(obj.getTotalQuantity());
			sb.append("\n");
		}

		boolean found = false;
		for (String line : sb.toString().split("\n")) {
			Matcher m;

			m = negPattern.matcher(line);
			if (m.find()) {
				continue;
			}

			m = posPattern.matcher(line);
			while (m.find()) {
				found = true;
				String ps = "0" + m.group("p").replaceAll(",", ".");
				try {
					Double p = Double.parseDouble(ps);
					if (p > maxPercent) {
						return returnFlag("info",
								"percent=" + String.format("%.1f", p));
					}
				} catch (NumberFormatException ex) {
				}
			}
		}

		if (!found) {
			if (sb.toString()
					.contains(
							"Az ajánlatkérő az így megadott mennyiségtől különdíjmentesen eltérhet.")) {
				return returnFlag("info2");
			} else {
				return new IndicatorResult(this,
						IndicatorResultType.IRRELEVANT_DATA);
			}
		}

		return null;
	}

	public double getMaxPercent() {
		return maxPercent;
	}

	public Pattern getNegPattern() {
		return negPattern;
	}

	public Pattern getPosPattern() {
		return posPattern;
	}

	public void setMaxPercent(double maxPercent) {
		this.maxPercent = maxPercent;
	}

	public void setNegPattern(Pattern negPattern) {
		this.negPattern = negPattern;
	}

	public void setPosPattern(Pattern posPattern) {
		this.posPattern = posPattern;
	}

}
