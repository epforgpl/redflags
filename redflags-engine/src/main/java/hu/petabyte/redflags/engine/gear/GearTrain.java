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
package hu.petabyte.redflags.engine.gear;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import hu.petabyte.redflags.engine.model.Notice;
import hu.petabyte.redflags.engine.model.NoticeID;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Zsolt Jurányi
 */
public class GearTrain implements Callable<Void> {

	private static final Logger LOG = LoggerFactory.getLogger(GearTrain.class);

	private Notice notice;
	private final List<? extends AbstractGear> gears;

	public GearTrain(Notice notice, List<? extends AbstractGear> gears) {
		this.notice = checkNotNull(notice, "notice should not be null.");
		this.gears = checkNotNull(gears, "gears should not be null.");
		checkArgument(!gears.isEmpty(), "gears should not be empty.");
	}

	@Override
	public Void call() throws Exception {
		LOG.info("Processing notice {} (HEAP: {}M)", notice.getId(), Runtime
				.getRuntime().totalMemory() / 1024 / 1024);
		for (Iterator<? extends AbstractGear> iterator = gears.iterator(); iterator
				.hasNext() && null != notice;) {
			AbstractGear gear = iterator.next();
			LOG.trace("Notice {} is at gear {}", notice.getId(), gear
					.getClass().getSimpleName());
			NoticeID id = notice.getId();
			try {
				notice = gear.process(notice);
			} catch (Exception e) {
				String s = String.format(
						"%s gear on notice %s threw exception: %s: %s", gear
								.getClass().getSimpleName(), id.toString(), e
								.getClass().getSimpleName(), e.getMessage());
				LOG.debug(s);
				LOG.trace(s, e);
			}
			if (null == notice) {
				LOG.debug("Notice {} dropped by {}", id, gear.getClass()
						.getSimpleName());
			}
		}
		if (null != notice) {
			LOG.debug("Processing of notice {} finished", notice.getId());
		}
		return null;
	}

	public List<? extends AbstractGear> getGears() {
		return gears;
	}

	public Notice getNotice() {
		return notice;
	}

}
