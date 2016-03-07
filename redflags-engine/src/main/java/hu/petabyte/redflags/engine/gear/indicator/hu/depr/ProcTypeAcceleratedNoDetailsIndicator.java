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
@ConfigurationProperties(prefix = "procTypeAcceleratedNoDetailsIndicator")
public class ProcTypeAcceleratedNoDetailsIndicator extends
AbstractTD3CIndicator {

	@Override
	protected IndicatorResult flagImpl(Notice notice) {
		String procType = fetchProcedureType(notice);
		if (!procType.matches("PR-[36]")) {
			return irrelevantData();
		}

		if (fetchProcedureTypeInfo(notice).split("\n").length < 2) {
			return returnFlag();
		}
		return null;
	}
}
