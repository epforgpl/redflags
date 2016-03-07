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
import java.util.List;

/**
 *
 * @author Zsolt Jurányi
 *
 */
public class Organization {

	private Address address;
	private String code;
	private String id;
	private List<String> mainActivities = new ArrayList<String>();
	private String name;
	private String rawMainActivities;
	private String type;

	public Organization() {
		// needed by BeanWrapper
	}

	public Address getAddress() {
		return address;
	}

	public String getCode() {
		return code;
	}

	public String getId() {
		return id;
	}

	public List<String> getMainActivities() {
		return mainActivities;
	}

	public String getName() {
		return name;
	}

	public String getRawMainActivities() {
		return rawMainActivities;
	}

	public String getType() {
		return type;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setMainActivities(List<String> mainActivities) {
		this.mainActivities = mainActivities;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setRawMainActivities(String rawMainActivities) {
		this.rawMainActivities = rawMainActivities;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "Org(name=" + name + ", address=" + address + ")";
	}

}
