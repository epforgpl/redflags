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
import hu.petabyte.redflags.engine.model.Address;
import hu.petabyte.redflags.engine.model.CPV;
import hu.petabyte.redflags.engine.model.DisplayLanguage;
import hu.petabyte.redflags.engine.model.Duration;
import hu.petabyte.redflags.engine.model.Notice;
import hu.petabyte.redflags.engine.model.noticeparts.Award;
import hu.petabyte.redflags.engine.model.noticeparts.Lot;
import hu.petabyte.redflags.engine.model.noticeparts.ObjOfTheContract;
import hu.petabyte.redflags.engine.parser.TemplateLoader;
import hu.petabyte.redflags.engine.parser.TemplateParser;
import hu.petabyte.redflags.engine.util.MappingUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Zsolt Jurányi
 */
@Component
public class RawValueParser extends AbstractGear {
	// TODO configuration properties

	private static final Logger LOG = LoggerFactory
			.getLogger(RawValueParser.class);

	private @Value("${redflags.engine.gear.parse.lang:EN}") String rawLang;
	private DisplayLanguage lang;

	@Override
	public void beforeSession() throws Exception {
		lang = DisplayLanguage.valueOf(checkNotNull(rawLang));
		LOG.debug("Parsing language is {}", lang);
	}

	private void parseAddress(Address address, Notice notice) {
		if (null != address) {
			String name = "address-" + lang.toString();
			String addressTemplate = null;
			String di = (String) MappingUtils.getDeepPropertyIfExists(notice,
					"data.directive");
			if (null != di) {
				di = di.replaceAll(".*\\(", "").replaceAll("\\).*", "")
						.replaceAll("/", "");
				addressTemplate = TemplateLoader.getTemplate(name + "-" + di);
			}
			if (null == addressTemplate) {
				addressTemplate = TemplateLoader.getTemplate(name);
			}
			if (null != addressTemplate) {
				Map<String, String> map = new TemplateParser().parse(
						address.getRaw(), addressTemplate);
				MappingUtils.setDeepProperties(address, map);
			}
		}
	}

	private void parseAddress(Object root, String property, Notice notice) {
		Address address = (Address) MappingUtils.getDeepPropertyIfExists(root,
				property);
		parseAddress(address, notice);
	}

	private void parseAwards(Notice notice) {
		String template = TemplateLoader.getTemplate("awardheader-"
				+ lang.toString());

		for (Award a : notice.getAwards()) {
			Map<String, String> map = new TemplateParser().parse(
					a.getRawHeader(), template);
			MappingUtils.setDeepProperties(a, map);

			if (null != a.getRawAwarded()) {
				parseBoolean(a, "rawAwarded", "awarded");
			}
			parseDate(a, "rawDecisionDate", "decisionDate");
			parseInteger(a, "rawLotNumber", "lotNumber");
			parseInteger(a, "rawNumber", "number");
			parseInteger(a, "rawNumberOfOffers", "numberOfOffers");
			parseLong(a, "rawTotalEstimatedValue", "totalEstimatedValue");
			parseVat(a, "rawTotalEstimatedValueVat", "totalEstimatedValueVat");
			parseLong(a, "rawTotalFinalValue", "totalFinalValue");
			parseVat(a, "rawTotalFinalValueVat", "totalFinalValueVat");
			parseAddress(a, "winnerOrg.address", notice);

			if (null != a.getSubcontracting()) {
				Matcher m = Pattern.compile("([0-9,.]+) ?%").matcher(
						a.getSubcontracting());
				if (m.find()) {
					String d = m.group(1);
					try {
						a.setSubcontractingRate(Double.parseDouble(d
								.replaceAll(",", ".")));
					} catch (Exception ex) {
					}
				}
			}

			// values
			if (0 < a.getTotalEstimatedValue()) {
				ObjOfTheContract o = notice.getObjs().get(0);
				o.setEstimatedValue(o.getEstimatedValue()
						+ a.getTotalEstimatedValue());
				o.setEstimatedValueCurr(a.getTotalEstimatedValueCurr());
			}
			if (0 < a.getTotalFinalValue()) {
				ObjOfTheContract o = notice.getObjs().get(0);
				o.setTotalFinalValue(o.getTotalFinalValue()
						+ a.getTotalFinalValue());
				o.setTotalFinalValueCurr(a.getTotalFinalValueCurr());
				o.setTotalFinalValueVat(a.getTotalFinalValueVat());
			} // currency is dirty...

			// some debug stuff
			if (0 == a.getNumber() && 0 == a.getLotNumber()
					&& null == a.getLotTitle()) {
				LOG.debug("Possibly unhandled award header format @ {}: {}",
						notice.getId(), a.getRawHeader());
			}
		}
	}

