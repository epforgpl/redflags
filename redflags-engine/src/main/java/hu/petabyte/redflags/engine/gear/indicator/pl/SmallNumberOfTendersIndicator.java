package hu.petabyte.redflags.engine.gear.indicator.pl;

import hu.petabyte.redflags.engine.gear.indicator.AbstractIndicator;
import hu.petabyte.redflags.engine.model.IndicatorResult;
import hu.petabyte.redflags.engine.model.Notice;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by marcin on 22.08.2016.
 */
@Component
@ConfigurationProperties(prefix = "smallNumberOfTendersIndicator")
public class SmallNumberOfTendersIndicator extends BZP2013Indicator {

    @Override
    public IndicatorResult flagImpl(Notice notice) {
        int offersCount = notice.getContr()!=null ? notice.getContr().getOffersCount() : -1;
        if(offersCount<=2 && offersCount >0){
            return returnFlag("smallNumberOfTenders", "count=" + offersCount);
        }
        return null;
    }
}
