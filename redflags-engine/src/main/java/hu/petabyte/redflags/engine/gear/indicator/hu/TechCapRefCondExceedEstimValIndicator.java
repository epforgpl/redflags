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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Zsolt Jurányi
 */
@Component
@ConfigurationProperties(prefix = "techCapRefCondExceedEstimValIndicator")
public class TechCapRefCondExceedEstimValIndicator extends
		AbstractTD3CIndicator {

	private static final Logger LOG = LoggerFactory
			.getLogger(TechCapRefCondExceedEstimValIndicator.class);

	private Pattern valuePattern = Pattern
			.compile("[^0-9] (?<v>\\d{1,3}( ?\\d{3}){2,10})(?<c> [A-Za-z]+)[^A-Za-z]");
	private Pattern creditPattern = Pattern
			.compile("(?<v>\\d{1,3}( ?\\d{3}){2,10})");

	public Pattern getValuePattern() {
		return valuePattern;
	}

	public void setValuePattern(Pattern valuePattern) {
		this.valuePattern = valuePattern;
	}

	public Pattern getCreditPattern() {
		return creditPattern;
	}

	public void setCreditPattern(Pattern creditPattern) {
		this.creditPattern = creditPattern;
	}

	@Override
	protected IndicatorResult flagImpl(Notice notice) {
		String s = String.format("%s", //
				fetchTechnicalCapacity(notice) //
				).trim();
		String contractType = fetchContractType(notice);
		long estimVal = fetchEstimatedValue(notice);
		String estimCurr = fetchEstimatedValueCurrency(notice);
		if (contractType.isEmpty() || 0 == estimVal || null == estimCurr) {
			return missingData();
		}

		if (estimCurr.matches("Ft|forint")) {
			estimCurr = "HUF";
		}

		long sum = 0;
		for (String line : s.split("\n")) {
			if (HuIndicatorHelper.isRef(line)
					|| line.matches("[0-9]\\. rész( eseté)?ben.*")) {

				// exceptions I
				line = line
						.replaceAll(
								"legalább.*?(értékű|értéket elérő).*?beruházás.*?(irányul|kapcsolód|vonatkoz)",
								"[CUT]");
				line = line.replaceAll(".*szerződések közül legalább.*",
						"[CUT]");
				line = line.replaceAll(
						".*építésekor végzett.*műszaki ellenőr.*", "[CUT]");
				if ("NC-4".equals(contractType)) {
					line = line
							.replaceAll(
									"kivitelezési munk.*?összérték.*?(?<v>\\d{1,3}( ?\\d{3}){2,10})(?<c> [A-Za-z]+)[^A-Za-z].*",
									"[CUT]");
				}
				line = line
						.replaceAll(
								".*(megvalósításához|beruházáshoz) kapcsolódó (mérnöki tanácsadás|műszaki ellenőri tevékenység).*",
								"[CUT]");
				line = line.replaceAll(
						".*hitel.*?(nyújtás|finanszírozás).*?referenci.*",
						"[CUT]");

				Matcher m = valuePattern.matcher(line);
				long mv = 0;
				while (m.find()) {

					// exceptions II
					String before = line.substring(0, m.start());
					int beforeLen = before.length();
					before = before.replaceAll(".*[,;.]", "");
					int commaBefore = beforeLen - before.length();
					String part = line.substring(commaBefore).split("[,;.]")[0];

					// REV 746 ---
					if ("NC-4".equals(contractType)
							&& part.matches(".*összértékű.*megvalósu(lt|ló) építési szerződés teljesítés.*")) {
						continue;
					}
					if (part.matches(".*(kivitelezési|beruházási|építési) értéket (elérő|meghaladó).*projektre vonatkozó beruházás.*")) {
						continue;
					}
					if (part.matches(".*(beruházás értéke|értékű projekt).*")) {
						continue;
					}

					// NEW
					if ("NC-1".equals(contractType)
							&& part.matches(".*összértékű.*?(létesítmény|intézmény).*?(építés|felújítás|megvalósítás).*?(irányul|kapcsolód|vonatkoz).*")) {
						continue;
					}
					if ("NC-4".equals(contractType)
							&& part.matches(".*kivitelezés.*? értéke.*")) {
						continue;
					}
					if ("NC-4".equals(contractType)
							&& part.matches(".*teljesítést elérő.*")) {
						continue;
					}
					if (part.matches(".*(költségvetési főössze(g|sítő)|saját tők|árbevétel|pénzforgalom).*")) {
						continue;
					}
					if (part.contains("beruházási értékű")) {
						continue;
					}
					if (part.matches(".*értékű .*?beruházás.*")) {
						continue;
					}
					if (part.matches(".*beruházás\\(?ok\\)? összes értéke.*")) {
						continue;
					}
					if (part.contains("projekttel kapcsolat")) {
						continue;
					}
					if (part.matches(".*projekt(\\(?ek\\)?)? (össz)?értéke.*")) {
						continue;
					}
					if (part.contains("projekt összköltségű")) {
						continue;
					}
					if (part.contains("projekt költségvetés")) {
						continue;
					}
					if (part.contains("biztosítási összeg")) {
						continue;
					}

					System.out.println("*** " + line);
					System.out.println(" >  " + part);

					// parse
					long v = Long.parseLong("0"
							+ m.group("v").replaceAll(" ", ""));
					String c = m.group("c").trim();
					if (c.matches("Ft|forint.*")) {
						c = "HUF";
					}
					if ("eFt".equals(c)) {
						v *= 1000;
						c = "HUF";
					}
					if (c.equals(estimCurr)) {
						// sum += v;
						mv = Math.max(mv, v);
					}
				}
				sum += mv;
			}
		}

		LOG.trace("Estimated value: {}, ref cond value: {}", estimVal, sum);
		if (sum > estimVal) {
			return returnFlag("info", "ref=" + (sum / 1000000), "estim="
					+ (estimVal / 1000000), "curr=" + estimCurr);
		}

		// credits
		long credit = 0L;
		long creditRef = 0L;
		if (!notice.getObjs().isEmpty()) {
			String tq = "" + notice.getObjs().get(0).getTotalQuantity();
			for (String line : tq.split("\n")) {
				if (line.toLowerCase().contains("hitel")) {
					Matcher m = creditPattern.matcher(line);
					if (m.find()) {
						credit = Math.max(
								credit,
								Long.parseLong("0"
										+ m.group("v").replaceAll(" ", "")));
					}
				}
			}
		}
		if (0 < credit) {
			for (String line : s.split("\n")) {
				if ((HuIndicatorHelper.isRef(line) || line
						.matches("[0-9]\\. rész( eseté)?ben"))
						&& line.matches(".*hitel.*?(nyújtás|finanszírozás).*")
						&& !line.matches(".*költségvetési.*")) {
					Matcher m = valuePattern.matcher(line);
					if (m.find()) {
						creditRef += Long.parseLong("0"
								+ m.group("v").replaceAll(" ", ""));
					}
				}
			}
		}
		LOG.trace("Credit: {}, ref cond for credit: {}", credit, creditRef);
		if (0 < credit && credit < creditRef) {
			return returnFlag("info", "ref=" + (sum / 1000000), "estim="
					+ (estimVal / 1000000), "curr=" + estimCurr);
		}
		return null;
	}
}
