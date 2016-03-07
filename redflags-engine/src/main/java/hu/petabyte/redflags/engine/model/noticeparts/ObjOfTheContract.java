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

import java.util.Date;

import hu.petabyte.redflags.engine.model.Duration;

/**
 *
 * @author Zsolt Jurányi
 *
 */
public class ObjOfTheContract {

	private String additionalInfo;
	private String contractTitle;
	private String contractTypeInfo;
	private Duration duration;
	private long estimatedValue;
	private String estimatedValueCurr;
	private long estimatedValueMin;
	private String financingConditions;
	private String frameworkAgreement;
	private Duration frameworkDuration;
	private int frameworkParticipants;
	private String gpa;
	private String id;
	private String lots;
	private String options;
	private String pcFaDps;
	private String placeOfPerformance;
	private Date plannedStartDate;
	private String rawPlannedStartDate;
	private String rawRenewable;
	private String rawRenewalCount;
	private String rawTotalFinalValue;
	private String rawTotalFinalValueVat;
	private boolean renewable;
	private int renewalCount;
	private Duration renewalDuration;
	private String shortDescription;
	private long totalFinalValue;
	private String totalFinalValueCurr;
	private double totalFinalValueVat;
	private String totalQuantity;
	private String variants;

	public ObjOfTheContract() {
		// needed by BeanWrapper
	}

	public String getAdditionalInfo() {
		return additionalInfo;
	}

	public String getContractTitle() {
		return contractTitle;
	}

	public String getContractTypeInfo() {
		return contractTypeInfo;
	}

	public Duration getDuration() {
		return duration;
	}

	public long getEstimatedValue() {
		return estimatedValue;
	}

	public String getEstimatedValueCurr() {
		return estimatedValueCurr;
	}

	public long getEstimatedValueMin() {
		return estimatedValueMin;
	}

	public String getFinancingConditions() {
		return financingConditions;
	}

	public String getFrameworkAgreement() {
		return frameworkAgreement;
	}

	public Duration getFrameworkDuration() {
		return frameworkDuration;
	}

	public int getFrameworkParticipants() {
		return frameworkParticipants;
	}

	public String getGpa() {
		return gpa;
	}

	public String getId() {
		return id;
	}

	public String getLots() {
		return lots;
	}

	public String getOptions() {
		return options;
	}

	public String getPcFaDps() {
		return pcFaDps;
	}

	public String getPlaceOfPerformance() {
		return placeOfPerformance;
	}

	public Date getPlannedStartDate() {
		return plannedStartDate;
	}

	public String getRawPlannedStartDate() {
		return rawPlannedStartDate;
	}

	public String getRawRenewable() {
		return rawRenewable;
	}

	public String getRawRenewalCount() {
		return rawRenewalCount;
	}

	public String getRawTotalFinalValue() {
		return rawTotalFinalValue;
	}

	public String getRawTotalFinalValueVat() {
		return rawTotalFinalValueVat;
	}

	public int getRenewalCount() {
		return renewalCount;
	}

	public Duration getRenewalDuration() {
		return renewalDuration;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public long getTotalFinalValue() {
		return totalFinalValue;
	}

	public String getTotalFinalValueCurr() {
		return totalFinalValueCurr;
	}

	public double getTotalFinalValueVat() {
		return totalFinalValueVat;
	}

	public String getTotalQuantity() {
		return totalQuantity;
	}

	public String getVariants() {
		return variants;
	}

	public boolean isRenewable() {
		return renewable;
	}

	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}

	public void setContractTitle(String contractTitle) {
		this.contractTitle = contractTitle;
	}

	public void setContractTypeInfo(String contractTypeInfo) {
		this.contractTypeInfo = contractTypeInfo;
	}

	public void setDuration(Duration duration) {
		this.duration = duration;
	}

	public void setEstimatedValue(long estimatedValue) {
		this.estimatedValue = estimatedValue;
	}

