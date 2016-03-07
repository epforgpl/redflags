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
import hu.petabyte.redflags.engine.tedintf.MaxNumberDeterminer;
import hu.petabyte.redflags.engine.tedintf.TedInterfaceHolder;
import hu.petabyte.redflags.engine.tedintf.cached.CachedTedInterface;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Properties;

import org.junit.Test;

/**
 * @author Zsolt Jurányi
 */
public class MaxNumberDeterminerTest {

	private final MaxNumberDeterminer mnd = new MaxNumberDeterminer();

	public MaxNumberDeterminerTest() {
		mnd.setTed(new TedInterfaceHolder());
		mnd.getTed().set(new CachedTedInterface("tenders-cache"));
	}

	// @Test
	public void readsCacheIfAvailable() throws IOException {
		int y = 2013, n = 42;
		FileWriter w = new FileWriter(MaxNumberDeterminer.CACHE_FILENAME);
		Properties p = new Properties();
		p.put(Integer.toString(y), Integer.toString(n));
		p.store(w, "ted max numbers for each year");
		w.close();
		assertEquals(n, mnd.maxNumberForYear(y));
	}

	@Test
	public void returnsRightNumberFor2014() {
		// it should fail only if we are in 2019 :)
		assertEquals(446419, mnd.maxNumberForYear(2014));
	}

	@Test
	public void returnsZeroForFutureYears() {
		int year = Calendar.getInstance().get(Calendar.YEAR) + 1;
		assertEquals(0, mnd.maxNumberForYear(year));
	}

	@Test
	public void returnsZeroForTooOldYears() {
		int year = Calendar.getInstance().get(Calendar.YEAR) - 5;
		assertEquals(0, mnd.maxNumberForYear(year));
	}

}
