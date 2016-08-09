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
package hu.petabyte.redflags.api;

import hu.petabyte.redflags.web.svc.NoticeSvc;
import hu.petabyte.redflags.web.svc.NoticesSvc;
import hu.petabyte.redflags.web.svc.OrganizationSvc;
import hu.petabyte.redflags.web.svc.OrganizationsSvc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @author Péter Szűcs
 */
@SpringBootApplication
public class Application {

	@Bean
	public NoticeSvc noticeSvc() {
		return new NoticeSvc();
	}

	@Bean
	public NoticesSvc noticesSvc() {
		return new NoticesSvc();
	}

	@Bean
	public OrganizationsSvc organizationsSvc() {
		return new OrganizationsSvc();
	}

	@Bean
	public OrganizationSvc organizationSvc() {
		return new OrganizationSvc();
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}