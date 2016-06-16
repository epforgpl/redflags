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
package hu.petabyte.redflags.engine.gear.export;

import hu.petabyte.redflags.engine.model.Address;
import hu.petabyte.redflags.engine.model.CPV;
import hu.petabyte.redflags.engine.model.Data;
import hu.petabyte.redflags.engine.model.Duration;
import hu.petabyte.redflags.engine.model.IndicatorResult;
import hu.petabyte.redflags.engine.model.IndicatorResult.IndicatorResultType;
import hu.petabyte.redflags.engine.model.Notice;
import hu.petabyte.redflags.engine.model.NoticeID;
import hu.petabyte.redflags.engine.model.Organization;
import hu.petabyte.redflags.engine.model.Type;
import hu.petabyte.redflags.engine.model.noticeparts.Award;
import hu.petabyte.redflags.engine.model.noticeparts.ComplementaryInfo;
import hu.petabyte.redflags.engine.model.noticeparts.ContractingAuthority;
import hu.petabyte.redflags.engine.model.noticeparts.LEFTInfo;
import hu.petabyte.redflags.engine.model.noticeparts.Lot;
import hu.petabyte.redflags.engine.model.noticeparts.ObjOfTheContract;
import hu.petabyte.redflags.engine.model.noticeparts.Procedure;
import hu.petabyte.redflags.engine.util.CSV2MySQL;
import hu.petabyte.redflags.engine.util.CSVFile;
import hu.petabyte.redflags.engine.util.CompanyNameUtils;
import hu.petabyte.redflags.engine.util.MD5;
import hu.petabyte.redflags.engine.util.MappingUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Zsolt Jurányi
 */
@Component
public class MySQLExporter extends AbstractExporter {

	private static final Logger LOG = LoggerFactory
			.getLogger(MySQLExporter.class);
	private static final String CSV_COLUMN_DELIMITER = "|||||";
	private static final String CSV_ROW_DELIMITER = "$$$$$";

	// EXPORT:
	// * "mysql" - save parsed data into a MySQL database. you must also set the
	// following options:
	// * --dbhost=ip:port
	// * --dbname=name_of_database
	// * --dbuser=username
	// * --dbpass=password

	private @Value("${db:0}") Integer enabled;
	private boolean skipping = false;
	private @Value("${dbhost}") String host;
	private @Value("${dbname}") String name;
	private @Value("${dbuser}") String user;
	private @Value("${dbpass}") String pass;
	private @Value("${rev:1}") int rev = 1;
	private Map<String, CSVFile> tableCSVs = new HashMap<String, CSVFile>();
	private CSV2MySQL csvLoader;
	private int counter;

	@Override
	public void afterSession() throws Exception {
		if (!skipping) {
			CSVFile csv;

			// CPV - id, code, name, rev
			csv = getCSV("te_cpv");
			for (CPV cpv : CPV.getPool()) {
				csv.writeCell(cpv.getId());
				csv.writeCell(cpv.getId());
				csv.writeCell(cpv.getName());
				csv.writeCell(rev);
				csv.endLine();
			}

			// DATATYPE (TYPE) - id, typeId, name, rev
			csv = getCSV("te_datatype");
			for (Type type : Type.getPool()) {
				csv.writeCell(type.getId());
				csv.writeCell(type.getId());
				csv.writeCell(type.getName());
				csv.writeCell(rev);
				csv.endLine();
			}

			for (Entry<String, CSVFile> e : tableCSVs.entrySet()) {
				String table = e.getKey();
				csv = e.getValue();
				csv.close();
				LOG.debug("Pushing CSV into MySQL: " + csv.getFilename());
				csvLoader.loadCSV(new File(csv.getFilename()), table,
						CSV_COLUMN_DELIMITER, CSV_ROW_DELIMITER);
				csv.deleteFile();
			}

			csvLoader.closeConnection();
			LOG.info("Exported {} notices into MySQL", counter);
		}
	}

	@Override
	public void beforeSession() throws Exception {
		counter = 0;
		skipping = 0 == enabled;
		if (!skipping) {
			tableCSVs.clear();
			csvLoader = new CSV2MySQL(host, name, user, pass);
		} else {
			LOG.debug("MySQL Exporter is off, start app with --db=1 option to turn it on");
		}
	}

