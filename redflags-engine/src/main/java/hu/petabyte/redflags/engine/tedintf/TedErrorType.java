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

/**
 * Describes the types of errors you can meet when requesting data from TED.
 * Error types have some extra information: they tell you whether you can retry
 * your last request or if you can continue crawling.
 *
 * @author Zsolt Jurányi
 *
 */
public enum TedErrorType {

	/**
	 * Failed to download from TED.
	 */
	IO_EXCEPTION, //
	/**
	 * TED responded with HTTP error, maybe there is a server error.
	 */
	HTTP_ERROR, //
	/**
	 * The response body is bigger than we expected.
	 */
	INCOMPLETE_FILE, //
	/**
	 * The requested NoticeID does not exist on TED, maybe it had been deleted.
	 */
	INVALID_UDL(false), //
	/**
	 * The requested NoticeID is no more available on TED, it is too old.
	 */
	EXPIRED_UDL(false), //
	/**
	 * The requested NoticeID is not yet available, try again later.
	 */
	UNAVAILABLE_UDL(false), //
	/**
	 * We have just got a temp ban for 24 hours, because we have sent too much
	 * requests recently.
	 */
	TEMPORARY_BAN(false, false), //
	/**
	 * The needed important content is missing from the page, maybe we have been
	 * redirected or there is a server bug on TED.
	 */
	MISSING_CONTENT, //
	/**
	 * Failed to parse the HTML code.
	 */
	PARSE_ERROR; //

	private final boolean canRetryHelp;
	private final boolean canContinueCrawling;

	private TedErrorType() {
		this(true); // Retry can help in most cases.
	}

	private TedErrorType(boolean canRetryHelp) {
		this(canRetryHelp, true); // Crawling can be continued in most cases.
	}

	private TedErrorType(boolean canRetryHelp, boolean canContinueCrawling) {
		this.canRetryHelp = canRetryHelp;
		this.canContinueCrawling = canContinueCrawling;
	}

	/**
	 * Tells you whether you can continue your crawling job with another Notice,
	 * or any other request. Basically it only tells <code>false</code> when we
	 * have been banned for 24 hours.
	 *
	 * @return <code>true</code> - when you can send more requests to TED <br/>
	 *         <code>false</code> - when we have been banned for 24 hours
	 */
	public boolean canContinueCrawling() {
		return canContinueCrawling;
	}

	/**
	 * Tells you whether you have chance of success when you retry your last
	 * requst. For example you can retry a download after a network error but
	 * there is no point in trying to download a non-existing document.
	 *
	 * @return <code>true</code> - when you can retry your last request <br/>
	 *         <code>false</code> - when there is no point in retrying
	 */
	public boolean canRetryHelp() {
		return canRetryHelp;
	}

	@Override
	public String toString() {
		return String.format("%s (canRetryHelp: %s, canContinueCrawling: %s)", name(), canRetryHelp,
				canContinueCrawling);
	}

}
