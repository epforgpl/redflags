package hu.petabyte.redflags.engine.gear.indicator.hu.depr;

import hu.petabyte.redflags.engine.gear.indicator.AbstractTD3CIndicator;
import hu.petabyte.redflags.engine.model.IndicatorResult;
import hu.petabyte.redflags.engine.model.Notice;
import hu.petabyte.redflags.engine.model.noticeparts.ObjOfTheContract;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Zsolt Jurányi
 */
@Deprecated
@Component
@ConfigurationProperties(prefix = "renewableContractIndicator")
public class RenewableContractIndicator extends AbstractTD3CIndicator {

	@Override
	protected IndicatorResult flagImpl(Notice notice) {
		boolean found = false;
		for (ObjOfTheContract obj : notice.getObjs()) {
			if (null != obj.getRawRenewable()) {
				found = true;
			}
			if (obj.isRenewable()) {
				return returnFlag();
			}
		}
		if (!found) {
			return missingData();
		}
		return null;
	}

}
