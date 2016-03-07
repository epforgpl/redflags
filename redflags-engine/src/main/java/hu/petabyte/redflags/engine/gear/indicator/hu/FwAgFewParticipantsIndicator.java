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
import hu.petabyte.redflags.engine.model.noticeparts.ObjOfTheContract;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Zsolt Jurányi
 */
@Component
@ConfigurationProperties(prefix = "fwAgFewParticipantsIndicator")
public class FwAgFewParticipantsIndicator extends AbstractTD3CIndicator {

	private byte minCount = 3;
	private String moreParticipantsSentence = "Keretmegállapodás több ajánlattevővel";

	@Override
	protected IndicatorResult flagImpl(Notice notice) {
		boolean found = false;
		for (ObjOfTheContract obj : notice.getObjs()) {
			String s = obj.getFrameworkAgreement();
			if (null != s) {
				found = true;
				if (s.contains(moreParticipantsSentence)
						&& obj.getFrameworkParticipants() < minCount) {
					return returnFlag("info",
							"count=" + obj.getFrameworkParticipants(), "min="
									+ minCount);
				}
			}
		}
		if (!found) {
			return irrelevantData();
		}
		return null;
	}

	public byte getMinCount() {
		return minCount;
	}

	public String getMoreParticipantsSentence() {
		return moreParticipantsSentence;
	}

	public void setMinCount(byte minCount) {
		this.minCount = minCount;
	}

	public void setMoreParticipantsSentence(String moreParticipantsSentence) {
		this.moreParticipantsSentence = moreParticipantsSentence;
	}

}
