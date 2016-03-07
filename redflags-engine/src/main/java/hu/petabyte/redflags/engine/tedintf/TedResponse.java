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
package hu.petabyte.redflags.engine.tedintf;

import org.jsoup.nodes.Document;

/**
 * Result type of <code>TedInterface</code>. It contains the raw HTML as String
 * and a parsed JSoup Document too.
 *
 * @author Zsolt Jurányi
 *
 */
public class TedResponse {

	/**
	 * The downloaded raw HTML.
	 */
	protected final String rawHTML;
	/**
	 * The document parsed by JSoup.
	 */
	protected final Document parsedDocument;

	/**
	 * Creates a <code>TedResponse</code> object.
	 *
	 * @param rawHTML
	 *            The raw HTML.
	 * @param parsedDocument
	 *            The document parsed by JSoup.
	 */
	public TedResponse(String rawHTML, Document parsedDocument) {
		this.rawHTML = rawHTML;
		this.parsedDocument = parsedDocument;
	}

	/**
	 * Returns parsed document.
	 *
	 * @return The document parsed by JSoup.
	 */
	public Document getParsedDocument() {
		return parsedDocument;
	}

	/**
	 * Returns the raw HTML.
	 *
	 * @return The raw HTML.
	 */
	public String getRawHTML() {
		return rawHTML;
	}

	@Override
	public String toString() {
		return String.format("TedResponse: len(HTML)=%d", null == rawHTML ? "-1" : rawHTML.length());
	}

}
