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
package hu.petabyte.redflags.engine.gear.indicator.hu;

import hu.petabyte.redflags.engine.gear.indicator.AbstractTD3CIndicator;
import hu.petabyte.redflags.engine.model.IndicatorResult;
import hu.petabyte.redflags.engine.model.Notice;

import java.util.Date;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Ajánlatok felbontási feltételei - időpont - a Kbt. alapján ez az időpont
 * azonos kell hogy legyen az ajánlattételi/részvételi határidő lejártával
 * (ilyen megkötés az uniós irányelvben nincs)
 *
 * @author Zsolt Jurányi
 *
 */
@Component
@ConfigurationProperties(prefix = "openingDateDiffersFromDeadlineIndicator")
public class OpeningDateDiffersFromDeadlineIndicator extends
		AbstractTD3CIndicator {

	@Override
	protected IndicatorResult flagImpl(Notice notice) {
		Date deadline = notice.getData().getDeadline();
		Date openingDate = null;
		if (null != notice.getProc()) {
			openingDate = notice.getProc().getOpeningDate();
		}

		if (null != openingDate && null != deadline
				&& !openingDate.equals(deadline)) {
			return returnFlag();
		}
		return null;
	}

}
