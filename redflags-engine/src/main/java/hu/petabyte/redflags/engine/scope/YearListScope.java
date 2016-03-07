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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import hu.petabyte.redflags.engine.model.NoticeID;
import hu.petabyte.redflags.engine.tedintf.MaxNumberDeterminer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Iterables;

/**
 * @author Zsolt Jurányi
 */
public class YearListScope extends AbstractScope {

	private final Iterator<NoticeID> it;
	private final List<Integer> years;

	public YearListScope(List<Integer> years, MaxNumberDeterminer mnd) {
		checkNotNull(years, "years should not be null.");
		checkArgument(!years.isEmpty(), "years should not be empty.");
		checkNotNull(mnd, "mnd should not be null.");
		Collections.sort(years);
		this.years = years;
		List<SingleYearScope> singleYears = new ArrayList<SingleYearScope>();
		for (int y : years) {
			singleYears.add(new SingleYearScope(y, mnd));
		}
		it = Iterables.concat(singleYears).iterator(); // guava magic :-)
	}

	public List<Integer> getYears() {
		return years;
	}

	@Override
	public boolean hasNext() {
		return it.hasNext();
	}

	@Override
	public NoticeID next() {
		return it.next();
	}

	@Override
	public String toString() {
		return "YearListScope [years=" + years + "]";
	}

}
