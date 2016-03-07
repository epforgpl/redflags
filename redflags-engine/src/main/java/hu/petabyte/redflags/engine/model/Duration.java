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
package hu.petabyte.redflags.engine.model;

import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Zsolt Jurányi
 *
 */
public class Duration {

	private Date begin;
	private Date end;
	private String id;
	private int inDays;
	private int inMonths;
	private String raw;

	public Duration() {
		// needed by BeanWrapper
	}

	public Duration fill() {
		return fill(null);
	}

	public Duration fill(Date defaultStartDate) {

		final int DAYS_IN_MONTH = 30;

		// THERE'S MONTH -> CALC DAYS

		if (0 < getInMonths()) {
			setInDays(getInMonths() * DAYS_IN_MONTH);
			return this;
		}

		// NO MONTH -> CALC FROM DAYS

		if (0 < getInDays()) {
			setInMonths(getInDays() / DAYS_IN_MONTH);
			return this;
		}

		// NO MONTH, NO DAYS -> CALC FROM DATES

		if (null == getBegin()) {
			setBegin(defaultStartDate);
		}
		if (null != getBegin() && null != getEnd()) {
			// long diff = getEnd().getTime() - getBegin().getTime();
			// long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
			Calendar cBegin = Calendar.getInstance();
			cBegin.setTime(getBegin());
			Calendar cEnd = Calendar.getInstance();
			cEnd.setTime(getEnd());
			int days = 0;
			while (cBegin.before(cEnd)) {
				days++;
				cBegin.add(Calendar.DATE, 1);
			}
			setInDays(days);
			setInMonths(days / DAYS_IN_MONTH);
			return this;
		}

		// EMPTY DURATION -> RETURN

		return this;
	}

	public Date getBegin() {
		return begin;
	}

	public Date getEnd() {
		return end;
	}

	public String getId() {
		return id;
	}

	public int getInDays() {
		return inDays;
	}

	public int getInMonths() {
		return inMonths;
	}

	public String getRaw() {
		return raw;
	}

	public void setBegin(Date begin) {
		this.begin = begin;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setInDays(int inDays) {
		this.inDays = inDays;
	}

	public void setInMonths(int inMonths) {
		this.inMonths = inMonths;
	}

	public void setRaw(String raw) {
		this.raw = raw;
	}

	@Override
	public String toString() {
		return "Duration [begin=" + begin + ", end=" + end + ", inDays=" + inDays + ", inMonths=" + inMonths + "]";
	}

}
