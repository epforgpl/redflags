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
package hu.petabyte.redflags.engine.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import hu.petabyte.redflags.engine.util.ArgsUtils;

/**
 * @author Zsolt Jurányi
 */
@Deprecated
public class ArgsCheckerTest {

	@Test
	public void argMissing() {
		assertTrue(ArgsUtils.isAnyArgMissing( //
				new String[] { "--scope=auto" }, //
				new String[] { "--scope=.+", "--cache=.+" } //
		));
		assertTrue(ArgsUtils.isAnyArgMissing( //
				new String[] { "--scope=auto" }, //
				new String[] { "--cache=.+", "--scope=.+" } //
		));
		assertTrue(ArgsUtils.isAnyArgMissing( //
				new String[] { "--cache=tenders" }, //
				new String[] { "--scope=.+", "--cache=.+" } //
		));
	}

	@Test
	public void noArgsMissing() {
		assertFalse(ArgsUtils.isAnyArgMissing( //
				new String[] { "--scope=auto" }, //
				new String[] { "--scope=.+" } //
		));
		assertFalse(ArgsUtils.isAnyArgMissing( //
				new String[] { "--scope=auto", "--cache=tenders" }, //
				new String[] { "--scope=.+", "--cache=.+" } //
		));
	}

}
