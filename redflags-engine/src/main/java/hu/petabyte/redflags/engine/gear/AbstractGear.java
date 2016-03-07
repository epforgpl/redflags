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

import static com.google.common.base.Preconditions.checkNotNull;
import hu.petabyte.redflags.engine.model.Notice;

// TODO doc: add @Component. gears are singletons by default. if u want 2 use
// instance variables, use @Scope("prototype"). for initialization, override
// beforeSession instead of using PostConstruct or InitializingBean

/**
 * @author Zsolt Jurányi
 */
public abstract class AbstractGear {

	public void afterSession() throws Exception {
	}

	public void beforeSession() throws Exception {
	}

	public final Notice process(Notice notice) throws Exception {
		checkNotNull(notice, "notice should not be null.");
		return processImpl(notice);
	}

	protected abstract Notice processImpl(Notice notice) throws Exception;

}
