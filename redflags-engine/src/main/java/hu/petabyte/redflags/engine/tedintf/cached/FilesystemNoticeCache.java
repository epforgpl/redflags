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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import hu.petabyte.redflags.engine.model.DisplayLanguage;
import hu.petabyte.redflags.engine.model.NoticeID;
import hu.petabyte.redflags.engine.model.Tab;

/**
 * @author Zsolt Jurányi
 */
public abstract class FilesystemNoticeCache implements NoticeCache {

	protected final String directory;

	public FilesystemNoticeCache(String directory) {
		this.directory = checkNotNull(directory, "directory should not be null.");
		checkArgument(!directory.isEmpty(), "directory should not be blank.");
	}

	public abstract String filename(NoticeID id, DisplayLanguage lang, Tab tab);

	public String getDirectory() {
		return directory;
	}

}
