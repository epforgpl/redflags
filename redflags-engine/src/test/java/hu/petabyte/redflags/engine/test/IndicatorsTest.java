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
package hu.petabyte.redflags.engine.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import hu.petabyte.redflags.engine.RedflagsEngineApp;
import hu.petabyte.redflags.engine.gear.indicator.AbstractIndicator;
import hu.petabyte.redflags.engine.gear.indicator.helper.KMonitorInstitutions;
import hu.petabyte.redflags.engine.gear.indicator.hu.AwCritMethodMissingIndicator;
import hu.petabyte.redflags.engine.gear.indicator.hu.AwCritPaymentDeadlineCondIndicator;
import hu.petabyte.redflags.engine.gear.indicator.hu.ContrDescCartellingIndicator;
import hu.petabyte.redflags.engine.gear.indicator.hu.ContractingOrgInKMDBIndicator;
import hu.petabyte.redflags.engine.gear.indicator.hu.DeadlineIsTightIndicator;
import hu.petabyte.redflags.engine.gear.indicator.hu.DecisionDateDiffersFromOpeningDateIndicator;
import hu.petabyte.redflags.engine.gear.indicator.hu.FinAbRevenueCondExceedEstimValIndicator;
import hu.petabyte.redflags.engine.gear.indicator.hu.FinalValFarFromEstimValIndicator;
import hu.petabyte.redflags.engine.gear.indicator.hu.HighFinalValueIndicator;
import hu.petabyte.redflags.engine.gear.indicator.hu.OpeningDateDiffersFromDeadlineIndicator;
import hu.petabyte.redflags.engine.gear.indicator.hu.TechCapEURefCondIndicator;
import hu.petabyte.redflags.engine.gear.indicator.hu.TechCapGeoCondIndicator;
import hu.petabyte.redflags.engine.gear.indicator.hu.TechCapRefCondExceedEstimValIndicator;
import hu.petabyte.redflags.engine.gear.indicator.hu.TechCapSingleContractRefCondIndicator;
import hu.petabyte.redflags.engine.gear.indicator.hu.TotalQuantityHiDeltaIndicator;
import hu.petabyte.redflags.engine.gear.indicator.hu.UnsuccessfulProcWithoutInfo1Indicator;
import hu.petabyte.redflags.engine.gear.indicator.hu.WinnerOrgInKMDBIndicator;
import hu.petabyte.redflags.engine.gear.indicator.hu.depr.AwCritNoPriceCondIndicator;
import hu.petabyte.redflags.engine.gear.indicator.hu.depr.FinAbRevenueCondExceedEstimValIndicator2;
import hu.petabyte.redflags.engine.gear.indicator.hu.depr.RenewableContractIndicator;
import hu.petabyte.redflags.engine.model.IndicatorResult;
import hu.petabyte.redflags.engine.model.Notice;
import hu.petabyte.redflags.engine.model.IndicatorResult.IndicatorResultType;
import hu.petabyte.redflags.engine.tedintf.TedInterface;
import hu.petabyte.redflags.engine.tedintf.TedInterfaceHolder;
import hu.petabyte.redflags.engine.tedintf.cached.CachedTedInterface;
import hu.petabyte.redflags.engine.test.helper.ProcessedNoticeProvider;
import hu.petabyte.redflags.engine.util.StrUtils;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.ConfigFileApplicationContextInitializer;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Zsolt Jurányi
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RedflagsEngineApp.class)
@ContextConfiguration(classes = RedflagsEngineApp.class, initializers = ConfigFileApplicationContextInitializer.class)
@ActiveProfiles("test")
public class IndicatorsTest {

	private static final TedInterface tedintf = new CachedTedInterface(
			"indicator-test-cache");
	private @Autowired TedInterfaceHolder ted;
	private @Autowired ProcessedNoticeProvider processedNoticeProvider;

	private @Autowired KMonitorInstitutions kmdb;