	@Override
	protected synchronized void export(Notice notice) {
		counter++;
		if (!skipping) {
			try {
				exportNotice(notice);
				exportData(notice);
				exportContractingAuthority(notice);
				exportObjs(notice);
				exportLots(notice);
				exportLEFT(notice);
				exportProcedure(notice);
				exportAwards(notice);
				exportComplementaryInfo(notice);
				exportFlags(notice);
			} catch (IOException e) {
				LOG.warn("Exception of notice " + notice.getId() + " failed", e);
			}
		}
	}

	private void exportAwards(Notice notice) throws IOException {
		// AWARD - id, ..., noticeID, rev
		int id = 1;
		for (Award a : notice.getAwards()) {
			CSVFile csv = getCSV("te_award");
			a.setId(notice.getId().toString() + "-A-" + (++id));
			csv.writeCell(a.getId());
			csv.writeCell(a.getDecisionDate());
			csv.writeCell(a.getLotNumber());
			csv.writeCell(a.getLotTitle());
			csv.writeCell(a.getNumber());
			csv.writeCell(a.getNumberOfOffers());
			csv.writeCell(a.getRawDecisionDate());
			csv.writeCell(a.getRawHeader());
			csv.writeCell(a.getRawLotNumber());
			csv.writeCell(a.getRawNumber());
			csv.writeCell(a.getRawNumberOfOffers());
			csv.writeCell(a.getRawTotalEstimatedValue());
			csv.writeCell(a.getRawTotalEstimatedValueVat());
			csv.writeCell(a.getRawTotalFinalValue());
			csv.writeCell(a.getRawTotalFinalValueVat());
			csv.writeCell(a.getSubcontracting());
			csv.writeCell(a.getSubcontractingRate());
			csv.writeCell(a.getTotalEstimatedValue());
			csv.writeCell(a.getTotalEstimatedValueCurr());
			csv.writeCell(a.getTotalEstimatedValueVat());
			csv.writeCell(a.getTotalFinalValue());
			csv.writeCell(a.getTotalFinalValueCurr());
			csv.writeCell(a.getTotalFinalValueVat());
			csv.writeCell(notice.getId().toString());
			csv.writeCell(rev);
			// 2014/24/EU:
			csv.writeCell(a.isAwarded());
			csv.writeCell(a.getRawAwarded());
			csv.writeCell(a.getNonAward());

			csv.endLine();

			if (null != a.getWinnerOrg()) {
				exportOrganization(a.getWinnerOrg());
				exportRelation(//
						"AWARD-" + a.getId() + "-2-ORG-"
						+ a.getWinnerOrg().getId(), //
						"AWARD_TO_ORGANIZATION", "winnerOrg", //
						a.getId(), a.getWinnerOrg().getId());
			}
		}
	}

	private void exportComplementaryInfo(Notice notice) throws IOException {
		// COMPLEMENTARY INFO - id, add, gen, lod, raw, rec, refs, rel, not, rev
		ComplementaryInfo compl = notice.getCompl();
		if (null != compl) {
			CSVFile csv = getCSV("te_complementaryinfo");
			compl.setId(notice.getId().toString());
			csv.writeCell(compl.getId());
			csv.writeCell(compl.getAdditionalInfo());
			csv.writeCell(compl.getGenRegFWInfo());
			csv.writeCell(compl.getLodgingOfAppeals());
			csv.writeCell(compl.getRawRelToEUProjects());
			csv.writeCell(compl.getRecurrence());
			csv.writeCell(compl.getRefsToEUProjects());
			csv.writeCell(compl.isRelToEUProjects());
			csv.writeCell(notice.getId().toString());
			csv.writeCell(rev);
			csv.endLine();

			// if (null != compl.getObtainLodgingOfAppealsInfoFromOrg()) {
			// exportOrganization(compl.getObtainLodgingOfAppealsInfoFromOrg());
			// exportRelation(
			// "COMPL-" + compl.getId() + "-2-LODG-ORG-"
			// + compl.getObtainLodgingOfAppealsInfoFromOrg().getId(),
			// "COMPLEMENTARYINFO_TO_ORGANIZATION",
			// "obtainLodgingOfAppealsInfoFromOrg", compl.getId(),
			// compl.getObtainLodgingOfAppealsInfoFromOrg().getId());
			// }
			//
			// if (null != compl.getRespForAppealOrg()) {
			// exportOrganization(compl.getRespForAppealOrg());
			// exportRelation("COMPL-" + compl.getId() + "-2-RESP-ORG-" +
			// compl.getRespForAppealOrg().getId(),
			// "COMPLEMENTARYINFO_TO_ORGANIZATION", "respForAppealOrg",
			// compl.getId(),
			// compl.getRespForAppealOrg().getId());
			// }
		}
	}

