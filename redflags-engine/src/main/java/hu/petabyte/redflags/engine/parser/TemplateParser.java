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

import hu.petabyte.redflags.engine.util.StrUtils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Zsolt Jurányi
 *
 */
public class TemplateParser {

	protected static final String OPT_PREFIX = "???#";
	protected int i;
	protected String[] inp;
	protected int t;
	protected String[] tpl;

	protected Map<String, String> var = new LinkedHashMap<String, String>();

	protected void lookForward() {

		// WE MOVE IN TEMPLATE LINES WHEN

		if (!match(i + 1, t)) {
			// (A) THE NEXT INPUT DOES NOT MATCH CURRENT TEMPLATE
			t++;
			// TODO MAYBE WE NEED TO LOOK MORE FORWARD!
			// INP: a,c
			// TPL: a,.*,c
			// we need: c-c
		} else {
			// (B) THE NEXT INPUT MATCHES THE NEXT* TEMPLATE
			// *: WE LOOK TILL THE FIRST NON-OPTIONAL TEMPLATE
			for (int tt = t + 1, f = 0; tt < tpl.length && 0 == f; tt++) {
				if (match(i + 1, tt)) {
					t = tt;
					f++;
				} else if (!optional(tpl[tt])) {
					f++;
				}
			}
		}
	}

	protected boolean match(int ii, int tt) {
		return ii < inp.length
				&& tt < tpl.length
				&& StrUtils.matchesCaseInsensitive(inp[ii],
						stripPrefix(tpl[tt]));
	}

	protected boolean optional(String templateLine) {
		return templateLine.startsWith(OPT_PREFIX);
	}

	public Map<String, String> parse(String input, String template) {

		// VALIDATE INPUT AND SPLIT TO LINES

		if (null == input || null == template) {
			return var;
		}
		tpl = template.split("\n");
		inp = input.split("\n");
		if (0 == inp.length || 0 == tpl.length) {
			return var;
		}

		// INITIALIZE

		i = 0;
		t = 0;
		var.clear();

		// THE MAGIC

		while (t < tpl.length && i < inp.length) {
			// System.out.println("TPL " + stripPrefix(tpl[t]));
			// System.out.println("INP " + inp[i]);
			if (StrUtils.matchesCaseInsensitive(inp[i], stripPrefix(tpl[t]))) {
				parseVars();
				lookForward();
				i++;
			} else if (optional(tpl[t])) {
				t++;
			} else {
				i++;
			}

			// (A) : if matches i++ , else if optoional t++ , else i++
			// (B) : if matches i++ . else t++
		}

		return var;
	}

	protected void parseVars() {
		Set<String> groupNames = StrUtils
				.getNamedGroupCandidates(stripPrefix(tpl[t]));
		String p = StrUtils.makeHunCharsCaseInsensitive(stripPrefix(tpl[t]));
		Pattern pattern = Pattern.compile(p, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(inp[i]);
		if (matcher.find()) {
			for (String name : groupNames) { // parse variables
				String matchedValue = matcher.group(name);
				if (null != matchedValue) {
					if (var.containsKey(name)) { // and append to output
						var.put(name, var.get(name) + "\n" + matchedValue);
					} else {
						var.put(name, matchedValue);
					}
				}
				// System.out.println("MAP " + name + " += " + matchedValue);
			}
		}
	}

	protected String stripPrefix(String templateLine) {
		return optional(templateLine) ? templateLine.substring(OPT_PREFIX
				.length()) : templateLine;
	}

}
