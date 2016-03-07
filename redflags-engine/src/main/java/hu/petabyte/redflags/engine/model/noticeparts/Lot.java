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

import java.util.ArrayList;
import java.util.List;

import hu.petabyte.redflags.engine.model.CPV;
import hu.petabyte.redflags.engine.model.Duration;

/**
 *
 * @author Zsolt Jurányi
 *
 */
public class Lot {

	private String additionalInfo;
	private List<CPV> cpvCodes = new ArrayList<CPV>();
	private Duration differentDuration;
	private String id;
	private int number;
	private String quantity;
	private String rawCpvCodes;
	private String rawNumber;
	private String shortDescription;
	private String title;

	public Lot() {
	}

	public String getAdditionalInfo() {
		return additionalInfo;
	}

	public List<CPV> getCpvCodes() {
		return cpvCodes;
	}

	public Duration getDifferentDuration() {
		return differentDuration;
	}

	public String getId() {
		return id;
	}

	public int getNumber() {
		return number;
	}

	public String getQuantity() {
		return quantity;
	}

	public String getRawCpvCodes() {
		return rawCpvCodes;
	}

	public String getRawNumber() {
		return rawNumber;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public String getTitle() {
		return title;
	}

	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}

	public void setCpvCodes(List<CPV> cpvCodes) {
		this.cpvCodes = cpvCodes;
	}

	public void setDifferentDuration(Duration differentDuration) {
		this.differentDuration = differentDuration;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public void setRawCpvCodes(String rawCpvCodes) {
		this.rawCpvCodes = rawCpvCodes;
	}

	public void setRawNumber(String rawNumber) {
		this.rawNumber = rawNumber;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String toString() {
		return "Lot [number=" + number + ", title=" + title + ", ...]";
	}
}
