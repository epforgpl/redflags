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
package hu.petabyte.redflags.web.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

/**
 * @author Zsolt Jurányi
 */
@Entity
@Table(name = "rfwl_userfilters")
public class UserFilter {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@NotNull
	private Long userId;
	@NotNull
	private String filter;
	private String name;
	private boolean subscribe;
	private Integer lastSentNumber;
	private Integer lastSentYear;

	@Transient
	private List<Filter> filters;

	public UserFilter() {
		super();
	}

	public UserFilter(Long userId, String filter, boolean subscribe) {
		super();
		this.userId = userId;
		this.filter = filter;
		this.subscribe = subscribe;
	}

	public String getFilter() {
		return filter;
	}

	public List<Filter> getFilters() {
		return filters;
	}

	public Long getId() {
		return id;
	}

	public Integer getLastSentNumber() {
		return lastSentNumber;
	}

	public Integer getLastSentYear() {
		return lastSentYear;
	}

	public String getName() {
		return name;
	}

	public Long getUserId() {
		return userId;
	}

	public boolean isSubscribe() {
		return subscribe;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	public void setFilters(List<Filter> filters) {
		this.filters = filters;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setLastSentNumber(Integer lastSentNumber) {
		this.lastSentNumber = lastSentNumber;
	}

	public void setLastSentYear(Integer lastSentYear) {
		this.lastSentYear = lastSentYear;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSubscribe(boolean subscribe) {
		this.subscribe = subscribe;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "UserFilter [id=" + id + ", userId=" + userId + ", filter="
				+ filter + ", name=" + name + ", subscribe=" + subscribe + "]";
	}

}
