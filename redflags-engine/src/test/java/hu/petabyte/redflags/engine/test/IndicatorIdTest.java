package hu.petabyte.redflags.engine.test;

import static org.junit.Assert.assertEquals;
import hu.petabyte.redflags.engine.gear.indicator.AbstractIndicator;
import hu.petabyte.redflags.engine.gear.indicator.hu.AwCritLacksIndicator;

import org.junit.Test;

/**
 * @author Zsolt Jurányi
 */
public class IndicatorIdTest {

	@Test
	public void test() {
		AbstractIndicator indicator = new AwCritLacksIndicator();
		assertEquals("hu.AwCritLacksIndicator", indicator.getIndicatorId());
	}

}
