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
package hu.petabyte.redflags.engine.test;

import hu.petabyte.redflags.engine.RedflagsEngineApp;
import hu.petabyte.redflags.engine.tedintf.TedInterface;
import hu.petabyte.redflags.engine.tedintf.TedInterfaceHolder;
import hu.petabyte.redflags.engine.tedintf.cached.CachedTedInterface;
import hu.petabyte.redflags.engine.test.helper.ProcessedNoticeProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.ConfigFileApplicationContextInitializer;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Zsolt Jurányi
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RedflagsEngineApp.class)
@ContextConfiguration(classes = RedflagsEngineApp.class, initializers = ConfigFileApplicationContextInitializer.class)
@ActiveProfiles("test")
public class TempTest {

	private static final TedInterface tedintf = new CachedTedInterface(
			"temp-test-cache");
	private @Autowired TedInterfaceHolder ted;
	private @Autowired ProcessedNoticeProvider processedNoticeProvider;

	@Before
	public void _init() {
		ted.set(tedintf);
	}

	@Test
	public void test() throws InterruptedException {
		// processedNoticeProvider.notice("185291-2016");
	}
}
