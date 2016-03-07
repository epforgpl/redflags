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
package hu.petabyte.redflags.engine.gear.parser;

import static com.google.common.base.Preconditions.checkNotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import hu.petabyte.redflags.engine.gear.AbstractGear;
import hu.petabyte.redflags.engine.model.DisplayLanguage;
import hu.petabyte.redflags.engine.model.Notice;
import hu.petabyte.redflags.engine.model.Tab;
import hu.petabyte.redflags.engine.parser.Tab012Parser;
import hu.petabyte.redflags.engine.parser.Tab012ParserConfig;
import hu.petabyte.redflags.engine.parser.TemplateLoader;
import hu.petabyte.redflags.engine.tedintf.TedInterfaceHolder;

/**
 * @author Zsolt Jurányi
 */
@Component
public class TemplateBasedDocumentParser extends AbstractGear {

	private static final Logger LOG = LoggerFactory.getLogger(TemplateBasedDocumentParser.class);

	private @Value("${redflags.engine.gear.parse.lang:EN}") String rawLang;
	private DisplayLanguage lang;
	private @Autowired TedInterfaceHolder ted;
	private @Autowired Tab012ParserConfig tab012ParserConfig;

	@Override
	public void beforeSession() throws Exception {
		lang = DisplayLanguage.valueOf(checkNotNull(rawLang));
		LOG.debug("Parsing language is {}", lang);
	}

	public DisplayLanguage getLang() {
		return lang;
	}

	public Tab012ParserConfig getTab012ParserConfig() {
		return tab012ParserConfig;
	}

	public TedInterfaceHolder getTed() {
		return ted;
	}

	@Override
	protected Notice processImpl(Notice notice) throws Exception {

		// load template
		String t = TemplateLoader.getTemplateFor(notice, lang.toString());
		if (null == t) {
			LOG.warn("No template for {} / {}", notice.getData().getDocumentType().getId(), lang.toString());
			return notice;
		}

		// choose tab (0 or 1)
		String ol = notice.getData().getOriginalLanguage();
		Tab tab = lang.name().equalsIgnoreCase(ol) ? Tab.ORIGINAL_LANGUAGE : Tab.CURRENT_LANGUAGE;
		LOG.trace("Parsing {}:{} tab of {}", lang, tab, notice.getId());

		// load doc
		String d = ted.get().requestNoticeTab(notice.getId(), lang, tab).getRawHTML();
		if (null == d) {
			LOG.warn("Failed to fetch tab {}:{}:{}", notice.getId(), lang, tab);
			return notice;
		}

		// call parser thingy
		LOG.trace("Calling parser...");
		Tab012Parser p = new Tab012Parser(lang.toString(), tab012ParserConfig);
		p.parse(notice, d, t);

		return notice;
	}

	public void setLang(DisplayLanguage lang) {
		this.lang = lang;
	}

	public void setTab012ParserConfig(Tab012ParserConfig tab012ParserConfig) {
		this.tab012ParserConfig = tab012ParserConfig;
	}

	public void setTed(TedInterfaceHolder ted) {
		this.ted = ted;
	}

}
