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
 * Own and only exception type of <code>TedInterface</code>. It holds
 * information about the error in a <code>TedErrorType</code> object.
 *
 * @author Zsolt Jurányi
 * @see hu.petabyte.redflags.engine.tedintf.TedErrorType
 * @see hu.petabyte.redflags.engine.tedintf.TedInterface
 */
public class TedError extends Exception {

	private static final long serialVersionUID = 1L;

	private final TedErrorType errorType;

	/**
	 * Creates a <code>TedError</code> object.
	 *
	 * @param errorType
	 *            Information about the error.
	 */
	public TedError(TedErrorType errorType) {
		this(errorType, "");
	}

	/**
	 * Creates a <code>TedError</code> object.
	 *
	 * @param errorType
	 *            Information about the error.
	 * @param message
	 *            Exception message.
	 */
	public TedError(TedErrorType errorType, String message) {
		this(errorType, message, null);
	}

	/**
	 * Creates a <code>TedError</code> object.
	 *
	 * @param errorType
	 *            Information about the error.
	 * @param message
	 *            Exception message.
	 * @param cause
	 *            The original exception.
	 */
	public TedError(TedErrorType errorType, String message, Throwable cause) {
		super(errorType.toString() + " : " + message, cause);
		this.errorType = errorType;
	}

	/**
	 * Creates a <code>TedError</code> object.
	 *
	 * @param errorType
	 *            Information about the error.
	 * @param cause
	 *            The original exception.
	 */
	public TedError(TedErrorType errorType, Throwable cause) {
		this(errorType, "", cause);
	}

	/**
	 * Returns information about the error.
	 *
	 * @return Information about the error.
	 */
	public TedErrorType getErrorType() {
		return errorType;
	}

}
