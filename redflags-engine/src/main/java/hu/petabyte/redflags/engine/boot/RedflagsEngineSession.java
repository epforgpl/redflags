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
package hu.petabyte.redflags.engine.boot;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import hu.petabyte.redflags.engine.gear.AbstractGear;
import hu.petabyte.redflags.engine.gear.GearTrain;
import hu.petabyte.redflags.engine.model.Notice;
import hu.petabyte.redflags.engine.model.NoticeID;
import hu.petabyte.redflags.engine.scope.AbstractScope;
import hu.petabyte.redflags.engine.util.TimeUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Zsolt Jurányi
 */
public class RedflagsEngineSession extends Thread {

	private static final Logger LOG = LoggerFactory
			.getLogger(RedflagsEngineSession.class);
	private static int N = 0;

	protected final AbstractScope scope;
	protected final List<AbstractGear> gears;
	protected final int threadCount;
	protected final boolean storeNotices;
	protected final List<Notice> notices = new ArrayList<Notice>();

	public RedflagsEngineSession(AbstractScope scope, List<AbstractGear> gears,
			int threadCount, boolean storeNotices) {
		super("rf-engine-" + (N++));
		this.scope = checkNotNull(scope, "scope should not be null.");
		this.gears = checkNotNull(gears, "gears should not be null.");
		checkArgument(!gears.isEmpty(), "gears should not be empty.");
		this.threadCount = threadCount;
		checkArgument(threadCount > 0);
		this.storeNotices = storeNotices;
	}

	public List<AbstractGear> getGears() {
		return gears;
	}

	public List<Notice> getNotices() {
		return notices;
	}

	public AbstractScope getScope() {
		return scope;
	}

	@Override
	public void run() {
		LOG.info("Starting Redflags engine session with scope {} (HEAP: {}M)",
				scope, Runtime.getRuntime().totalMemory() / 1024 / 1024);
		long t = -System.currentTimeMillis();

		// initialize gears
		LOG.debug("Initializing gears");
		try {
			for (AbstractGear gear : gears) {
				gear.beforeSession();
			}
		} catch (Exception e) {
			LOG.error("Gear initialization failed, session ends here.", e);
			// TODO ? should we call finalize on them ?
			return;
		} // TODO ? or just delete faulty gears ?

		// TODO maybe we can append a cleanup gear which turns notice 2 null

		// start gears
		LOG.debug("Starting gears");

		// create thread pool
		ExecutorService executor = Executors.newFixedThreadPool(threadCount);
		List<Future<Void>> futures = new ArrayList<Future<Void>>();

		// go thru notices
		for (NoticeID id : scope) {
			Notice notice = new Notice(id);

			// store notice in memory (if requested)
			if (storeNotices) {
				notices.add(notice);
				// XXX this way, dropped notices will remain in this list!
			}

			// start gear train for notice
			GearTrain gearTrain = new GearTrain(notice, gears);
			Future<Void> future = executor.submit(gearTrain);
			futures.add(future);
		}
		LOG.debug("{} threads submitted to executor", futures.size());

		for (int i = 0; i < futures.size(); i++) {
			Future<Void> future = futures.get(i);
			try {
				future.get();
			} catch (Exception e) {
			}
		}
		executor.shutdown();

		// finalize gears
		LOG.debug("Finalizing gears");
		for (AbstractGear gear : gears) {
			try {
				gear.afterSession();
			} catch (Exception e) {
				LOG.error("Gear finalization failed.", e);
			}
		}

		t += System.currentTimeMillis();
		LOG.info("Finished Redflags engine session with scope {} (HEAP: {}M)",
				scope, Runtime.getRuntime().totalMemory() / 1024 / 1024);
		LOG.info("Processed {} notices in {}", futures.size(),
				TimeUtils.readableTime(t));
	}

}
