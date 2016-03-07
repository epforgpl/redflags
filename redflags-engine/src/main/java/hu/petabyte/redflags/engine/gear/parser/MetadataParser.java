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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import hu.petabyte.redflags.engine.gear.AbstractGear;
import hu.petabyte.redflags.engine.model.CPV;
import hu.petabyte.redflags.engine.model.Data;
import hu.petabyte.redflags.engine.model.DisplayLanguage;
import hu.petabyte.redflags.engine.model.Notice;
import hu.petabyte.redflags.engine.model.Tab;
import hu.petabyte.redflags.engine.model.Type;
import hu.petabyte.redflags.engine.tedintf.TedInterfaceHolder;
import hu.petabyte.redflags.engine.tedintf.TedResponse;
import hu.petabyte.redflags.engine.util.JsoupUtils;

/**
 * @author Zsolt Jurányi
 */
@Component
public class MetadataParser extends AbstractGear {

	private static final Logger LOG = LoggerFactory.getLogger(MetadataParser.class);
	protected static final String DATE_FORMAT = "dd/MM/yyyy";

	protected @Value("${redflags.engine.gear.parse.lang:EN}") String rawLang;
	protected DisplayLanguage lang;

	protected @Autowired TedInterfaceHolder ted;

	@Override
	public void beforeSession() throws Exception {
		lang = DisplayLanguage.valueOf(checkNotNull(rawLang));
		LOG.debug("Parsing language is {}", lang);
	}

	protected List<CPV> parseCPVs(String value) {
		List<CPV> cpvs = new ArrayList<CPV>();
		for (String line : value.split("\n")) {
			try {
				String[] p = line.split(" - ");
				int id = Integer.parseInt(p[0]);
				String name = p[1];
				CPV cpv = CPV.findOrCreate(id, name);
				cpvs.add(cpv);
			} catch (Exception e) {
				LOG.warn("Cannot parse CPV: {}", line);
			}
		}
		return cpvs;
	}

	public Notice parseDataTab(Notice notice, Document dataTab) {
		Data data = notice.getData();

		notice.setCancelled(!dataTab.select("div#cancelDoc").isEmpty());

		for (Element tr : dataTab.select("table.data tr")) {
			String field = tr.select("th").first().text();
			String value = JsoupUtils.text(tr.select("td").last());
			LOG.trace("{} data: {} = {}", notice.getId(), field, value);
			setDataField(data, field, value);
		}
		return notice;
	}

	protected Date parseDate(String value) {
		try {
			// simple date format is not thread safe, so we create new instances
			return new SimpleDateFormat(DATE_FORMAT).parse(value);
		} catch (Exception e) {
			LOG.warn("Cannot parse date: {}", value);
			return null;
		}
	}

	protected Type parseType(String field, String value) {
		try {
			String[] p = value.split(" - ", 2);
			String id = field + "-" + p[0];
			String name = p[1];
			return Type.findOrCreate(id, name);
		} catch (Exception e) {
			LOG.warn("Cannot parse type: {}", value);
			return null;
		}
	}

	@Override
	protected Notice processImpl(Notice notice) throws Exception {
		TedResponse r = ted.get().requestNoticeTabQuietly(notice.getId(), lang, Tab.DATA);
		if (null != r) {
			Document dataTab = r.getParsedDocument();
			parseDataTab(notice, dataTab);
		}
		return notice;
	}

	protected void setDataField(Data data, String field, String value) {

		// categories
		if ("AA".equalsIgnoreCase(field)) {
			data.setAuthorityType(parseType(field, value));
		} else if ("AC".equalsIgnoreCase(field)) {
			data.setAwardCriteria(parseType(field, value));
		} else if ("TY".equalsIgnoreCase(field)) {
			data.setBidType(parseType(field, value));
		} else if ("NC".equalsIgnoreCase(field)) {
			data.setContractType(parseType(field, value));
		} else if ("TD".equalsIgnoreCase(field)) {
			data.setDocumentType(parseType(field, value));
		} else if ("PR".equalsIgnoreCase(field)) {
			data.setProcedureType(parseType(field, value));
		} else if ("RP".equalsIgnoreCase(field)) {
			data.setRegulationType(parseType(field, value));
		}

		// dates
		else if ("DT".equalsIgnoreCase(field)) {
			data.setDeadline(parseDate(value));
		} else if ("DD".equalsIgnoreCase(field)) {
			data.setDeadlineForDocs(parseDate(value));
		} else if ("DS".equalsIgnoreCase(field)) {
			data.setDocumentSent(parseDate(value));
		} else if ("PD".equalsIgnoreCase(field)) {
			data.setPublicationDate(parseDate(value));
		}

		// strings
		else if ("CY".equalsIgnoreCase(field)) {
			data.setCountry(value);
		} else if ("DI".equalsIgnoreCase(field)) {
			data.setDirective(value);
		} else if ("IA".equalsIgnoreCase(field)) {
			data.setInternetAddress(value);
		} else if ("OJ".equalsIgnoreCase(field)) {
			data.setOj(value);
		} else if ("OL".equalsIgnoreCase(field)) {
			data.setOriginalLanguage(value);
		} else if ("TW".equalsIgnoreCase(field)) {
			data.setPlace(value);
		} else if ("TI".equalsIgnoreCase(field)) {
			data.setTitle(value);
		}

		// lists
		else if ("RC".equalsIgnoreCase(field)) {
			for (String line : value.split("\n")) {
				data.getNutsCodes().add(line.trim());
			}
		} else if ("PC".equalsIgnoreCase(field)) {
			data.setCpvCodes(parseCPVs(value));
		} else if ("OC".equalsIgnoreCase(field)) {
			data.setOriginalCpvCodes(parseCPVs(value));
		}
	}

}
