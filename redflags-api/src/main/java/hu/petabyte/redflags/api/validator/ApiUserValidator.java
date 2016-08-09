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
package hu.petabyte.redflags.api.validator;

import hu.petabyte.redflags.api.model.ApiUser;
import hu.petabyte.redflags.api.repository.ApiUserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * @author Péter Szűcs
 */
@Component("apiUserValidator")
public class ApiUserValidator implements Validator {

	@Autowired
	private ApiUserRepository apiUserRepository;
	
	@Override
	public boolean supports(Class<?> clazz) {
		return ApiUser.class.equals(clazz);
	}
	
	@Override
	public void validate(Object target, Errors errors) {
		ApiUser a = (ApiUser) target;
		boolean emailExists = false;
		
		if(a.getEmail() == null || a.getEmail().equals("")){
			errors.reject("emailError", "validator.apiUser.error.emailError");
		} else {
			ApiUser apiUser = (ApiUser) apiUserRepository.findByEmail(a.getEmail());
			if(apiUser != null) {
				emailExists = true;
			}
			if(emailExists) {
				errors.reject("emailExistsError", "validator.apiUser.error.emailExists");
			}
		}
		
		if(a.getName() == null || a.getName().equals("")){
			errors.reject("nameError", "validator.apiUser.error.nameError");
		}
	}
	
}