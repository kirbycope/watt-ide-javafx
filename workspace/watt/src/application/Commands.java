package application;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
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
	        ,"goForward" // Not in Selenium IDE
	        ,"goForwardAndWait" // Not in Selenium IDE
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
		// Properties
		String commandArgument = "";
		String argument1 = "";
		String argument2 = "";
		String descritpion = "";

		// Load the commands.xml file
		Document doc = Utilities.LoadDocumentFromResource("/res/doc/commands.xml");
		// Get all "commands"
		NodeList commands = doc.getDocumentElement().getElementsByTagName("command");
		// Find the <command> whose value="" matches the given command
		for (int i = 0; i < commands.getLength(); i++) {
			// Get the current <command>'s value attribute
			String value = Utilities.getXmlNodeAttribute(commands.item(i), "value");
			// Check for a match
			if (value.equals(command)) {
				// Cast current <command> Node as an Element
				Element currentCommand = (Element) commands.item(i);
				// Get the <commandArgument> Node
				Node commandArgumentNode = currentCommand.getElementsByTagName("commandArgument").item(0);
				// Get the commandArgument Node's value attribute
				commandArgument =  Utilities.getXmlNodeAttribute(commandArgumentNode, "value");

				// Get the <arguments>
				Node arguments = currentCommand.getElementsByTagName("arguments").item(0);
				// Cast current <arguments> Node as an Element
				Element currentArguments = (Element) arguments;
				// Get any/all <argument> Node(s) of the <arguments> Node
				NodeList argumentNodeList = currentArguments.getElementsByTagName("argument");
				// Parse each <argument>
				for (int j = 0; j < argumentNodeList.getLength(); j ++) {
					// Get the <argument>'s value attribute
					String argValue = Utilities.getXmlNodeAttribute(argumentNodeList.item(j), "value");
					if (j == 0) {
						argument1 = argValue;
					}
					else if (j == 1){
						argument2 = argValue;
					}
				}
				// Get the <description> Node
				Node descriptionNode = currentCommand.getElementsByTagName("description").item(0);
				// Get/Set the description's value attribute
				descritpion =  Utilities.getXmlNodeAttribute(descriptionNode, "value");
				// Stop iterating
				break;
			}
		}
		// Update the Reference in the UI only if there is something to update with
		if (commandArgument.length() > 0) {
			// Get the Reference wrapper
			VBox taReference = UiHelpers.GetReferenceWrapperNode();
			// Create a Label for {command}
			Label l1 = new Label(commandArgument);
			// Set the Label to be *bold*
			l1.setFont(Font.font(l1.getFont().getName(), FontWeight.BOLD, l1.getFont().getSize()));
			// Add the Label to the wrapper
			taReference.getChildren().add(l1);
			// Add Arguments only if there are some to add
			if (argument1.length() > 0) {
				// Create a Label for "Argument(s):"
				Label l2 = new Label(System.lineSeparator() + "Argument(s):");
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
			}
			// Create a Label for the {description}
			Label l5 = new Label(System.lineSeparator() + descritpion);
			l5.setWrapText(true);;
			// Add the Label to the wrapper
			taReference.getChildren().add(l5);
		}
	}
}
