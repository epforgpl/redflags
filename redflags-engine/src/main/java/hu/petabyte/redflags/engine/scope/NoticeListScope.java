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

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @author Zsolt Jurányi
 */
public class NoticeListScope extends AbstractScope {

	private final List<NoticeID> ids;
	private final Iterator<NoticeID> it;

	public NoticeListScope(List<NoticeID> ids) {
		this.ids = checkNotNull(ids, "ids should not be null.");
		checkArgument(!ids.isEmpty(), "ids shoult not be empty.");
		Collections.sort(ids);
		this.it = ids.iterator();
	}

	public List<NoticeID> getIds() {
		return ids;
	}

	public boolean hasNext() {
		return it.hasNext();
	}

	public NoticeID next() {
		return it.next();
	}

	@Override
	public String toString() {
		return "NoticeListScope [ids.size=" + ids.size() + "]";
	}

}
