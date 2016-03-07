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
package hu.petabyte.redflags.engine.test.helper;

import hu.petabyte.redflags.engine.boot.GearLoader;
import hu.petabyte.redflags.engine.boot.RedflagsEngineSession;
import hu.petabyte.redflags.engine.model.Notice;
import hu.petabyte.redflags.engine.scope.AbstractScope;
import hu.petabyte.redflags.engine.scope.ScopeProvider;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Zsolt Jurányi
 */
@Service
public class ProcessedNoticeProvider {

	private @Autowired GearLoader gearLoader;
	private @Autowired ScopeProvider scopeProvider;

	public Notice notice(String id) throws InterruptedException {
		return notices(id).get(0);
	}

	public List<Notice> notices(String rawScope) throws InterruptedException {
		AbstractScope scope = scopeProvider.scope(rawScope);
		RedflagsEngineSession session = new RedflagsEngineSession(scope,
				gearLoader.loadGears(), 1, true);
		session.start();
		session.join();
		return session.getNotices();
	}

}
