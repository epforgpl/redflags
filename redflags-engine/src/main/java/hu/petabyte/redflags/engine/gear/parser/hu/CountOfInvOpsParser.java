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
package hu.petabyte.redflags.engine.gear.parser.hu;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import hu.petabyte.redflags.engine.gear.AbstractGear;
import hu.petabyte.redflags.engine.model.Notice;

/**
 * @author Zsolt Jurányi
 */
@Component
public class CountOfInvOpsParser extends AbstractGear {

	protected static final Pattern COUNT_OF_INV_OPS = Pattern
			.compile("([Tt]ervezett.*?(?<c1>\\d+)|maximum (?<c2>\\d+) alkalmas .*jelentkez)");

	@Override
	protected Notice processImpl(Notice notice) throws Exception {
		if (null != notice.getProc() && null != notice.getProc().getLimitOfInvitedOperators()) {
			String cip = notice.getProc().getLimitOfInvitedOperators();
			String i = cip.split("\n")[0];
			Matcher m = COUNT_OF_INV_OPS.matcher(i);
			if (m.find()) {
				if (null != m.group("c1")) {
					notice.getProc().setCountOfInvitedOperators(Integer.parseInt(m.group("c1")));
				} else if (null != m.group("c2")) {
					notice.getProc().setCountOfInvitedOperators(Integer.parseInt(m.group("c2")));
				}
			}
		}
		return notice;
	}

}
