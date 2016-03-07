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

/**
 * Utility functions to work with time units.
 *
 * @author Zsolt Jurányi
 *
 */
public class TimeUtils {

	/**
	 * Takes a millisecond value and converts it to a readable text like
	 * "x days y hours z minutes". Only non-zero parts will be in the string. It
	 * is useful for user friendly logs and messages.
	 *
	 * @param ms
	 *            Time in milliseconds.
	 * @return Human readable time.
	 */
	public static String readableTime(long ms) {
		long z = ms % 1000;
		long s = ms / 1000 % 60;
		long m = ms / 1000 / 60 % 60;
		long h = ms / 1000 / 60 / 60 % 24;
		long d = ms / 1000 / 60 / 60 / 24;
		StringBuilder sb = new StringBuilder();
		if (0 < d) {
			sb.append(d).append(" days ");
		}
		if (0 < h) {
			sb.append(h).append(" hours ");
		}
		if (0 < m) {
			sb.append(m).append(" minutes ");
		}
		if (0 < s) {
			sb.append(s).append(" seconds ");
		}
		if (0 < z) {
			sb.append(z).append(" milliseconds");
		}
		return sb.toString().trim();
	}

}
