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
package hu.petabyte.redflags.engine.tedintf.cached;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hu.petabyte.redflags.engine.model.DisplayLanguage;
import hu.petabyte.redflags.engine.model.NoticeID;
import hu.petabyte.redflags.engine.model.Tab;

/**
 * Very simple notice cache which saves tabs with filenames like:
 * <code>cache/year/number/tab#-LANG.html</code>.
 *
 * @author Zsolt Jurányi
 *
 */
public class SimpleNoticeCache extends FilesystemNoticeCache {

	private static final Logger LOG = LoggerFactory.getLogger(SimpleNoticeCache.class);

	public SimpleNoticeCache(String directory) {
		super(directory);
	}

	@Override
	public synchronized String fetch(NoticeID id, DisplayLanguage lang, Tab tab) {
		checkNotNull(id, "id should not be null.");
		checkNotNull(lang, "lang should not be null.");
		checkNotNull(tab, "tab should not be null.");
		String fn = filename(id, lang, tab);
		File f = new File(fn);
		String r = null;
		if (f.exists()) {
			try {
				Scanner s = new Scanner(f, "UTF-8");
				r = s.useDelimiter("\\Z").next();
				s.close();
			} catch (Exception e) {
				LOG.warn("Failed to read file: " + f.getAbsolutePath(), e);
			}
		}
		return r;
	}

	@Override
	public String filename(NoticeID id, DisplayLanguage lang, Tab tab) {
		checkNotNull(id, "id should not be null.");
		checkNotNull(lang, "lang should not be null.");
		checkNotNull(tab, "tab should not be null.");
		return new StringBuilder()//
				.append(directory).append(File.separator)//
				.append(id.year()).append(File.separator)//
				.append(id.number()).append(File.separator)//
				.append("tab")//
				.append(tab.getId()).append("-").append(lang.name())//
				.append(".html")//
				.toString();
	}

	@Override
	public synchronized void remove(NoticeID id, DisplayLanguage lang, Tab tab) {
		checkNotNull(id, "id should not be null.");
		checkNotNull(lang, "lang should not be null.");
		checkNotNull(tab, "tab should not be null.");
		new File(filename(id, lang, tab)).delete();
	}

	@Override
	public synchronized boolean store(NoticeID id, DisplayLanguage lang, Tab tab, String raw) {
		checkNotNull(id, "id should not be null.");
		checkNotNull(lang, "lang should not be null.");
		checkNotNull(tab, "tab should not be null.");
		String fn = filename(id, lang, tab);
		File f = new File(fn);
		File d = f.getParentFile();
		if (null != d) {
			d.mkdirs();
		}
		try {
			Writer out = new OutputStreamWriter(new FileOutputStream(f), "UTF8");
			out.write(raw);
			out.close();
			LOG.trace("Saved to cache: {}", f.getAbsolutePath());
			return true;
		} catch (IOException e) {
			LOG.warn("Failed to write file: " + f.getAbsolutePath(), e);
			return false;
		}

	}

}
