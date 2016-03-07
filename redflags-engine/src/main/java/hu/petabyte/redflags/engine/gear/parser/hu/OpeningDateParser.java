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
import hu.petabyte.redflags.engine.gear.parser.RawValueParser;
import hu.petabyte.redflags.engine.model.Notice;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Zsolt Jurányi
 */
@Component
public class OpeningDateParser extends AbstractGear {

	private Pattern P1 = Pattern
			.compile("részvételi.*?bontás.*?határidő lejártakor");
	private Pattern P2 = Pattern
			.compile("([Rr]észvételi jelentkezés.*?bontás|bontási eljárás)[^\\n]+?\\n?[^\\n]*?(?<d>\\d{1,2}\\.\\d{1,2}\\.\\d{4})");

	private @Autowired RawValueParser rvp;

	public RawValueParser getRvp() {
		return rvp;
	}

	@Override
	protected Notice processImpl(Notice notice) throws Exception {
		if (null != notice.getProc()) {
			String s = notice.getProc().getOpeningConditions();
			if (null != s) {
				Pattern p = Pattern
						.compile("(?<d>\\d{1,2}\\.\\d{1,2}\\.\\d{4})");
				Matcher m = p.matcher(s);
				if (m.find()) {
					notice.getProc()
							.setOpeningDate(rvp.parseDate(m.group("d")));
				}
			}
			if (null == notice.getProc().getOpeningDate()) {

				try {
					String ai = notice.getCompl().getAdditionalInfo();
					if (P1.matcher(ai).find()) {
						notice.getProc().setOpeningDate(
								notice.getData().getDeadline());
					}
					Matcher mr = P2.matcher(ai);
					if (mr.find()) {
						String d = mr.group("d");
						notice.getProc().setOpeningDate(rvp.parseDate(d));
					}
				} catch (Exception e) {
				}
			}
		}
		return notice;
	}

	public void setRvp(RawValueParser rvp) {
		this.rvp = rvp;
	}

}
