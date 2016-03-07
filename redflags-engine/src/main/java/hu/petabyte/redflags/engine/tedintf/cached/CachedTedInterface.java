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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import hu.petabyte.redflags.engine.model.DisplayLanguage;
import hu.petabyte.redflags.engine.model.NoticeID;
import hu.petabyte.redflags.engine.model.Tab;
import hu.petabyte.redflags.engine.tedintf.TedError;
import hu.petabyte.redflags.engine.tedintf.TedInterface;
import hu.petabyte.redflags.engine.tedintf.TedResponse;

import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Zsolt Jurányi
 */
public class CachedTedInterface extends TedInterface { // TODO test

	private static final Logger LOG = LoggerFactory
			.getLogger(CachedTedInterface.class);
	private final String directory;
	private final NoticeCache oldCache; // TODO list
	private final NoticeCache cache;

	public CachedTedInterface(String directory) {
		this(directory, new SimpleNoticeCache(directory),
				new GzippedNoticeCache(directory));
	}

	public CachedTedInterface(String directory, NoticeCache cache) {
		this(directory, null, cache);
	}

	public CachedTedInterface(String directory, NoticeCache oldCache,
			NoticeCache cache) {
		this.directory = checkNotNull(directory,
				"directory should not be null.");
		checkArgument(!directory.isEmpty(), "directory should not be blank.");
		this.oldCache = oldCache;
		this.cache = checkNotNull(cache);
	}

	@Override
	public synchronized TedResponse requestNoticeTab(NoticeID id,
			DisplayLanguage lang, Tab tab) throws TedError {
		checkNotNull(id, "id should not be null.");
		checkNotNull(lang, "lang should not be null.");
		checkNotNull(tab, "tab should not be null.");
		LOG.trace("Fetching {}:{}:{} from cache", id, lang, tab);
		String raw = null;
		if (null != oldCache) {
			raw = oldCache.fetch(id, lang, tab);
		}
		if (null == raw) {
			raw = cache.fetch(id, lang, tab);
		} else {
			LOG.trace("{}:{}:{} was in the old cache, moving to new cache", id,
					lang, tab);
			if (cache.store(id, lang, tab, raw)) {
				oldCache.remove(id, lang, tab);
			}
		}

		if (null != raw && raw.contains(getConf().getMustHaveContent())) {
			return new TedResponse(raw, Jsoup.parse(raw));
		} else {
			LOG.trace("{}:{}:{} was not in cache, calling TED interface", id,
					lang, tab);
			TedResponse r = super.requestNoticeTab(id, lang, tab);
			cache.store(id, lang, tab, r.getRawHTML());
			return r;
		}
	}

	public synchronized TedResponse requestNoticeTab(NoticeID id,
			DisplayLanguage lang, Tab tab, boolean forceDownload)
			throws TedError {
		return forceDownload ? super.requestNoticeTab(id, lang, tab) : this
				.requestNoticeTab(id, lang, tab);
	}

	@Override
	public String toString() {
		return "CachedTedInterface [directory=" + directory + ", getConf()="
				+ getConf() + "]";
	}
}
