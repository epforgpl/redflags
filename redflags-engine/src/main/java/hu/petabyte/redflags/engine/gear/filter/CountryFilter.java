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

import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import hu.petabyte.redflags.engine.model.Notice;

/**
 * Filters notices by country. The input notice must be processed before by
 * <code>MetadataParser</code>.
 *
 * @author Zsolt Jurányi
 * @see hu.petabyte.redflags.engine.model.Notice
 * @see hu.petabyte.redflags.engine.model.Data
 * @see hu.petabyte.redflags.engine.gear.parser.MetadataParser
 *
 */
@Component
public class CountryFilter extends AbstractFilter {

	private static final Logger LOG = LoggerFactory.getLogger(CountryFilter.class);

	/**
	 * Raw country pattern. The value of
	 * <code>redflags.engine.gear.filter.country</code> property will be injected
	 * automatically when the gear is loaded.
	 */
	protected @Value("${redflags.engine.gear.filter.country}") String rawCountryPattern;
	/**
	 * Parsed country pattern.
	 */
	protected Pattern countryPattern;

	/**
	 * Accepts notices where the country value matches the country pattern.
	 */
	@Override
	public boolean accept(Notice notice) throws Exception {
		String country = notice.getData().getCountry();
		return null != country && countryPattern.matcher(country).matches();
	}

	/**
	 * Parses raw country pattern. This method will be called automatically
	 * before the session starts.
	 */
	@Override
	public void beforeSession() throws Exception {
		countryPattern = Pattern.compile(rawCountryPattern);
		LOG.debug("Country filter: {}", countryPattern.pattern());
	}

	/**
	 * Parsed country pattern.
	 *
	 * @return Parsed country pattern.
	 */
	public Pattern getCountryPattern() {
		return countryPattern;
	}

	/**
	 * Raw country pattern.
	 *
	 * @return Raw country pattern.
	 */
	public String getRawCountryPattern() {
		return rawCountryPattern;
	}

	/**
	 * Sets parsed country pattern.
	 *
	 * @param countryPattern
	 *            Parsed country pattern.
	 */
	public void setCountryPattern(Pattern countryPattern) {
		this.countryPattern = countryPattern;
	}

	/**
	 * Sets raw country pattern.
	 *
	 * @param rawCountryPattern
	 *            Raw country pattern.
	 */
	public void setRawCountryPattern(String rawCountryPattern) {
		this.rawCountryPattern = rawCountryPattern;
	}

}
