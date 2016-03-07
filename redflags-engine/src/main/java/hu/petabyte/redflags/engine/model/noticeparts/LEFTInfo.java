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

/**
 * 
 * @author Zsolt Jurányi
 * 
 */
public class LEFTInfo {
	// Legal, economic, financial, technical information

	private String depositsAndGuarantees;
	private String executionStaff;
	private String financialAbility;
	private String financingConditions;
	private String id;
	private String legalFormToBeTaken;
	private String otherParticularConditions;
	private String particularProfession;
	private String personalSituation;
	private String qualificationForTheSystem;
	private String reservedContracts;
	private String technicalCapacity;

	public LEFTInfo() {
		// needed by BeanWrapper
	}

	public String getDepositsAndGuarantees() {
		return depositsAndGuarantees;
	}

	public String getExecutionStaff() {
		return executionStaff;
	}

	public String getFinancialAbility() {
		return financialAbility;
	}

	public String getFinancingConditions() {
		return financingConditions;
	}

	public String getId() {
		return id;
	}

	public String getLegalFormToBeTaken() {
		return legalFormToBeTaken;
	}

	public String getOtherParticularConditions() {
		return otherParticularConditions;
	}

	public String getParticularProfession() {
		return particularProfession;
	}

	public String getPersonalSituation() {
		return personalSituation;
	}

	public String getQualificationForTheSystem() {
		return qualificationForTheSystem;
	}

	public String getReservedContracts() {
		return reservedContracts;
	}

	public String getTechnicalCapacity() {
		return technicalCapacity;
	}

	public void setDepositsAndGuarantees(String depositsAndGuarantees) {
		this.depositsAndGuarantees = depositsAndGuarantees;
	}

	public void setExecutionStaff(String executionStaff) {
		this.executionStaff = executionStaff;
	}

	public void setFinancialAbility(String financialAbility) {
		this.financialAbility = financialAbility;
	}

	public void setFinancingConditions(String financingConditions) {
		this.financingConditions = financingConditions;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setLegalFormToBeTaken(String legalFormToBeTaken) {
		this.legalFormToBeTaken = legalFormToBeTaken;
	}

	public void setOtherParticularConditions(String otherParticularConditions) {
		this.otherParticularConditions = otherParticularConditions;
	}

	public void setParticularProfession(String particularProfession) {
		this.particularProfession = particularProfession;
	}

	public void setPersonalSituation(String personalSituation) {
		this.personalSituation = personalSituation;
	}

	public void setQualificationForTheSystem(String qualificationForTheSystem) {
		this.qualificationForTheSystem = qualificationForTheSystem;
	}

	public void setReservedContracts(String reservedContracts) {
		this.reservedContracts = reservedContracts;
	}

	public void setTechnicalCapacity(String technicalCapacity) {
		this.technicalCapacity = technicalCapacity;
	}

}
