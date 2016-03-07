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

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Zsolt Jurányi
 */
public class DirectoryScope extends AbstractScope {

	private static class NoticeDirectoryComparator implements Comparator<File> {

		@Override
		public int compare(File f1, File f2) {
			int i1 = Integer.valueOf(f1.getName());
			int i2 = Integer.valueOf(f2.getName());
			return i1 - i2;
		}

	}

	private static class NoticeDirectoryFilter implements FileFilter {

		@Override
		public boolean accept(File f) {
			return f.isDirectory() && f.getName().matches("\\d+");
		}

	}

	private static class YearDirectoryFilter implements FileFilter {

		@Override
		public boolean accept(File f) {
			return f.isDirectory() && f.getName().matches("\\d{4}");
		}

	}

	private static final Logger LOG = LoggerFactory
			.getLogger(DirectoryScope.class);

	private final File dir;
	private List<File> years = new ArrayList<File>();
	private List<File> notices = new ArrayList<File>();

	public DirectoryScope(String directory) {
		this.dir = new File(checkNotNull(directory,
				"directory should not be null."));
		checkArgument(!directory.isEmpty(), "directory should not be blank.");
		checkArgument(this.dir.exists() && this.dir.isDirectory(),
				"directory should name an existing directory in the filesystem.");

		LOG.debug("Enumerating year directories in {}",
				this.dir.getAbsolutePath());
		Collections
				.addAll(years, this.dir.listFiles(new YearDirectoryFilter()));
		Collections.sort(years);
	}

	public File getDir() {
		return dir;
	}

	@Override
	public boolean hasNext() {
		return !years.isEmpty();
	}

	@Override
	public NoticeID next() {

		// while there is a year to be enumerated
		while (!years.isEmpty() && notices.isEmpty()) {
			File year = years.get(0);

			// enumerate notices
			LOG.debug("Enumerating notice directories in {}",
					year.getAbsolutePath());
			Collections.addAll(notices,
					year.listFiles(new NoticeDirectoryFilter()));
			LOG.debug("{} notices found in year directory {}", notices.size(),
					year.getName());
			if (!notices.isEmpty()) {
				Collections.sort(notices, new NoticeDirectoryComparator());
			} else {

				// empty year should be removed
				years.remove(0);
			}
		}

		// if there is no next year
		if (years.isEmpty()) {
			throw new NoSuchElementException();
		}

		// we have a year and notices too
		File year = years.get(0);
		File notice = notices.remove(0);

		// empty year should be removed
		if (notices.isEmpty()) {
			years.remove(0);
		}
		return new NoticeID(String.format("%s-%s", notice.getName(),
				year.getName()));
	}

	@Override
	public String toString() {
		return "DirectoryScope [dir=" + dir + "]";
	}

}
