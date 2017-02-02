package demo;

import org.junit.*;
import org.openqa.selenium.*;
import demo.TestBase;

public class TestCase01 extends TestBase {
  @Test
  public void testCase01() throws Exception {
    // Open: Google
    driver.get("https://www.google.com" + "/");
    // Click: Logo
    driver.findElement(By.id("hplogo")).click();
  }
}
