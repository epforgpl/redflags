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
public class Procedure {

	private String awardCriteria;
	private int awardCriteriaCondCount;
	private boolean awardCriteriaPaymentDeadline;
	private boolean awardCriteriaPriceCond;
	private double awardCriteriaPriceWeight;
	private int awardCriteriaSubCondCount;
	private double awardCriteriaWeightSum;
	private int countOfInvitedOperators;
	private String electronicAuction;
	private String fileRefNumber;
	private String id;
	private Date interestDeadline;
	private Date invitationsDispatchDate;
	private String limitOfInvitedOperators;
	private Duration minMaintainDuration;
	private String obtainingSpecs;
	private String openingConditions;
	private Date openingDate;
	private String previousPublication;
	private String procedureTypeInfo;
	private Duration qualificationSystemDuration;
	private String rawCountOfInvitedOperators;
	private String rawInterestDeadline;
	private String rawInvitationsDispatchDate;
	private String reductionOfOperators;
	private String renewalInfo;
	private String tenderLanguage;

	public Procedure() {
		// needed by BeanWrapper
	}

	public String getAwardCriteria() {
		return awardCriteria;
	}

	public int getAwardCriteriaCondCount() {
		return awardCriteriaCondCount;
	}

	public double getAwardCriteriaPriceWeight() {
		return awardCriteriaPriceWeight;
	}

	public int getAwardCriteriaSubCondCount() {
		return awardCriteriaSubCondCount;
	}

	public double getAwardCriteriaWeightSum() {
		return awardCriteriaWeightSum;
	}

	public int getCountOfInvitedOperators() {
		return countOfInvitedOperators;
	}

	public String getElectronicAuction() {
		return electronicAuction;
	}

	public String getFileRefNumber() {
		return fileRefNumber;
	}

	public String getId() {
		return id;
	}

	public Date getInterestDeadline() {
		return interestDeadline;
	}

	public Date getInvitationsDispatchDate() {
		return invitationsDispatchDate;
	}

	public String getLimitOfInvitedOperators() {
		return limitOfInvitedOperators;
	}

	public Duration getMinMaintainDuration() {
		return minMaintainDuration;
	}

	public String getObtainingSpecs() {
		return obtainingSpecs;
	}

	public String getOpeningConditions() {
		return openingConditions;
	}

	public Date getOpeningDate() {
		return openingDate;
	}

	public String getPreviousPublication() {
		return previousPublication;
	}

	public String getProcedureTypeInfo() {
		return procedureTypeInfo;
	}

	public Duration getQualificationSystemDuration() {
		return qualificationSystemDuration;
	}

	public String getRawCountOfInvitedOperators() {
		return rawCountOfInvitedOperators;
	}

	public String getRawInterestDeadline() {
		return rawInterestDeadline;
	}

	public String getRawInvitationsDispatchDate() {
		return rawInvitationsDispatchDate;
	}

	public String getReductionOfOperators() {
		return reductionOfOperators;
	}

	public String getRenewalInfo() {
		return renewalInfo;
	}

	public String getTenderLanguage() {
		return tenderLanguage;
	}

	public boolean isAwardCriteriaPaymentDeadline() {
		return awardCriteriaPaymentDeadline;
	}

	public boolean isAwardCriteriaPriceCond() {
		return awardCriteriaPriceCond;
	}

	public void setAwardCriteria(String awardCriteria) {
		this.awardCriteria = awardCriteria;
	}

	public void setAwardCriteriaCondCount(int awardCriteriaCondCount) {
		this.awardCriteriaCondCount = awardCriteriaCondCount;
	}

	public void setAwardCriteriaPaymentDeadline(boolean awardCriteriaPaymentDeadline) {
		this.awardCriteriaPaymentDeadline = awardCriteriaPaymentDeadline;
	}

	public void setAwardCriteriaPriceCond(boolean awardCriteriaPriceCond) {
		this.awardCriteriaPriceCond = awardCriteriaPriceCond;
	}

	public void setAwardCriteriaPriceWeight(double awardCriteriaPriceWeight) {
		this.awardCriteriaPriceWeight = awardCriteriaPriceWeight;
	}

	public void setAwardCriteriaSubCondCount(int awardCriteriaSubCondCount) {
		this.awardCriteriaSubCondCount = awardCriteriaSubCondCount;
	}

	public void setAwardCriteriaWeightSum(double awardCriteriaWeightSum) {
		this.awardCriteriaWeightSum = awardCriteriaWeightSum;
	}

	public void setCountOfInvitedOperators(int countOfInvitedOperators) {
		this.countOfInvitedOperators = countOfInvitedOperators;
	}

	public void setElectronicAuction(String electronicAuction) {
		this.electronicAuction = electronicAuction;
	}

	public void setFileRefNumber(String fileRefNumber) {
		this.fileRefNumber = fileRefNumber;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setInterestDeadline(Date interestDeadline) {
		this.interestDeadline = interestDeadline;
	}

	public void setInvitationsDispatchDate(Date invitationsDispatchDate) {
		this.invitationsDispatchDate = invitationsDispatchDate;
	}

	public void setLimitOfInvitedOperators(String limitOfInvitedOperators) {
		this.limitOfInvitedOperators = limitOfInvitedOperators;
	}

	public void setMinMaintainDuration(Duration minMaintainDuration) {
		this.minMaintainDuration = minMaintainDuration;
	}

	public void setObtainingSpecs(String obtainingSpecs) {
		this.obtainingSpecs = obtainingSpecs;
	}

	public void setOpeningConditions(String openingConditions) {
		this.openingConditions = openingConditions;
	}

	public void setOpeningDate(Date openingDate) {
		this.openingDate = openingDate;
	}

	public void setPreviousPublication(String previousPublication) {
		this.previousPublication = previousPublication;
	}

	public void setProcedureTypeInfo(String procedureTypeInfo) {
		this.procedureTypeInfo = procedureTypeInfo;
	}

	public void setQualificationSystemDuration(Duration qualificationSystemDuration) {
		this.qualificationSystemDuration = qualificationSystemDuration;
	}

	public void setRawCountOfInvitedOperators(String rawCountOfInvitedOperators) {
		this.rawCountOfInvitedOperators = rawCountOfInvitedOperators;
	}

	public void setRawInterestDeadline(String rawInterestDeadline) {
		this.rawInterestDeadline = rawInterestDeadline;
	}

	public void setRawInvitationsDispatchDate(String rawInvitationsDispatchDate) {
		this.rawInvitationsDispatchDate = rawInvitationsDispatchDate;
	}

	public void setReductionOfOperators(String reductionOfOperators) {
		this.reductionOfOperators = reductionOfOperators;
	}

	public void setRenewalInfo(String renewalInfo) {
		this.renewalInfo = renewalInfo;
	}

	public void setTenderLanguage(String tenderLanguage) {
		this.tenderLanguage = tenderLanguage;
	}

}
