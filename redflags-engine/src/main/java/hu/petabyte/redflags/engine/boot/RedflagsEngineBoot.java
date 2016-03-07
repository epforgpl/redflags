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

import hu.petabyte.redflags.engine.RedflagsEngineApp;
import hu.petabyte.redflags.engine.gear.AbstractGear;
import hu.petabyte.redflags.engine.scope.AbstractScope;
import hu.petabyte.redflags.engine.scope.ScopeProvider;
import hu.petabyte.redflags.engine.tedintf.TedInterfaceHolder;
import hu.petabyte.redflags.engine.tedintf.cached.CachedTedInterface;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

/**
 * @author Zsolt Jurányi
 */
@Service
public class RedflagsEngineBoot implements InitializingBean, DisposableBean {

	private static final Logger LOG = LoggerFactory
			.getLogger(RedflagsEngineBoot.class);

	protected @Autowired Environment env;
	protected @Autowired RedflagsEngineConfig config;
	protected @Autowired ScopeProvider scopeProvider;
	protected @Autowired GearLoader gearLoader;
	protected @Autowired TedInterfaceHolder tedInterface;
	protected Thread engineSessionThread;

	// protected Thread heapSizeLogger = new HeapSizeLogger();

	@Override
	public void afterPropertiesSet() throws Exception {

		// if we r in a test case, skip boot and main session

		for (String profile : env.getActiveProfiles()) {
			if ("test".equals(profile)) {
				LOG.warn("'test' profile is active, boot cancelled");
				return;
			}
		}

		// otherwise, let's gather session parameters

		AbstractScope scope;
		try {
			scope = scopeProvider.scope(config.getScope());
		} catch (IllegalArgumentException e) {
			RedflagsEngineApp.printArgsHelp();
			return;
		}
		LOG.debug("Scope: {}", scope);

		LOG.debug("Thread count: {} (processors: {})", config.getThreadCount(),
				Runtime.getRuntime().availableProcessors());

		tedInterface.set(new CachedTedInterface(config.getCacheDirectory()));
		tedInterface.get().setConf(config.getTedinterface());
		LOG.debug("TED Interface: {}", tedInterface.get());

		List<AbstractGear> gears = gearLoader.loadGears();
		LOG.debug("Gear train:", gears.size());
		for (int i = 0; i < gears.size(); i++) {
			AbstractGear gear = gears.get(i);
			LOG.debug("  #{} {}", i + 1, gear.getClass().getName());
		}

		LOG.trace("Starting main session");
		engineSessionThread = new RedflagsEngineSession(scope, gears,
				config.getThreadCount(), false);
		engineSessionThread.start();
	}

	@Override
	public void destroy() throws Exception {
		Thread.currentThread().setName("main-destroy");
		if (null != engineSessionThread) {
			engineSessionThread.join();
		}
		LOG.info("*** REDFLAGS ENGINE - Stopping");
	}

}
