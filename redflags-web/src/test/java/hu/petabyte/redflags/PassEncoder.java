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
package hu.petabyte.redflags;

import java.util.Scanner;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author Zsolt Jurányi
 */
public class PassEncoder {

	@Test
	public void test() {
		BCryptPasswordEncoder e = new BCryptPasswordEncoder();
		Scanner s = new Scanner(System.in);
		String i;
		while (!(i = s.nextLine()).isEmpty()) {
			System.out.println("ENC: " + e.encode(i));
		}
		s.close();
	}

}
