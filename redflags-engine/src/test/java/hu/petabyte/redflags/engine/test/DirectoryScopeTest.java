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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import hu.petabyte.redflags.engine.model.DisplayLanguage;
import hu.petabyte.redflags.engine.model.NoticeID;
import hu.petabyte.redflags.engine.model.Tab;
import hu.petabyte.redflags.engine.scope.AbstractScope;
import hu.petabyte.redflags.engine.scope.DirectoryScope;
import hu.petabyte.redflags.engine.tedintf.cached.FilesystemNoticeCache;
import hu.petabyte.redflags.engine.tedintf.cached.SimpleNoticeCache;

/**
 * @author Zsolt Jurányi
 */
public class DirectoryScopeTest {

	private static final String directory = "scope-test-cache";

	private static final List<NoticeID> expectedIDs = Arrays.asList( //
			new NoticeID("10-2015"), //
			new NoticeID("1-2015"), //
			new NoticeID("1000-2013"), //
			new NoticeID("100-2013") //
	);

	@AfterClass
	public static void cleanup() {
		try {
			FileUtils.deleteDirectory(new File(directory));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@BeforeClass
	public static void generate() {
		FilesystemNoticeCache cache = new SimpleNoticeCache(directory);
		for (NoticeID id : expectedIDs) {
			cache.store(id, DisplayLanguage.EN, Tab.DATA, "");
		}

		// create an empty year directory too, just for fun
		new File(String.format("%s%s%d", directory, File.separator, 2014)).mkdirs();
	}

	@Test
	public void directoryScopeWorksFine() {
		AbstractScope scope = new DirectoryScope(directory);
		List<NoticeID> scopedIDs = new ArrayList<NoticeID>();
		for (NoticeID id : scope) {
			scopedIDs.add(id);
		}

		List<NoticeID> sortedExpectedIDs = new ArrayList<NoticeID>(expectedIDs);
		Collections.sort(sortedExpectedIDs);

		List<NoticeID> sortedScopedIDs = new ArrayList<NoticeID>(scopedIDs);
		Collections.sort(sortedScopedIDs);

		assertEquals(sortedExpectedIDs, sortedScopedIDs);
	}

}
