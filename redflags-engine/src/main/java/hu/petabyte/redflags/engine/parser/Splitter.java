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
import java.util.List;

/**
 * Splits a normalized text (or template) into hierarchy of blocks. Normalized
 * text consists of content lines and header lines. Header lines have prefixes:
 * "#" for level-1 heading, "##" for level-2 heading and so on. Every heading
 * creates a new block.
 * 
 * @author Zsolt Jurányi
 * 
 */
public class Splitter {

	/**
	 * Splits a normalized text (or template) into hiearchy of blocks. Calls the
	 * splitBlock to perform the splitting mechanism recursively.
	 * 
	 * @param normalizedText
	 *            Normalized text.
	 * @return List of blocks.
	 */
	public List<Block> split(String normalizedText) {
		Block block = new Block(null, normalizedText);
		splitBlock(block, 1);
		return new ArrayList<Block>(block.getChildren());
	}

	protected void splitBlock(Block block, int level) {
		String[] lines = block.getContent().split("\n");
		Block currentBlock = null;
		for (String line : lines) {
			if (line.matches("^#{" + level + "}[^# ].*")) { // title line
				if (null != currentBlock) {
					block.getChildren().add(currentBlock);
				}
				currentBlock = new Block(line, "");
			} else { // content line
				if (null != currentBlock) {
					currentBlock.setContent(currentBlock.getContent() + line
							+ "\n");
				} else { // first block is untitled
					// currentBlock = new Block("", line);
					// we don't need it :)
				}
			}
		}

		// store last block
		if (null != currentBlock && !currentBlock.getTitle().isEmpty()) {
			block.getChildren().add(currentBlock);
		}

		// clear current block's content
		String[] p = block.getContent().split("(^#|\\n#)");
		if (p.length > 0) {
			block.setContent(p[0]);
		} else {
			block.setContent("");
		}

		// recursively split children blocks
		if (!block.getChildren().isEmpty()) {
			for (Block child : block.getChildren()) {
				splitBlock(child, level + 1);
			}
		}

	}

}
