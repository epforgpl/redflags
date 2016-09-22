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

import static com.google.common.base.Preconditions.checkArgument;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Zsolt Jurányi
 */
public class NoticeID implements Comparable<NoticeID> {

	private static final int MIN_NUMBER = 1;
	private static final int MAX_NUMBER = 999999;
	private static final int MIN_YEAR = 2000;
	private static final int MAX_YEAR = Calendar.getInstance().get(Calendar.YEAR);
	private static final Pattern ID_PATTERN = Pattern.compile("^(?<n>\\d{1,6})-(?<y>\\d{4})$");
	private static final int PAD = 1000000;

	private static int calculateID(int number, int year) {
		return validateYear(year) * PAD + validateNumber(number);
	}

	private static int validateNumber(int n) {
		checkArgument(n >= MIN_NUMBER, "Number should be at least %s. Your is %s", MIN_NUMBER, n);
		checkArgument(n <= MAX_NUMBER, "Number should be at most %s. Your is %s", MAX_NUMBER, n);
		return n;
	}

	public String calculateURL(){
		return String.format("http://ted.europa.eu/udl?uri=TED:NOTICE:%d-%d:TEXT:EN:HTML&tabId=1", this.number(),this.year());
	}

	private static int validateStringID(String s) {
		Matcher m = ID_PATTERN.matcher(s);
		checkArgument(m.find(),
				"String ID should have the format 'number-year', where number length is between 1 and 6, and year is always 4 characters long. Yours: " + s);
		return calculateID(Integer.parseInt(m.group("n")), Integer.parseInt(m.group("y")));
	}

	private static int validateYear(int y) {
		checkArgument(y >= MIN_YEAR, "Year should be at least %s.", MIN_YEAR);
		checkArgument(y <= MAX_YEAR, "Year should be at most %s.", MAX_YEAR);
		return y;
	}

	protected final int id;

	private final String lang;

	public NoticeID(int id) {
		this(id, "HU");
	}

	public NoticeID(int id, String lang) {
		this.id = id;
		this.lang = lang;
	}

	public String getLang() {
		return lang;
	}

	public NoticeID(int number, int year) {
		this(calculateID(number, year));
	}

	public NoticeID(String s) {
		this(validateStringID(s));
	}

	public int compareTo(NoticeID that) {
		return Integer.compare(this.get(), that.get());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NoticeID other = (NoticeID) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public int get() {
		return id;
	}

	@Override
	public int hashCode() {
		return id;
	}

	public int number() {
		return get() % PAD;
	}

	@Override
	public String toString() {
		return String.format("%d-%d", number(), year());
	}

	public int year() {
		return get() / PAD;
	}

}
