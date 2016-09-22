package hu.petabyte.redflags.engine.gear.indicator.pl;

import hu.petabyte.redflags.engine.gear.indicator.AbstractIndicator;
import hu.petabyte.redflags.engine.model.IndicatorResult;
import hu.petabyte.redflags.engine.model.Notice;
import hu.petabyte.redflags.engine.model.noticeparts.ObjOfTheContract;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 III.3.5. “Sytuacja ekonomiczna i finansowa”   – brak wpisu
 W przypadku badania postępowań ogłoszonych jeszcze w BZP2013 proponuję ograniczyć monitoring wyłącznie do robót budowlanych. W przypadku akceptacji mojej propozycji proszę dodatkowo uwzględnić
 II.1.2. “Rodzaj zamówienia” – zaznaczona opcja „roboty budowlane”
 */
@Component
@ConfigurationProperties(prefix = "economicAbilityNoMinReqsIndicator")
public class EconomicAbilityNoMinReqsIndicator extends BZP2013Indicator {

    private static final String ROADWORKS = "Roboty budowlane";

    @Override
    public IndicatorResult flagImpl(Notice notice) {

        boolean noMinReqs = true;
        if(notice.getData().getContractType()!=null && notice.getData().getContractType().getName().equalsIgnoreCase(ROADWORKS)){
            for(ObjOfTheContract o : notice.getObjs()){
                if(o.getFinancingConditions()!=null && !o.getFinancingConditions().trim().isEmpty()){
                    noMinReqs = false;
                }
            }
            if(noMinReqs){
                return returnFlag("noMinReqs");
            }
        }
        return null;
    }
}
