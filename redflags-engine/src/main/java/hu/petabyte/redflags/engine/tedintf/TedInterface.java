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

import static com.google.common.base.Preconditions.checkNotNull;
import static hu.petabyte.redflags.engine.tedintf.TedErrorType.EXPIRED_UDL;
import static hu.petabyte.redflags.engine.tedintf.TedErrorType.HTTP_ERROR;
import static hu.petabyte.redflags.engine.tedintf.TedErrorType.INCOMPLETE_FILE;
import static hu.petabyte.redflags.engine.tedintf.TedErrorType.INVALID_UDL;
import static hu.petabyte.redflags.engine.tedintf.TedErrorType.IO_EXCEPTION;
import static hu.petabyte.redflags.engine.tedintf.TedErrorType.MISSING_CONTENT;
import static hu.petabyte.redflags.engine.tedintf.TedErrorType.PARSE_ERROR;
import static hu.petabyte.redflags.engine.tedintf.TedErrorType.UNAVAILABLE_UDL;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hu.petabyte.redflags.engine.model.DisplayLanguage;
import hu.petabyte.redflags.engine.model.NoticeID;
import hu.petabyte.redflags.engine.model.Tab;

/**
 * Easy-to-use downloader interface for TED (
 * <a href="http://ted.europa.eu/">Tenders Electronic Daily</a> ). It provides
 * synchronized methods to download one tab of a notice in a specified display
 * language. It can retry requests and sleep before every download.
 *
 * @author Zsolt Jurányi
 *
 */
public class TedInterface { // TODO publish

	private static final Logger LOG = LoggerFactory.getLogger(TedInterface.class);
	private static TedInterface INSTANCE = new TedInterface();

	/**
	 * Get the
	 * <code>TedInterface<code> instance. It is used as a singleton, because
	 * parallel downloading can cause TED to be angry and temporarily ban your
	 * IP.
	 *
	 * @return The instance of <code>TedInterface</code>.
	 */
	public static TedInterface getInstance() {
		return INSTANCE;
	}

	/**
	 * Set the <code>TedInterface<code> instance, for example with your own
	 * extending implementation.
	 *
	 * @param tedInterface
	 *            The <code>TedInterface<code> instance.
	 */
	public static void setInstance(TedInterface tedInterface) {
		INSTANCE = tedInterface;
	}

	private TedInterfaceConf conf = new TedInterfaceConf();
	private final Map<String, String> cookies = new HashMap<String, String>();

	/**
	 * Constructor is not public because <code>TedInterface</code> should be
	 * used as a singleton.
	 */
	protected TedInterface() {
	}

	/**
	 * Get <code>TedInterface<code> configuration.
	 *
	 * @return <code>TedInterface<code> configuration.
	 */
	public TedInterfaceConf getConf() {
		return conf;
	}

	/**
	 * Get cookies.
	 *
	 * @return Cookies.
	 */
	public Map<String, String> getCookies() {
		return cookies;
	}

	/**
	 * Calls <code>requestNoticeTabWithoutRetrying</code>, but retries failed
	 * requests at most <code>retryCount</code> times and only if
	 * <code>TedErrorType</code> says it can help. Sleeps before every request,
	 * the amount can be configured, it can also be increased and/or multiplied
	 * before retries. The formula of the next sleep is: <code>sleep*m+a</code>,
	 * where <code>m</code> is <code>mulSleepBeforeRetry</code> and
	 * <code>a</code> is <code>addSleepBeforeRetry</code> in the configuration.
	 * The initial sleep is <code>sleepBeforeRequest</code>.
	 *
	 * @param id
	 *            Identifier of the notice.
	 * @param lang
	 *            The display language.
	 * @param tab
	 *            The requested tab.
	 * @return A <code>TedResponse</code> object with the raw and parsed HTML.
	 * @throws TedError
	 *             Throws the last <code>TedError</code> exception if all
	 *             retries failed.
	 */
	public synchronized TedResponse requestNoticeTab(NoticeID id, DisplayLanguage lang, Tab tab) throws TedError {
		checkNotNull(id, "id should not be null.");
		checkNotNull(lang, "lang should not be null.");
		checkNotNull(tab, "tab should not be null.");
		String args = String.format("(%s, %s, %s)", id.toString(), lang.name(), tab.name());
		LOG.debug("Starting a download {}", args);
		long sleep = conf.getSleepBeforeRequest();
		int remainingRetries = conf.getRetryCount() + 1; // +1 is the 1st try
		TedError error = null;
		TedResponse response = null;
		while (0 < remainingRetries && null == response) {
			LOG.trace("Sleeping {} ms", sleep);
			sleep(sleep);
			try {
				response = requestNoticeTabWithoutRetrying(id, lang, tab);
			} catch (TedError e) {
				error = e;
				LOG.trace("Download error: {}", e.getErrorType());

				remainingRetries -= e.getErrorType().canRetryHelp() ? 1 : remainingRetries;
				// remaining - remaining = 0 will jump out of the cycle :)
				LOG.trace("Remaining retries: {}", remainingRetries);

				// sleep = (sleep * m) + a
				sleep *= conf.getMulSleepBeforeRetry();
				sleep += conf.getAddSleepBeforeRetry();
			}
		}
		if (null == response) {
			LOG.debug("Giving up {}, last error was: {}", args, error.getMessage());
			throw error;
		}
		return response;
	}

