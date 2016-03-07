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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Scanner;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hu.petabyte.redflags.engine.model.DisplayLanguage;
import hu.petabyte.redflags.engine.model.NoticeID;
import hu.petabyte.redflags.engine.model.Tab;

/**
 * @author Zsolt Jurányi
 */
public class GzippedNoticeCache extends SimpleNoticeCache {

	private static final Logger LOG = LoggerFactory.getLogger(GzippedNoticeCache.class);

	public GzippedNoticeCache(String directory) {
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
				FileInputStream fileInput = new FileInputStream(f);
				InputStream gzipFileInput = new GZIPInputStream(fileInput);
				Scanner scanner = new Scanner(gzipFileInput, "UTF8");
				r = scanner.useDelimiter("\\Z").next();
				scanner.close();
				gzipFileInput.close();
				fileInput.close();
			} catch (Exception e) {
				LOG.warn("Failed to read file: " + f.getAbsolutePath(), e);
			}
		}
		return r;
	}

	@Override
	public String filename(NoticeID id, DisplayLanguage lang, Tab tab) {
		return super.filename(id, lang, tab) + ".gz";
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
		FileOutputStream fileOutput = null;
		try {
			fileOutput = new FileOutputStream(fn);
			OutputStream gzipFileOutput = new GZIPOutputStream(fileOutput);
			Writer writer = new OutputStreamWriter(gzipFileOutput, "UTF-8");
			try {
				writer.write(raw);
				writer.flush();
			} finally {
				writer.close();
			}
			fileOutput.close();
			LOG.trace("Saved to cache: {}", f.getAbsolutePath());
			return true;
		} catch (Exception e) {
			LOG.warn("Failed to write file: " + f.getAbsolutePath(), e);
			return false;
		}
	}

}