	private void parseBoolean(Object root, String fromProperty,
			String toProperty) {
		String f = (String) MappingUtils.getDeepPropertyIfExists(root,
				fromProperty);
		MappingUtils.setDeepProperty(root, toProperty, parseBoolean(f));
	}

	private boolean parseBoolean(String s) { // TODO prop
		return s != null && "igen".equals(s.trim().toLowerCase());
	}

	private void parseCompl(Notice notice) {
		parseAddress(notice, "compl.obtainLodgingOfAppealsInfoFromOrg.address",
				notice);
		parseAddress(notice, "compl.respForAppealOrg.address", notice);
		parseBoolean(notice, "compl.rawRelToEUProjects",
				"compl.relToEUProjects");
	}

	private void parseContr(Notice notice) {
		parseAddress(notice, "contr.contractingOrg.address", notice);
		parseList(notice, "contr.contractingOrg.rawMainActivities",
				"contr.contractingOrg.mainActivities", "\n");
		parseBoolean(notice, "contr.rawPurchasingOnBehalfOfOther",
				"contr.purchasingOnBehalfOfOther");
		parseAddress(notice, "contr.obtainFurtherInfoFromOrg.address", notice);
		parseAddress(notice, "contr.obtainSpecsFromOrg.address", notice);
		parseAddress(notice, "contr.purchasingOnBehalfOfOrg.address", notice);
		parseAddress(notice, "contr.sendTendersToOrg.address", notice);
	}

	private void parseDate(Object root, String fromProperty, String toProperty) {
		String f = (String) MappingUtils.getDeepPropertyIfExists(root,
				fromProperty);
		MappingUtils.setDeepProperty(root, toProperty, parseDate(f));
	}

	public Date parseDate(String s) {
		try {
			SimpleDateFormat df = null;
			if (s.matches("\\d+\\.\\d+\\.\\d{4}")) {
				df = new SimpleDateFormat("d.M.yyyy");
			} else if (s.matches("\\d{2}/\\d{2}/\\d{4}")) {
				df = new SimpleDateFormat("dd/MM/yyyy");
			}
			return null == df ? null : df.parse(s.trim());
		} catch (Exception ex) {
			return null;
		}
	}

	public void parseDuration(Duration duration) {
		String durationTemplate = TemplateLoader.getTemplate("duration-"
				+ lang.toString());
		if (null != duration && null != durationTemplate) {
			Map<String, String> m = new TemplateParser().parse(
					duration.getRaw(), durationTemplate);
			duration.setInDays(parseInteger(m.get("d")));
			duration.setInMonths(parseInteger(m.get("m")));
			duration.setBegin(parseDate(m.get("b")));
			duration.setEnd(parseDate(m.get("e")));
			duration.fill();
		}
	}

	private void parseDuration(Object root, String property) {
		Duration duration = (Duration) MappingUtils.getDeepPropertyIfExists(
				root, property);
		parseDuration(duration);
	}

	private void parseInteger(Object root, String fromProperty,
			String toProperty) {
		String f = (String) MappingUtils.getDeepPropertyIfExists(root,
				fromProperty);
		MappingUtils.setDeepProperty(root, toProperty, parseInteger(f));
	}

