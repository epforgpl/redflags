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
        int offersCount = -1;
        if(notice.getObjs()!=null && notice.getObjs().size()>0 && notice.getObjs().get(0).getOffersCount()!=null){
            offersCount = notice.getObjs().get(0).getOffersCount();
        }
        if(offersCount<=2 && offersCount >0){
            return returnFlag("smallNumberOfTenders", "count=" + offersCount);
        }
        return null;
    }
}
