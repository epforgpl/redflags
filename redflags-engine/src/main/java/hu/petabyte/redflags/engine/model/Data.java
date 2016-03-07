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
package hu.petabyte.redflags.engine.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Zsolt Jurányi
 *
 */
public class Data {

	private Type authorityType;
	private Type awardCriteria;
	private Type bidType;
	private Type contractType;
	private String country;
	private List<CPV> cpvCodes = new ArrayList<CPV>();
	private Date deadline;
	private Date deadlineForDocs;
	private String directive;
	private Date documentSent;
	private Type documentType;
	private String id;
	private String internetAddress;
	private List<String> nutsCodes = new ArrayList<String>();
	private String oj;
	private List<CPV> originalCpvCodes = new ArrayList<CPV>();
	private String originalLanguage;
	private String place;
	private Type procedureType;
	private Date publicationDate;
	private Type regulationType;
	private String title;

	public Data() {
		// needed by BeanWrapper
	}

	public Type getAuthorityType() {
		return authorityType;
	}

	public Type getAwardCriteria() {
		return awardCriteria;
	}

	public Type getBidType() {
		return bidType;
	}

	public Type getContractType() {
		return contractType;
	}

	public String getCountry() {
		return country;
	}

	public List<CPV> getCpvCodes() {
		return cpvCodes;
	}

	public Date getDeadline() {
		return deadline;
	}

	public Date getDeadlineForDocs() {
		return deadlineForDocs;
	}

	public String getDirective() {
		return directive;
	}

	public Date getDocumentSent() {
		return documentSent;
	}

	public Type getDocumentType() {
		return documentType;
	}

	public String getId() {
		return id;
	}

	public String getInternetAddress() {
		return internetAddress;
	}

	public List<String> getNutsCodes() {
		return nutsCodes;
	}

	public String getOj() {
		return oj;
	}

	public List<CPV> getOriginalCpvCodes() {
		return originalCpvCodes;
	}

	public String getOriginalLanguage() {
		return originalLanguage;
	}

	public String getPlace() {
		return place;
	}

	public Type getProcedureType() {
		return procedureType;
	}

	public Date getPublicationDate() {
		return publicationDate;
	}

	public Type getRegulationType() {
		return regulationType;
	}

	public String getTitle() {
		return title;
	}

	public void setAuthorityType(Type authorityType) {
		this.authorityType = authorityType;
	}

	public void setAwardCriteria(Type awardCriteria) {
		this.awardCriteria = awardCriteria;
	}

	public void setBidType(Type bidType) {
		this.bidType = bidType;
	}

	public void setContractType(Type contractType) {
		this.contractType = contractType;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public void setCpvCodes(List<CPV> cpvCodes) {
		this.cpvCodes = cpvCodes;
	}

	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}

	public void setDeadlineForDocs(Date deadlineForDocs) {
		this.deadlineForDocs = deadlineForDocs;
	}

	public void setDirective(String directive) {
		this.directive = directive;
	}

	public void setDocumentSent(Date documentSent) {
		this.documentSent = documentSent;
	}

	public void setDocumentType(Type documentType) {
		this.documentType = documentType;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setInternetAddress(String internetAddress) {
		this.internetAddress = internetAddress;
	}

	public void setNutsCodes(List<String> nutsCodes) {
		this.nutsCodes = nutsCodes;
	}

	public void setOj(String oj) {
		this.oj = oj;
	}

	public void setOriginalCpvCodes(List<CPV> originalCpvCodes) {
		this.originalCpvCodes = originalCpvCodes;
	}

	public void setOriginalLanguage(String originalLanguage) {
		this.originalLanguage = originalLanguage;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public void setProcedureType(Type procedureType) {
		this.procedureType = procedureType;
	}

	public void setPublicationDate(Date publicationDate) {
		this.publicationDate = publicationDate;
	}

	public void setRegulationType(Type regulationType) {
		this.regulationType = regulationType;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String toString() {
		return "Data [documentType=" + documentType + ", originalLanguage=" + originalLanguage + "]";
	}

}
