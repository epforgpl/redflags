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
package hu.petabyte.redflags.engine.scope;

import static com.google.common.base.Preconditions.checkNotNull;
import hu.petabyte.redflags.engine.model.NoticeID;
import hu.petabyte.redflags.engine.tedintf.MaxNumberDeterminer;

import java.util.NoSuchElementException;

/**
 * @author Zsolt Jurányi
 */
public class SingleYearScope extends AbstractScope {

	private final int year;
	private final MaxNumberDeterminer mnd;
	private int maxNumber = -1;
	private int currentNumber = 0;

	public SingleYearScope(int year, MaxNumberDeterminer mnd) {
		this.year = year;
		this.mnd = checkNotNull(mnd, "mnd should not be null.");
	}

	public int getYear() {
		return year;
	}

	@Override
	public boolean hasNext() {
		if (-1 == maxNumber) {
			maxNumber = mnd.maxNumberForYear(year);
		}
		return currentNumber < maxNumber;
	}

	@Override
	public NoticeID next() {
		if (!hasNext()) {
			throw new NoSuchElementException();
		}
		currentNumber++;
		return new NoticeID(currentNumber, year);
	}

	@Override
	public String toString() {
		return "SingleYearScope [year=" + year + "]";
	}

}
