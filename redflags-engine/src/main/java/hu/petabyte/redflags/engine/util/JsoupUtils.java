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

import static com.google.common.base.Preconditions.checkNotNull;

import org.jsoup.nodes.Element;

/**
 * Utility functions to help working with Jsoup.
 *
 * @author Zsolt Jurányi
 *
 */
public class JsoupUtils {

	/**
	 * Fetches the text of an element but preserves newlines. Appends one
	 * newline before every BR tag, prepends two newline before every P tag,
	 * before calling Jsoup's text() on the element.
	 *
	 * @param e
	 *            The element which has the text you need.
	 * @return The text of the element with newlines inside where BR and P
	 */
	public static String text(Element e) {
		checkNotNull(e, "e should not be null.");
		e.select("br").append("\\n");
		e.select("p").prepend("\\n\\n");
		return e.text().replaceAll("\\\\n", "\n").trim();
	}

}
