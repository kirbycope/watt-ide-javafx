package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Commands {

	/**
	 * Provides a list of Commands
	 */
	public static ObservableList<String> options =
	    FXCollections.observableArrayList(
	        "click"
	        ,"clickAndWait"
	        ,"deleteAllVisibleCookies"
	        ,"echo"
	        ,"goBack"
	        ,"goBackAndWait"
	        ,"goForward"
	        ,"goForwardAndWait"
	        ,"open"
	        ,"pause"
	        ,"refresh"
	        ,"refreshAndWait"
	        ,"runScript"
	        ,"select"
	        ,"store"
	        ,"submit"
	        ,"submitAndWait"
	        ,"type"
	        ,"verifyChecked"
	        ,"verifyElementNotPresent"
	        ,"verifyElementPresent"
	        ,"verifyLocation"
	        ,"verifyNotChecked"
	        ,"verifyTable"
	        ,"verifyText"
	        ,"verifyTitle"
	        ,"verifyValue"
	        ,"waitForLocation"
		)
    ;

	/**
	 * Sets the Reference based on the Command ComboBox value
	 */
	public static void SetReferenceForCommand(String command) {
		String commandArguments = "";
		String argument1 = "";
		String argument2 = "";
		String descritpion = "";
		// Handle the provided command
		if (command.equals("click")) {
			commandArguments = "click(locator)";
			argument1 = "locator - an element locator";
			descritpion = "Clicks on a link, button, checkbox or radio button. If the click action causes a new page to load (like a link usually does), call waitForPageToLoad.";
		}
		// TODO: other commands... in XML file?
		// Update the Reference in the UI only if there is something to update with
		if (commandArguments.length() > 0) {
			// Get the Reference wrapper
			VBox taReference = UiHelpers.GetReferenceWrapperNode();
			// Create a Label for {command}
			Label l1 = new Label(commandArguments + System.lineSeparator());
			// Set the Label to be *bold*
			l1.setFont(Font.font(l1.getFont().getName(), FontWeight.BOLD, l1.getFont().getSize()));
			// Add the Label to the wrapper
			taReference.getChildren().add(l1);
			// Create a Label for "Argument(s):"
			Label l2 = new Label("Argument(s):");
			// Add the Label to the wrapper
			taReference.getChildren().add(l2);
			if (argument1.length() > 0) {
				// Create a Label for {argument1}
				Label l3 = new Label(" • " + argument1);
				// Add the Label to the wrapper
				taReference.getChildren().add(l3);
			}
			if (argument2.length() > 0) {
				// Create a Label for {argument2}
				Label l4 = new Label(" • " + argument2);
				// Add the Label to the wrapper
				taReference.getChildren().add(l4);
			}
			// Create a Label for the {description}
			Label l5 = new Label(System.lineSeparator() + descritpion);
			l5.setWrapText(true);;
			// Add the Label to the wrapper
			taReference.getChildren().add(l5);
		}
	}
}
