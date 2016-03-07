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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Zsolt Jurányi
 */
@Component
@ConfigurationProperties(prefix = "redflags.engine.parser.tab012")
public class Tab012ParserConfig {

	public static class LanguageSpecificConfig {
		private List<String> repeatingBlocks = new ArrayList<String>();
		private String objBlock;
		private String lotBlock;
		private String awardBlock;

		public String getAwardBlock() {
			return awardBlock;
		}

		public String getLotBlock() {
			return lotBlock;
		}

		public String getObjBlock() {
			return objBlock;
		}

		public List<String> getRepeatingBlocks() {
			return repeatingBlocks;
		}

		public void setAwardBlock(String awardBlock) {
			this.awardBlock = awardBlock;
		}

		public void setLotBlock(String lotBlock) {
			this.lotBlock = lotBlock;
		}

		public void setObjBlock(String objBlock) {
			this.objBlock = objBlock;
		}

		public void setRepeatingBlocks(List<String> repeatingBlocks) {
			this.repeatingBlocks = repeatingBlocks;
		}

	}

	private Map<String, LanguageSpecificConfig> langspec = new HashMap<String, LanguageSpecificConfig>();

	public Map<String, LanguageSpecificConfig> getLangspec() {
		return langspec;
	}

	public void setLangspec(Map<String, LanguageSpecificConfig> langspec) {
		this.langspec = langspec;
	}

}
