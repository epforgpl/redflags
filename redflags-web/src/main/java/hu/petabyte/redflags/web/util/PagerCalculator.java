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
package hu.petabyte.redflags.web.util;

/**
 * @author Zsolt Jurányi
 */
public class PagerCalculator {

	private final long all;
	private final long offset;
	private final int pageCount;
	private final int pageIndex;
	private final int perPage;

	public PagerCalculator(long all, int perPage, int pageIndex) {
		this.all = all;
		this.perPage = perPage;
		this.pageCount = (int) Math.ceil((double) this.all
				/ (double) this.perPage);
		this.pageIndex = Math.max(0, Math.min(pageIndex, this.pageCount - 1)); // 0..max
		this.offset = this.pageIndex * this.perPage;
	}

	public long getAll() {
		return all;
	}

	public long getOffset() {
		return offset;
	}

	public int getPageCount() {
		return pageCount;
	}

	public int getPageIndex() {
		return pageIndex;
	}

	public int getPerPage() {
		return perPage;
	}

}
