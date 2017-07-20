package application;

import static org.junit.Assert.*;

import org.junit.*;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.Select;

public class TestCase01 extends TestBase {
	  @Test
	  public void testcase01() throws Exception {
	    // Command: open
	    driver.get("http://timothycope.com/html-test-page/");
	    // Command: click
	    driver.findElement(By.id("myCheck")).click();
	    // Command: clickAndWait
	    driver.findElement(By.id("myCheck")).click();
	    // Command: deleteAllVisibleCookies
	    driver.manage().deleteAllCookies();
	    // Command: echo
	    System.out.println("Hello World!");
	    // Command: goBack
	    driver.navigate().back();
	    // Command: goBackAndWait
	    driver.navigate().back();
	    // Command: open
	    driver.get("http://timothycope.com/html-test-page/");
	    // Command: pause
	    Thread.sleep(3000);
	    // Command: refresh
	    driver.navigate().refresh();
	    // Command: refreshAndWait
	    driver.navigate().refresh();
	    // Command: runScript
	    ((JavascriptExecutor) driver).executeScript("console.log('Hello World!')");
	    // Command: select
	    new Select(driver.findElement(By.id("cars"))).selectByVisibleText("Audi");
	    // Command: store
	    @SuppressWarnings("unused")
		String variableName = "expression";
	    // Command: submit
	    driver.findElement(By.id("NameForm")).submit();
	    // Command: submitAndWait
	    driver.findElement(By.id("NameForm")).submit();
	    // Command: type
	    driver.findElement(By.id("firstName")).sendKeys("Timothy");
	    // Command: click
	    driver.findElement(By.id("myCheck")).click();
	    // Command: verifyChecked
	    assertTrue(driver.findElement(By.id("myCheck")).isSelected());
	    // Command: verifyElementNotPresent
	    assertFalse(isElementPresent(By.id("fakeID")));
	    // Command: verifyElementPresent
	    assertTrue(isElementPresent(By.id("myCheck")));
	    // Command: verifyLocation
	    assertEquals("http://timothycope.com/html-test-page/?FirstName=&LastName=", driver.getCurrentUrl());
	    // Command: click
	    driver.findElement(By.id("myCheck")).click();
	    // Command: verifyNotChecked
	    assertFalse(driver.findElement(By.id("myCheck")).isSelected());
	    // Command: verifyTable

	    // Command: verifyText
	    assertEquals("HTML Test Page", driver.findElement(By.cssSelector("h1.entry-title")).getText());
	    // Command: verifyTitle
	    assertEquals("HTML Test Page - Quality Consulting", driver.getTitle());
	    // Command: verifyValue
	    assertEquals("Timothy", driver.findElement(By.id("firstName")).getAttribute("value"));
	    // Command: waitForLocation
	    for (int second = 0;; second++) {
	      if (second >= 60) fail("timeout");
	      try { if (driver.getCurrentUrl().equals("http://timothycope.com/html-test-page/?FirstName=&LastName=")) break; }
	      catch (Exception e) { /* do nothing */ }
	      Thread.sleep(1000);
	    }
	  }
	}


