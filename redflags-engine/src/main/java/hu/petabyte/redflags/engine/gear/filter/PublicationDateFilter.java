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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import hu.petabyte.redflags.engine.model.Notice;

/**
 * @author Zsolt Jurányi
 */
@Component
public class PublicationDateFilter extends AbstractFilter {

	private static final Logger LOG = LoggerFactory.getLogger(PublicationDateFilter.class);

	protected @Value("${redflags.engine.gear.filter.publicationDateMin}") String rawMinDate;
	protected Date minDate;

	@Override
	public boolean accept(Notice notice) throws Exception {
		if (null == notice.getData().getPublicationDate()) {
			return false;
		}

		Calendar noticeDateCal = Calendar.getInstance();
		noticeDateCal.setTime(notice.getData().getPublicationDate());

		Calendar minDateCal = Calendar.getInstance();
		minDateCal.setTime(minDate);

		return !noticeDateCal.before(minDateCal);
	}

	@Override
	public void beforeSession() throws Exception {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		minDate = df.parse(rawMinDate);
		LOG.debug("Notices published before {} will be dropped", df.format(minDate));
	}

	public Date getMinDate() {
		return minDate;
	}

	public String getRawMinDate() {
		return rawMinDate;
	}

	public void setMinDate(Date minDate) {
		this.minDate = minDate;
	}

	public void setRawMinDate(String rawMinDate) {
		this.rawMinDate = rawMinDate;
	}

}
