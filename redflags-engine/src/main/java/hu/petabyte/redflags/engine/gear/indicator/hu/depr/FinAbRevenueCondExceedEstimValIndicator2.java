package hu.petabyte.redflags.engine.gear.indicator.hu.depr;

import hu.petabyte.redflags.engine.gear.indicator.AbstractTD3CIndicator;
import hu.petabyte.redflags.engine.gear.indicator.helper.HuIndicatorHelper;
import hu.petabyte.redflags.engine.model.IndicatorResult;
import hu.petabyte.redflags.engine.model.Notice;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Zsolt Jurányi
 */
@Deprecated
@Component
@ConfigurationProperties(prefix = "finAbRevenueCondExceedEstimValIndicator2")
public class FinAbRevenueCondExceedEstimValIndicator2 extends
AbstractTD3CIndicator {

	private static final Logger LOG = LoggerFactory
			.getLogger(FinAbRevenueCondExceedEstimValIndicator2.class);

	@Override
	protected IndicatorResult flagImpl(Notice notice) {
		String s = String.format("%s\n%s", //
				fetchFinancialAbility(notice), //
				fetchAdditionalInfo(notice) //
				).trim();
		long estimVal = fetchEstimatedValue(notice);
		String estimCurr = fetchEstimatedValueCurrency(notice);
		if (s.isEmpty() || 0L == estimVal || null == estimCurr) {
			return missingData();
		}

		s = s.replaceAll("\\([^\\)]+\\)", "");
		s = HuIndicatorHelper.words2Digits(s);

		if (estimCurr.matches("Ft|forint")) {
			estimCurr = "HUF";
		}

		Pattern p = Pattern
				.compile("teljes( nettó)? árbevétel.+?[^0-9] (?<v>\\d{1,3}( ?\\d{3}){2,10})(?<c> [A-Za-z]+)[^A-Za-z]");

		long sum = 0;
		for (String line : s.split("\n")) {
			Matcher m = p.matcher(line);

			// exclude
			if (line.matches("Valamennyi részre történő ajánlattétel esetén.*")) {
				continue;
			}

			if (m.find()) {
				long v = Long.parseLong("0" + m.group("v").replaceAll(" ", ""));
				String c = m.group("c").trim();
				if (c.matches("Ft|forint.*")) {
					c = "HUF";
				}
				if ("eFt".equals(c)) {
					v *= 1000;
					c = "HUF";
				}
				if (c.equals(estimCurr)) {
					sum += v;
				}
			}
		}

		LOG.trace("{}, revenue condition value = {}, estimated value = {}",
				notice.getId(), sum, estimVal);
		if (sum > estimVal) {
			return returnFlag("info", "revenue=" + (sum / 1000000), "estim="
					+ (estimVal / 1000000), "curr=" + estimCurr);
		}
		return null;
	}
}
