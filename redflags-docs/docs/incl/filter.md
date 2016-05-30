```java
@Component
public class MyFilter extends AbstractFilter {

  @Override
  public boolean accept(Notice notice) throws Exception {
    return condition(notice);
  }
}
```