	private void exportContr2Org(ContractingAuthority contr, String field)
			throws IOException {
		Organization org = (Organization) MappingUtils.getDeepPropertyIfExists(
				contr, field);
		if (null != org) {
			exportOrganization(org);

			CSVFile csv = getCSV("te_relationdescriptor");
			csv.writeCell("CONTR-" + contr.getId() + "-2-" + org.getId()
					+ "-AS-" + field);
			csv.writeCell("CONTRACTINGAUTHORITY_TO_ORGANIZATION");
			csv.writeCell(contr.getId());
			csv.writeCell(org.getId());
			csv.writeCell(field);
			csv.writeCell(rev);
			csv.endLine();
		}
	}

	private void exportContractingAuthority(Notice notice) throws IOException {
		// CONTRACTING AUTHORITY - id, purchasingOnBehalfOfOther,
		// rawPurchasingOnBehalfOfOther, rev, noticeID
		ContractingAuthority contr = notice.getContr();
		if (null != contr) {
			CSVFile csv = getCSV("te_contractingauthority");
			contr.setId(notice.getId().toString());
			csv.writeCell(contr.getId());
			csv.writeCell(contr.isPurchasingOnBehalfOfOther());
			csv.writeCell(contr.getRawPurchasingOnBehalfOfOther());
			csv.writeCell(rev);
			csv.writeCell(notice.getId().toString());
			csv.endLine();

			exportContr2Org(contr, "contractingOrg");
			// exportContr2Org(contr, "obtainFurtherInfoFromOrg");
			// exportContr2Org(contr, "obtainSpecsFromOrg");
			// exportContr2Org(contr, "purchasingOnBehalfOfOrg");
			// exportContr2Org(contr, "sendTendersToOrg");
		}
	}

	private void exportData(Notice notice) throws IOException {
		// DATA - id, country, deadline, deadlineForDocs, directive,
		// documentSent, internetAddress, oj, originalLanguage, place,
		// publicationDate, title, noticeId, rev
		CSVFile csv = getCSV("te_data");
		Data data = notice.getData();
		data.setId(notice.getId().toString());
		csv.writeCell(data.getId());
		csv.writeCell(data.getCountry());
		csv.writeCell(data.getDeadline());
		csv.writeCell(data.getDeadlineForDocs());
		csv.writeCell(data.getDirective());
		csv.writeCell(data.getDocumentSent());
		csv.writeCell(data.getInternetAddress());
		csv.writeCell(data.getOj());
		csv.writeCell(data.getOriginalLanguage());
		csv.writeCell(data.getPlace());
		csv.writeCell(data.getPublicationDate());
		csv.writeCell(data.getTitle());
		csv.writeCell(notice.getId().toString());
		csv.writeCell(rev);
		csv.endLine();

		exportData2Type(data, "authorityType");
		exportData2Type(data, "awardCriteria");
		exportData2Type(data, "bidType");
		exportData2Type(data, "contractType");
		exportData2Type(data, "documentType");
		exportData2Type(data, "procedureType");
		exportData2Type(data, "regulationType");

		for (CPV cpv : data.getCpvCodes()) {
			exportRelation(//
					"DATA-" + data.getId() + "-2-CPV-" + cpv.getId() + "-cpv", //
					"DATA_TO_CPV", "cpvCodes", //
					data.getId(), Integer.toString(cpv.getId()));
		}

		for (CPV cpv : data.getOriginalCpvCodes()) {
			exportRelation(//
					"DATA-" + data.getId() + "-2-CPV-" + cpv.getId()
					+ "-originalCpv", //
					"DATA_TO_CPV", "originalCpvCodes", //
					data.getId(), Integer.toString(cpv.getId()));
		}
	}