	@Test
	public void __revenue() throws InterruptedException {
		Class<? extends AbstractIndicator> c1 = FinAbRevenueCondExceedEstimValIndicator.class;
		Class<? extends AbstractIndicator> c2 = FinAbRevenueCondExceedEstimValIndicator2.class;

		// assertTrue(flags("88111-2014", c1)); // estim: 79 M, rev1: 90 M
		// assertFalse(flags("88111-2014", c2));

		assertTrue(flags("8814-2014", c1)); // estim: 2.2 Mrd, rev1: 2.7 Mrd
		assertFalse(flags("8814-2014", c2));

		assertFalse(flags("112256-2015", c1)); // estim: 154 M, rev1: 150 M
		assertTrue(flags("112256-2015", c2)); // rev2: 300 M

		assertTrue(flags("46171-2014", c1)); // estim: 55 M, rev1: 100 M
		assertFalse(flags("46171-2014", c2));

		assertFalse(flags("55252-2014", c1)); // estim: 169 M, rev1: 55 M
		assertFalse(flags("55252-2014", c2));

		assertTrue(flags("661-2015", c1)); // estim: 63 M, rev1: 140 M
		assertFalse(flags("661-2015", c2));
	}

	@Before
	public void _init() {
		ted.set(tedintf);
	}

	@Test
	public void awCritMethodMissingIndicatorTest() {
		Class<? extends AbstractIndicator> clazz = AwCritMethodMissingIndicator.class;
		assertFalse(flags("185824-2012", clazz));
		assertFalse(flags("187532-2012", clazz));
		assertFalse(flags("193640-2012", clazz));
		assertFalse(flags("197621-2012", clazz));
		assertFalse(flags("219551-2012", clazz));
		assertFalse(flags("242429-2012", clazz));
		assertTrue(flags("313862-2012", clazz));
		assertTrue(flags("319306-2012", clazz));
		assertTrue(flags("350331-2012", clazz));
		assertFalse(flags("120289-2013", clazz));
		assertFalse(flags("253530-2013", clazz));
		assertFalse(flags("268802-2013", clazz));
		assertFalse(flags("14643-2014", clazz));
		assertFalse(flags("70034-2014", clazz));
		assertFalse(flags("32536-2015", clazz));
	}

	@Ignore
	@Test
	public void awCritNoPriceCondIndicatorTest() {
		Class<? extends AbstractIndicator> clazz = AwCritNoPriceCondIndicator.class;

		// korabbi, korrekt talalatok:
		assertFalse(flags("210605-2012", clazz));
		assertTrue(flags("210792-2012", clazz));
		assertTrue(flags("235828-2012", clazz));
		assertTrue(flags("275788-2012", clazz));
		assertTrue(flags("350487-2012", clazz));
		assertTrue(flags("377492-2012", clazz));
		assertTrue(flags("214619-2013", clazz));
		assertTrue(flags("396544-2013", clazz));
		assertFalse(flags("402931-2013", clazz));
		assertTrue(flags("210404-2014", clazz));
		assertTrue(flags("217711-2014", clazz));
		assertTrue(flags("293629-2014", clazz));
		assertTrue(flags("34442-2014", clazz));
		assertTrue(flags("782-2015", clazz));

		// egyeb szuroproba szeruen
		assertTrue(flags("189193-2012", clazz));
		assertFalse(flags("212692-2012", clazz));
		assertTrue(flags("217930-2012", clazz));
		assertTrue(flags("225082-2012", clazz));
		assertFalse(flags("354397-2014", clazz));
		assertFalse(flags("661-2015", clazz));
		assertFalse(flags("662-2015", clazz));
		assertTrue(flags("22537-2015", clazz));
		assertTrue(flags("30889-2015", clazz));
		assertFalse(flags("33612-2015", clazz));
		assertFalse(flags("41352-2015", clazz));
		assertTrue(flags("71717-2015", clazz));
		assertTrue(flags("97719-2015", clazz));
		assertFalse(flags("161519-2015", clazz));
		assertTrue(flags("179351-2015", clazz));
	}

	@Test
	public void awCritPaymentDeadlineCondIndicatorTest() {
		Class<? extends AbstractIndicator> clazz = AwCritPaymentDeadlineCondIndicator.class;
		assertTrue(flags("407174-2012", clazz));
		assertFalse(flags("217674-2013", clazz));
		assertFalse(flags("421411-2013", clazz));
		assertFalse(flags("55252-2014", clazz));
		assertFalse(flags("388685-2014", clazz));
		assertFalse(flags("104934-2015", clazz));
	}

	@Test
	public void contrDescCartellingIndicatorTest() {
		Class<? extends AbstractIndicator> clazz = ContrDescCartellingIndicator.class;
		assertFalse(flags("179319-2012", clazz));
		// assertTrue(flags("228639-2012", clazz));
		assertTrue(flags("30555-2014", clazz));
	}

