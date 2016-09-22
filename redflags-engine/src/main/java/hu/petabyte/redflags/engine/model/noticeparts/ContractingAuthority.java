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
public class ContractingAuthority {

	private Organization contractingOrg;
	private String id;
	private Organization obtainFurtherInfoFromOrg;
	private Organization obtainSpecsFromOrg;
	private Organization purchasingOnBehalfOfOrg;
	private boolean purchasingOnBehalfOfOther;
	private String rawPurchasingOnBehalfOfOther;
	private Organization sendTendersToOrg;

	public int getOffersCount() {
		return offersCount;
	}

	public void setOffersCount(int offersCount) {
		this.offersCount = offersCount;
	}

	private int offersCount;

	public ContractingAuthority() {
		// needed by BeanWrapper
	}

	public Organization getContractingOrg() {
		return contractingOrg;
	}

	public String getId() {
		return id;
	}

	public Organization getObtainFurtherInfoFromOrg() {
		return obtainFurtherInfoFromOrg;
	}

	public Organization getObtainSpecsFromOrg() {
		return obtainSpecsFromOrg;
	}

	public Organization getPurchasingOnBehalfOfOrg() {
		return purchasingOnBehalfOfOrg;
	}

	public String getRawPurchasingOnBehalfOfOther() {
		return rawPurchasingOnBehalfOfOther;
	}

	public Organization getSendTendersToOrg() {
		return sendTendersToOrg;
	}

	public boolean isPurchasingOnBehalfOfOther() {
		return purchasingOnBehalfOfOther;
	}

	public void setContractingOrg(Organization contractingOrg) {
		this.contractingOrg = contractingOrg;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setObtainFurtherInfoFromOrg(Organization obtainFurtherInfoFromOrg) {
		this.obtainFurtherInfoFromOrg = obtainFurtherInfoFromOrg;
	}

	public void setObtainSpecsFromOrg(Organization obtainSpecsFromOrg) {
		this.obtainSpecsFromOrg = obtainSpecsFromOrg;
	}

	public void setPurchasingOnBehalfOfOrg(Organization purchasingOnBehalfOfOrg) {
		this.purchasingOnBehalfOfOrg = purchasingOnBehalfOfOrg;
	}

	public void setPurchasingOnBehalfOfOther(boolean purchasingOnBehalfOfOther) {
		this.purchasingOnBehalfOfOther = purchasingOnBehalfOfOther;
	}

	public void setRawPurchasingOnBehalfOfOther(String rawPurchasingOnBehalfOfOther) {
		this.rawPurchasingOnBehalfOfOther = rawPurchasingOnBehalfOfOther;
	}

	public void setSendTendersToOrg(Organization sendTendersToOrg) {
		this.sendTendersToOrg = sendTendersToOrg;
	}

	@Override
	public String toString() {
		return "ContractingAuthority{" +
				"contractingOrg=" + contractingOrg +
				", id='" + id + '\'' +
				", obtainFurtherInfoFromOrg=" + obtainFurtherInfoFromOrg +
				", obtainSpecsFromOrg=" + obtainSpecsFromOrg +
				", purchasingOnBehalfOfOrg=" + purchasingOnBehalfOfOrg +
				", purchasingOnBehalfOfOther=" + purchasingOnBehalfOfOther +
				", rawPurchasingOnBehalfOfOther='" + rawPurchasingOnBehalfOfOther + '\'' +
				", sendTendersToOrg=" + sendTendersToOrg +
				'}';
	}
}
