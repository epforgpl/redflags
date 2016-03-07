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
import hu.petabyte.redflags.engine.gear.parser.MetadataParser;
import hu.petabyte.redflags.engine.model.Duration;
import hu.petabyte.redflags.engine.model.IndicatorResult;
import hu.petabyte.redflags.engine.model.Notice;
import hu.petabyte.redflags.engine.model.NoticeID;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Zsolt Jurányi
 */
@Component
@ConfigurationProperties(prefix = "deadlineIsTightIndicator")
public class DeadlineIsTightIndicator extends AbstractTD3CIndicator {

	// TODO numbers?

	private static final Logger LOG = LoggerFactory
			.getLogger(DeadlineIsTightIndicator.class);

	private Pattern docPattern = Pattern.compile(
			"(dokumentáció|kiegészítő irat)", Pattern.CASE_INSENSITIVE);
	private Pattern freePattern = Pattern
			.compile(
					"(fizetni kell: nem|ingyenes|térítésmentes|térítés nélkül|letöltés|letölthető|tölthető le|elérhető|érhető el|hozzáférhető|férhet.*?hozzá|megkérhető|megküld|közzétesz|átvehető|rendelkezésre bocsáj?t|bocsáj?t.*?rendelkezés)",
					Pattern.CASE_INSENSITIVE);
	private Pattern urgencyPattern = Pattern.compile("sürgősség");

	private @Autowired MetadataParser metadataParser;

	private boolean docs(Notice notice) {
		String s = String.format("%s\n%s", //
				fetchObtainingSpecs(notice), //
				fetchAdditionalInfo(notice) //
				);
		for (String line : s.split("\n")) {
			if (docPattern.matcher(line).find()
					&& freePattern.matcher(line).find()) {
				return true;
			}
		}
		return false;
	}

	@Override
	protected IndicatorResult flagImpl(Notice notice) {
		String ai = fetchAdditionalInfo(notice);
		String procType = fetchProcedureType(notice);
		Date deadline = notice.getData().getDeadline();
		Date sent = notice.getData().getDocumentSent();

		int minimum = 0;
		Date reference = sent;

		// CONDITION HILL

		if ("PR-1".equals(procType)) {
			minimum = 45;
			if (prev(notice, sent)) {
				minimum = 29;
				if (urgencyPattern.matcher(ai).find()) {
					minimum = 22;
				}
			}
			if (docs(notice) && minimum >= 29) {
				minimum -= 5;
			}
		} else if ("PR-2".equals(procType)) {
			minimum = 30;
		} else if (procType.matches("PR-[36]")) {
			minimum = 10;
		} else if (procType.matches("PR-[4C]")) {
			minimum = 30;
		}

		// FINAL BATTLE

		if (null == reference) {
			return null;
		}

		Duration d = new Duration();
		d.setBegin(reference);
		d.setEnd(deadline);
		d.fill();
		if (d.getInDays() < minimum) {
			return returnFlag("info", "days=" + d.getInDays(), "min=" + minimum);
		}

		return null;
	}

	public Pattern getDocPattern() {
		return docPattern;
	}

	public Pattern getFreePattern() {
		return freePattern;
	}

	public Pattern getUrgencyPattern() {
		return urgencyPattern;
	}

	private boolean prev(Notice notice, Date sent) {
		Notice prev = null;
		if (null != notice.getProc()
				&& null != notice.getProc().getPreviousPublication()) {
			boolean inside = false;
			for (String line : notice.getProc().getPreviousPublication()
					.split("\n")) {
				if (!inside
						&& line.matches(".*(Előzetes tájékoztató|Egyéb korábbi közzététel|Felhasználói oldalon közzétett hirdetmény).*")) {
					inside = true;
				} else if (inside) {
					Pattern p = Pattern
							.compile("(\\d{4})\\/.+ \\d+-(\\d+)( \\d{1,2}\\.\\d{1,2}\\.\\d{4})?");
					Matcher m = p.matcher(line);
					if (m.find()) {
						String y = m.group(1);
						String n = m.group(2).replaceAll("^0", "");
						String id = String.format("%s-%s", n, y);
						prev = new Notice(new NoticeID(id));
					}
					inside = false;
				}
			}
		}
		if (null != prev) {
			try {
				LOG.debug(
						"Fetching metadata of previous notice {} (current: {})",
						prev.getId(), notice.getId());
				metadataParser.process(prev);
			} catch (Exception e) {
				LOG.warn("Failed to parse metadata of " + prev.getId(), e);
			}
			Date prevSend = prev.getData().getDocumentSent();
			if (null != prevSend) {
				Duration dur = new Duration();
				dur.setBegin(prevSend);
				dur.setEnd(sent);
				dur.fill();
				if (dur.getInDays() > 51 && dur.getInDays() < 366) {
					return true;
				}
			}
		}
		LOG.trace("No valid previous notice found");
		return false;
	}

	public void setDocPattern(Pattern docPattern) {
		this.docPattern = docPattern;
	}

	public void setFreePattern(Pattern freePattern) {
		this.freePattern = freePattern;
	}

	public void setUrgencyPattern(Pattern urgencyPattern) {
		this.urgencyPattern = urgencyPattern;
	}

}
