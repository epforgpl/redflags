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
import hu.petabyte.redflags.engine.model.Notice;
import hu.petabyte.redflags.engine.model.noticeparts.Lot;
import hu.petabyte.redflags.engine.model.noticeparts.ObjOfTheContract;
import hu.petabyte.redflags.engine.util.StrUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Zsolt Jurányi
 */
@Component
public class EstimatedValueParser extends AbstractGear {

	private static final Logger LOG = LoggerFactory
			.getLogger(EstimatedValueParser.class);
	protected static final Pattern ESTIM_VAL_PATTERN = Pattern
			.compile("^((Becsült( nettó)? érték( áfa nélkül)?|Áfa nélkül):?( nettó)?|.*?becsült.*? (összérték|nettó).*?) (?<v>[0-9 ,]+ )(?<c>[A-Z]{2,})[^\\/]*$|^(?<min>[0-9 ,]+ )és (?<max>[0-9 ,]+ )között (?<c2>[A-Z]+)$");
	protected static final Pattern EXCL_PATTERN = Pattern
			.compile("beruházás|építés|kivitelezés|projekt|tartalékkeretet tartalmaz|tartalék(kal|kerettel) együtt|[Ee]lszámolható|tartalékkeret|támogatott feladat|szolgáltatás|rekonstrukció|feladat|eszköz");

	private @Autowired RawValueParser rvp;

	public RawValueParser getRvp() {
		return rvp;
	}

	private boolean parseFromLots(Notice notice) {
		for (Lot lot : notice.getLots()) {
			String q = lot.getQuantity();
			if (null != q) {
				String r = StrUtils.lastLineOf(q);
				Matcher m = ESTIM_VAL_PATTERN.matcher(r);
				long ev = 0, em = 0;
				String ec = null;
				while (m.find()) {
					String v = m.group("v");
					String c = m.group("c");
					String min = m.group("min");
					String max = m.group("max");
					String c2 = m.group("c2");
					LOG.trace("Lot parser method found: " + v + "," + c + ","
							+ min + "," + max + "," + c2);
					ev = rvp.parseLong(null == max ? v : max);
					em = rvp.parseLong(null == min ? v : min);
					ec = null == c ? c2 : c;
				}
				if (!notice.getObjs().isEmpty()) {
					ev += notice.getObjs().get(0).getEstimatedValue();
					em += notice.getObjs().get(0).getEstimatedValueMin();
					notice.getObjs().get(0).setEstimatedValue(ev);
					notice.getObjs().get(0).setEstimatedValueMin(em);
					notice.getObjs().get(0).setEstimatedValueCurr(ec);
				} // currency is dirty...
			}
		}
		return !notice.getObjs().isEmpty()
				&& notice.getObjs().get(0).getEstimatedValue() > 0;
	}

	private void parseFromObj(Notice notice) {
		for (ObjOfTheContract obj : notice.getObjs()) {
			StringBuilder sb = new StringBuilder();
			sb.append(obj.getTotalQuantity());
			sb.append("\n");
			sb.append(obj.getShortDescription());
			sb.append("\n");
			sb.append(obj.getFrameworkAgreement());

			String s = sb.toString();
			if (null != s) {
				long ev = 0, em = 0;
				for (String line : s.split("\n")) {
					if (EXCL_PATTERN.matcher(line).find()) {
						continue;
					}
					Matcher m = ESTIM_VAL_PATTERN.matcher(line);
					while (m.find()) {
						String v = m.group("v");
						String c = m.group("c");
						String min = m.group("min");
						String max = m.group("max");
						String c2 = m.group("c2");
						LOG.trace("Obj parser method found: " + v + "," + c
								+ "," + min + "," + max + "," + c2);
						ev = Math.max(ev, rvp.parseLong(null == max ? v : max));
						em = Math.max(em, rvp.parseLong(null == min ? v : min));
						obj.setEstimatedValueCurr(null == c ? c2 : c);

					}
				}
				obj.setEstimatedValue(ev);
				obj.setEstimatedValueMin(em);

				// VAT? examples?
			}
		}
	}

	@Override
	protected Notice processImpl(Notice notice) throws Exception {
		boolean foundInLots = parseFromLots(notice);
		if (!foundInLots) {
			parseFromObj(notice);
			if (!notice.getObjs().isEmpty()) {
				LOG.trace("Notice {}, estimated before fix: {}", notice.getId()
						.toString(), notice.getObjs().get(0)
						.getEstimatedValue());
			}
			fixFromTotalQ(notice); // experimental
		}
		return notice;
	}

	/**
	 * Experimental feature. It made a lot of wrong decisions and results.
	 *
	 * @param notice
	 */
	@Deprecated
	private void fixFromTotalQ(Notice notice) {
		for (ObjOfTheContract obj : notice.getObjs()) {

			if (notice.getObjs().get(0).getEstimatedValueMin() < notice
					.getObjs().get(0).getEstimatedValue()) {
				LOG.trace(
						"Estimated value fix skipped, we found minimum earlier: {}",
						notice.getObjs().get(0).getEstimatedValueMin());
				return;
			}

			StringBuilder sb = new StringBuilder();
			sb.append(obj.getTotalQuantity());

			List<Long> evs = new ArrayList<Long>();

			for (String line : sb.toString().split("\n")) {
				if (EXCL_PATTERN.matcher(line).find()) {
					continue;
				}
				Matcher m = ESTIM_VAL_PATTERN.matcher(line);
				while (m.find()) {
					String v = m.group("v");
					String c = m.group("c");
					String min = m.group("min");
					String max = m.group("max");
					String c2 = m.group("c2");
					LOG.trace("Fixer method found: " + v + "," + c + "," + min
							+ "," + max + "," + c2);
					evs.add(rvp.parseLong(null == max ? v : max));
				}
			}

			long sum = 0;
			long last = 0;
			for (Long ev : evs) {
				sum += ev;
				last = ev;
			}
			if (0 < sum) {
				if (2 < evs.size() && sum / 2 == last) {
					// if SUM(a[1..N-1]) == a[N]
					obj.setEstimatedValue(last);
					LOG.trace("Notice {}, estimated value fixed: last = {}",
							notice.getId().toString(), last);
				} else if (1 < evs.size()) {
					obj.setEstimatedValue(sum);
					LOG.trace("Notice {}, estimated value fixed: sum = {}",
							notice.getId().toString(), sum);
				}
			}
		}
	}

	public void setRvp(RawValueParser rvp) {
		this.rvp = rvp;
	}

}
