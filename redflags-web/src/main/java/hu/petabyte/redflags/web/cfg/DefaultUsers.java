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
package hu.petabyte.redflags.web.cfg;

import java.nio.charset.Charset;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

/**
 * @author Zsolt Jurányi
 */
@Service
public class DefaultUsers implements InitializingBean {

	private @Autowired JdbcTemplate jdbc;
	private @Value("classpath:default-users.sql") Resource sqlFile;

	@Override
	public void afterPropertiesSet() throws Exception {
		String sql = StreamUtils.copyToString(sqlFile.getInputStream(),
				Charset.forName("UTF-8"));
		jdbc.execute(sql);

		// Why we need it this way: because the original JPA version was working
		// differently on different servers. On the first server it replaced
		// existing records (good), on the other server, it added new ones.
	}

}
