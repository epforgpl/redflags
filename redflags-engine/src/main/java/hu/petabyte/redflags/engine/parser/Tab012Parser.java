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

import hu.petabyte.redflags.engine.model.Notice;
import hu.petabyte.redflags.engine.model.noticeparts.Award;
import hu.petabyte.redflags.engine.model.noticeparts.Lot;
import hu.petabyte.redflags.engine.model.noticeparts.ObjOfTheContract;
import hu.petabyte.redflags.engine.parser.Tab012ParserConfig.LanguageSpecificConfig;
import hu.petabyte.redflags.engine.util.MappingUtils;
import hu.petabyte.redflags.engine.util.StrUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Zsolt Jurányi
 *
 */
public class Tab012Parser {

	private static final Logger LOG = LoggerFactory
			.getLogger(Tab012Parser.class);

	protected final String lang;
	protected final Tab012ParserConfig config;
	protected final LanguageSpecificConfig languageSpecificConfig;
	protected DocumentParser documentParser = new DocumentParser();
	protected Notice notice;
	protected Splitter splitter = new Splitter();
	protected TemplateParser templateParser = new TemplateParser();

	public Tab012Parser(String lang, Tab012ParserConfig config) {
		this.lang = lang;
		this.config = config;
		this.languageSpecificConfig = config.getLangspec().get(lang);
	}

	protected void debugPrintBlockTitles(List<Block> documentBlocks) {
		System.out.println("---DEBUG");
		for (Block b : documentBlocks) {
			System.out.println(b.getTitle());
			for (Block bb : b.getChildren()) {
				System.out.println("  " + bb.getTitle());
			}
		}
		System.out.println("---/DEBUG");
	}

	protected boolean isRepeatingBlock(Block block) {
		boolean is = false;
		if (null != languageSpecificConfig) {
			List<String> repeatingBlocks = languageSpecificConfig
					.getRepeatingBlocks();
			for (int i = 0; i < repeatingBlocks.size() && !is; i++) {
				is = StrUtils.matchesCaseInsensitive(block.getTitle(),
						repeatingBlocks.get(i));
			}
		}
		return is;
	}

	protected void mapBlocks(Object root, List<Block> blocks) {
		for (Block b : blocks) {
			if (!isRepeatingBlock(b)) {
				MappingUtils.setDeepProperties(root, b.getVariables());
				mapBlocks(root, b.getChildren());
			} else {
				mapRepeatingBlock(root, b);
			}
		}
	}

	protected void mapRepeatingBlock(Object root, Block b) {
		if (!(root instanceof Notice)) {
			return;
		}
		if (StrUtils.matchesCaseInsensitive(b.getTitle(),
				languageSpecificConfig.getObjBlock())) {
			ObjOfTheContract obj = new ObjOfTheContract();
			((Notice) root).getObjs().add(obj);
			MappingUtils.setDeepProperties(obj, b.getVariables());
			mapBlocks(obj, b.getChildren());
		} else if (StrUtils.matchesCaseInsensitive(b.getTitle(),
				languageSpecificConfig.getLotBlock())) {
			Lot lot = new Lot();
			((Notice) root).getLots().add(lot);
			MappingUtils.setDeepProperties(lot, b.getVariables());
			mapBlocks(lot, b.getChildren());
		} else if (StrUtils.matchesCaseInsensitive(b.getTitle(),
				languageSpecificConfig.getAwardBlock())) {
			Award a = new Award();
			((Notice) root).getAwards().add(a);
			MappingUtils.setDeepProperties(a, b.getVariables());
			mapBlocks(a, b.getChildren());
		}
	}

	protected void moveRepeatingBlocksTogether(List<Block> documentBlocks,
			String template) {
		Collections.sort(documentBlocks, new BlockComparator(template));
	}

	public void parse(Notice notice, String tab1, String template) {
		this.notice = notice;

		String html = tab1;

		String normalizedDocument = DocumentNormalizer.normalizeDocument(html);
		List<Block> documentBlocks = splitter.split(normalizedDocument);
		List<Block> templateBlocks = splitter.split(template);

		moveRepeatingBlocksTogether(documentBlocks, template);

		// TODO move 2 properties
		resolveRepeatingSubBlockSeqs(documentBlocks, "#V\\..*",
				"##V\\.1\\.?\\).*", "TD-7");
		resolveRepeatingSubBlockSeqs(documentBlocks,
				"#II\\.?[AB]?\\.? szakasz.*", "##II\\.1\\.?\\).*", "TD-0");

		documentParser.parse(documentBlocks, templateBlocks);
		mapBlocks(notice, documentBlocks);
	}

	protected void resolveRepeatingSubBlockSeqs(List<Block> documentBlocks,
			String mainBlockTitlePattern, String firstSubBlockTitlePattern,
			String docTypeId) {
		if (!docTypeId.equals(MappingUtils.getDeepPropertyIfExists(notice,
				"data.documentType.id"))) {
			return;
		}

		int originalBlockIndex = -1;
		for (int i = 0; i < documentBlocks.size() && -1 == originalBlockIndex; i++) {
			Block b = documentBlocks.get(i);
			if (null != b.getTitle()
					&& StrUtils.matchesCaseInsensitive(b.getTitle(),
							mainBlockTitlePattern)) {
				originalBlockIndex = i;
			}
		}

		if (-1 == originalBlockIndex) {
			return;
		}

		Block originalBlock = documentBlocks.get(originalBlockIndex);
		Block currentBlock = null;
		boolean first = true;
		int currentBlockIndex = originalBlockIndex + 1;
		int secondSubBlockIndex = -1;

		// go thru sub blocks
		List<Block> children = originalBlock.getChildren();
		for (int i = 0; i < children.size(); i++) {
			Block b = children.get(i);

			// an award's 1st sub block
			if (null != b.getTitle()
					&& StrUtils.matchesCaseInsensitive(b.getTitle(),
							firstSubBlockTitlePattern)) {
				if (null != currentBlock) { // Nth (N>2) award block:
					// create award block and append to docBlocks
					documentBlocks.add(currentBlockIndex++, currentBlock);
					currentBlock = new Block(originalBlock.getTitle(), null);
				} else {
					if (first) { // 1st award block: skip
						first = false;
					} else { // 2nd award block: create it
						secondSubBlockIndex = i;
						currentBlock = new Block(originalBlock.getTitle(), null);
					}
				}
			}

			// copy sub blocks to current award block
			if (null != currentBlock) {
				currentBlock.getChildren().add(b);
			}
		}

		// add last award
		if (null != currentBlock && !currentBlock.getChildren().isEmpty()) {
			documentBlocks.add(currentBlockIndex++, currentBlock);
		}

		// crop first award
		if (-1 < secondSubBlockIndex) {
			List<Block> list = new ArrayList<Block>(originalBlock.getChildren()
					.subList(0, secondSubBlockIndex));
			originalBlock.getChildren().clear();
			originalBlock.getChildren().addAll(list);
		}
	}
}
