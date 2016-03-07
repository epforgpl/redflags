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
package hu.petabyte.redflags.engine.tedintf;

import hu.petabyte.redflags.engine.model.DisplayLanguage;
import hu.petabyte.redflags.engine.model.NoticeID;
import hu.petabyte.redflags.engine.model.Tab;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Determines the freshest available notice number in a given year. It uses a
 * file cache to store max numbers for each year. If the cache doesn't know
 * about a given year, a magic algorhytm is started which downloads as many data
 * tabs as needed to identify the largest available notice number.
 *
 * @author Zsolt Jurányi
 *
 */
@Service
public class MaxNumberDeterminer {

	private static final Logger LOG = LoggerFactory
			.getLogger(MaxNumberDeterminer.class);
	public static final String CACHE_FILENAME = "ted-maxnumber.properties";
	private static final int MAX_B = 999999;
	private static final int INITIAL_A = 400000;
	private static final int INITIAL_B = MAX_B;

	private int year;
	private int a, b;
	private final Map<Integer, Set<Integer>> invNumCache = new HashMap<Integer, Set<Integer>>();
	private final Map<Integer, Integer> maxNumCache = new HashMap<Integer, Integer>();
	private @Autowired TedInterfaceHolder ted;

	private void findAbove() {
		while (isValid(b) && b <= MAX_B) {
			a = b;
			b *= 2;
			b = Math.min(b, MAX_B);
			LOG.trace("b = {} > valid > a,b = {},{}", a, a, b);
		}
	}

	private void findBelow() {
		while (a > 0 && !isValid(a)) {
			b = a - 1;
			a /= 2;
			LOG.trace("a = {} > invalid > a,b = {},{}", b, a, b);
		}
	}

	private int findBetween() {
		if (0 == a) {
			LOG.trace("a = 0 > giving up, year {} is not available", year);
			return a;
		}
		if (b - a <= 1) {
			LOG.trace("a,b = {},{} > too close > returning {}", a, b, a);
			return a;
		}
		int center = (a + b) / 2;
		if (isValid(center)) {
			LOG.trace("a,b = {},{} > center = {} > valid", a, b, center);
			a = center;
			return findBetween();
		} else {
			LOG.trace("a,b = {},{} > center = {} > invalid", a, b, center);
			b = center;
			return findBetween();
		}
	}

	/**
	 * Get the <code>TedInterfaceHolder</code> instance used by
	 * <code>MaxNumberDeterminer</code>.
	 *
	 * @return The <code>TedInterfaceHolder</code> instance used by
	 *         <code>MaxNumberDeterminer</code>.
	 */
	public TedInterfaceHolder getTed() {
		return ted;
	}

	private boolean isValid(int number) {
		return 0 < number
				&& !invNumCache.get(year).contains(number)
				&& null != ted.get().requestNoticeTabQuietly(
						new NoticeID(number, year), DisplayLanguage.EN,
						Tab.DATA);
	}

	private void loadMaxNumCache() {
		try {
			FileReader r = new FileReader(CACHE_FILENAME);
			Properties p = new Properties();
			p.load(r);
			for (Entry<Object, Object> e : p.entrySet()) {
				if (e.getKey().toString().matches("\\d+")
						&& e.getValue().toString().matches("\\d+")) {
					int y = Integer.valueOf(e.getKey().toString());
					int n = Integer.valueOf(e.getValue().toString());
					maxNumCache.put(y, n);
				}
			}
			r.close();
			LOG.debug("Max number cache loaded: {}", maxNumCache);
		} catch (IOException e) {
			LOG.warn("Failed to load max number cache");
			LOG.trace("Failed to load max number cache", e);
		}
	}

	/**
	 * Firstly checks for the validity of <code>year</code>, then tries to fetch
	 * max number from cache. If it fails, starts the magic algorhytm do
	 * determine max number. Return 0 if the year is invalid or the algorhytm
	 * fails.
	 *
	 * @param year
	 *            The year which you are interested in.
	 * @return The largest available notice number in the given year or 0 if the
	 *         year is invalid or it couldn't be crawled.
	 */
	public synchronized int maxNumberForYear(int year) {

		// TED removes notices older than 5 year
		// in 2015, 2010 will be incomplete, this algo cannot handle it
		int minYear = Calendar.getInstance().get(Calendar.YEAR) - 4;
		// future years cannot be crawled:
		int maxYear = Calendar.getInstance().get(Calendar.YEAR);

		if (year < minYear || year > maxYear) {
			LOG.debug(
					"{} year is out of crawlable range ({}-{}), returning 0 as max number",
					year, minYear, maxYear);
			return 0;
		}

		loadMaxNumCache();
		if (maxNumCache.containsKey(year)) {
			int number = maxNumCache.get(year);
			LOG.debug("Max number for year {} is returned from cache: {}",
					year, number);
			return number;
		}

		this.a = INITIAL_A;
		this.b = INITIAL_B;
		this.year = year;
		this.invNumCache.put(year, new HashSet<Integer>());
		LOG.info("Searching max number with initial a,b = {},{}", a, b);
		findBelow();
		findAbove();
		int number = findBetween();
		LOG.info("Max number for year {} is {}", year, number);
		maxNumCache.put(year, number);
		saveMaxNumCache();
		return number;
	}

	private void saveMaxNumCache() {
		try {
			FileWriter w = new FileWriter(CACHE_FILENAME);
			Properties p = new Properties();
			for (Entry<Integer, Integer> e : maxNumCache.entrySet()) {
				int year = e.getKey();
				int number = e.getValue();
				if (year < Calendar.getInstance().get(Calendar.YEAR)) {
					// should not save current year, because it changes!
					p.put(Integer.toString(year), Integer.toString(number));
				}
			}
			p.store(w, "ted max numbers for each year");
			w.close();
		} catch (IOException e) {
			LOG.warn("Failed to save max number cache");
			LOG.trace("Failed to save max number cache", e);
		}
	}

	/**
	 *
	 * Set the <code>TedInterfaceHolder</code> instance to be used by
	 * <code>MaxNumberDeterminer</code>.
	 *
	 * @param ted
	 *            The <code>TedInterfaceHolder</code> instance used by
	 *            <code>MaxNumberDeterminer</code>.
	 */
	public void setTed(TedInterfaceHolder ted) {
		this.ted = ted;
	}

}
