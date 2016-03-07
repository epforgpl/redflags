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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Zsolt Jurányi
 */
public class CompanyNameUtils {

	private static final String[] SUFFIX_ARR = {
		/**/"a g|ag",
		/**/"bt|beteti tarsasag",
		/**/"bt|tarsasag", // exp miatt
		/**/"corp|corporation",
		/**/"gmbh",
		/**/"inc",
		/**/"kft|korlatolt felelossegu tarsasag",
		/**/"ltd|limited",
		/**/"rt|reszvenytarsasag|nyrt|nyilvanosan mukodo reszvenytarsasag|zrt|zartkoruen mukodo reszvenytarsasag",
		/**/"rt|reszvenytarsasag|nyrt|nyilvanosan reszvenytarsasag|zrt|zartkoruen reszvenytarsasag", // exp
		/**/"sro",
	/**/};
	private static final Pattern SUFFIX_PATTERN;

	static {
		StringBuilder sb = new StringBuilder();
		sb.append(" (?<s>");
		for (String s : SUFFIX_ARR) {
			sb.append("(");
			sb.append(s);
			sb.append(")|");
		}
		sb.append(")$");
		SUFFIX_PATTERN = Pattern.compile(sb.toString());
	}

	public static String normalizeCompanyName(String rawCN) {
		String normalizedCN = rawCN.toLowerCase();

		// XXX experimental
		normalizedCN = normalizedCN.replaceAll(" (és|holding|international) ",
				" ");
		normalizedCN = normalizedCN.replaceAll(" ([^ ]+[ióő],? )+", " ");

		normalizedCN = StrUtils.removeAccents(normalizedCN);
		normalizedCN = StrUtils.leaveOnlyAlphanumerics(normalizedCN);

		return normalizedCN.trim().replaceAll(" +", " ");
	}

	public static boolean hasNoSuffix(String normCN) {
		return normCN.equals(removeSuffixes(normCN));
	}

	public static String removeSuffixes(String normalizedCN) {
		return normalizedCN.replaceAll(SUFFIX_PATTERN.pattern(), "").trim();
	}

	public static boolean matchSuffixesNorm(String normCN1, String normCN2) {
		if (hasNoSuffix(normCN1) && hasNoSuffix(normCN2)) {
			return true;
		}

		Matcher m1 = SUFFIX_PATTERN.matcher(normCN1);
		Matcher m2 = SUFFIX_PATTERN.matcher(normCN2);
		if (m1.find() && m2.find()) {
			String p1 = "(^|.*\\|)" + m1.group("s") + "(\\|.*|$)";
			String p2 = "(^|.*\\|)" + m2.group("s") + "(\\|.*|$)";
			for (String s : SUFFIX_ARR) {
				if (s.matches(p1) && s.matches(p2)) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean matchSuffixesRaw(String rawCN1, String rawCN2) {
		String n1 = normalizeCompanyName(rawCN1);
		String n2 = normalizeCompanyName(rawCN2);
		return matchSuffixesNorm(n1, n2);
	}

	public static String normalizeCompanyNameForUser(String raw) {
		raw = raw.trim();
		raw = raw.replaceAll("[‘’„”\\\"]", "");
		raw = raw.replaceAll("(?iu) betéti társaság| bt\\.?", " Bt.");
		raw = raw.replaceAll("(?iu) korlátolt felelősségű társaság| kft\\.?",
				" Kft.");
		raw = raw.replaceAll("(?iu) közkereseti társaság| kkt\\.?", " KKt.");
		raw = raw
				.replaceAll(
						"(?iu) nyilvánosan működő részvénytársaság| nyrt\\.?",
						" Nyrt.");
		raw = raw.replaceAll(
				"(?iu) zártkörűen működő részvénytársaság| zrt\\.?", " Zrt.");
		raw = raw.replaceAll("(?iu) közös vállalat| k\\.? ?v\\.?", " K.V.");
		raw = raw.replaceAll("(?iu)i önkormányzat| önkormányzata",
				" Önkormányzat");
		raw = raw.replaceAll("(?iu) hivatala?", " Hivatal");
		raw = raw.replaceAll("(?iu), (bt|kft|kkt|nyrt|zrt)", " $1");
		return raw;
	}

	public static String normalizeCompanyNameForHash(String raw) {
		return raw.trim().toLowerCase().replaceAll("[^a-záéíóöőúüű0-9]", "");
	}

}
