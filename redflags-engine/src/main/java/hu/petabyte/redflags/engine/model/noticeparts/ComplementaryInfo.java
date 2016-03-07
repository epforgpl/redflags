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
package hu.petabyte.redflags.engine.model.noticeparts;

import hu.petabyte.redflags.engine.model.Organization;

/**
 *
 * @author Zsolt Jurányi
 *
 */
public class ComplementaryInfo {

	private String additionalInfo;
	private String genRegFWInfo;
	private long guaranteeValue;
	private String guaranteeValueCurr;
	private String id;
	private String lodgingOfAppeals;
	private Organization obtainLodgingOfAppealsInfoFromOrg;
	private String rawRelToEUProjects;
	private String recurrence;
	private String refsToEUProjects;
	private boolean relToEUProjects;
	private Organization respForAppealOrg;

	public ComplementaryInfo() {
		// needed by BeanWrapper
	}

	public String getAdditionalInfo() {
		return additionalInfo;
	}

	public String getGenRegFWInfo() {
		return genRegFWInfo;
	}

	public long getGuaranteeValue() {
		return guaranteeValue;
	}

	public String getGuaranteeValueCurr() {
		return guaranteeValueCurr;
	}

	public String getId() {
		return id;
	}

	public String getLodgingOfAppeals() {
		return lodgingOfAppeals;
	}

	public Organization getObtainLodgingOfAppealsInfoFromOrg() {
		return obtainLodgingOfAppealsInfoFromOrg;
	}

	public String getRawRelToEUProjects() {
		return rawRelToEUProjects;
	}

	public String getRecurrence() {
		return recurrence;
	}

	public String getRefsToEUProjects() {
		return refsToEUProjects;
	}

	public Organization getRespForAppealOrg() {
		return respForAppealOrg;
	}

	public boolean isRelToEUProjects() {
		return relToEUProjects;
	}

	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}

	public void setGenRegFWInfo(String genRegFWInfo) {
		this.genRegFWInfo = genRegFWInfo;
	}

	public void setGuaranteeValue(long guaranteeValue) {
		this.guaranteeValue = guaranteeValue;
	}

	public void setGuaranteeValueCurr(String guaranteeValueCurr) {
		this.guaranteeValueCurr = guaranteeValueCurr;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setLodgingOfAppeals(String lodgingOfAppeals) {
		this.lodgingOfAppeals = lodgingOfAppeals;
	}

	public void setObtainLodgingOfAppealsInfoFromOrg(Organization obtainLodgingOfAppealsInfoFromOrg) {
		this.obtainLodgingOfAppealsInfoFromOrg = obtainLodgingOfAppealsInfoFromOrg;
	}

	public void setRawRelToEUProjects(String rawRelToEUProjects) {
		this.rawRelToEUProjects = rawRelToEUProjects;
	}

	public void setRecurrence(String recurrence) {
		this.recurrence = recurrence;
	}

	public void setRefsToEUProjects(String refsToEUProjects) {
		this.refsToEUProjects = refsToEUProjects;
	}

	public void setRelToEUProjects(boolean relToEUProjects) {
		this.relToEUProjects = relToEUProjects;
	}

	public void setRespForAppealOrg(Organization respForAppealOrg) {
		this.respForAppealOrg = respForAppealOrg;
	}
}
