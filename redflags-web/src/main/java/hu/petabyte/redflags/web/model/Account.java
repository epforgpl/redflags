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

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Zsolt Jurányi
 */
@Entity
@Table(name = "rfwl_users")
public class Account {

	@Column
	private Boolean active;
	@Column(nullable = false)
	private String cryptedPassword;
	@Column(nullable = false)
	private String emailAddress;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Column
	private String name;
	@Column
	private Date registeredAt;
	@Column
	private String rememberToken;
	@Column
	private Date rememberTokenExpiresAt;
	@Column
	private String lang = "en";

	public Account() {
		super();
	}

	public Account(Long id, String cryptedPassword, String rememberToken,
			Date rememberTokenExpiresAt, String name, String emailAddress,
			Date registeredAt, Boolean active) {
		this(cryptedPassword, rememberToken, rememberTokenExpiresAt, name,
				emailAddress, registeredAt, active);
		this.id = id;
	}

	public Account(String cryptedPassword, String rememberToken,
			Date rememberTokenExpiresAt, String name, String emailAddress,
			Date registeredAt, Boolean active) {
		this();
		this.cryptedPassword = cryptedPassword;
		this.rememberToken = rememberToken;
		this.rememberTokenExpiresAt = rememberTokenExpiresAt;
		this.name = name;
		this.emailAddress = emailAddress;
		this.registeredAt = registeredAt;
		this.active = active;
	}

	public Boolean getActive() {
		return active;
	}

	public String getCryptedPassword() {
		return cryptedPassword;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public Long getId() {
		return id;
	}

	public String getLang() {
		return lang;
	}

	public String getName() {
		return name;
	}

	public Date getRegisteredAt() {
		return registeredAt;
	}

	public String getRememberToken() {
		return rememberToken;
	}

	public Date getRememberTokenExpiresAt() {
		return rememberTokenExpiresAt;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public void setCryptedPassword(String cryptedPassword) {
		this.cryptedPassword = cryptedPassword;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setRegisteredAt(Date registeredAt) {
		this.registeredAt = registeredAt;
	}

	public void setRememberToken(String rememberToken) {
		this.rememberToken = rememberToken;
	}

	public void setRememberTokenExpiresAt(Date rememberTokenExpiresAt) {
		this.rememberTokenExpiresAt = rememberTokenExpiresAt;
	}

	@Override
	public String toString() {
		return "Account [id=" + id + ", cryptedPassword=" + cryptedPassword
				+ ", rememberToken=" + rememberToken
				+ ", rememberTokenExpiresAt=" + rememberTokenExpiresAt
				+ ", name=" + name + ", emailAddress=" + emailAddress
				+ ", registeredAt=" + registeredAt + ", active=" + active + "]";
	}

}
