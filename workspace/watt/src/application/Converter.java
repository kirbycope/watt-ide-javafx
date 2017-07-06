package application;

import application.models.TestStep;

public class Converter {
	public static String TestStepToJava(TestStep testStep){
		String code = null;

		// Add Description to exported file
		if (testStep.Description.length() > 0) {
			code = "    // " + testStep.Description;
		}

		// Command: click and clickAndWait
		if ( (testStep.Command =="click") || (testStep.Command =="clickAndWait") ) {
			// Get findElement(By) selector
			String target = FindElementSelector(testStep.Target);
			// Assemble the code
			code = "    driver.findElement(" + target +  ").click();";
		}
		// Command: clickAndWait
		if (testStep.Command =="click") {
			// Get findElement(By) selector
			String target = FindElementSelector(testStep.Target);
			// Assemble the code
			code = "    driver.findElement(" + target +  ").click();";
		}
		// Command: deleteAllVisibleCookies
		if (testStep.Command == "deleteAllVisibleCookies") {
			code = "    driver.manage().deleteAllCookies();";
		}
		// Command: echo
		if (testStep.Command == "echo") {
			code = "    System.out.println(" + testStep.Target + ")";
		}
		// Command: goBack and goBackAndWait
		if ( (testStep.Command == "goBack") || (testStep.Command == "goBackAndWait") ) {
			code = "    driver.navigate().back();";
		}
		// Command: goForward and goForwardAndWait
		if ( (testStep.Command == "goForward") || (testStep.Command == "goForward") ) {
			code = "    driver.navigate().forward();";
		}
		// Command: open
		if (testStep.Command == "open") {
			// TODO: BaseURL
			// Assemble the code
			code = "    driver.url = \"" + testStep.Target + "\";";
		}
		// Command: pause
		if (testStep.Command == "pause") {
			code = "    Thread.Sleep(" + testStep.Target + ")";
		}
		// Command: refresh and refreshAndWait
		if ( (testStep.Command == "refresh") || (testStep.Command == "refreshAndWait") ) {
			code = "    driver.navigate().refresh();";
		}
		// Command: runScript
		if (testStep.Command == "runScript") {
			code = "    (JavascriptExecutor) driver.executeScript(" + testStep.Target +")";
		}
		// Command: select
		if (testStep.Command == "select") {
			// Get findElement(By) selector
			String target = FindElementSelector(testStep.Target);
			// Assemble the code
			code = "    new Select(driver.findElement(" + target + ")";
			// Select by Index
			if (testStep.Value.startsWith("index=")) {
				code = code + ".selectByIndex" + testStep.Value.substring(6) + ")";
			}
			// Select by Label (Visible Text)
			if (testStep.Value.startsWith("label=")) {
				code = code + ".selectByVisibleText(" + testStep.Value.substring(6) + ")";
			}
			// Select by Value
			if (testStep.Value.startsWith("value=")) {
				code = code + ".selectByValue(" + testStep.Value.substring(6) + ")";
			}
		}
		// Command: store
		if (testStep.Command == "store") {
			code = "    " + testStep.Value + " = " + testStep.Target + ";";
		}
		// Command: submit and submitAndWait
		if ( (testStep.Command == "submit") || (testStep.Command == "submitAndWait") ) {
			// Get findElement(By) selector
			String target = FindElementSelector(testStep.Target);
			// Assemble the code
			code = "    driver.findElement(" + target +  ").submit();";
		}
		// Command: type
		if (testStep.Command == "type") {
			// Get findElement(By) selector
			String target = FindElementSelector(testStep.Target);
			// Assemble the code
			code = "    driver.findElement(" + target +  ").sendKeys(" + testStep.Value + ");";
		}
		// Command: verifyChecked
		if (testStep.Command == "verifyChecked") {
			// Get findElement(By) selector
			String target = FindElementSelector(testStep.Target);
			// Assemble the code
			code = "    assertTrue(driver.findElement(" + target + ").isSelected());";
		}
		// Command: verifyElementNotPresent
		if (testStep.Command == "verifyElementNotPresent") {
			// Get findElement(By) selector
			String target = FindElementSelector(testStep.Target);
			// Assemble the code
			code = "    assertFalse(isElementPresent(" + target + "));";
		}
		// Command: verifyElementPresent
		if (testStep.Command == "verifyElementPresent") {
			// Get findElement(By) selector
			String target = FindElementSelector(testStep.Target);
			// Assemble the code
			code = "    assertTrue(isElementPresent(" + target + "));";
		}
		// Command: verifyLocation
		if (testStep.Command == "verifyLocation") {
			code = "    assertEquals(" + testStep.Target + ", driver.url;)"; // TODO: Handle BaseUrl
		}
		// Command: verifyNotChecked
		if (testStep.Command == "verifyNotChecked") {
			// Get findElement(By) selector
			String target = FindElementSelector(testStep.Target);
			// Assemble the code
			code = "    assertFalse(driver.findElement(" + target + ").isSelected());";
		}
		// Command: verifyTable
		if (testStep.Command == "verifyTable") {
			// TODO
		}
		// Command: verifyText
		if (testStep.Command == "verifyText") {
			// Get findElement(By) selector
			String target = FindElementSelector(testStep.Target);
			// Assemble the code
			code = "    assertEquals(" + testStep.Target + ", driver.findElement(" + target + ").getText());";
		}
		// Command: verifyTitle
		if (testStep.Command == "verifyTitle") {
			code = "    assertEquals(" + testStep.Target + ", driver.getTitle();)";
		}
		// Command: verifyValue
		if (testStep.Command == "verifyValue") {
			// Get findElement(By) selector
			String target = FindElementSelector(testStep.Target);
			// Assemble the code
			code = "    assertEquals(" + testStep.Value + ", driver.findElement(" + target + ").getAttribute(\"value\"));";
		}
		// Command: waitForLocation
		if (testStep.Command == "waitForLocation") {
			       code = "    for (int second = 0;; second++) {";
			code = code + "      if (second >= 60) fail(\"timeout\");";
			code = code + "      try { if (" + testStep.Target + ".equals(driver.url)) break; }";
			code = code + "      catch (Exception e) { /* do nothing */ }";
			code = code + "      Thread.sleep(1000);";
			code = code + "    }";
		}

		// Return the code
		return code  + System.lineSeparator();
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
