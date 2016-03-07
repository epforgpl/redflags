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

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Zsolt Jurányi
 */
@Repository
public interface UserFilterRepo extends CrudRepository<UserFilter, Long> {

	UserFilter findByIdAndUserId(long id, long userId);

	List<UserFilter> findBySubscribeTrueOrderByUserIdAsc();

	List<UserFilter> findByUserId(long id, Sort sort);

	List<UserFilter> findByUserIdAndFilter(long id, String filter);
}
