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
package hu.petabyte.redflags.engine.parser;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import hu.petabyte.redflags.engine.util.StrUtils;

/**
 *
 * @author Zsolt Jurányi
 *
 */
public class BlockComparator implements Comparator<Block> {

	protected final List<String> blockTitles = new ArrayList<String>();

	public BlockComparator(String template) {
		String[] t = template.split("\n");
		for (String l : t) {
			if (l.startsWith("#")) {
				blockTitles.add(l);
			}
		}
	}

	@Override
	public int compare(Block arg0, Block arg1) {
		return find(arg0.getTitle()) - find(arg1.getTitle());
	}

	protected int find(String title) {
		int r = 0;
		for (; r < blockTitles.size() && !StrUtils.matchesCaseInsensitive(title, blockTitles.get(r)); r++)
			;
		return r;
	}
}
