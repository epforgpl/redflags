package hu.petabyte.redflags.engine.gear.indicator.pl;

import hu.petabyte.redflags.engine.gear.indicator.pl.TechnicalCapacityPeriodOfExperienceIndicator;
import hu.petabyte.redflags.engine.model.IndicatorResult;
import hu.petabyte.redflags.engine.model.Notice;
import hu.petabyte.redflags.engine.model.NoticeID;
import hu.petabyte.redflags.engine.model.Type;
import hu.petabyte.redflags.engine.model.noticeparts.ObjOfTheContract;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Created by marcin on 13.10.2016.
 */
public class TechnicalCapacityPeriodOfExperienceIndicatorTest {

    TechnicalCapacityPeriodOfExperienceIndicator ind = null;

    @Before
    public void prepare(){
        ind = new TechnicalCapacityPeriodOfExperienceIndicator();
    }


    @Test
    public void testValidRoadworksFiveNumber(){

        Notice n = mockNotice(TechnicalCapacityPeriodOfExperienceIndicator.ROADWORKS, "lorem ipsum w okresie ostatnich 5 przed upływem terminu składania ofert lorem ipsum ");
        IndicatorResult r = ind.flag(n);
        Assert.assertTrue("no flag should be assigned. Flag description: " + (r!=null ? r.getDescription() : "null"), r==null || IndicatorResult.IndicatorResultType.NO_FLAG==r.getType());
    }

    @Test
    public void testValidRoadworksFiveWord(){

        Notice n = mockNotice(TechnicalCapacityPeriodOfExperienceIndicator.ROADWORKS, "lorem ipsum w okresie ostatnich pięć lat przed upływem terminu składania ofert lorem ipsum ");
        IndicatorResult r = ind.flag(n);
        Assert.assertTrue("no flag should be assigned. Flag description: " + (r!=null ? r.getDescription() : "null"), r==null || IndicatorResult.IndicatorResultType.NO_FLAG==r.getType());
    }

    @Test
    public void testValidRoadworksFiveWordV2(){

        Notice n = mockNotice(TechnicalCapacityPeriodOfExperienceIndicator.ROADWORKS, "lorem ipsum w okresie ostatnich pięciu lat przed upływem terminu składania ofert lorem ipsum ");
        IndicatorResult r = ind.flag(n);
        Assert.assertTrue("no flag should be assigned. Flag description: " + (r!=null ? r.getDescription() : "null"), r==null || IndicatorResult.IndicatorResultType.NO_FLAG==r.getType());
    }

    @Test
    public void testValidServicesThreeNumber(){

        Notice n = mockNotice(TechnicalCapacityPeriodOfExperienceIndicator.SERVICES, "lorem ipsum w okresie ostatnich 3 lat przed upływem terminu składania ofert lorem ipsum ");
        IndicatorResult r = ind.flag(n);
        Assert.assertTrue("no flag should be assigned. Flag description: " + (r!=null ? r.getDescription() : "null"), r==null || IndicatorResult.IndicatorResultType.NO_FLAG==r.getType());
    }

    @Test
    public void testValidServicesThreeWord(){

        Notice n = mockNotice(TechnicalCapacityPeriodOfExperienceIndicator.SERVICES, "lorem ipsum w okresie ostatnich trzech lat przed upływem terminu składania ofert lorem ipsum ");
        IndicatorResult r = ind.flag(n);
        Assert.assertTrue("no flag should be assigned. Flag description: " + (r!=null ? r.getDescription() : "null"), r==null || IndicatorResult.IndicatorResultType.NO_FLAG==r.getType());
    }

    @Test
    public void testInvalidServicesFourNumber(){

        Notice n = mockNotice(TechnicalCapacityPeriodOfExperienceIndicator.SERVICES, "lorem ipsum w okresie ostatnich 4 lat przed upływem terminu składania ofert lorem ipsum ");
        IndicatorResult r = ind.flag(n);
        Assert.assertTrue("flag should be assigned. Flag description: " + (r!=null ? r.getDescription() : "null"),
                r!=null && IndicatorResult.IndicatorResultType.NO_FLAG!=r.getType() && r.getDescription().contains(TechnicalCapacityPeriodOfExperienceIndicator.PERIOD_OF_EXPERIENCE));
    }

    @Test
    public void testInvalidRoadworksThreeNumber(){

        Notice n = mockNotice(TechnicalCapacityPeriodOfExperienceIndicator.ROADWORKS, "lorem ipsum w okresie ostatnich 3 lat przed upływem terminu składania ofert lorem ipsum ");
        IndicatorResult r = ind.flag(n);
        Assert.assertTrue("flag should be assigned. Flag description: " + (r!=null ? r.getDescription() : "null"),
                r!=null && IndicatorResult.IndicatorResultType.NO_FLAG!=r.getType() && r.getDescription().contains(TechnicalCapacityPeriodOfExperienceIndicator.PERIOD_OF_EXPERIENCE));
    }

    private Notice mockNotice(String type, String info) {
        ObjOfTheContract obj = new ObjOfTheContract();
        obj.setAdditionalInfo(info);
        Notice n = new Notice(new NoticeID(1));
        n.getObjs().add(obj);
        n.getData().setContractType(Type.findOrCreate(null,type));
        return n;
    }
}
