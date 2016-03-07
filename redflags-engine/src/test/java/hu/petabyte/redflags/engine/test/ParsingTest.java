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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import hu.petabyte.redflags.engine.RedflagsEngineApp;
import hu.petabyte.redflags.engine.model.Notice;
import hu.petabyte.redflags.engine.tedintf.TedInterface;
import hu.petabyte.redflags.engine.tedintf.TedInterfaceHolder;
import hu.petabyte.redflags.engine.tedintf.cached.CachedTedInterface;
import hu.petabyte.redflags.engine.test.helper.ProcessedNoticeProvider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.ConfigFileApplicationContextInitializer;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author Zsolt Jurányi
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RedflagsEngineApp.class)
@ContextConfiguration(classes = RedflagsEngineApp.class, initializers = ConfigFileApplicationContextInitializer.class)
@ActiveProfiles("test")
public class ParsingTest implements InitializingBean {
	// TODO detailed parse test (for every relevant gear, method)

	// TODO resource cache??? takes dir + resource dir. resdir is readonly

	// private static final Logger LOG =
	// LoggerFactory.getLogger(ParsingTest.class);

	private static final TedInterface tedintf = new CachedTedInterface(
			"test-cache");

	private @Autowired TedInterfaceHolder ted;
	private @Autowired ProcessedNoticeProvider processedNoticeProvider;
	private Gson gson = new GsonBuilder().setPrettyPrinting().create();

	private long estimVal(String id) throws InterruptedException {
		try {
			return processedNoticeProvider.notice(id).getObjs().get(0)
					.getEstimatedValue();
		} catch (IndexOutOfBoundsException e) {
			return -1;
		}
	}

	@Test
	public void estimValParsingTest2() throws InterruptedException {
		String[] testTable = { //
				// --- critic ---
		// "10926-2013|75000000", // cancelled
		// "216378-2013|72000000", // no difference in text...
		// "217023-2013|3 205 190 000", // no difference in text...
		// "303521-2013|172 255 735", // no difference in text...
		// "310315-2013|509 538 000", // no difference in text...
		// "438538-2013|411 700 000", // cancelled
		// "245499-2014|685 000 000", // strange
		// "253160-2014|80 000 000", // cancelled
		// "287241-2014|800 000", // cancelled
		// "323534-2014|80000000", // cancelled
				"372278-2012|80000000", //
				"10704-2013|4 610 766 569", //
				"10923-2013|177 261 000", //
				"100357-2013|412251715", //
				"102064-2013|505990000", //
				"199030-2013|8263001312", //
				"215531-2013|465625000", //
				"217046-2013|2 640 233 280", //
				"311020-2013|221 000 000", //
				"335646-2013|7 699 374 588", //
				"409224-2013|146 160 237", //
				"427962-2013|2 864 346 057", //
				"429140-2013|113 068 375", //
				"744-2014|2 900 000 000", //
				"30555-2014|5 198 477 804", //
				"62981-2014|75 655 000", //
				"102487-2014|17 022 730 865", //
				"103484-2014|110 236 220", //
				"132626-2014|1 833 724 236", //
				"156252-2014|6 427 497 447", //
				"165005-2014|72025000", //
				"166367-2014|261500000", //
				"177848-2014|3 507 903 698", //
				"217397-2014|140 000 000", //
				"219551-2014|276 262 501", //
				"235843-2014|950 000 000", //
				"246198-2014|78 000 000", //
				"246251-2014|101 064 000", //
				"337007-2014|103000000", //
				"400576-2014|176 845 735", //
				"278765-2014|0", //
				"59426-2015|14000000", //
				// --- random ---
				// "320667-2012|396 000 000", // cancelled
				// "322380-2012|150 000 000", // cancelled
				"319306-2012|0", //
				"319616-2012|0", //
				"320228-2012|0", //
				"320643-2012|13 000 000 000", //
				"320661-2012|0", //
				"320931-2012|0", //
				"320932-2012|58 000 000", //
				"320969-2012|57 600 000", //
				"321246-2012|960 000 000", //
				"321341-2012|0", //
				"322381-2012|251 293 686", //
				"322379-2012|0", //
				"322585-2012|0", //
				"322580-2012|0", //
				"322581-2012|24 900 000", //
				"322586-2012|30 000 000", //
				"324931-2012|0", //
				"325275-2012|265 900 000", //
				"325278-2012|0", //
				"325280-2012|138 750 000", //
				"325491-2012|0", //
				"325574-2012|0", //
				"325711-2012|104 000 000", //
				"325719-2012|0", //
				"325717-2012|0", //
				"325720-2012|53 508 000", //
				"326035-2012|0", //
				"326108-2012|288 165 778", //
				"327130-2012|0", //
				"327233-2012|301 000 000", //
				"327454-2012|0", //
				"327583-2012|0", //
				"327581-2012|0", //
				"327582-2012|0", //
				"327588-2012|0", //
				"327585-2012|0", //
				"327587-2012|0", //
				"327589-2012|0", //
				"327649-2012|22 500 000", //
				"327650-2012|0", //
				"327590-2012|0", //
				"327926-2012|0", //
				"329055-2012|71 016 000", //
				"329090-2012|0", //
				"329091-2012|0", //
				"329089-2012|279 970 000", //
				"329452-2012|70 576 000", //
				"329451-2012|2 600 000 000", //
				"330875-2012|0",//
		};
		Map<String, Long> expected = new HashMap<String, Long>();
		Map<String, Long> parsed = new HashMap<String, Long>();
		for (String test : testTable) {
			String id = test.split("\\|")[0];
			long exp = Long
					.valueOf(test.split("\\|")[1].replaceAll("\\D+", ""));
			long act = estimVal(id);
			expected.put(id, exp);
			parsed.put(id, act);
			if (exp != act) {
				break;
			}
		}
		System.out.println("***");
		for (String test : testTable) {
			String id = test.split("\\|")[0];
			long exp = expected.get(id);
			long act = parsed.get(id);
			System.out.printf("%s\t%s\t%d\t%d\n", Boolean.toString(exp == act),
					id, exp, act);
			if (exp != act) {
				fail();
			}
		}
		System.out.println("***");

	}