	private void exportData2Type(Data data, String field) throws IOException {
		Type type = (Type) MappingUtils.getDeepPropertyIfExists(data, field);
		if (null != type) {
			exportRelation(//
					"DATA-" + data.getId() + "-2-TYPE-" + type.getId(), //
					"DATA_TO_DATATYPE", field, //
					data.getId(), type.getId());
		}
	}

	private void exportDuration(Duration duration) throws IOException {
		// DURATION - id, begin, end, days, months, raw, rev
		CSVFile csv = getCSV("te_duration");
		csv.writeCell(duration.getId());
		csv.writeCell(duration.getBegin());
		csv.writeCell(duration.getEnd());
		csv.writeCell(duration.getInDays());
		csv.writeCell(duration.getInMonths());
		if (null != duration.getRaw() && duration.getRaw().length() > 200) {
			csv.writeCell(duration.getRaw().substring(0, 150) + " [TRIMMED]");
			// TODO investigate trimmed ones - parser bug? or correct?
		} else {
			csv.writeCell(duration.getRaw());
		}
		csv.writeCell(rev);
		csv.endLine();
	}

	private void exportFlags(Notice notice) throws IOException {
		// FLAG - id, effectlevel, flagtype, information, score, rev, noticeID
		for (Entry<String, IndicatorResult> e : notice.getIndicatorResults()
				.entrySet()) {
			String indicator = e.getKey();
			IndicatorResult flag = e.getValue();
			if (flag.getType() == IndicatorResultType.FLAG) {
				CSVFile csv = getCSV("te_flag");
				csv.writeCell(notice.getId().toString() + "-F-" + indicator);
				csv.writeCell(""); // TODO DB remove effect level
				csv.writeCell(flag.getFlagCategory()); // TODO DB rename 2 cat
				csv.writeCell(flag.getDescription());
				csv.writeCell(flag.getWeight()); // TODO DB rename 2 weight
				csv.writeCell(rev);
				csv.writeCell(notice.getId().toString());
				csv.endLine();
			}
		}
	}

	private void exportLEFT(Notice notice) throws IOException {
		// LEFTINFO - id, ...., rev, noticeId
		LEFTInfo left = notice.getLeft();
		if (null != left) {
			CSVFile csv = getCSV("te_leftinfo");
			left.setId(notice.getId().toString());
			csv.writeCell(left.getId());
			csv.writeCell(left.getDepositsAndGuarantees());
			csv.writeCell(left.getExecutionStaff());
			csv.writeCell(left.getFinancialAbility());
			csv.writeCell(left.getFinancingConditions());
			csv.writeCell(left.getLegalFormToBeTaken());
			csv.writeCell(left.getOtherParticularConditions());
			csv.writeCell(left.getParticularProfession());
			csv.writeCell(left.getPersonalSituation());
			csv.writeCell(left.getQualificationForTheSystem());
			csv.writeCell(left.getReservedContracts());
			csv.writeCell(left.getTechnicalCapacity());
			csv.writeCell(rev);
			csv.writeCell(notice.getId().toString());
			csv.endLine();
		}
	}

