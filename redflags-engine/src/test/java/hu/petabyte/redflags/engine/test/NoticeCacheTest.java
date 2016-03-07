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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import hu.petabyte.redflags.engine.model.DisplayLanguage;
import hu.petabyte.redflags.engine.model.NoticeID;
import hu.petabyte.redflags.engine.model.Tab;
import hu.petabyte.redflags.engine.tedintf.cached.FilesystemNoticeCache;
import hu.petabyte.redflags.engine.tedintf.cached.GzippedNoticeCache;
import hu.petabyte.redflags.engine.tedintf.cached.SimpleNoticeCache;

/**
 * @author Zsolt Jurányi
 */
public class NoticeCacheTest {

	private static final String directory = "test-cache";
	private static final NoticeID id = new NoticeID("1-2015");
	private static final DisplayLanguage lang = DisplayLanguage.EN;
	private static final Tab tab = Tab.DATA;
	private static final String raw = "This is the test text which will be cached.";
	private static final SimpleNoticeCache simpleCache = new SimpleNoticeCache(directory);
	private static final GzippedNoticeCache gzippedCache = new GzippedNoticeCache(directory);

	@BeforeClass
	@AfterClass
	public static void cleanup() {
		try {
			FileUtils.deleteDirectory(new File(directory));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void filenamesAreDifferent() {
		String s = simpleCache.filename(id, lang, tab);
		String g = gzippedCache.filename(id, lang, tab);
		assertNotEquals(s, g);
	}

	private void fsCacheWorksFine(FilesystemNoticeCache cache) {
		File file = new File(cache.filename(id, lang, tab));

		// file does not exist, fetch should return null
		assertNull(cache.fetch(id, lang, tab));

		// let's store something
		cache.store(id, lang, tab, raw);

		// now the file should exist
		assertTrue(file.exists());

		// and we should get back the content
		assertEquals(raw, cache.fetch(id, lang, tab));

		// but if we remove it
		cache.remove(id, lang, tab);

		// the file should disappear
		assertFalse(file.exists());
	}

	@Test
	public void gzippedCacheWorksFine() {
		fsCacheWorksFine(gzippedCache);
	}

	@Test
	public void simpleCacheWorksFine() {
		fsCacheWorksFine(simpleCache);
	}

}
