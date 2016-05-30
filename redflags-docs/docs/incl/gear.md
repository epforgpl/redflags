```java
@Component
@ConfigurationProperties(prefix = "mygear")
// @Scope("prototype")                               // in case you need separate instances
public class MyGear extends AbstractGear {           // "myGear" in the gear list

  private String  strOption = "default value";       // mygear.strOption
  private int     intOption = 42;                    // mygear.intOption
  private Pattern myPattern = Pattern.compile(".*"); // mygear.myPattern
  //private @Autowired OtherGear g;                  // in case you need another gear

  @Override
  protected Notice processImpl(Notice notice) throws Exception {
    // do something with notice and fields
    return notice;
  }
}
```
