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

import static hu.petabyte.redflags.engine.util.CompanyNameUtils.normalizeCompanyNameForHash;
import static hu.petabyte.redflags.engine.util.CompanyNameUtils.normalizeCompanyNameForUser;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * @author Zsolt Jurányi
 */
public class OrgNormTest {

	private String norm(String raw) {
		return normalizeCompanyNameForHash(normalizeCompanyNameForUser(raw));
	}

	private String readable(String raw) {
		return normalizeCompanyNameForUser(raw);
	}

	@Test
	public void sameHashTest() {
		String etalon = norm("Egertej Tejipari Kft.");
		assertEquals(etalon, norm("\"Egertej\" Tejipari Kft. "));
		assertEquals(etalon, norm("‘Egertej’ Tejipari Kft."));
		assertEquals(etalon, norm("„Egertej” Tejipari Kft"));
		assertEquals(etalon, norm("„Egertej” Tejipari Kft."));

		etalon = norm("EUROMEDIC-PHARMA Gyógyszer-nagykereskedelmi Zártkörűen Működő Részvénytársaság");
		assertEquals(
				etalon,
				norm("EUROMEDIC-PHARMA Gyógyszernagykereskedelmi Zártkörűen Működő Részvénytársaság"));
		assertEquals(
				etalon,
				norm("Euromedic-Pharma Gyógyszernagykereskedelmi Zártkörűen Működő Részvénytársaság"));

		etalon = norm("BKK Budapesti Közlekedési Központ Zártkörűen Működő Részvénytársaság");
		assertEquals(etalon, norm("BKK Budapesti Közlekedési Központ Zrt."));
		assertEquals(
				etalon,
				norm("BKK Budapesti Közlekedési Központ, Zártkörűen Működő Részvénytársaság"));

		etalon = norm("Baja Város Önkormányzat");
		assertEquals(etalon, norm("Baja Városi Önkormányzat"));
		assertEquals(etalon, norm("Baja Város Önkormányzata"));
	}

	@Test
	public void readableTest() {
		assertEquals("Valami Bt.", readable("Valami Betéti Társaság"));

		String etalon = readable("Egertej Tejipari Kft.");
		assertEquals(etalon, readable("\"Egertej\" Tejipari Kft. "));
		assertEquals(etalon, readable("‘Egertej’ Tejipari Kft."));
		assertEquals(etalon, readable("„Egertej” Tejipari Kft"));
		assertEquals(etalon, readable("„Egertej” Tejipari Kft."));
	}
}