	public int parseInteger(String s) {
		try {
			return Integer.parseInt(s.replaceAll("[.,].*$", "").replaceAll(
					"[^0-9]+", ""));
		} catch (Exception ex) {
			return 0;
		}
	}

	private void parseList(Object root, String fromProperty, String toProperty,
			String delim) {
		String f = (String) MappingUtils.getDeepPropertyIfExists(root,
				fromProperty);
		MappingUtils.setDeepProperty(root, toProperty, parseList(f, delim));
	}

	private List<String> parseList(String s, String d) {
		List<String> list = new ArrayList<String>();
		if (null != s) {
			for (String e : s.split(d)) {
				list.add(e.trim());
			}
		}
		return list;
	}

	public void parseLong(Object root, String fromProperty, String toProperty) {
		String f = (String) MappingUtils.getDeepPropertyIfExists(root,
				fromProperty);
		MappingUtils.setDeepProperty(root, toProperty, parseLong(f));
	}

	public long parseLong(String s) {
		try {
			return Long.parseLong(s.replaceAll("[.,].*$", "").replaceAll(
					"[^0-9]+", ""));
		} catch (Exception ex) {
			return 0;
		}
	}

	private void parseLots(Notice notice) {
		for (Lot lot : notice.getLots()) {
			parseInteger(lot, "rawNumber", "number");
			parseDuration(lot.getDifferentDuration());

			String rawCpvCodes = lot.getRawCpvCodes();
			if (null != rawCpvCodes) {
				for (String cpv : rawCpvCodes.split(",")) {
					cpv = cpv.trim();
					if (cpv.matches("\\d+")) {
						lot.getCpvCodes().add(
								CPV.findOrCreate(Integer.parseInt(cpv), null));
					}
				}
			}
		}
	}

	private void parseObjs(Notice notice) {
		for (ObjOfTheContract obj : notice.getObjs()) {
			parseDuration(obj, "duration");
			parseDate(obj, "rawPlannedStartDate", "plannedStartDate");
			parseBoolean(obj, "rawRenewable", "renewable");
			parseInteger(obj, "rawRenewalCount", "renewalCount");
			parseDuration(obj, "renewalDuration");
			// if (notice.getObjs().get(0).getTotalFinalValue() == 0) {
			// parseLong(obj, "rawTotalFinalValue", "totalFinalValue");
			// parseVat(obj, "rawTotalFinalValueVat", "totalFinalValueVat");
			// }

			long fv = parseLong(obj.getRawTotalFinalValue());
			if (0 < fv /* && fv != obj.getTotalFinalValue() */) {
				obj.setTotalFinalValue(fv);
				parseVat(obj, "rawTotalFinalValueVat", "totalFinalValueVat");
			}
		}
	}

	private void parseProc(Notice notice) {
		parseDate(notice, "proc.rawInterestDeadline", "proc.interestDeadline");
		parseDate(notice, "proc.rawInvitationsDispatchDate",
				"proc.invitationsDispatchDate");
		parseDuration(notice, "proc.minMaintainDuration");
		parseDuration(notice, "proc.qualificationSystemDuration");
	}

	private void parseVat(Object root, String fromProperty, String toProperty) {
		String f = (String) MappingUtils.getDeepPropertyIfExists(root,
				fromProperty);
		MappingUtils.setDeepProperty(root, toProperty, parseVat(f));
	}

	private double parseVat(String s) {
		if (null != s) { // TODO prop
			Matcher m = Pattern.compile(".*Áfakulcs \\(%\\) (?<v>[0-9,.]+).*")
					.matcher(s);
			if (m.find()) {
				try {
					return Double
							.parseDouble(m.group("v").replaceAll(",", "."));
				} catch (Exception ex) {
				}
			}
		}
		return 0.0;
	}

	@Override
	protected Notice processImpl(Notice notice) throws Exception {
		parseContr(notice);
		parseAwards(notice); // should b called before objs
		parseObjs(notice);
		parseLots(notice);
		parseProc(notice);
		parseCompl(notice);
		return notice;
	}

}
