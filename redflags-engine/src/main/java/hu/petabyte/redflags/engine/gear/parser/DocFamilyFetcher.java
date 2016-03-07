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
package hu.petabyte.redflags.engine.gear.parser;

import static com.google.common.base.Preconditions.checkNotNull;
import hu.petabyte.redflags.engine.gear.AbstractGear;
import hu.petabyte.redflags.engine.model.DisplayLanguage;
import hu.petabyte.redflags.engine.model.Notice;
import hu.petabyte.redflags.engine.model.NoticeID;
import hu.petabyte.redflags.engine.model.Tab;
import hu.petabyte.redflags.engine.tedintf.TedInterfaceHolder;
import hu.petabyte.redflags.engine.tedintf.TedResponse;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Zsolt Jurányi
 */
@Component
public class DocFamilyFetcher extends AbstractGear {

	// TODO AVOID INFINITE LOOPS - AVOID STARTING NEW SUB SESSION WITH THIS GEAR

	protected static final Logger LOG = LoggerFactory
			.getLogger(DocFamilyFetcher.class);
	protected static final String DATE_FORMAT = "dd-MM-yyyy";

	protected @Value("${redflags.engine.gear.parse.lang:EN}") String rawLang;
	protected DisplayLanguage lang;
	protected @Autowired TedInterfaceHolder ted;

	@Override
	public void beforeSession() throws Exception {
		lang = DisplayLanguage.valueOf(checkNotNull(rawLang));
		LOG.debug("Parsing language is {}", lang);
	}

	private void determineDocFamilyId(Notice notice) {
		NoticeID docFamilyId = notice.getId();
		for (Notice n : notice.getFamilyMembers()) {
			NoticeID memberId = n.getId();
			if (memberId.compareTo(docFamilyId) < 0) {
				docFamilyId = memberId;
			}
		}
		notice.setDocumentFamilyId(docFamilyId);
		for (Notice m : notice.getFamilyMembers()) {
			m.setDocumentFamilyId(docFamilyId);
		}
		LOG.debug("Document family ID for notice {} is {}", notice.getId(),
				docFamilyId);
	}

	public Notice parseDocFamilyTab(Notice notice, Document docFamilyTab) {
		for (Element memberTable : docFamilyTab.select("table.family")) {
			try {
				NoticeID memberId = new NoticeID(memberTable
						.select("thead a[href~=TED:NOTICE:]").first().text()
						.split(":", 2)[0]);
				if (!notice.getId().equals(memberId)) {
					Notice memberNotice = new Notice(memberId);

					Elements tds = memberTable.select("tbody tr").first()
							.select("td.bgGreen");

					String rawMemberPubDate = tds.get(0).text();
					Date memberPubDate = new SimpleDateFormat(DATE_FORMAT)
					.parse(rawMemberPubDate);
					memberNotice.getData().setPublicationDate(memberPubDate);

					if (tds.size() > 1) {
						String rawMemberDeadline = tds.get(1).text();
						Date memberDeadline = new SimpleDateFormat(DATE_FORMAT)
						.parse(rawMemberDeadline);
						memberNotice.getData().setDeadline(memberDeadline);
					}

					notice.getFamilyMembers().add(memberNotice);
					LOG.trace("{} Member found: {} - {} (deadline: {})",
							notice.getId(), memberId, memberPubDate,
							memberNotice.getData().getDeadline());
				}
			} catch (Exception e) {
				LOG.warn("Cannot parse a document family member of notice "
						+ notice.getId(), e);
			}
		}
		return notice;
	}

	@Override
	protected Notice processImpl(Notice notice) throws Exception {
		TedResponse r = ted.get().requestNoticeTabQuietly(notice.getId(), lang,
				Tab.DATA);
		if (null != r) {
			Document dataTab = r.getParsedDocument();
			if (!dataTab.select("a[href~=tabId=4").isEmpty()) {
				TedResponse r2 = ted.get().requestNoticeTabQuietly(
						notice.getId(), lang, Tab.DOCUMENT_FAMILY);
				if (null != r2) {
					Document docFamilyTab = r2.getParsedDocument();
					notice = parseDocFamilyTab(notice, docFamilyTab);
				}
			}
		}

		determineDocFamilyId(notice);

		return notice;
	}

}
