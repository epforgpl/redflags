```java
@Component
@Scope("prototype") // you probably need only one of this
public class MyExporter extends AbstractExporter {

  @Override
  public void export(Notice notice) {
    // write out notice
  }

  @Override
  public void beforeSession() throws Exception {
  	// initialize output
  }

  @Override
  public void afterSession() throws Exception {
    // finalize output
  }
}
```
