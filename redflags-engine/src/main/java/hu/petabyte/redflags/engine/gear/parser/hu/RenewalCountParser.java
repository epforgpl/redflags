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

import hu.petabyte.redflags.engine.gear.AbstractGear;
import hu.petabyte.redflags.engine.gear.parser.RawValueParser;
import hu.petabyte.redflags.engine.model.Duration;
import hu.petabyte.redflags.engine.model.Notice;
import hu.petabyte.redflags.engine.model.noticeparts.ObjOfTheContract;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Zsolt Jurányi
 *
 */
@Component
public class RenewalCountParser extends AbstractGear {

	private @Autowired RawValueParser rvp;

	@Override
	protected Notice processImpl(Notice notice) throws Exception {
		for (ObjOfTheContract o : notice.getObjs()) {
			if (0 == o.getRenewalCount()) {
				parseFrom27(o);
			}
			if (0 == o.getRenewalCount()) {
				parseFrom214(o);
			}
		}
		return notice;
	}

	private void parseFrom214(ObjOfTheContract o) {
		if (null != o.getAdditionalInfo()) {
			o.setRenewalCount(parseFromStr(o.getAdditionalInfo()));
			if (0 < o.getRenewalCount()) {
				o.setRenewable(true);
			}
		}
	}

	private void parseFrom27(ObjOfTheContract o) {
		Duration d = o.getRenewalDuration();
		if (null != d) {
			String r = d.getRaw();
			o.setRenewalCount(parseFromStr(r));
		}
	}

	private int parseFromStr(String s) {
		for (String line : s.split("\n")) {
			if (line.matches(".*szerződés[^.]+?(egyszer|egy alkalommal)[^.]+?meghosszabbítható.*")) {
				return 1;
			} else {
				Matcher m = Pattern
						.compile(
								"szerződés[^.]+?(?<c>\\d+) alkalommal[^.]+?meghosszabbítható")
						.matcher(line);
				if (m.find()) {
					return rvp.parseInteger(m.group("c"));
				}
			}
		}
		return 0;
	}

}