	public void setEstimatedValueCurr(String estimatedValueCurr) {
		this.estimatedValueCurr = estimatedValueCurr;
	}

	public void setEstimatedValueMin(long estimatedValueMin) {
		this.estimatedValueMin = estimatedValueMin;
	}

	public void setFinancingConditions(String financingConditions) {
		this.financingConditions = financingConditions;
	}

	public void setFrameworkAgreement(String frameworkAgreement) {
		this.frameworkAgreement = frameworkAgreement;
	}

	public void setFrameworkDuration(Duration frameworkDuration) {
		this.frameworkDuration = frameworkDuration;
	}

	public void setFrameworkParticipants(int frameworkParticipants) {
		this.frameworkParticipants = frameworkParticipants;
	}

	public void setGpa(String gpa) {
		this.gpa = gpa;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setLots(String lots) {
		this.lots = lots;
	}

	public void setOptions(String options) {
		this.options = options;
	}

	public void setPcFaDps(String pcFaDps) {
		this.pcFaDps = pcFaDps;
	}

	public void setPlaceOfPerformance(String placeOfPerformance) {
		this.placeOfPerformance = placeOfPerformance;
	}

	public void setPlannedStartDate(Date plannedStartDate) {
		this.plannedStartDate = plannedStartDate;
	}

	public void setRawPlannedStartDate(String rawPlannedStartDate) {
		this.rawPlannedStartDate = rawPlannedStartDate;
	}

	public void setRawRenewable(String rawRenewable) {
		this.rawRenewable = rawRenewable;
	}

	public void setRawRenewalCount(String rawRenewalCount) {
		this.rawRenewalCount = rawRenewalCount;
	}

	public void setRawTotalFinalValue(String rawTotalFinalValue) {
		this.rawTotalFinalValue = rawTotalFinalValue;
	}

	public void setRawTotalFinalValueVat(String rawTotalFinalValueVat) {
		this.rawTotalFinalValueVat = rawTotalFinalValueVat;
	}

	public void setRenewable(boolean renewable) {
		this.renewable = renewable;
	}

	public void setRenewalCount(int renewalCount) {
		this.renewalCount = renewalCount;
	}

	public void setRenewalDuration(Duration renewalDuration) {
		this.renewalDuration = renewalDuration;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public void setTotalFinalValue(long totalFinalValue) {
		this.totalFinalValue = totalFinalValue;
	}

	public void setTotalFinalValueCurr(String totalFinalValueCurr) {
		this.totalFinalValueCurr = totalFinalValueCurr;
	}

	public void setTotalFinalValueVat(double totalFinalValueVat) {
		this.totalFinalValueVat = totalFinalValueVat;
	}

	public void setTotalQuantity(String totalQuantity) {
		this.totalQuantity = totalQuantity;
	}

	public void setVariants(String variants) {
		this.variants = variants;
	}

	@Override
	public String toString() {
		return "ObjOfTheContract [additionalInfo=" + additionalInfo + ", contractTitle=" + contractTitle
				+ ", contractTypeInfo=" + contractTypeInfo + ", duration=" + duration + ", estimatedValue="
				+ estimatedValue + ", estimatedValueCurr=" + estimatedValueCurr + ", financingConditions="
				+ financingConditions + ", frameworkAgreement=" + frameworkAgreement + ", gpa=" + gpa + ", lots=" + lots
				+ ", options=" + options + ", pcFaDps=" + pcFaDps + ", placeOfPerformance=" + placeOfPerformance
				+ ", plannedStartDate=" + plannedStartDate + ", renewable=" + renewable + ", renewalCount="
				+ renewalCount + ", renewalDuration=" + renewalDuration + ", shortDescription=" + shortDescription
				+ ", totalFinalValue=" + totalFinalValue + ", totalFinalValueCurr=" + totalFinalValueCurr
				+ ", totalFinalValueVat=" + totalFinalValueVat + ", totalQuantity=" + totalQuantity + ", variants="
				+ variants + "]";
	}

}
