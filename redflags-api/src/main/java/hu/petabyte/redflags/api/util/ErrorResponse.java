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
package hu.petabyte.redflags.api.util;

/**
 * @author Péter Szűcs
 */
public class ErrorResponse {

	String parameter;
	String title;
	String detail;
	String status;
	String pointer;
	boolean isMessageFromProperty;
	
	public ErrorResponse(String parameter, String title, String detail, String status, String pointer, boolean isMessageFromProperty) {
		super();
		this.parameter = parameter;
		this.title = title;
		this.detail = detail;
		this.status = status;
		this.pointer = pointer;
		this.isMessageFromProperty = isMessageFromProperty;
	}

	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	public String getTitle() {
		return title;
	}

	public boolean isMessageFromProperty() {
		return isMessageFromProperty;
	}

	public void setMessageFromProperty(boolean isMessageFromProperty) {
		this.isMessageFromProperty = isMessageFromProperty;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPointer() {
		return pointer;
	}

	public void setPointer(String pointer) {
		this.pointer = pointer;
	}
	
}
