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
package hu.petabyte.redflags.engine.gear.filter;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import hu.petabyte.redflags.engine.model.Notice;

/**
 * @author Zsolt Jurányi
 */
@Component
public class OriginalLanguageFilter extends AbstractFilter {

	private static final Logger LOG = LoggerFactory.getLogger(OriginalLanguageFilter.class);

	protected @Value("${redflags.engine.gear.filter.originalLanguage}") String rawOriginalLanguagePattern;
	protected Pattern originalLanguagePattern;

	@Override
	public boolean accept(Notice notice) throws Exception {
		String originalLanguage = notice.getData().getOriginalLanguage();
		return null != originalLanguage && originalLanguagePattern.matcher(originalLanguage).matches();
	}

	@Override
	public void beforeSession() throws Exception {
		originalLanguagePattern = Pattern.compile(checkNotNull(rawOriginalLanguagePattern));
		LOG.debug("Original language filter: {}", originalLanguagePattern.pattern());
	}

	public Pattern getOriginalLanguagePattern() {
		return originalLanguagePattern;
	}

	public String getRawOriginalLanguagePattern() {
		return rawOriginalLanguagePattern;
	}

	public void setOriginalLanguagePattern(Pattern originalLanguagePattern) {
		this.originalLanguagePattern = originalLanguagePattern;
	}

	public void setRawOriginalLanguagePattern(String rawOriginalLanguagePattern) {
		this.rawOriginalLanguagePattern = rawOriginalLanguagePattern;
	}

}
