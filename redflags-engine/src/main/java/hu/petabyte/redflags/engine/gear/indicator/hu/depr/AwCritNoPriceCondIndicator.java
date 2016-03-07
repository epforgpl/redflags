package hu.petabyte.redflags.engine.gear.indicator.hu.depr;

import hu.petabyte.redflags.engine.gear.indicator.AbstractTD3CIndicator;
import hu.petabyte.redflags.engine.model.IndicatorResult;
import hu.petabyte.redflags.engine.model.Notice;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Zsolt Jurányi
 */
@Deprecated
@Component
@ConfigurationProperties(prefix = "awCritNoPriceCondIndicator")
public class AwCritNoPriceCondIndicator extends AbstractTD3CIndicator {

	@Override
	protected IndicatorResult flagImpl(Notice notice) {
		if (!fetchAwardCriteria(notice).equals("AC-2")) {
			return irrelevantData();
		}

		if (null != notice.getProc()
				&& !notice.getProc().isAwardCriteriaPriceCond()) {
			return returnFlag();
		}
		return null;
	}

}
