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
package hu.petabyte.redflags.engine.gear.indicator.pl;

import hu.petabyte.redflags.engine.gear.indicator.AbstractIndicator;
import hu.petabyte.redflags.engine.model.IndicatorResult;
import hu.petabyte.redflags.engine.model.Notice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BZP2013Indicator extends AbstractIndicator {

	private static final Logger LOG = LoggerFactory.getLogger(BZP2013Indicator.class);

	@Override
	public final IndicatorResult flag(Notice notice) {
		IndicatorResult ir =  flagImpl(notice);
		if(ir!=null){
			LOG.warn("notice flagged: " + notice.getId().toString() + ", result: " + ir);
		}
		return ir;
	}

	protected abstract IndicatorResult flagImpl(Notice notice);

	protected IndicatorResult returnFlag(String label, String... args){
		LOG.info("Flagged: " + label + " args: " + args);
		return super.returnFlag(label, args);
	}

}