	@Test
	public void deadlineIsTightIndicatorTest() {
		Class<? extends AbstractIndicator> clazz = DeadlineIsTightIndicator.class;
		assertFalse(flags("182464-2013", clazz));
		assertFalse(flags("86739-2015", clazz));
		assertFalse(flags("93537-2015", clazz));
		assertFalse(flags("151457-2015", clazz));
		assertTrue(flags("186835-2015", clazz));
		assertTrue(flags("206324-2015", clazz));
		assertFalse(flags("230119-2015", clazz));
	}

	@Test
	public void decisionDateDiffersFromOpeningDateIndicatorTest() {
		Class<? extends AbstractIndicator> clazz = DecisionDateDiffersFromOpeningDateIndicator.class;

		assertFalse(flags("120530-2014", clazz));
		assertFalse(flags("20538-2013", clazz)); // 120-120
		assertFalse(flags("33362-2013", clazz));
		assertTrue(flags("56575-2013", clazz));
		assertFalse(flags("195717-2013", clazz));
		assertTrue(flags("230608-2013", clazz));
		assertFalse(flags("233023-2013", clazz));
		assertTrue(flags("325314-2013", clazz));
		assertTrue(flags("328389-2013", clazz));
		assertTrue(flags("331081-2013", clazz));
		assertTrue(flags("335737-2013", clazz));
		assertTrue(flags("335748-2013", clazz));
		assertTrue(flags("337168-2013", clazz));
		assertTrue(flags("337776-2013", clazz));
		assertTrue(flags("338305-2013", clazz));
		assertTrue(flags("365872-2013", clazz));
	}

	@Ignore
	@Test
	public void finAbRevenueCondExceedEstimValIndicator2Test() {
		Class<? extends AbstractIndicator> clazz = FinAbRevenueCondExceedEstimValIndicator2.class;
		assertFalse(flags("220959-2015", clazz));
		assertTrue(flags("247939-2014", clazz));
		assertFalse(flags("88137-2014", clazz));
		assertTrue(flags("362624-2013", clazz)); // correct
		assertFalse(flags("111783-2013", clazz));
		assertTrue(flags("292760-2012", clazz)); // correct
		assertFalse(flags("217658-2012", clazz));
	}

	@Test
	public void finAbRevenueCondExceedEstimValIndicatorTest() {
		Class<? extends AbstractIndicator> clazz = FinAbRevenueCondExceedEstimValIndicator.class;
		// assertFalse(flags("661-2015", clazz));
		// assertFalse(flags("94595-2015", clazz));

		// fiatal AT:
		assertFalse(flags("206844-2012", clazz));
		assertFalse(flags("294895-2012", clazz));
		assertFalse(flags("404274-2012", clazz));
		assertFalse(flags("816-2013", clazz));
		assertFalse(flags("131471-2013", clazz));
		assertFalse(flags("218491-2013", clazz));
		assertFalse(flags("270825-2013", clazz));
		assertFalse(flags("292948-2013", clazz));
		assertFalse(flags("427966-2013", clazz));
		assertFalse(flags("222249-2015", clazz));
		assertFalse(flags("196251-2015", clazz));
		assertFalse(flags("377962-2015", clazz));

		// NEW
		assertFalse(flags("365811-2015", clazz));
		assertFalse(flags("283763-2015", clazz));
		assertFalse(flags("207958-2015", clazz));
		assertFalse(flags("127251-2015", clazz));
		assertFalse(flags("115607-2015", clazz));
		assertFalse(flags("103212-2015", clazz));
		// assertFalse(flags("407079-2014", clazz)); // TODO műk id,
		// szakasz...
		assertFalse(flags("403861-2014", clazz));
		assertFalse(flags("324905-2014", clazz));
		assertFalse(flags("281075-2014", clazz));
		assertTrue(flags("257284-2014", clazz));
		assertFalse(flags("242181-2014", clazz));
		assertFalse(flags("123735-2014", clazz));
		// assertFalse(flags("14632-2014", clazz)); // estim v parse bug...
		// assertFalse(flags("439017-2013", clazz)); // FEEDBACK
		assertTrue(flags("362624-2013", clazz));
		assertFalse(flags("361797-2013", clazz));
		assertFalse(flags("303521-2013", clazz));
		assertFalse(flags("301367-2013", clazz));
		assertFalse(flags("253530-2013", clazz));
		assertFalse(flags("215730-2013", clazz));
		assertFalse(flags("151339-2013", clazz));
		assertFalse(flags("149430-2013", clazz));
		// assertFalse(flags("131471-2013", clazz)); // estim v parse bug...
		assertFalse(flags("83715-2013", clazz));
		// assertFalse(flags("79841-2013", clazz)); // also VI3, 1 word diff
		assertFalse(flags("392699-2012", clazz));
		assertFalse(flags("322586-2012", clazz));
		assertFalse(flags("294587-2012", clazz));
		assertFalse(flags("265567-2012", clazz));
	}

