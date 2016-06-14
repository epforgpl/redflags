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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Zsolt Jurányi
 *
 */
public class DocumentNormalizer {

	protected static void buildTitle(Element element, String selector,
			String linePrefix, StringBuilder builder) {
		for (Element titleElement : element.select(selector)) {
			builder.append(linePrefix);
			builder.append(titleElement.text());
		}
	}

	/**
	 * Cleans the document HTML code, and transforms the headings to a form
	 * which can be matched easily with the templates.
	 *
	 *
	 */
	public static String normalizeDocument(String html) {
		Element doc = Jsoup.parse(html);
		Elements els = doc.select("div#fullDocument");
		if (!els.isEmpty()) {
			doc = els.first();
		}

		StringBuilder sb = new StringBuilder();
		for (Element grseq : doc.select("div.grseq")) {
			buildTitle(grseq, "> p.tigrseq", "#", sb);
			buildTitle(grseq, "> span", "#", sb);
			sb.append("\n");
			for (Element mlioccur : grseq.select("> div.mlioccur")) {
				buildTitle(mlioccur, "> span.nomark", "##", sb);
				buildTitle(mlioccur, "> span.timark", " ", sb);
				sb.append("\n");
				mlioccur.select("> span.nomark").remove();
				mlioccur.select("> span.timark").remove();
				sb.append(StrUtils.cleanHtmlCode(mlioccur.html())
						.replaceAll("#+", "××").trim());
				// replace is needed because there are '#' in the text
				// and they confuse the Splitter (I don't know why...)
				sb.append("\n");
			}
		}

		return sb.toString();
	}

}
