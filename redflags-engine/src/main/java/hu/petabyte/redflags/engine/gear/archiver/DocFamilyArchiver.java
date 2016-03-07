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
package hu.petabyte.redflags.engine.gear.archiver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import hu.petabyte.redflags.engine.gear.AbstractGear;
import hu.petabyte.redflags.engine.model.Notice;

/**
 * @author Zsolt Jurányi
 */
@Component
public class DocFamilyArchiver extends AbstractGear {

	private static final Logger LOG = LoggerFactory.getLogger(DocFamilyArchiver.class);

	protected @Autowired Archiver archiver;

	public Archiver getArchiver() {
		return archiver;
	}

	@Override
	protected Notice processImpl(Notice notice) throws Exception {
		for (Notice member : notice.getFamilyMembers()) {
			LOG.debug("Archiving member notice of {}: {}", notice.getId(), member.getId());
			archiver.process(member);
		}
		return notice;
	}

	public void setArchiver(Archiver archiver) {
		this.archiver = archiver;
	}

}