	@Test
	public void highFinalValueIndicatorTest() {
		Class<? extends AbstractIndicator> clazz = HighFinalValueIndicator.class;

		assertFalse(flags("275997-2015", clazz));
		// assertFalse(flags("165243-2015", clazz)); // estimval strange format

		assertFalse(flags("412629-2015", clazz));
		assertFalse(flags("405118-2015", clazz));
		assertFalse(flags("286644-2015", clazz));
		// assertFalse(flags("237305-2015", clazz)); // estimval strange format
		assertFalse(flags("217349-2015", clazz));
		assertFalse(flags("176014-2015", clazz));
		// assertFalse(flags("173314-2015", clazz)); // not TD3 but content is
		assertFalse(flags("153270-2015", clazz));
		// assertFalse(flags("138587-2015", clazz));// not TD3 but content is
		assertFalse(flags("32055-2015", clazz));
		// assertFalse(flags("438575-2014", clazz)); // estimval strange format
		assertFalse(flags("417627-2014", clazz));
		assertFalse(flags("402453-2014", clazz));
		assertTrue(flags("69858-2015", clazz));
		assertTrue(flags("59682-2015", clazz));
		assertTrue(flags("59632-2015", clazz));
		assertTrue(flags("56004-2015", clazz));
		assertTrue(flags("48313-2015", clazz));
	}

	@Test
	public void finalValFarFromEstimValIndicatorTest() {
		Class<? extends AbstractIndicator> clazz = FinalValFarFromEstimValIndicator.class;
		assertTrue(flags("21538-2015", clazz));
	}

	private boolean flags(String id,
			Class<? extends AbstractIndicator> indicator) {
		Notice notice;
		try {
			notice = processedNoticeProvider.notice(id);
			IndicatorResult ir = notice.getIndicatorResults().get(
					indicator.getSimpleName());
			return null != ir && ir.getType() == IndicatorResultType.FLAG;
		} catch (InterruptedException e) {
			throw new IllegalStateException(e);
		}
	}

	@Test
	public void kMonitorInstIndicatorsTest() {
		// Közgép
		assertTrue(flags("202190-2015", WinnerOrgInKMDBIndicator.class));

		// Saxum
		assertTrue(flags("202414-2014", ContractingOrgInKMDBIndicator.class));

		// NFÜ
		assertTrue(flags("744-2014", ContractingOrgInKMDBIndicator.class));

		// String rfName = "Nemzeti Fejlesztési Ügynökség";
		// String kmName = "Nemzeti Fejlesztési Ügynökség (NFÜ)";
		// rfName = norm(rfName);
		// System.out.println("RF N: " + rfName);
		// String rfNameNoSuffix = CompanyNameUtils.removeSuffixes(rfName);
		// System.out.println("RF S: " + rfNameNoSuffix);
		// String rfNamePattern = rfNameNoSuffix.replaceAll(" |$",
		// "( \\\\w+)* ")
		// .trim();
		// System.out.println("RF P: " + rfNamePattern);
		//
		// String originalKmName = kmName;
		// kmName = norm(kmName);
		// System.out.println("KM N: " + rfName);
		// String kmNameNoSuffix = CompanyNameUtils.removeSuffixes(kmName);
		// String kmNamePattern = kmNameNoSuffix.replaceAll(" |$",
		// "( \\\\w+)* ")
		// .trim();
		// if (CompanyNameUtils.matchSuffixesNorm(rfName, kmName)) {
		// System.out.println("SUFFIX MATCH");
		// if (rfNameNoSuffix.matches(kmNamePattern)
		// || kmNameNoSuffix.matches(rfNamePattern)) {
		// System.out.println("WHOLE MATCH");
		// }
		// }

	}

	private String norm(String name) {
		name = name.toLowerCase();
		name = StrUtils.removeAccents(name);
		name = StrUtils.leaveOnlyAlphanumerics(name);
		return name;
	}