	@Test
	public void estimValParsingTest() throws InterruptedException {
		assertEquals(950000000, estimVal("235843-2014"));

		assertEquals(0, estimVal("205543-2012"));
		assertEquals(72000000, estimVal("249535-2012"));
		assertEquals(90000000, estimVal("254522-2012"));
		assertEquals(150400000, estimVal("265908-2012"));
		assertEquals(120000000, estimVal("274005-2012"));
		assertEquals(123480000, estimVal("278984-2012"));
		assertEquals(633000000, estimVal("294937-2012"));
		assertEquals(152000000, estimVal("308089-2012"));
		assertEquals(67000000, estimVal("316143-2012"));
		assertEquals(56000000, estimVal("127734-2013"));
		assertEquals(96000000, estimVal("142329-2013"));
		assertEquals(150000000, estimVal("263179-2013"));
		assertEquals(0, estimVal("366259-2013"));
		assertEquals(0, estimVal("41352-2015"));
		assertEquals(500000000, estimVal("187100-2015"));
		assertEquals(0, estimVal("220959-2015"));
		assertEquals(0, estimVal("323035-2015"));
	}

	@Before
	public void _init() {
		ted.set(tedintf);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		ted.set(tedintf);
	}

	@Test
	public void finalValueTest() throws InterruptedException {
		long fv;

		fv = processedNoticeProvider.notice("30077-2016").getObjs().get(0)
				.getTotalFinalValue();
		assertEquals(176754240L, fv);

		fv = processedNoticeProvider.notice("184526-2015").getObjs().get(0)
				.getTotalFinalValue();
		assertEquals(100000000L, fv);

		fv = processedNoticeProvider.notice("335379-2015").getObjs().get(0)
				.getTotalFinalValue();
		assertEquals(5071090198L, fv);
	}

	// @Test
	public void cancelledNoticeDetectionTest() throws InterruptedException {
		assertTrue(processedNoticeProvider.notice("312660-2013").isCancelled());
	}

	// @Test
	public void threadsDontMix() throws InterruptedException {
		// single-thread download (notice X)
		String json = gson
				.toJson(processedNoticeProvider.notice("210605-2012"));

		// multi-thread download (fetching notice X twice)
		List<Notice> notices = processedNoticeProvider
				.notices("210605-2012,286167-2015,324259-2015,210605-2012");

		// all 3 jsons are the same
		assertEquals(json, gson.toJson(notices.get(0)));
		assertEquals(json, gson.toJson(notices.get(3)));
	}

}
