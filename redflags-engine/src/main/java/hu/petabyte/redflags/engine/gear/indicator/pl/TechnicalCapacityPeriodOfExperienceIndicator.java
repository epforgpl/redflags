package hu.petabyte.redflags.engine.gear.indicator.pl;

import hu.petabyte.redflags.engine.gear.indicator.AbstractIndicator;
import hu.petabyte.redflags.engine.model.IndicatorResult;
import hu.petabyte.redflags.engine.model.Notice;
import hu.petabyte.redflags.engine.model.Type;
import hu.petabyte.redflags.engine.model.noticeparts.ObjOfTheContract;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by marcin on 21.08.2016.
 */
@Component
@ConfigurationProperties(prefix = "technicalCapacityPeriodOfExperienceIndicator")
public class TechnicalCapacityPeriodOfExperienceIndicator extends BZP2013Indicator {

    static final String PERIOD_OF_EXPERIENCE = "periodOfExperience";
    static final String ROADWORKS = "Roboty budowlane";
    static final String SERVICES = "Usługi";
    static final String SUPPLIES = "Dostawy";

    private static final String PATTERN = "w okresie ostatnich (.*) lat przed upływem terminu składania ofert";

    @Override
    public IndicatorResult flagImpl(Notice notice) {
        Type type = notice.getData().getContractType();
        if (type == null) {
            return null;
        }
        if (type.getName().equalsIgnoreCase(ROADWORKS)) {
            for (ObjOfTheContract o : notice.getObjs()) {
                if (o.getAdditionalInfo() != null && !o.getAdditionalInfo().trim().isEmpty()) {
                    return follows(o.getAdditionalInfo(), Arrays.asList("pięć", "pięciu", "5"));
                }
            }
        } else if (type.getName().equalsIgnoreCase(SERVICES) || type.getName().equalsIgnoreCase(SUPPLIES)) {
            for (ObjOfTheContract o : notice.getObjs()) {
                if (o.getAdditionalInfo() != null && !o.getAdditionalInfo().trim().isEmpty()) {
                    return follows(o.getAdditionalInfo(), Arrays.asList("trzy", "trzech", "3"));
                }
            }
        }

        return null;
    }


    private IndicatorResult follows(String experience, Collection allowed) {
        Pattern pattern = Pattern.compile(PATTERN);
        Matcher m = pattern.matcher(experience);
        if (m.find()) {
            String found = m.group(1);
            found = found!=null ? found.trim() : null;
            if (!allowed.contains(found)) {
                return returnFlag(PERIOD_OF_EXPERIENCE, "period=" + found);
            }
        }
        return null;
    }
}
