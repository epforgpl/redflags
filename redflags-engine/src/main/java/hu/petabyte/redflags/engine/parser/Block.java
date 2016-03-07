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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a document or template block, which has title, content, children
 * blocks and a Map of variables.
 *
 * @author Zsolt Jurányi
 *
 */
public class Block {

	protected List<Block> children = new ArrayList<Block>();

	protected String content;
	protected String title;
	protected Map<String, String> variables = new LinkedHashMap<String, String>();

	public Block() {
	}

	public Block(String title, String content) {
		this.title = title;
		this.content = content;
	}

	public List<Block> getChildren() {
		return children;
	}

	public String getContent() {
		return content;
	}

	public String getTitle() {
		return title;
	}

	public Map<String, String> getVariables() {
		return variables;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setVariables(Map<String, String> variables) {
		this.variables = variables;
	}

	@Override
	public String toString() {
		return "Block [title=" + title + ", content=" + content + ", variables=" + variables + ", children=" + children
				+ "]";
	}

}
