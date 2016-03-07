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

import hu.petabyte.redflags.engine.gear.indicator.AbstractTD7Indicator;
import hu.petabyte.redflags.engine.gear.parser.MetadataParser;
import hu.petabyte.redflags.engine.gear.parser.RawValueParser;
import hu.petabyte.redflags.engine.gear.parser.TemplateBasedDocumentParser;
import hu.petabyte.redflags.engine.model.Duration;
import hu.petabyte.redflags.engine.model.IndicatorResult;
import hu.petabyte.redflags.engine.model.Notice;
import hu.petabyte.redflags.engine.model.noticeparts.Award;
import hu.petabyte.redflags.engine.model.noticeparts.Procedure;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Zsolt Jurányi
 */
@Component
@ConfigurationProperties(prefix = "decisionDateDiffersFromOpeningDateIndicator")
public class DecisionDateDiffersFromOpeningDateIndicator extends
AbstractTD7Indicator {

	private static final Logger LOG = LoggerFactory
			.getLogger(DecisionDateDiffersFromOpeningDateIndicator.class);

	private @Autowired MetadataParser metadataParser;
	private @Autowired TemplateBasedDocumentParser docParser;
	private @Autowired RawValueParser rawParser;
	private int addToMaintainDays = 60;

	@Override
	protected IndicatorResult flagImpl(Notice notice) {
		String procType = fetchProcedureType(notice);
		if (!procType.equals("PR-1")) {
			return irrelevantData();
		}

		parseFamilyMeta(notice);
		int selfIndex = memberIndexOfSelf(notice);
		int td3Index = memberIndexOfClosestTD3(notice, selfIndex);

		Notice deadlineDoc = null;
		Notice td3 = null;
		Procedure proc = null;
		Duration maintainDuration = null;
		if (null == (deadlineDoc = getDeadlineDoc(notice, selfIndex, td3Index))
				|| null == (td3 = getTD3(notice, td3Index))
				|| null == (proc = td3.getProc())
				|| null == (maintainDuration = proc.getMinMaintainDuration())) {
			return missingData();
		}

		Date td3Deadline = td3.getData().getDeadline();
		Date deadline = deadlineDoc.getData().getDeadline();
		String mod = td3Deadline.equals(deadline) ? "" : "módosító doksi: "
				+ deadlineDoc.getUrl();

		if (0 == maintainDuration.getInDays()) {
			Date d = rawParser.parseDate(maintainDuration.getRaw());
			maintainDuration.setBegin(deadline);
			maintainDuration.setEnd(d);
			maintainDuration.fill();
		}

		LOG.trace("Deadline: {}", deadline);
		LOG.trace("Maintain: {}", proc.getMinMaintainDuration().getRaw());

		int maxDays = addToMaintainDays + maintainDuration.getInDays();
		for (Award a : notice.getAwards()) {
			Duration d = new Duration();
			d.setBegin(deadlineDoc.getData().getDeadline());
			d.setEnd(a.getDecisionDate());
			d.fill();
			LOG.trace("Decision: {} ({} days) (max={})", a.getDecisionDate(),
					d.getInDays(), maxDays);
			if (d.getInDays() > maxDays) {
				// System.out
				// .println("DECISION DATE FLAGGED DOCS;"
				// + td3.getUrl()
				// + ";"
				// + notice.getUrl()
				// + ";"
				// + String.format(
				// "Az ajánlatok felbontása és a döntés között túl hosszú idő telt el: %d > %d %s",
				// d.getInDays(), maxDays, mod));
				return returnFlag("info", "days=" + d.getInDays(), "max="
						+ maxDays);
			}
		}

		return null;
	}

	public int getAddToMaintainDays() {
		return addToMaintainDays;
	}

	private int memberIndexOfSelf(Notice notice) {
		List<Notice> familyMembers = notice.getFamilyMembers();
		for (int i = 0; i < familyMembers.size(); i++) {
			Notice m = familyMembers.get(i);
			if (m.getId().equals(notice.getId())) {
				return i;
			}
		}
		return familyMembers.size() - 1;
	}

	private int memberIndexOfClosestTD3(Notice notice, int selfIndex) {
		List<Notice> familyMembers = notice.getFamilyMembers();
		for (int i = selfIndex - 1; i > 0; i--) {
			Notice m = familyMembers.get(i);
			if (m.getData().getDocumentType().getName().equals("TD-3")) {
				return i;
			}
		}
		return 0;
	}

	private Notice getDeadlineDoc(Notice notice, int selfIndex, int td3Index) {
		List<Notice> familyMembers = notice.getFamilyMembers();
		for (int i = selfIndex; i >= td3Index; i--) {
			Notice member = familyMembers.get(i);
			Date memberDeadline = member.getData().getDeadline();
			if (null != memberDeadline) {
				return member;
			}
		}
		return null;
	}

	private void parseFamilyMeta(Notice notice) {
		for (Notice n : notice.getFamilyMembers()) {
			try {
				metadataParser.process(n);
			} catch (Exception e) {
				LOG.warn("Could not parse metadata of {} notice's member: {}",
						notice.getId(), n.getId());
				LOG.trace("Failed to parse metadata of " + n.getId(), e);
			}
		}
	}

	private Notice getTD3(Notice notice, int td3Index) {
		Notice n = notice.getFamilyMembers().get(td3Index);
		try {
			docParser.process(n);
			rawParser.process(n);
			return n;
		} catch (Exception e) {
			LOG.warn("Could not parse TD-3 of {} notice's member: {}",
					notice.getId(), n.getId());
			LOG.trace("Failed to parse metadata of " + n.getId(), e);
		}
		return null;
	}

	public void setAddToMaintainDays(int addToMaintainDays) {
		this.addToMaintainDays = addToMaintainDays;
	}
}
