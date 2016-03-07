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
package hu.petabyte.redflags.engine.tedintf.cached;

import hu.petabyte.redflags.engine.model.DisplayLanguage;
import hu.petabyte.redflags.engine.model.NoticeID;
import hu.petabyte.redflags.engine.model.Tab;

/**
 * Describes what should a notice cache provide.
 *
 * @author Zsolt Jurányi
 *
 */
public interface NoticeCache {

	/**
	 * Retrieves the specified document from the cache.
	 *
	 * @param id
	 *            Identifier of the notice.
	 * @param lang
	 *            The display language.
	 * @param tab
	 *            The requested tab.
	 * @return The saved document (HTML) or <code>null</code> if it was not in
	 *         the cache.
	 */
	String fetch(NoticeID id, DisplayLanguage lang, Tab tab);

	/**
	 * Remove the specified document from the cache.
	 *
	 * @param id
	 *            Identifier of the notice.
	 * @param lang
	 *            The display language.
	 * @param tab
	 *            The requested tab.
	 */
	void remove(NoticeID id, DisplayLanguage lang, Tab tab);

	/**
	 * Stores the specified document in the cache.
	 *
	 * @param id
	 *            Identifier of the notice.
	 * @param lang
	 *            The display language.
	 * @param tab
	 *            The requested tab.
	 * @param raw
	 *            The document (raw HTML) to save.
	 * @return <code>true</code> if it stored successfully or <code>false</code>
	 *         if it failed.
	 */
	boolean store(NoticeID id, DisplayLanguage lang, Tab tab, String raw);
}
