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

import hu.petabyte.redflags.engine.gear.indicator.helper.KMonitorInstitutions;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Zsolt Jurányi
 */
public class KMonitorInstitutionsTest {

	private static final KMonitorInstitutions kmdb = new KMonitorInstitutions();

	@BeforeClass
	public static void init() {
		kmdb.setDbhost("kmonitor.hu");
		kmdb.setDbname("kmonitordb");
		kmdb.setDbpass("eEbuQeeV");
		kmdb.setDbuser("kmonitor_read1");
		kmdb.init();
	}

	private String qnp(String name) {
		System.out.println("T: " + name);
		String k = kmdb.findOrgName(name);
		System.out.println("K: " + k);
		return k;
	}

	@Test
	public void test() {
		// qnp("Mezőgazdasági és Vidékfejlesztési Hivatal");
		// qnp("Budapest Főváros Önkormányzata");
		// qnp("Budapest Főváros IV. kerület Újpest Önkormányzata");
		// qnp("Budapest Főváros VI. kerület Terézváros Önkormányzata");

		qnp("Budapest Főváros Önkormányzata");
		qnp("Budapest Főváros VI. kerület Terézváros Önkormányzata");
		qnp("Budapest Főváros VII. kerület Erzsébetváros Önkormányzata");
		qnp("Budapest Főváros XI. kerület Újbuda Önkormányzata");
		qnp("Budapest Főváros XI Kerület Újbuda Önkormányzata Gazdasági Műszaki Ellátó Szolgálat");
		qnp("Budapest Főváros XIV Kerület Zugló Önkormányzata");
		qnp("Budapest Főváros XVII. kerület Rákosmente Önkormányzata");
		qnp("Budapest Főváros XX. kerület, Pesterzsébet Önkormányzata");
		qnp("Budapest Főváros XXIII. Kerület Soroksár Önkormányzata");

	}
}
