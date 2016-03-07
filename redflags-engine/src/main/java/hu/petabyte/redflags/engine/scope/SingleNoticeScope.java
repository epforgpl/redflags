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

import java.util.NoSuchElementException;

import hu.petabyte.redflags.engine.model.NoticeID;

/**
 * @author Zsolt Jurányi
 */
public class SingleNoticeScope extends AbstractScope {

	private final NoticeID id;
	private boolean taken = false;

	public SingleNoticeScope(NoticeID id) {
		this.id = checkNotNull(id, "id shoult not be null.");
	}

	public boolean hasNext() {
		return !taken;
	}

	public NoticeID next() {
		if (taken) {
			throw new NoSuchElementException();
		}
		taken = true;
		return id;
	}

	@Override
	public String toString() {
		return "SingleNoticeScope [id=" + id + "]";
	}

}
