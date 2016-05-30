```java
@Component
public class MyIndicator extends AbstractIndicator {

  @Override
  protected IndicatorResult flagImpl(Notice notice) {
    if (!dataExistsInNotice(notice)) {
      return missingData();                            // MISSING_DATA
    } else if (condition1(notice)) {
      return returnFlag();                             // FLAG, "flag.MyIndicator.info"
    } else if (condition2(notice)) {
      return returnFlag("info2");                      // FLAG, "flag.MyIndicator.info2"
      // or with parameters:
      // return returnFlag("info2", "p1=v1", "p2=v2");
    }
    return null;                                       // NO_FLAG
  }
}
```
