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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

/**
 * @author Zsolt Jurányi
 */
public class NoticeRangeScope extends AbstractScope {

	private static class NoticeRangePredicate implements Predicate<NoticeID> {

		private final NoticeID first, last;

		public NoticeRangePredicate(NoticeID first, NoticeID last) {
			if (first.compareTo(last) > 0) {
				NoticeID t = first;
				first = last;
				last = t;
			}
			this.first = first;
			this.last = last;
		}

		@Override
		public boolean apply(NoticeID id) {
			boolean b1 = first.compareTo(id) <= 0;
			boolean b2 = id.compareTo(last) <= 0;
			return b1 && b2;
		}
	}

	private final NoticeRangePredicate noticeRange;
	private final Iterator<NoticeID> it;

	public NoticeRangeScope(NoticeID first, NoticeID last,
			MaxNumberDeterminer mnd) {
		checkNotNull(first, "first should not be null.");
		checkNotNull(last, "last should not be null.");
		checkNotNull(mnd, "mnd should not be null.");
		this.noticeRange = new NoticeRangePredicate(first, last);
		List<Integer> years = new ArrayList<Integer>();
		for (int y = first.year(); y <= last.year(); y++) {
			years.add(y);
		}
		this.it = Iterables.filter(new YearListScope(years, mnd), noticeRange)
				.iterator(); // guava magic :-)
	}

	public NoticeID getFirst() {
		return noticeRange.first;
	}

	public NoticeID getLast() {
		return noticeRange.last;
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
		return "NoticeRangeScope [first=" + noticeRange.first + ", last="
				+ noticeRange.last + "]";
	}

}
