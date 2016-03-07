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

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Zsolt Jurányi
 */
@Component
@ConfigurationProperties(prefix = "countOfInvOpsLowIndicator")
public class CountOfInvOpsLowIndicator extends AbstractTD3CIndicator {

	private byte minWhenNegotiated = 3; // tárgyalásos
	private byte minWhenRestricted = 5; // meghívásos

	@Override
	protected IndicatorResult flagImpl(Notice notice) {
		String procType = fetchProcedureType(notice);
		if (!procType.matches("PR-[2346CT]")) {
			return irrelevantData();
		}
		int c = 0;
		if (null != notice.getProc()) {
			c = notice.getProc().getCountOfInvitedOperators();
		}
		if (0 == c) {
			return irrelevantData();
		}

		if (procType.matches("PR-[46CT]") && c < minWhenNegotiated) {
			return returnFlag("infoNegot", "count=" + c, "min="
					+ minWhenNegotiated);
		}

		if (procType.matches("PR-[23]") && c < minWhenRestricted) {
			return returnFlag("infoNegot", "count=" + c, "min="
					+ minWhenRestricted);
		}
		return null;
	}

	public byte getMinWhenNegotiated() {
		return minWhenNegotiated;
	}

	public byte getMinWhenRestricted() {
		return minWhenRestricted;
	}

	public void setMinWhenNegotiated(byte minWhenNegotiated) {
		this.minWhenNegotiated = minWhenNegotiated;
	}

	public void setMinWhenRestricted(byte minWhenRestricted) {
		this.minWhenRestricted = minWhenRestricted;
	}

}
