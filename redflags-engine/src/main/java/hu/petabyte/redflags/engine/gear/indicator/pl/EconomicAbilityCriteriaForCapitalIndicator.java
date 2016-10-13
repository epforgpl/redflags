package hu.petabyte.redflags.engine.gear.indicator.pl;

import hu.petabyte.redflags.engine.gear.indicator.AbstractIndicator;
import hu.petabyte.redflags.engine.model.IndicatorResult;
import hu.petabyte.redflags.engine.model.Notice;
import hu.petabyte.redflags.engine.model.noticeparts.ObjOfTheContract;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by marcin on 21.08.2016.
 */
@Component
@ConfigurationProperties(prefix = "economicAbilityCriteriaForCapitalIndicator")

public class EconomicAbilityCriteriaForCapitalIndicator extends BZP2013Indicator {

    private static final String[] SHARE_CAPITAL_PHRASES = {
            "kapitał zakładowy",
            "kapitału zakładowego",
            "kapitałowi zakładowemu",
            "kapitałem zakładowym",
            "kapitale zakładowym",
            "kapitał akcyjny",
            "kapitału akcyjnego",
            "kapitałowi akcyjnemu",
            "kapitałem akcyjnym",
            "kapitale akcyjny"};

    @Override
    public IndicatorResult flagImpl(Notice notice) {
        for(ObjOfTheContract o : notice.getObjs()){
            if(o.getFinancingConditions()!=null && !o.getFinancingConditions().trim().isEmpty()){
               if(containsProhibitedPhrases(o.getFinancingConditions(), SHARE_CAPITAL_PHRASES)){
                    return returnFlag("criteriaForCapital");
                }
            }
        }
        return null;
    }

    private boolean containsProhibitedPhrases(String sentence, String[] phrases){
        for(String phrase : phrases){
            if(sentence.toUpperCase().contains(phrase.toUpperCase())){
                return true;
            }
        }
        return false;
    }
}
