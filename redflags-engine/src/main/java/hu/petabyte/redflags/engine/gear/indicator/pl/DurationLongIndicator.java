package hu.petabyte.redflags.engine.gear.indicator.pl;

import hu.petabyte.redflags.engine.gear.indicator.AbstractIndicator;
import hu.petabyte.redflags.engine.model.IndicatorResult;
import hu.petabyte.redflags.engine.model.Notice;
import hu.petabyte.redflags.engine.model.noticeparts.ObjOfTheContract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Term of the contract (long or indefinite) (2.2.1.10)
 *
 II.2 – wskazany okres przekracza 4 lata
 ponadto:
 II.1.7. – zaznaczona opcja „TAK”
 Załącznik I (informacje dotyczące ofert częściowych) pkt 3 - wskazany okres przekracza 4 lata
 */

// TODO map framework agreement
@Component
@ConfigurationProperties(prefix = "durationLongIndicator")
public class DurationLongIndicator extends BZP2013Indicator {

    private static final Logger LOG = LoggerFactory.getLogger(DurationLongIndicator.class);

    @Override
    public IndicatorResult flagImpl(Notice notice) {
        for(ObjOfTheContract o : notice.getObjs()){
            if(!Boolean.TRUE.toString().equals(o.getFrameworkAgreement()) && o.getFrameworkDuration().getInMonths()>4*12){

                return returnFlag("durationLong", "months=" + o.getFrameworkDuration().getInMonths());
            }
        }
        return null;
    }
}
