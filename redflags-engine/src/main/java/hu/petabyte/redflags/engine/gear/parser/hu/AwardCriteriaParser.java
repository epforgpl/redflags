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
package hu.petabyte.redflags.engine.gear.parser.hu;

import hu.petabyte.redflags.engine.gear.AbstractGear;
import hu.petabyte.redflags.engine.model.Notice;
import hu.petabyte.redflags.engine.model.noticeparts.Procedure;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author Zsolt Jurányi
 */
@Component
public class AwardCriteriaParser extends AbstractGear {

	private static final Logger LOG = LoggerFactory
			.getLogger(AwardCriteriaParser.class);

	// TODO private, String, get-set, @ConfigurationProperties
	protected static final Pattern CONDITION_LINE = Pattern
			.compile("^[0-9]+\\..*[Ss]úlyszám.*$");
	protected static final Pattern CUT_1 = Pattern
			.compile("^[0-9]+\\. (?<r>[0-9]+[.:].*)");
	protected static final Pattern CUT_2 = Pattern
			.compile("^[0-9]+\\. rész[ \\/](?<r>[0-9]+[.:].*)");
	protected static final Pattern CONDITION_DETAILED = Pattern
			.compile("^(?<c>[0-9]+)[.:] ?((?<sc>[0-9]+)[.:])?.*([Ss]úlyszám:? (?<w>[0-9,]+)[^\\d]*|(?<ww>[0-9,]+) [Ss]úlyszám.*?)$");
	// protected static final Pattern PRICE_COND_POS = Pattern
	// .compile("(((\\.| )á|( |ajánlati |átalány|egység|össz|vétel|bruttó|nettó|készlet|kút)á)r(?!am)|díj|ellenérték|ellenszolgáltatás|(beszerzési)? (össz)?érték|ft|forint|eur(?!óp)|huf|(nettó|bruttó)? összeg|pénzügyi ajánlat)");
	// protected static final Pattern PRICE_COND_NEG = Pattern
	// .compile("(ártól való.*eltérés|beszerzési ár.*emel|bubor|csoportos beszedés|díjazás|hitel|jutalék|költség|kötbér|szolgáltatási díj|utalás|%-os megajánlás.*oep finanszírozásból való ajánlattevő által kért arány)");
	protected static final Pattern PAYM_DEADLINE_POS = Pattern
			.compile("(fizetési|ár(ának)? |díj|ellenszolgáltatás|ellentérték).* határidő");
	protected static final Pattern PAYM_DEADLINE_NEG = Pattern
			.compile("teljesítés");

	public void parseAwardCriteria(Notice notice) {

		if (!"AC-2".equals(notice.getData().getAwardCriteria().getId())) {
			return;
		}

		Procedure proc = notice.getProc();
		if (null == proc || null == proc.getAwardCriteria()) {
			return;
		}

		for (String line : proc.getAwardCriteria().split("\n")) {
			if (CONDITION_LINE.matcher(line).find()) {
				Matcher m;

				if ((m = CUT_1.matcher(line)).find()) {
					line = m.group("r");
				}

				if ((m = CUT_2.matcher(line)).find()) {
					line = m.group("r");
				}

				if ((m = CONDITION_DETAILED.matcher(line)).find()) {
					int c = Integer.parseInt(m.group("c"));
					int sc = 0;
					try {
						sc = Integer.parseInt(m.group("sc"));
					} catch (Exception e) {
					}
					double w = 0.0;
					if (null != m.group("w")) {
						w = Double.parseDouble(m.group("w"));
					}
					if (null != m.group("ww")) {
						w = Double.parseDouble(m.group("ww"));
					}

					proc.setAwardCriteriaCondCount(Math.max(c,
							proc.getAwardCriteriaCondCount()));
					proc.setAwardCriteriaSubCondCount(Math.max(sc,
							proc.getAwardCriteriaSubCondCount()));
					proc.setAwardCriteriaWeightSum(w
							+ proc.getAwardCriteriaWeightSum());

					line = line.toLowerCase();

					// Matcher pricePos = PRICE_COND_POS.matcher(line);
					// Matcher priceNeg = PRICE_COND_NEG.matcher(line);
					// boolean price = false;
					// if (pricePos.find()) {
					// price = true;
					// LOG.trace("{} possibly price cond: {}", notice.getId(),
					// pricePos.group(0));
					// }
					// if (price && priceNeg.find()) {
					// price = false;
					// LOG.trace("{} dropped because of: {}", notice.getId(),
					// priceNeg.group(0));
					// }
					// if (price) {
					// proc.setAwardCriteriaPriceCond(true);
					// proc.setAwardCriteriaPriceWeight(w
					// + proc.getAwardCriteriaPriceWeight());
					// }

					if (PAYM_DEADLINE_POS.matcher(line).find()
							&& !PAYM_DEADLINE_NEG.matcher(line).find()) {
						proc.setAwardCriteriaPaymentDeadline(true);
					}
				} // still cond
			} // cond
		} // lines
	}

	@Override
	protected Notice processImpl(Notice notice) throws Exception {
		try {
			parseAwardCriteria(notice);
		} catch (NullPointerException e) {
		}
		return notice;
	}

}