	private void exportLots(Notice notice) throws IOException {
		int id = 0;
		for (Lot lot : notice.getLots()) {
			CSVFile csv = getCSV("te_lot");
			lot.setId(notice.getId().toString() + "-L-" + lot.getNumber());
			if (0 == lot.getNumber()) {
				lot.setId(lot.getId() + "-" + (++id));
			}
			csv.writeCell(lot.getId());
			csv.writeCell(lot.getAdditionalInfo());
			csv.writeCell(lot.getNumber());
			csv.writeCell(lot.getQuantity());
			csv.writeCell(lot.getRawCpvCodes());
			csv.writeCell(lot.getRawNumber());
			csv.writeCell(lot.getShortDescription());
			csv.writeCell(lot.getTitle());
			csv.writeCell(notice.getId().toString());
			csv.writeCell(rev);
			csv.endLine();

			csv = getCSV("te_relationdescriptor");
			for (CPV cpv : lot.getCpvCodes()) {
				exportRelation("LOT-" + lot.getId() + "-2-CPV-" + cpv.getId(),
						"LOT_TO_CPV", "cpvCodes", lot.getId(),
						Integer.toString(cpv.getId()));
			}

			if (null != lot.getDifferentDuration()) {
				lot.getDifferentDuration().setId(lot.getId());
				exportDuration(lot.getDifferentDuration());
				exportRelation("LOT-" + lot.getId() + "-2-DUR-"
						+ lot.getDifferentDuration().getId(),
						"LOT_TO_DURATION", "differentDuration", lot.getId(),
						lot.getDifferentDuration().getId());
			}
		}
	}

	private void exportNotice(Notice notice) throws IOException {
		// NOTICE - id, number, tedURL, year, docFamilyId, rev
		CSVFile csv = getCSV("te_notice");
		csv.writeCell(notice.getId().toString());
		csv.writeCell(notice.getId().number());
		csv.writeCell(notice.getUrl());
		csv.writeCell(notice.getId().year());
		NoticeID dfid = notice.getDocumentFamilyId();
		csv.writeCell(null == dfid ? null : dfid.toString());
		csv.writeCell(rev);
		csv.endLine();
	}

	private void exportObjs(Notice notice) throws IOException {
		// OBJ - id, add, contrTit, contrTyp, estVal, estValCur, finCond, frame,
		// gpa, lots, options, pcfadps, place, planned, rawestval, rawplan,
		// rawren, rawrencount, tawtot, rawtotvat, ren, rencount, short, tot,
		// totcur, totvat, totquant, var, noticeid, rev, framePart
		int id = 0;
		for (ObjOfTheContract obj : notice.getObjs()) {
			CSVFile csv = getCSV("te_objofthecontract");
			obj.setId(notice.getId().toString() + "-O-" + (++id));
			csv.writeCell(obj.getId());
			csv.writeCell(obj.getAdditionalInfo());
			csv.writeCell(obj.getContractTitle());
			csv.writeCell(obj.getContractTypeInfo());
			csv.writeCell(obj.getEstimatedValue());
			csv.writeCell(obj.getEstimatedValueCurr());
			csv.writeCell(obj.getFinancingConditions());
			csv.writeCell(obj.getFrameworkAgreement());
			csv.writeCell(obj.getGpa());
			csv.writeCell(obj.getLots());
			csv.writeCell(obj.getOptions());
			csv.writeCell(obj.getPcFaDps());
			csv.writeCell(obj.getPlaceOfPerformance());
			csv.writeCell(obj.getPlannedStartDate());
			csv.writeCell(obj.getRawEstimatedValue());
			csv.writeCell(obj.getRawPlannedStartDate());
			csv.writeCell(obj.getRawRenewable());
			csv.writeCell(obj.getRawRenewalCount());
			csv.writeCell(obj.getRawTotalFinalValue());
			csv.writeCell(obj.getRawTotalFinalValueVat());
			csv.writeCell(obj.isRenewable());
			csv.writeCell(obj.getRenewalCount());
			csv.writeCell(obj.getShortDescription());
			csv.writeCell(obj.getTotalFinalValue());
			csv.writeCell(obj.getTotalFinalValueCurr());
			csv.writeCell(obj.getTotalFinalValueVat());
			csv.writeCell(obj.getTotalQuantity());
			csv.writeCell(obj.getVariants());
			csv.writeCell(notice.getId().toString());
			csv.writeCell(rev);
			csv.writeCell(obj.getFrameworkParticipants());
			// 2014/24/EU:
			csv.writeCell(obj.getLotTitle());
			csv.writeCell(obj.getRawLotCpvCodes());
			csv.writeCell(obj.getAwardCriteria());
			csv.writeCell(obj.getRawLotEstimatedValue());
			csv.writeCell(obj.getLotEstimatedValue());
			csv.writeCell(obj.getLotEstimatedValueCurr());
			csv.endLine();

			if (null != obj.getDuration()) {
				obj.getDuration().setId(obj.getId() + "-CONTRACT-DUR");
				exportDuration(obj.getDuration());
				exportRelation("OBJ-" + obj.getId() + "-2-DUR-"
						+ obj.getDuration().getId(),
						"OBJOFTHECONTRACT_TO_DURATION", "duration",
						obj.getId(), obj.getDuration().getId());
			}

			if (null != obj.getFrameworkDuration()) {
				obj.getFrameworkDuration().setId(obj.getId() + "-FRAMEW-DUR");
				exportDuration(obj.getFrameworkDuration());
				exportRelation("OBJ-" + obj.getId() + "-2-DUR-"
						+ obj.getFrameworkDuration().getId(),
						"OBJOFTHECONTRACT_TO_DURATION", "frameworkDuration",
						obj.getId(), obj.getFrameworkDuration().getId());
			}

			if (null != obj.getRenewalDuration()) {
				obj.getRenewalDuration().setId(obj.getId() + "-RENEWAL-DUR");
				exportDuration(obj.getRenewalDuration());
				exportRelation("OBJ-" + obj.getId() + "-2-DUR-"
						+ obj.getRenewalDuration().getId(),
						"OBJOFTHECONTRACT_TO_DURATION", "renewalDuration",
						obj.getId(), obj.getRenewalDuration().getId());
			}
		}

	}

