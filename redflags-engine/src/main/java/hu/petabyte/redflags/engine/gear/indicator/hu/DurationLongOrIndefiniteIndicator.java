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
import hu.petabyte.redflags.engine.model.CPV;
import hu.petabyte.redflags.engine.model.Duration;
import hu.petabyte.redflags.engine.model.IndicatorResult;
import hu.petabyte.redflags.engine.model.Notice;
import hu.petabyte.redflags.engine.model.noticeparts.ObjOfTheContract;

import java.util.regex.Pattern;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Zsolt Jurányi
 */
@Component
@ConfigurationProperties(prefix = "durationLongOrIndefiniteIndicator")
public class DurationLongOrIndefiniteIndicator extends AbstractTD3CIndicator {

	private byte maxYears = 4;
	private int compensationMonths = 12;
	private Pattern exceptionCPVPattern = Pattern
			.compile("66000000|66100000|66110000|66113000|66113100|66114000|66517100");
	private Pattern indefinitePattern = Pattern.compile("határozatlan",
			Pattern.CASE_INSENSITIVE);

	@Override
	protected IndicatorResult flagImpl(Notice notice) {
		for (ObjOfTheContract obj : notice.getObjs()) {
			if (null != obj.getDuration()) {
				if (indefinitePattern.matcher(obj.getDuration().getRaw())
						.find()) {
					return returnFlag("infoIndef");
				}

				Duration d = new Duration();
				d.setBegin(obj.getDuration().getBegin());
				d.setEnd(obj.getDuration().getEnd());
				d.setInDays(obj.getDuration().getInDays());
				d.setInMonths(obj.getDuration().getInMonths());

				if (null != d.getBegin() && null == d.getEnd()) {
					return returnFlag("infoIndef2");
				}

				if (null != exceptionCPVPattern && null != notice.getData()
						&& null != notice.getData().getCpvCodes()) {
					for (CPV cpv : notice.getData().getCpvCodes()) {
						String c = Integer.toString(cpv.getId());
						if (exceptionCPVPattern.matcher(c).find()) {
							return null;
						}
					}
				}

				int delta = 0;
				if (null == d.getBegin() && null != d.getEnd()) {
					d.fill(notice.getData().getPublicationDate());
					delta += compensationMonths;
				}

				long m = d.getInMonths();
				long max = maxYears * 12 + delta;
				if (max < m) {
					String label = delta > 0 ? "info2" : "info";
					return returnFlag(label, "months=" + m, "maxYears="
							+ maxYears, "comp" + compensationMonths);
				}
			}
		}
		return null;
	}

	public int getCompensationMonths() {
		return compensationMonths;
	}

	public Pattern getExceptionCPVPattern() {
		return exceptionCPVPattern;
	}

	public Pattern getIndefinitePattern() {
		return indefinitePattern;
	}

	public byte getMaxYears() {
		return maxYears;
	}

	public void setCompensationMonths(int compensationMonths) {
		this.compensationMonths = compensationMonths;
	}

	public void setExceptionCPVPattern(Pattern exceptionCPVPattern) {
		this.exceptionCPVPattern = exceptionCPVPattern;
	}

	public void setIndefinitePattern(Pattern indefinitePattern) {
		this.indefinitePattern = indefinitePattern;
	}

	public void setMaxYears(byte maxYears) {
		this.maxYears = maxYears;
	}

}
