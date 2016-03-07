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

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

/**
 * Defines an automatic scope. Basically it's a notice range scope, but reads
 * the first notice from a file and the last notice is 999999-YYYY, where YYYY
 * is the current year. If it fails to load the previously saved ID, 1-YYYY will
 * be used. It updates the file everytime <code>next()</code> is called.
 *
 * @author Zsolt Jurányi
 * @see NoticeRangeScope
 *
 */
public class AutoScope extends AbstractScope {

	public static final String FILENAME = "redflags-engine-auto.last-id";
	private static final Logger LOG = LoggerFactory.getLogger(AutoScope.class);

	private final NoticeID first;
	private final Iterator<NoticeID> it;
	private final NoticeID last;

	public AutoScope(MaxNumberDeterminer mnd) {
		checkNotNull(mnd, "mnd should not be null.");
		this.first = load();
		this.last = new NoticeID(999999, Calendar.getInstance().get(
				Calendar.YEAR));
		this.it = new NoticeRangeScope(first, last, mnd);
	}

	public NoticeID getFirst() {
		return first;
	}

	public NoticeID getLast() {
		return last;
	}

	@Override
	public boolean hasNext() {
		return it.hasNext();
	}

	private NoticeID load() {
		try {
			String s = Files.toString(new File(FILENAME), Charsets.UTF_8);
			String nid = s.split("\n")[0].trim();
			LOG.trace("Loaded notice id: {}", nid);
			return new NoticeID(nid);
		} catch (IOException e) {
			NoticeID id = new NoticeID(1, Calendar.getInstance().get(
					Calendar.YEAR));
			LOG.debug("Couldn't load last notice id, using {} as first", id);
			return id;
		}
	}

	@Override
	public NoticeID next() {
		NoticeID current = it.next();
		save(current);
		return current;
	}

	private void save(NoticeID id) {
		try {
			Files.write(id.toString(), new File(FILENAME), Charsets.UTF_8);
		} catch (IOException e) {
			LOG.warn("Failed to save notice id: {}", e.getMessage());
			LOG.trace("Failed to save notice id", e);
		}
	}

	@Override
	public String toString() {
		return "AutoScope [first=" + first + ", last=" + last + "]";
	}

}