	private void exportOrganization(Organization org) throws IOException {
		if (null != org) {
			String name = CompanyNameUtils.normalizeCompanyNameForUser(org
					.getName());
			org.setName(name);
			String nameForId = CompanyNameUtils.normalizeCompanyNameForHash(org
					.getName());
			org.setId("ORG-" + MD5.fromString(nameForId));

			// ORGANIZATION - id, code, mainActs, name, rawMainActs, _type, rev
			CSVFile csv = getCSV("te_organization");
			csv.writeCell(org.getId());
			csv.writeCell(org.getCode());
			StringBuilder sb = new StringBuilder();
			for (String act : org.getMainActivities()) {
				sb.append(act);
				sb.append(", ");
			}
			csv.writeCell(sb.toString().replaceAll(", $", ""));
			csv.writeCell(org.getName());
			csv.writeCell(org.getRawMainActivities());
			csv.writeCell(org.getType());
			csv.writeCell(rev);
			csv.endLine();

			// ADDRESS - id, buyerProf, city, contactPerson, contactPoint,
			// country,
			// email, fax, infoUrl, phone, raw, street,url, zip, org id, rev
			Address a = org.getAddress();
			if (null != a) {
				StringBuilder aid = new StringBuilder();
				aid.append(a.getBuyerProfileUrl());
				aid.append(a.getCity());
				aid.append(a.getContactPerson());
				aid.append(a.getContactPoint());
				aid.append(a.getEmail());
				aid.append(a.getFax());
				aid.append(a.getPhone());
				aid.append(a.getStreet());
				aid.append(a.getUrl());
				aid.append(a.getZip());
				a.setId("ADDR-" + MD5.fromString(aid.toString()));

				csv = getCSV("te_address");
				csv.writeCell(a.getId());
				csv.writeCell(a.getBuyerProfileUrl());
				csv.writeCell(a.getCity());
				if (null != a.getContactPerson()
						&& a.getContactPerson().length() > 200) {
					csv.writeCell(a.getContactPerson().substring(0, 150)
							+ " [TRIMMED]");
					// TODO investigate trimmed ones - parser bug? or correct?
				} else {
					csv.writeCell(a.getContactPerson());
				}
				csv.writeCell(a.getContactPoint());
				if (null != a.getCountry()) {
					csv.writeCell(a.getCountry().split("\n")[0]);
					// TODO 16641-2014, purchasingOnBehalfOfOtherOrg bug fix
				} else {
					csv.writeCell(a.getCountry());
				}
				csv.writeCell(a.getEmail());
				csv.writeCell(a.getFax());
				csv.writeCell(a.getInfoUrl());
				csv.writeCell(a.getPhone());
				csv.writeCell(a.getRaw());
				if (null != a.getStreet() && a.getStreet().length() > 200) {
					csv.writeCell(a.getStreet().substring(0, 150)
							+ " [TRIMMED]");
					// TODO investigate trimmed ones - parser bug? or correct?
				} else {
					csv.writeCell(a.getStreet());
				}
				csv.writeCell(a.getUrl());
				csv.writeCell(a.getZip());
				csv.writeCell(org.getId());
				csv.writeCell(rev);
				csv.endLine();
			}
		}
	}

