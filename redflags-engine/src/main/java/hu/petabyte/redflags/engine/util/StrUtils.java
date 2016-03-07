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
package hu.petabyte.redflags.engine.util;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;

/**
 * Useful String operations.
 *
 * @author Zsolt Jurányi
 *
 */
public class StrUtils {

	public static String cleanHtmlCode(String html) {
		String s = html;

		// insert line break symbols
		s = s.replaceAll("(</?(p|div)|<br( ?/)?>)", "#####$1");

		// reparse HTML, extract text
		s = Jsoup.parse(s).text();

		// replace line break symbols with line breaks
		s = s.replaceAll("#####", "\n");

		// trim lines, delete empty ones
		StringBuilder sb = new StringBuilder();
		for (String line : s.split("\n")) {
			String trimmed = line.trim();
			if (!trimmed.isEmpty()) {
				sb.append(line.trim());
				sb.append("\n");
			}
		}

		s = sb.toString();
		return s;
	}

	/**
	 * Escape regular expressions' special characters in a String. E.g.
	 * "I.1) Section" will be converted into "I\.1\) Section".
	 *
	 * @param text
	 *            Input text, e.g. a normalized document.
	 * @return The escaped text.
	 */
	public static String escapeRegexChars(String text) {
		return text.replaceAll("\\\\", "\\\\\\\\").replaceAll("\\.", "\\\\.")
				.replaceAll("\\*", "\\\\*").replaceAll("\\+", "\\\\+")
				.replaceAll("\\)", "\\\\)").replaceAll("\\(", "\\\\(")
				.replaceAll("\\]", "\\\\]").replaceAll("\\[", "\\\\[")
				.replaceAll("\\}", "\\\\}").replaceAll("\\{", "\\\\{")
				.replaceAll("\\?", "\\\\?");
	}

	public static Set<String> getNamedGroupCandidates(String regex) {
		// http://stackoverflow.com/questions/15588903/get-group-names-in-java-regex
		Set<String> namedGroups = new TreeSet<String>();
		Matcher m = Pattern.compile("\\(\\?<([a-zA-Z][a-zA-Z0-9]*)>").matcher(
				regex);
		while (m.find()) {
			namedGroups.add(m.group(1));
		}
		return namedGroups;
	}

	public static String lastLineOf(String s) {
		return s.trim().replaceAll(".*\n", "");
	}

	public static String leaveOnlyAlphanumerics(String s) {
		return s.replaceAll("[^a-z0-9 ]", "");
	}

	public static String makeHunCharsCaseInsensitive(String s) {
		// s = s.replaceAll("[áÁ]", "[áÁ]");
		// s = s.replaceAll("[éÉ]", "[éÉ]");
		// s = s.replaceAll("[íÍ]", "[íÍ]");
		// s = s.replaceAll("[óÓ]", "[óÓ]");
		// s = s.replaceAll("[öÖ]", "[öÖ]");
		// s = s.replaceAll("[őŐ]", "[őŐ]");
		// s = s.replaceAll("[úÚ]", "[úÚ]");
		// s = s.replaceAll("[üÜ]", "[üÜ]");
		// s = s.replaceAll("[űŰ]", "[űŰ]");
		s = s.replaceAll("[áÁéÉíÍóÓöÖőŐúÚüÜűŰ]", ".");
		// s = s.replaceAll("[^A-Za-z0-9]", ".");
		return s;
	}

	public static String mapToString(Map<String, String> m) {
		StringBuilder sb = new StringBuilder();
		for (String k : m.keySet()) {
			sb.append(String.format("%s=%s", k, m.get(k)));
		}
		return sb.toString();
	}

	public static boolean matchesCaseInsensitive(String text, String pattern) {
		return text
				.matches("(?i:" + makeHunCharsCaseInsensitive(pattern) + ")");
	}

	public static boolean prefixMatch(String a, String b) {
		return a.matches(b + ".*") || b.matches(a + ".*");
	}

	public static String removeAccents(String s) {
		s = s.replaceAll("[Áá]", "a");
		s = s.replaceAll("[Éé]", "e");
		s = s.replaceAll("[Íí]", "i");
		s = s.replaceAll("[ÓóÖöŐő]", "o");
		s = s.replaceAll("[ÚúÜüŰű]", "u");
		return s;
	}

	public static String shorten(String s) {
		if (null == s) {
			return "NULL";
		}
		if (/* s.length() > 50 || */s.indexOf("\n") > -1) {
			int lines = s.split("\n").length;
			int length = s.length();
			s = s.replaceAll("\n", "\\n");
			return String.format("%s(...)%s (len: %d, lines: %d)",
					s.substring(0, 10), s.substring(length - 10, length),
					length, lines);

		} else {
			return s;
		}
	}

}
