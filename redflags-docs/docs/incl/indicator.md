```java
package mypackage.lang;

import hu.petabyte.redflags.engine.gear.indicator.AbstractIndicator;
import hu.petabyte.redflags.engine.model.IndicatorResult;
import hu.petabyte.redflags.engine.model.Notice;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

@Component
public class MyIndicator extends AbstractIndicator {

  @Override
  protected IndicatorResult flagImpl(Notice notice) {
    if (!dataExistsInNotice(notice)) {
      return missingData();                            // MISSING_DATA
    } else if (condition1(notice)) {
      return returnFlag();                             // FLAG, "flag.lang.MyIndicator.info"
    } else if (condition2(notice)) {
      return returnFlag("info2");                      // FLAG, "flag.lang.MyIndicator.info2"
      // or with parameters:
      // return returnFlag("info2", "p1=v1", "p2=v2");
    }
    return null;                                       // NO_FLAG
  }
}
```
