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
package hu.petabyte.redflags.api.service.impl;

import hu.petabyte.redflags.api.model.ApiUser;
import hu.petabyte.redflags.api.repository.ApiUserRepository;
import hu.petabyte.redflags.api.service.ApiUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Péter Szűcs
 */
@Service
public class ApiUserServiceImpl implements ApiUserService {

	@Autowired
	private ApiUserRepository apiUserRepository;

	@Override
	public ApiUser findByEmail(String email) {
		return apiUserRepository.findByEmail(email);
	}

	@Override
	public ApiUser findByAccessToken(String accessToken) {
		return apiUserRepository.findByAccessToken(accessToken);
	}

	@Override
	public void save(ApiUser apiUser) {
		apiUserRepository.save(apiUser);
	}

}
