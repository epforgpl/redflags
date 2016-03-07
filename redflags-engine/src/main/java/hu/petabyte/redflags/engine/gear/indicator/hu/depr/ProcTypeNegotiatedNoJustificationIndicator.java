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
@ConfigurationProperties(prefix = "procTypeNegotiatedNoJustificationIndicator")
public class ProcTypeNegotiatedNoJustificationIndicator extends
AbstractTD3CIndicator {

	@Override
	protected IndicatorResult flagImpl(Notice notice) {
		String procType = fetchProcedureType(notice);
		if (!procType.matches("PR-4")) {
			return irrelevantData();
		}

		String s = fetchAdditionalInfo(notice);
		for (String line : s.split("\n")) {
			if (line.matches(".*89.*?(\\(2\\))?.*?[a-d][\\),].*")
					|| line.matches(".*([Ee]ljárás|[Tt]árgyalás)( alkalmazásának)? indok.*")
					|| line.matches(/* a) */".*eredménytelen.*feltételek időközben lényegesen nem változtak meg.*")
					|| line.matches(
							/* b) */".*objektív természete.*kockázatok.*ellenszolgáltatás( előzetes)?.*meghatározás.*")
					|| line.matches(
									/* c) */".*kutatási.*kísérleti.*fejlesztési.*piacképesség.*kutatásfejlesztés költség.*")
					|| line.matches(
											/* d) */".*nem (határozható|lehetséges).*?(olyan|kell).*?(pontossággal|részletességgel).*")) {
				return null;
			}
		}

		return returnFlag();
	}
}
