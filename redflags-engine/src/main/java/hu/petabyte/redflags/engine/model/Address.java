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

/**
 * 
 * @author Zsolt Jurányi
 * 
 */
public class Address {

	private String buyerProfileUrl;
	private String city;
	private String contactPerson;
	private String contactPoint;
	private String country;
	private String email;
	private String fax;
	private String id;
	private String infoUrl;
	private String organizationId;
	private String phone;
	private String raw;
	private String street;
	private String url;
	private String zip;

	public Address() {
		// needed by BeanWrapper
	}

	public String getBuyerProfileUrl() {
		return buyerProfileUrl;
	}

	public String getCity() {
		return city;
	}

	public String getContactPerson() {
		return contactPerson;
	}

	public String getContactPoint() {
		return contactPoint;
	}

	public String getCountry() {
		return country;
	}

	public String getEmail() {
		return email;
	}

	public String getFax() {
		return fax;
	}

	public String getId() {
		return id;
	}

	public String getInfoUrl() {
		return infoUrl;
	}

	public String getOrganizationId() {
		return organizationId;
	}

	public String getPhone() {
		return phone;
	}

	public String getRaw() {
		return raw;
	}

	public String getStreet() {
		return street;
	}

	public String getUrl() {
		return url;
	}

	public String getZip() {
		return zip;
	}

	public void setBuyerProfileUrl(String buyerProfileUrl) {
		this.buyerProfileUrl = buyerProfileUrl;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	public void setContactPoint(String contactPoint) {
		this.contactPoint = contactPoint;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setInfoUrl(String infoUrl) {
		this.infoUrl = infoUrl;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setRaw(String raw) {
		this.raw = raw;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	@Override
	public String toString() {
		return "Address(zip=" + zip + ", city=" + city + ", street=" + street
				+ ")";
	}

}
