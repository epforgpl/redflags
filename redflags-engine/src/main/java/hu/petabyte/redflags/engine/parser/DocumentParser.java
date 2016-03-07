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

import java.util.List;

import hu.petabyte.redflags.engine.util.StrUtils;

/**
 * 
 * @author Zsolt Jurányi
 * 
 */
public class DocumentParser {

	protected BlockParser blockParser = new BlockParser();

	public void parse(List<Block> documentBlocks, List<Block> templateBlocks) {
		matchAndParseBlocks(documentBlocks, templateBlocks);
	}

	protected void matchAndParseBlocks(List<Block> documentBlocks,
			List<Block> templateBlocks) {
		int i = 0;
		int t = 0;
		while (t < templateBlocks.size()) {
			Block templateBlock = templateBlocks.get(t);

			// find first match of block
			int index = lookForMatchingBlock(templateBlock, documentBlocks, i);

			// if found, parse it
			if (-1 < index) {
				Block documentBlock = documentBlocks.get(index);

				// next search will start after this block, to preserve order
				i = index + 1;

				// parse blocks own title and content
				blockParser.parse(documentBlock, templateBlock);

				// recursively match&parse children blocks
				if (!templateBlock.getChildren().isEmpty()
						&& !documentBlock.getChildren().isEmpty()) {
					matchAndParseBlocks(documentBlock.getChildren(),
							templateBlock.getChildren());
				}
			} else {
				// otherwise move on to next block
				t++;
			}

		}
	}

	protected int lookForMatchingBlock(Block tWhat, List<Block> dWhere, int from) {
		// System.out.println("T: " + tWhat.getTitle());
		int index = -1;
		for (int i = from; i < dWhere.size() && -1 == index; i++) {
			// System.out.println("  SEEK:" + dWhere.get(i).getTitle());
			if (StrUtils.matchesCaseInsensitive(dWhere.get(i).getTitle(),
					tWhat.getTitle())) {
				// System.out.println("D: " + dWhere.get(i).getTitle());
				index = i;
			}
		}
		return index;
	}

}
