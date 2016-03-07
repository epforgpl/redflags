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

import hu.petabyte.redflags.engine.model.Organization;

/**
 *
 * @author Zsolt Jurányi
 *
 */
public class Award {

	private Date decisionDate;
	private String id;
	private int lotNumber;
	private String lotTitle;
	private int number;
	private int numberOfOffers;
	private String rawDecisionDate;
	private String rawHeader;
	private String rawLotNumber;
	private String rawNumber;
	private String rawNumberOfOffers;
	private String rawTotalEstimatedValue;
	private String rawTotalEstimatedValueVat;
	private String rawTotalFinalValue;
	private String rawTotalFinalValueVat;
	private String subcontracting;
	private double subcontractingRate;
	private long totalEstimatedValue;
	private String totalEstimatedValueCurr;
	private double totalEstimatedValueVat;
	private long totalFinalValue;
	private String totalFinalValueCurr;
	private double totalFinalValueVat;
	private Organization winnerOrg;

	public Award() {
		// needed by BeanWrapper
	}

	public Date getDecisionDate() {
		return decisionDate;
	}

	public String getId() {
		return id;
	}

	public int getLotNumber() {
		return lotNumber;
	}

	public String getLotTitle() {
		return lotTitle;
	}

	public int getNumber() {
		return number;
	}

	public int getNumberOfOffers() {
		return numberOfOffers;
	}

	public String getRawDecisionDate() {
		return rawDecisionDate;
	}

	public String getRawHeader() {
		return rawHeader;
	}

	public String getRawLotNumber() {
		return rawLotNumber;
	}

	public String getRawNumber() {
		return rawNumber;
	}

	public String getRawNumberOfOffers() {
		return rawNumberOfOffers;
	}

	public String getRawTotalEstimatedValue() {
		return rawTotalEstimatedValue;
	}

	public String getRawTotalEstimatedValueVat() {
		return rawTotalEstimatedValueVat;
	}

	public String getRawTotalFinalValue() {
		return rawTotalFinalValue;
	}

	public String getRawTotalFinalValueVat() {
		return rawTotalFinalValueVat;
	}

	public String getSubcontracting() {
		return subcontracting;
	}

	public double getSubcontractingRate() {
		return subcontractingRate;
	}

	public long getTotalEstimatedValue() {
		return totalEstimatedValue;
	}

	public String getTotalEstimatedValueCurr() {
		return totalEstimatedValueCurr;
	}

	public double getTotalEstimatedValueVat() {
		return totalEstimatedValueVat;
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

	public Organization getWinnerOrg() {
		return winnerOrg;
	}

	public void setDecisionDate(Date decisionDate) {
		this.decisionDate = decisionDate;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setLotNumber(int lotNumber) {
		this.lotNumber = lotNumber;
	}

	public void setLotTitle(String lotTitle) {
		this.lotTitle = lotTitle;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public void setNumberOfOffers(int numberOfOffers) {
		this.numberOfOffers = numberOfOffers;
	}

	public void setRawDecisionDate(String rawDecisionDate) {
		this.rawDecisionDate = rawDecisionDate;
	}

	public void setRawHeader(String rawHeader) {
		this.rawHeader = rawHeader;
	}

	public void setRawLotNumber(String rawLotNumber) {
		this.rawLotNumber = rawLotNumber;
	}

	public void setRawNumber(String rawNumber) {
		this.rawNumber = rawNumber;
	}

	public void setRawNumberOfOffers(String rawNumberOfOffers) {
		this.rawNumberOfOffers = rawNumberOfOffers;
	}

	public void setRawTotalEstimatedValue(String rawTotalEstimatedValue) {
		this.rawTotalEstimatedValue = rawTotalEstimatedValue;
	}

	public void setRawTotalEstimatedValueVat(String rawTotalEstimatedValueVat) {
		this.rawTotalEstimatedValueVat = rawTotalEstimatedValueVat;
	}

	public void setRawTotalFinalValue(String rawTotalFinalValue) {
		this.rawTotalFinalValue = rawTotalFinalValue;
	}

	public void setRawTotalFinalValueVat(String rawTotalFinalValueVat) {
		this.rawTotalFinalValueVat = rawTotalFinalValueVat;
	}

	public void setSubcontracting(String subcontracting) {
		this.subcontracting = subcontracting;
	}

	public void setSubcontractingRate(double subcontractingRate) {
		this.subcontractingRate = subcontractingRate;
	}

	public void setTotalEstimatedValue(long totalEstimatedValue) {
		this.totalEstimatedValue = totalEstimatedValue;
	}

	public void setTotalEstimatedValueCurr(String totalEstimatedValueCurr) {
		this.totalEstimatedValueCurr = totalEstimatedValueCurr;
	}

	public void setTotalEstimatedValueVat(double totalEstimatedValueVat) {
		this.totalEstimatedValueVat = totalEstimatedValueVat;
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

	public void setWinnerOrg(Organization winnerOrg) {
		this.winnerOrg = winnerOrg;
	}

	@Override
	public String toString() {
		return "Award(decisionDate=" + decisionDate + ", lotNumber=" + lotNumber + ", number=" + number
				+ ", numberOfOffers=" + numberOfOffers + ", totalEstimatedValue=" + totalEstimatedValue
				+ ", totalFinalValue=" + totalFinalValue + ", winnerOrg=" + winnerOrg + ")";
	}
}
