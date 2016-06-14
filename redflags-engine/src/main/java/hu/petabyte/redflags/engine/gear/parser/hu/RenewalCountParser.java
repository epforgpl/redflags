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
