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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import hu.petabyte.redflags.engine.gear.AbstractGear;
import hu.petabyte.redflags.engine.gear.parser.RawValueParser;
import hu.petabyte.redflags.engine.model.Duration;
import hu.petabyte.redflags.engine.model.Notice;
import hu.petabyte.redflags.engine.model.noticeparts.ObjOfTheContract;

/**
 * @author Zsolt Jurányi
 */
@Component
public class FrameworkAgreementParser extends AbstractGear {

	private @Autowired RawValueParser rvp;

	public RawValueParser getRvp() {
		return rvp;
	}

	@Override
	protected Notice processImpl(Notice notice) throws Exception {
		for (ObjOfTheContract obj : notice.getObjs()) {
			if (null != obj.getFrameworkAgreement()) {
				String[] p = obj.getFrameworkAgreement().split("A keretmegállapodás időtartama");
				if (p.length > 1) {
					String raw = p[1].trim().split("\n")[0];
					Duration d = new Duration();
					d.setRaw(raw);
					rvp.parseDuration(d);
					obj.setFrameworkDuration(d);
				}
			}

			String s = obj.getFrameworkAgreement();
			if (null != s) {
				String regex = "Keretmegállapodás (?<a>egy|több) ajánlattevővel(.*?(?<b>\\d+).*idő)?";
				Pattern p = Pattern.compile(regex, Pattern.DOTALL);
				Matcher m = p.matcher(s);
				if (m.find()) {
					if ("egy".equals(m.group("a"))) {
						obj.setFrameworkParticipants(1);
					} else {
						obj.setFrameworkParticipants(Integer.parseInt(m.group("b")));
					}
				}
			}
		}
		return notice;
	}

	public void setRvp(RawValueParser rvp) {
		this.rvp = rvp;
	}

}