	@Test
	public void openingDateDiffersFromDeadlineIndicatorTest() {
		Class<? extends AbstractIndicator> clazz = OpeningDateDiffersFromDeadlineIndicator.class;
		assertFalse(flags("316144-2012", clazz));
		assertTrue(flags("69418-2014", clazz));
		assertFalse(flags("87033-2014", clazz));
		assertTrue(flags("402278-2014", clazz));
		assertTrue(flags("3999-2015", clazz));
		assertFalse(flags("125929-2015", clazz));
		assertFalse(flags("154475-2015", clazz));
		assertFalse(flags("161271-2015", clazz));
	}

	@Ignore
	@Test
	public void renewableContractIndicatorTest() {
		Class<? extends AbstractIndicator> clazz = RenewableContractIndicator.class;
		assertTrue(flags("189193-2012", clazz));
		assertTrue(flags("239955-2013", clazz));
		assertTrue(flags("78563-2015", clazz));
	}

	@Test
	public void techCapEURefCondIndicatorTest() {
		Class<? extends AbstractIndicator> clazz = TechCapEURefCondIndicator.class;

		// sárgák:
		assertFalse(flags("189063-2012", clazz));
		assertFalse(flags("111953-2013", clazz));
		assertFalse(flags("17370-2013", clazz));
		assertFalse(flags("408340-2013", clazz));
		assertFalse(flags("424993-2013", clazz));
		assertFalse(flags("45601-2013", clazz));

		assertFalse(flags("327925-2012", clazz));
		assertFalse(flags("112014-2013", clazz));
		assertFalse(flags("300620-2013", clazz));
		assertFalse(flags("433129-2013", clazz));
		// assertFalse(flags("70760-2013", clazz));
		assertFalse(flags("145927-2015", clazz));
		assertFalse(flags("52340-2015", clazz));
		// assertFalse(flags("68718-2015", clazz));

		assertTrue(flags("288966-2012", clazz));
		assertTrue(flags("293100-2012", clazz));
		assertTrue(flags("293101-2012", clazz));
		assertTrue(flags("299173-2012", clazz));
		assertTrue(flags("320969-2012", clazz));
	}

	@Test
	public void techCapGeoCondIndicatorTest() {
		Class<? extends AbstractIndicator> clazz = TechCapGeoCondIndicator.class;
		assertFalse(flags("329640-2013", clazz));
	}

