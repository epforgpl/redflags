package hu.petabyte.redflags.engine.gear.indicator.pl;

import hu.petabyte.redflags.engine.gear.indicator.AbstractIndicator;
import hu.petabyte.redflags.engine.model.IndicatorResult;
import hu.petabyte.redflags.engine.model.Notice;
import hu.petabyte.redflags.engine.model.noticeparts.ObjOfTheContract;

/**
 Część wstępna ogłoszenia – zaznaczona opcja „ogłoszenie dotyczy zawarcia umowy ramowej”
 II.2 – wskazany okres przekracza 4 lata
 */
public class DurationLongFrameworkIndicator extends BZP2013Indicator {

    @Override
    public IndicatorResult flagImpl(Notice notice) {
        for(ObjOfTheContract o : notice.getObjs()){
            if(Boolean.TRUE.toString().equals(o.getFrameworkAgreement()) && o.getFrameworkDuration().getInMonths()>4*12){

                return returnFlag("durationLongFramework", "months=" + o.getFrameworkDuration().getInMonths());
            }
        }
        return null;
    }
}
