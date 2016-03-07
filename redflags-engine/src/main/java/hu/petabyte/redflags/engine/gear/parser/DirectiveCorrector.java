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
package hu.petabyte.redflags.engine.gear.parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import hu.petabyte.redflags.engine.gear.AbstractGear;
import hu.petabyte.redflags.engine.model.Notice;

/**
 * We saw that 'Additional information' notices doesn't have the directive in
 * the data tab, so our DirectiveFilter would reject them. But we want to store
 * them with our exporter gear at the end, so we made this gear to fill in
 * directive from the document family. This gear calls MetadataParser on all
 * member notices.
 *
 * @author Zsolt Jurányi
 *
 */
@Component
public class DirectiveCorrector extends AbstractGear {

	private static final Logger LOG = LoggerFactory.getLogger(DirectiveCorrector.class);

	protected @Autowired MetadataParser parser;

	/**
	 * If the current notice doesn't have the directive value, we walk thru
	 * document family member notices and the first directive we'll find will be
	 * the directive of the current notice.
	 */
	@Override
	protected Notice processImpl(Notice notice) throws Exception {
		String directive = notice.getData().getDirective();
		if (null == directive) {
			for (Notice member : notice.getFamilyMembers()) {
				parser.process(member);
				String memberDirective = member.getData().getDirective();
				directive = null == memberDirective ? directive : memberDirective;
			}
			if (null != directive) {
				notice.getData().setDirective(directive);
				LOG.debug("{} directive set to: {}", notice.getId(), directive);
			}
		}
		return notice;
	}

}
