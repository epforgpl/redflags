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

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * @author Zsolt Jurányi
 */
@Service
public class GearLoader {

	private static final Logger LOG = LoggerFactory.getLogger(GearLoader.class);

	private @Autowired ApplicationContext ctx;
	private @Autowired RedflagsEngineConfig config;

	public RedflagsEngineConfig getConfig() {
		return config;
	}

	public List<AbstractGear> loadGears() {
		return loadGears(config.getGears());
	}

	public AbstractGear loadGear(String gearName) {
		checkNotNull(gearName, "gearName should not be null.");
		checkArgument(!gearName.isEmpty(), "gearName should not be blank.");
		try {
			// LOG.trace("Finding gear class: {}", gearName);
			// Class<? extends AbstractGear> c = (Class<? extends AbstractGear>)
			// Class.forName(gearName);

			LOG.trace("Requesting bean for gear: {}", gearName);
			// AbstractGear gear = ctx.getBean(c);
			AbstractGear gear = (AbstractGear) ctx.getBean(gearName);

			return gear;
		} catch (Exception e) {
			LOG.error("Failed to load gear: {} - {}: {}", gearName, e
					.getClass().getSimpleName(), e.getMessage());
		}
		return null;
	}

	public List<AbstractGear> loadGears(List<String> requiredGears) {
		checkNotNull(requiredGears, "requiredGears should not be null.");
		checkArgument(!requiredGears.isEmpty(),
				"requiredGears should not be blank.");

		LOG.info("Loading gears");

		List<AbstractGear> gears = new ArrayList<AbstractGear>();
		for (String gearName : requiredGears) {

			if ("stopGear".equals(gearName)) {
				LOG.debug("stopGear found in list, skipping remaining gears to optimize startup");
				break;
			}

			AbstractGear gear = loadGear(gearName);
			if (null != gear) {
				gears.add(gear);
			}
		}

		LOG.info("{}/{} gears loaded", gears.size(), config.getGears().size());

		return gears;
	}

	public void setConfig(RedflagsEngineConfig config) {
		this.config = checkNotNull(config, "config should not be null.");
	}

}