	private void exportProcedure(Notice notice) throws IOException {
		// PROC - id, ..., rev, noticeID
		Procedure proc = notice.getProc();
		if (null != proc) {
			CSVFile csv = getCSV("te_procedure");
			notice.getProc().setId(notice.getId().toString());
			csv.writeCell(proc.getId());
			csv.writeCell(proc.getAwardCriteria());
			csv.writeCell(proc.getElectronicAuction());
			if (null != proc.getFileRefNumber()
					&& proc.getFileRefNumber().length() > 200) {
				csv.writeCell(proc.getFileRefNumber().substring(0, 150)
						+ " [TRIMMED]");
				// TODO investigate trimmed ones - parser bug? or correct?
			} else {
				csv.writeCell(proc.getFileRefNumber());
			}
			csv.writeCell(proc.getInterestDeadline());
			csv.writeCell(proc.getInvitationsDispatchDate());
			csv.writeCell(proc.getLimitOfInvitedOperators());
			csv.writeCell(proc.getObtainingSpecs());
			csv.writeCell(proc.getOpeningConditions());
			csv.writeCell(proc.getOpeningDate());
			csv.writeCell(proc.getPreviousPublication());
			csv.writeCell(proc.getProcedureTypeInfo());
			csv.writeCell(proc.getRawInterestDeadline());
			csv.writeCell(proc.getRawInvitationsDispatchDate());
			csv.writeCell(proc.getReductionOfOperators());
			csv.writeCell(proc.getRenewalInfo());
			csv.writeCell(proc.getTenderLanguage());
			csv.writeCell(rev);
			csv.writeCell(notice.getId().toString());
			// 2014/24/EU
			csv.writeCell(proc.getFaDps());
			csv.writeCell(proc.getGpa());
			csv.endLine();

			if (null != proc.getMinMaintainDuration()) {
				proc.getMinMaintainDuration().setId(
						proc.getId() + "-MINMAIN-DUR");
				exportDuration(proc.getMinMaintainDuration());
				exportRelation("PROC-" + proc.getId() + "-2-DUR-"
						+ proc.getMinMaintainDuration().getId(),
						"PROCEDURE_TO_DURATION", "minMaintainDuration",
						proc.getId(), proc.getMinMaintainDuration().getId());
			}

			if (null != proc.getQualificationSystemDuration()) {
				proc.getQualificationSystemDuration().setId(
						proc.getId() + "-QSYS-DUR");
				exportDuration(proc.getQualificationSystemDuration());
				exportRelation("PROC-" + proc.getId() + "-2-DUR-"
						+ proc.getQualificationSystemDuration().getId(),
						"PROCEDURE_TO_DURATION", "qualificationSystemDuration",
						proc.getId(), proc.getQualificationSystemDuration()
						.getId());
			}
		}

	}

	private void exportRelation(String id, String type, String field,
			String id1, String id2) throws IOException {
		CSVFile csv = getCSV("te_relationdescriptor");
		csv.writeCell(id);
		csv.writeCell(type);
		csv.writeCell(id1);
		csv.writeCell(id2);
		csv.writeCell(field);
		csv.writeCell(rev);
		csv.endLine();

	}

	private CSVFile getCSV(String table) throws IOException {
		CSVFile csv = tableCSVs.get(table);
		if (null == csv) {
			tableCSVs.put(table, new CSVFile(table + "_"
					+ new SimpleDateFormat("yyyyMMdd").format(new Date()),
					CSV_COLUMN_DELIMITER, CSV_ROW_DELIMITER));
			csv = tableCSVs.get(table);
		}
		return csv;
	}

}
