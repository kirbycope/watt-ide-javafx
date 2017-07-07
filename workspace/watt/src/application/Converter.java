package application;

import application.models.TestStep;

public class Converter {

	public static String TestStepToJava(TestStep testStep){
		String baseUrl = Settings.GetBaseAddress();
		String code = null;
		// Add Description to exported file
		if (testStep.Description.length() > 0) {
			code = "    // " + testStep.Description + System.lineSeparator();
		}
		// Command: click and clickAndWait
		if ( (testStep.Command =="click") || (testStep.Command =="clickAndWait") ) {
			// Get findElement(By) selector
			String target = FindElementSelector(testStep.Target);
			// Assemble the code
			code = code + "    driver.findElement(" + target +  ").click();";
		}
		// Command: deleteAllVisibleCookies
		else if (testStep.Command == "deleteAllVisibleCookies") {
			code = code + "    driver.manage().deleteAllCookies();";
		}
		// Command: echo
		else if (testStep.Command == "echo") {
			code = code + "    System.out.println(\"" + testStep.Target + "\");";
		}
		// Command: goBack and goBackAndWait
		else if ( (testStep.Command == "goBack") || (testStep.Command == "goBackAndWait") ) {
			code = code + "    driver.navigate().back();";
		}
		// Command: goForward and goForwardAndWait
		else if ( (testStep.Command == "goForward") || (testStep.Command == "goForward") ) {
			code = code + "    driver.navigate().forward();";
		}
		// Command: open
		else if (testStep.Command == "open") {
			code = code + "    driver.get(\"" + baseUrl + testStep.Target + "\");";
		}
		// Command: pause
		else if (testStep.Command == "pause") {
			code = code + "    Thread.sleep(" + testStep.Target + ");";
		}
		// Command: refresh and refreshAndWait
		else if ( (testStep.Command == "refresh") || (testStep.Command == "refreshAndWait") ) {
			code = code + "    driver.navigate().refresh();";
		}
		// Command: runScript
		else if (testStep.Command == "runScript") {
			code = code + "    ((JavascriptExecutor) driver).executeScript(\"" + testStep.Target +"\");";
		}
		// Command: select
		else if (testStep.Command == "select") {
			// Get findElement(By) selector
			String target = FindElementSelector(testStep.Target);
			// Assemble the code
			code = code + "    new Select(driver.findElement(" + target + "))";
			// Select by Index
			if (testStep.Value.startsWith("index=")) {
				code = code + ".selectByIndex(\"" + testStep.Value.substring(6) + "\");";
			}
			// Select by Label (Visible Text)
			else if (testStep.Value.startsWith("label=")) {
				code = code + ".selectByVisibleText(\"" + testStep.Value.substring(6) + "\");";
			}
			// Select by Value
			else if (testStep.Value.startsWith("value=")) {
				code = code + ".selectByValue(\"" + testStep.Value.substring(6) + "\");";
			}
		}
		// Command: store
		else if (testStep.Command == "store") {
			code = code + "    String " + testStep.Value + " = \"" + testStep.Target + "\";";
		}
		// Command: submit and submitAndWait
		else if ( (testStep.Command == "submit") || (testStep.Command == "submitAndWait") ) {
			// Get findElement(By) selector
			String target = FindElementSelector(testStep.Target);
			// Assemble the code
			code = code + "    driver.findElement(" + target +  ").submit();";
		}
		// Command: type
		else if (testStep.Command == "type") {
			// Get findElement(By) selector
			String target = FindElementSelector(testStep.Target);
			// Assemble the code
			code = code + "    driver.findElement(" + target +  ").sendKeys(\"" + testStep.Value + "\");";
		}
		// Command: verifyChecked
		else if (testStep.Command == "verifyChecked") {
			// Get findElement(By) selector
			String target = FindElementSelector(testStep.Target);
			// Assemble the code
			code = code + "    assertTrue(driver.findElement(" + target + ").isSelected());";
		}
		// Command: verifyElementNotPresent
		else if (testStep.Command == "verifyElementNotPresent") {
			// Get findElement(By) selector
			String target = FindElementSelector(testStep.Target);
			// Assemble the code
			code = code + "    assertFalse(isElementPresent(" + target + "));";
		}
		// Command: verifyElementPresent
		else if (testStep.Command == "verifyElementPresent") {
			// Get findElement(By) selector
			String target = FindElementSelector(testStep.Target);
			// Assemble the code
			code = code + "    assertTrue(isElementPresent(" + target + "));";
		}
		// Command: verifyLocation
		else if (testStep.Command == "verifyLocation") {
			code = code + "    assertEquals(\"" + baseUrl + testStep.Target + "\", driver.getCurrentUrl());";
		}
		// Command: verifyNotChecked
		else if (testStep.Command == "verifyNotChecked") {
			// Get findElement(By) selector
			String target = FindElementSelector(testStep.Target);
			// Assemble the code
			code = code + "    assertFalse(driver.findElement(" + target + ").isSelected());";
		}
		// Command: verifyTable
		else if (testStep.Command == "verifyTable") {
			// TODO
		}
		// Command: verifyText
		else if (testStep.Command == "verifyText") {
			// Get findElement(By) selector
			String target = FindElementSelector(testStep.Target);
			// Assemble the code
			code = code + "    assertEquals(\"" + testStep.Value + "\", driver.findElement(" + target + ").getText());";
		}
		// Command: verifyTitle
		else if (testStep.Command == "verifyTitle") {
			code = code + "    assertEquals(\"" + testStep.Target + "\", driver.getTitle());";
		}
		// Command: verifyValue
		else if (testStep.Command == "verifyValue") {
			// Get findElement(By) selector
			String target = FindElementSelector(testStep.Target);
			// Assemble the code
			code = code + "    assertEquals(\"" + testStep.Value + "\", driver.findElement(" + target + ").getAttribute(\"value\"));";
		}
		// Command: waitForLocation
		else if (testStep.Command == "waitForLocation") {
			code = code + "    for (int second = 0;; second++) {" + System.lineSeparator();
			code = code + "      if (second >= 60) fail(\"timeout\");" + System.lineSeparator();
			code = code + "      try { if (driver.getCurrentUrl().equals(\"" + baseUrl + testStep.Target + "\")) break; }" + System.lineSeparator();
			code = code + "      catch (Exception e) { /* do nothing */ }" + System.lineSeparator();
			code = code + "      Thread.sleep(1000);" + System.lineSeparator();
			code = code + "    }";
		}
		// Return the code
		return code + System.lineSeparator();
	}

	private static String FindElementSelector(String target) {
		String selector = null;

		if (target.startsWith("id=")) {
			selector = "By.id(\"" + target.substring(3) + "\")";
		}
		else if (target.startsWith("css=")) {
			selector = "By.cssSelector(\"" + target.substring(4) + "\")";
		}
		else if (target.startsWith("link=")) {
			selector = "By.linkText(\"" + target.substring(5) + "\")";
		}
		// TODO: Name + Type + Value

		return selector;
	}
}
