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

import hu.petabyte.redflags.engine.util.MappingUtils;

/**
 * The implementation of block parser. Its parse method does the following:
 * calls a template parser for the document block's title and content. The
 * template parser implementation is stored in a protected field.
 *
 * @author Zsolt Jurányi
 *
 */
public class BlockParser {

	/**
	 * Template parser to use when parsing document block's texts.
	 */
	protected final TemplateParser templateParser = new TemplateParser();

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * hu.juranyi.zsolt.ted.parser.block.BlockParser#parse(hu.juranyi.zsolt.
	 * ted.parser.block.Block, hu.juranyi.zsolt.ted.parser.block.Block)
	 */
	public void parse(Block documentBlock, Block templateBlock) {
		// System.out.println("[TITLE] " + documentBlock.getTitle());
		// System.out.println("[CONTENT] " + documentBlock.getContent());

		documentBlock.getVariables().putAll(
				templateParser.parse(documentBlock.getTitle(),
						templateBlock.getTitle()));

		documentBlock.getVariables().putAll(
				templateParser.parse(documentBlock.getContent(),
						templateBlock.getContent()));

		// System.out.println("[VARS]" + documentBlock.getVariables());

		// convert "a0b0c0d" variable names to "a.b.c.d" valid property names
		MappingUtils.normalizeMapKeys(documentBlock.getVariables());
	}
}