	/**
	 * Calls <code>requestNoticeTab</code> and catches its <code>TedError</code>
	 * exception.
	 *
	 * @param id
	 *            Identifier of the notice.
	 * @param lang
	 *            The display language.
	 * @param tab
	 *            The requested tab.
	 * @return A <code>TedResponse</code> object with the raw and parsed HTML or
	 *         <code>null</code> if there was an error.
	 */
	public synchronized TedResponse requestNoticeTabQuietly(NoticeID id, DisplayLanguage lang, Tab tab) {
		try {
			return requestNoticeTab(id, lang, tab);
		} catch (TedError e) {
			LOG.warn("Failed to download {}:{}:{} - {}", id, lang, tab, e.getErrorType());
			return null;
		}
	}

	/**
	 * Attempts to download the requested tab of a notice. Checks for various
	 * known errors and throws an exception if any of them occurs.
	 *
	 * @param id
	 *            Identifier of the notice.
	 * @param lang
	 *            The display language.
	 * @param tab
	 *            The requested tab.
	 * @return A <code>TedResponse</code> object with the raw and parsed HTML.
	 * @throws TedError
	 *             Throws <code>TedError</code> exception when a known error
	 *             occurs, and provides information about the error.
	 */
	public synchronized TedResponse requestNoticeTabWithoutRetrying(NoticeID id, DisplayLanguage lang, Tab tab)
			throws TedError {
		checkNotNull(id, "id should not be null.");
		checkNotNull(lang, "lang should not be null.");
		checkNotNull(tab, "tab should not be null.");

		// TODO when id.year < current year -5, throw ted error expired_udl

		String args = String.format("(%s, %s, %s)", id.toString(), lang.name(), tab.name());
		LOG.debug("Download attempt {}", args);
		String uri = String.format("TED:NOTICE:%s:DATA:%s:HTML", id.toString(), lang);
		String tabId = tab.getId();
		Connection c = Jsoup.connect("http://ted.europa.eu/udl").data("uri", uri).data("tabId", tabId).cookies(cookies)
				.followRedirects(true).maxBodySize(conf.getMaxBodySize()).method(Method.GET).timeout(conf.getTimeout());
		LOG.trace("URL: http://ted.europa.eu/udl?uri={}&tabId={}", uri, tabId);
		try {

			// send request, receive response
			Response r = c.execute(); // throws IOException

			// save cookies
			cookies.putAll(r.cookies());

			// check HTTP status
			if (r.statusCode() != 200) {
				throw new TedError(HTTP_ERROR, r.statusCode() + " - " + r.statusMessage());
			}

			// check if incomplete
			if (-1 == r.body().lastIndexOf("</html")) {
				throw new TedError(INCOMPLETE_FILE, r.bodyAsBytes().length + " bytes");
			}

			String url = r.url().toString();

			// check if invalid UDL
			if (url.contains("invalidUDL")) {
				throw new TedError(INVALID_UDL, id.toString());
			}

			// check if expired UDL
			if (url.contains("expiredUDL")) {
				throw new TedError(EXPIRED_UDL, id.toString());
			}

			// check if unavailable UDL
			if (url.contains("unavailableUDL")) {
				throw new TedError(UNAVAILABLE_UDL, id.toString());
			}

			// check if we are banned for 24 hours
			if (url.contains("temporaryBanished")) {
				LOG.error("Temp ban detected!");
				throw new TedError(UNAVAILABLE_UDL, new Date().toString());
			}

			// check if it has important content
			if (null != conf.getMustHaveContent() && !r.body().contains(conf.getMustHaveContent())) {
				throw new TedError(MISSING_CONTENT, url);
			}

			// parse
			String rawHTML = r.body();
			Document parsedDocument = null;
			try {
				parsedDocument = r.parse();
			} catch (IOException e) {
				throw new TedError(PARSE_ERROR, url);
			}

			// return with response
			LOG.debug("Successful download {}", args);
			return new TedResponse(rawHTML, parsedDocument);

		} catch (IOException e) {
			throw new TedError(IO_EXCEPTION, e);
		}
	}

	/**
	 * Sets configuration.
	 *
	 * @param conf
	 *            Configuration.
	 */
	public void setConf(TedInterfaceConf conf) {
		this.conf = checkNotNull(conf, "conf should not be null.");
	}

	/**
	 * Takes a short nap. Used before downloads.
	 *
	 * @param ms
	 *            Amount of sleep in milliseconds.
	 */
	protected void sleep(long ms) {
		// TODO maybe we should throw up interruption
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			LOG.warn("Sleep before TED request interrupted");
			Thread.currentThread().interrupt();
		}
	}

	@Override
	public String toString() {
		return "TedInterface [conf=" + conf + "]";
	}

}