	@Test
	public void techCapRefValueIndicatorTest() {
		Class<? extends AbstractIndicator> clazz = TechCapRefCondExceedEstimValIndicator.class;

		// credit
		assertFalse(flags("189765-2013", clazz));
		assertFalse(flags("3325-2013", clazz));
		assertFalse(flags("349880-2013", clazz));
		assertFalse(flags("266186-2015", clazz));
		assertFalse(flags("396496-2013", clazz));
		assertFalse(flags("364421-2012", clazz));
		// correct flag
		assertTrue(flags("242181-2014", clazz));
		assertTrue(flags("178998-2014", clazz));
		assertTrue(flags("88111-2014", clazz));
		assertTrue(flags("439092-2013", clazz));
		assertFalse(flags("402815-2013", clazz));
		assertTrue(flags("252965-2013", clazz));
		assertTrue(flags("215531-2013", clazz));
		assertTrue(flags("205254-2013", clazz));
		assertTrue(flags("122242-2013", clazz));

		assertTrue(flags("222229-2012", clazz));
		// incorrect flag
		assertFalse(flags("196251-2015", clazz));
		assertFalse(flags("96297-2015", clazz));
		assertFalse(flags("62665-2015", clazz));
		assertFalse(flags("50209-2015", clazz));
		assertFalse(flags("277-2015", clazz));
		assertFalse(flags("343774-2014", clazz));
		assertFalse(flags("320802-2014", clazz));
		assertFalse(flags("307843-2014", clazz));
		assertFalse(flags("287756-2014", clazz));
		assertFalse(flags("278197-2014", clazz));
		assertFalse(flags("256515-2014", clazz));
		assertFalse(flags("145837-2014", clazz));
		assertFalse(flags("131864-2014", clazz));
		assertFalse(flags("102442-2014", clazz));
		assertFalse(flags("101542-2014", clazz));
		assertFalse(flags("421515-2013", clazz));
		assertFalse(flags("402815-2013", clazz));
		assertFalse(flags("369414-2013", clazz));
		assertFalse(flags("369255-2013", clazz));
		assertFalse(flags("356998-2013", clazz));
		assertFalse(flags("342325-2013", clazz));
		assertFalse(flags("328267-2013", clazz));
		assertFalse(flags("310315-2013", clazz));
		assertFalse(flags("268161-2013", clazz));
		assertFalse(flags("266802-2013", clazz));
		assertFalse(flags("249166-2013", clazz));
		assertFalse(flags("218595-2013", clazz));
		assertFalse(flags("216387-2013", clazz));
		assertFalse(flags("216378-2013", clazz));
		assertFalse(flags("216147-2013", clazz));
		assertFalse(flags("201678-2013", clazz));
		assertFalse(flags("171215-2013", clazz));
		assertFalse(flags("156953-2013", clazz));
		assertFalse(flags("155237-2013", clazz));
		assertFalse(flags("151327-2013", clazz));
		assertFalse(flags("149430-2013", clazz));
		assertFalse(flags("111923-2013", clazz));
		assertFalse(flags("110427-2013", clazz));
		assertFalse(flags("100882-2013", clazz));
		assertFalse(flags("55591-2013", clazz));
		assertFalse(flags("2156-2013", clazz));
		assertFalse(flags("392634-2012", clazz));
		assertFalse(flags("342426-2012", clazz));
		assertFalse(flags("299173-2012", clazz));
		assertFalse(flags("296483-2012", clazz));
		assertFalse(flags("294815-2012", clazz));
		assertFalse(flags("288966-2012", clazz));
		assertFalse(flags("272733-2012", clazz));
		assertFalse(flags("272498-2012", clazz));
		assertFalse(flags("252918-2012", clazz));
		assertFalse(flags("244207-2012", clazz));
		assertFalse(flags("222226-2012", clazz));
		assertFalse(flags("217885-2012", clazz));
	}

	@Test
	public void techCapSingleContractRefCondIndicatorTest() {
		Class<? extends AbstractIndicator> clazz = TechCapSingleContractRefCondIndicator.class;
		assertFalse(flags("52340-2015", clazz));

		assertTrue(flags("221902-2012", clazz));
		assertTrue(flags("239282-2012", clazz));
		assertFalse(flags("242402-2012", clazz));
		assertFalse(flags("278982-2012", clazz));
		assertTrue(flags("327588-2012", clazz));
		assertFalse(flags("364421-2012", clazz));
		assertFalse(flags("40488-2013", clazz));
		assertFalse(flags("113069-2014", clazz));
		assertFalse(flags("161263-2015", clazz));
	}

	@Test
	public void totalFinalValueHiIndicatorTest() {
		Class<? extends AbstractIndicator> clazz = HighFinalValueIndicator.class;
		assertTrue(flags("409228-2015", clazz));
		assertFalse(flags("416098-2015", clazz));
	}

	@Test
	public void totalQuantityHiDeltaIndicatorTest() {
		Class<? extends AbstractIndicator> clazz = TotalQuantityHiDeltaIndicator.class;
		assertFalse(flags("30555-2014", clazz));
	}

	@Test
	public void unsucc1IndicatorTest() throws InterruptedException {
		Class<? extends AbstractIndicator> clazz = UnsuccessfulProcWithoutInfo1Indicator.class;
		assertFalse(flags("235141-2015", clazz));
		assertFalse(flags("157643-2015", clazz));
		assertFalse(flags("148260-2015", clazz));
		// assertFalse(flags("114215-2015", clazz));
		assertFalse(flags("212410-2014", clazz));
		assertFalse(flags("192078-2014", clazz));
		assertFalse(flags("133341-2014", clazz));
		assertFalse(flags("348333-2013", clazz));
		// assertFalse(flags("290537-2013", clazz));
		assertFalse(flags("249430-2013", clazz));
		assertFalse(flags("220580-2013", clazz));
		assertFalse(flags("201714-2013", clazz));
		assertFalse(flags("193311-2013", clazz));
		assertFalse(flags("176948-2013", clazz));
		assertFalse(flags("98789-2013", clazz));
		assertFalse(flags("71604-2013", clazz));
		assertFalse(flags("33225-2013", clazz));
		assertFalse(flags("29800-2013", clazz));
		assertFalse(flags("386203-2012", clazz));
	}

}
