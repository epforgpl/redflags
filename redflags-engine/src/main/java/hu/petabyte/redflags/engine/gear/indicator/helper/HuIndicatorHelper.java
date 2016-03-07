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
package hu.petabyte.redflags.engine.gear.indicator.helper;

/**
 * @author Zsolt Jurányi
 */
public class HuIndicatorHelper { // TODO component?, config params?

	public static boolean isRef(String line) {
		String r = "(szakmai|szakértői)?([^ ]+)?( szerzett)? (tapasztalat|gyakorlat)(tal)?( rendelkező szakember)?";
		if (line.matches(".*" + r + ".*")) {
			return false;
		}

		r = "(csatol|nyilatkoz)";
		if (line.matches(".*" + r + ".*")) {
			return false;
		}

		if (line.contains(" referenci")) {
			return true;
		}

		if (line.matches(".*(korábbi|jelentős(ebb)?|teljesített|közbeszerzés tárgya szerinti).*(szerződés|szállítás|építés|beruházás|(áru)?beszerzés|munk[aá]|szolgáltatás).*")) {
			return true;
		}

		return false;
	}

	public static String words2Digits(String s) {
		s = s.replaceAll(" (kettő|két) ", " 2 ");
		s = s.replaceAll(" három ", " 3 ");
		s = s.replaceAll(" négy ", " 4 ");
		s = s.replaceAll(" öt ", " 5 ");
		s = s.replaceAll(" hat ", " 6 ");
		s = s.replaceAll(" hét ", " 7 ");
		s = s.replaceAll(" nyolc ", " 8 ");
		s = s.replaceAll(" kilenc ", " 9 ");
		s = s.replaceAll(" tíz ", " 10 ");
		s = s.replaceAll(" ezer ", " 000 ");
		s = s.replaceAll(" millió ", " 000 000 ");
		s = s.replaceAll(" milliárd ", " 000 000 000 ");
		return s;
	}
}
