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
package hu.petabyte.redflags.engine.gear.archiver;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import hu.petabyte.redflags.engine.gear.AbstractGear;
import hu.petabyte.redflags.engine.model.DisplayLanguage;
import hu.petabyte.redflags.engine.model.Notice;
import hu.petabyte.redflags.engine.model.Tab;
import hu.petabyte.redflags.engine.tedintf.TedInterfaceHolder;
import hu.petabyte.redflags.engine.tedintf.TedResponse;

/**
 * @author Zsolt Jurányi
 */
@Component
public class Archiver extends AbstractGear {

	private static final Logger LOG = LoggerFactory.getLogger(Archiver.class);

	protected @Value("${redflags.engine.gear.archive.langs}") String rawLangs;
	protected List<DisplayLanguage> langs = new ArrayList<DisplayLanguage>();
	protected @Autowired TedInterfaceHolder ted;

	@Override
	public void beforeSession() throws Exception {
		checkNotNull(rawLangs);
		for (String rawLang : rawLangs.split("[^A-Za-z]")) {
			try {
				langs.add(DisplayLanguage.valueOf(rawLang));
			} catch (Exception e) {
			}
		}
		checkArgument(!langs.isEmpty(), "langs should not be empty.");
		LOG.debug("Will archive notices in display languages: {}", langs);
	}

	public List<DisplayLanguage> getLangs() {
		return langs;
	}

	public String getRawLangs() {
		return rawLangs;
	}

	public TedInterfaceHolder getTed() {
		return ted;
	}

	@Override
	protected Notice processImpl(Notice notice) throws Exception {
		for (DisplayLanguage lang : langs) {

			// get data tab
			TedResponse r = ted.get().requestNoticeTabQuietly(notice.getId(), lang, Tab.DATA);

			// get other tabs
			if (null != r) {
				Document d = r.getParsedDocument();
				for (Tab tab : Tab.values()) {
					if (tab != Tab.DATA && !d.select("a[href~=tabId=" + tab.getId()).isEmpty()) {
						LOG.trace("Found tab: {}:{}:{}, fetching", notice.getId(), lang, tab);
						ted.get().requestNoticeTabQuietly(notice.getId(), lang, tab);
					}
				}
			}
		}
		return notice;
	}

	public void setLangs(List<DisplayLanguage> langs) {
		this.langs = langs;
	}

	public void setRawLangs(String rawLangs) {
		this.rawLangs = rawLangs;
	}

	public void setTed(TedInterfaceHolder ted) {
		this.ted = ted;
	}

}
