package application.controllers;

import java.io.IOException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import application.Commands;
import application.IDE;
import application.Main;
import application.UiHelpers;
import application.Utilities;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class IdeController {

	/* -------- CLICK HANDLERS -------- */

	/**
	 * Toggles the Expand/Collapse state of the Console
	 */
	public void ExpandCollapseConsole(MouseEvent mouseEvent) {
		// Get the source of the event
		Label source = (Label) mouseEvent.getSource();
		// Handle source state
		if (source.getText().equals("➖")) {
			UiHelpers.GetConsoleTextAreaNode().setManaged(false);
			UiHelpers.GetConsoleTextAreaNode().setVisible(false);
			source.setText("➕");
			source.setTooltip(new Tooltip("Expand console"));
		}
		else {
			UiHelpers.GetConsoleTextAreaNode().setManaged(true);
			UiHelpers.GetConsoleTextAreaNode().setVisible(true);
			source.setText("➖");
			source.setTooltip(new Tooltip("Collapse console"));
		}
	}

	/**
	 * Toggles the Expand/Collapse state of the Test Step
	 */
	public static void ExpandCollapseTestStep(MouseEvent mouseEvent) {
		// Get the source of the event
		Button source = (Button) mouseEvent.getSource();
		HBox parent = (HBox) source.getParent();
		VBox grandParent = (VBox) parent.getParent();
		// Get the Expand/Collapse Button initial ToolTip value
		String initialTooltipText = source.getTooltip().getText();
		// Expand or Collapse the rows
		if (initialTooltipText.equals("Expand Test Step")){
			// Make each row visible
			ExpandTestStep(source, grandParent.getChildren());
		}
		else {
			// Make each row (except the first) hidden
			CollapseTestStep(source, grandParent.getChildren());
		}
	}

	/**
	 * Plays the current Tab's test case
	 */
	public void Play(MouseEvent mouseEvent) {
		// Get selected tab
		Tab selectedTab = Utilities.GetCurrentTab();
		// Ensure there is a selected Tab
		if(selectedTab != null) {
			// TODO: Save the test case
				// For now there is a demo file to roll with (TestCase01.html)
			// TODO: Convert the HTML file to a JAVA file
				// For now there is a demo file to roll with (TestCase01.java)
			// TODO: Build out the .bat file to compile
				// Compile the JAVA file to a CLASS file
				Utilities.RunCommand(IDE.projectFolderPath + "\\compile-script.bat");
			// TODO: Build out the .bat file to compile
				// Call JUnit with the class file
			Utilities.RunCommand(IDE.projectFolderPath + "\\run-script.bat");
		}
		else {
			// Get the "console" TextArea
			TextArea taConsole = (TextArea) IDE.ideStage.getScene().lookup("#console-text-area");
			// Add a new line separator if there is text already in the TextArea
			if (taConsole.getText().length() > 0){ taConsole.appendText(System.lineSeparator()); }
			// Write message to the "console"
			taConsole.appendText("No Test Case (Tab) open");
		}
	}

	/**
	 * Opens the Settings menu
	 */
	public void Settings(MouseEvent mouseEvent) {
		try {
			OpenSettingsWindow();
		}
		catch (IOException e) { e.printStackTrace();  }
	}

	/* -------- OTHER METHODS -------- */

	/**
	 * Adds a Test Step to the currently 'selected' Tab
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void AddStep(boolean execute, String description, String command, String target, String value, boolean continueOnFailure) {
		// Get the TabPane from the IDE stage
		TabPane tabPane = (TabPane) IDE.ideStage.getScene().lookup("#tab-pane");
		// Get the selected Tab
		Tab tab = tabPane.getSelectionModel().getSelectedItem();
		if (tab != null) {
			// Get the VBox of the selected Tab's content
			VBox content = (VBox) tab.getContent();
			// Get the ScrollPane of the Tab
			ScrollPane scrollPane = (ScrollPane) content.getChildren().get(0);
			// Get the child VBox of the ScrollPane
			VBox scrollPaneChildVBox = (VBox) scrollPane.getContent();
			// Create a VBox to hold the Test Step
			VBox vbox = new VBox();
			// Add the VBox to the ScrollPane's child VBox
			scrollPaneChildVBox.getChildren().add(vbox);
			// Set the ScrollPane's content to the new VBox
			scrollPane.setContent(scrollPaneChildVBox);
			// Create a HBox to hold the first row
			HBox firstRow = new HBox();
			// Set the alignment of the HBox
			firstRow.setAlignment(Pos.CENTER_LEFT);
			// Add the new HBox to the VBox
			vbox.getChildren().add(firstRow);
			// Create a CheckBox for Execute (checked) or Skip (unchecked)
			CheckBox checkBox = new CheckBox();
			// Set the Margin(s) for the CheckBox
			HBox.setMargin(checkBox, new Insets(0, 0, 0, 4));
			// Set the CheckBox to selected by default
			checkBox.selectedProperty().set(execute);
			// Set the ToolTip for the CheckBox
			checkBox.setTooltip(new Tooltip("Execute Test Step"));
			// Add the CheckBox to the HBox (first row)
			firstRow.getChildren().add(checkBox);
			// Create a TextField for Description
			TextField textField = new TextField(description);
			// Set the ToolTip for the TextField
			textField.promptTextProperty().setValue("Description");
			// Set the HGrow of the TextField to ALWAYS (which make the width 100% of its container)
			HBox.setHgrow(textField, Priority.ALWAYS);
			// Add the TextField to the HBox (first row)
			firstRow.getChildren().add(textField);
			// Create a Button for Expand or Collapse
			Button button1 = new Button("➕");
			// Set the ToolTip for the Button
			button1.setTooltip(new Tooltip("Expand Test Step"));
			// Set the onClick for the Button
			button1.setOnMouseClicked(
				new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent mouseEvent) {
						ExpandCollapseTestStep(mouseEvent);
					}
				}
			);
			// Add the Button to the HBox (first row)
			firstRow.getChildren().add(button1);
			// Create a Button for Remove Test Step
			Button button2 = new Button("❌");
			// Set the ToolTip for the Button
			button2.setTooltip(new Tooltip("Remove Test Step"));
			// Set the onClick for the Button
			button2.setOnMouseClicked(
				new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent mouseEvent) {
						RemoveStep(mouseEvent);
					}
				}
			);
			// Add the Button to the HBox (first row)
			firstRow.getChildren().add(button2);

			// Create a HBox to hold the second row
			HBox secondRow = new HBox();
			// Add the new HBox to the VBox
			vbox.getChildren().add(secondRow);
			// Create a ComboBox for Command
			ComboBox comboBox = new ComboBox(Commands.options);
			// Bind the ComboBox width to that of the firstRow
			comboBox.prefWidthProperty().bind(firstRow.widthProperty());
			// Set the placeholder text for the ComboBox
			comboBox.promptTextProperty().set("Command");
			// Set the ComboBox value
			comboBox.setValue(command);
			// Add the ComboBox to the HBox (second row)
			secondRow.getChildren().add(comboBox);

			// Create a HBox to hold the third row
			HBox thirdRow = new HBox();
			// Add the new HBox to the VBox
			vbox.getChildren().add(thirdRow);
			// Create a TextField for Target
			TextField tfTarget = new TextField(target);
			// Set the ToolTip for the TextField
			tfTarget.promptTextProperty().setValue("Target");
			// Set the HGrow of the TextField to ALWAYS (which make the width 100% of its container)
			HBox.setHgrow(tfTarget, Priority.ALWAYS);
			// Add the TextField to the HBox (third row)
			thirdRow.getChildren().add(tfTarget);

			// Create a HBox to hold the fourth row
			HBox fourthRow = new HBox();
			// Add the new HBox to the VBox
			vbox.getChildren().add(fourthRow);
			// Create a TextField for Value
			TextField tfValue = new TextField(value);
			// Set the ToolTip for the TextField
			tfValue.promptTextProperty().setValue("Value");
			// Set the HGrow of the TextField to ALWAYS (which make the width 100% of its container)
			HBox.setHgrow(tfValue, Priority.ALWAYS);
			// Add the TextField to the HBox (fourth row)
			fourthRow.getChildren().add(tfValue);

			// Hide Rows 2-4
			secondRow.setVisible(false);
			secondRow.setManaged(false);
			thirdRow.setVisible(false);
			thirdRow.setManaged(false);
			fourthRow.setVisible(false);
			fourthRow.setManaged(false);
		}
	}

	/**
	 * Adds a Tab to the TabPane and then adds any/all Test Steps into the Tab.
	 */
	public static void AddTab(String name) {
		// Add only if it is a document
		if (name.contains("🗋 ")) {
			// Trim "🗋 " from name
			name = name.substring(2);
			// Get TabPane
			TabPane tabPane = (TabPane) IDE.ideStage.getScene().lookup("#tab-pane");
			// Get the Tabs of the TabPane
			ObservableList<Tab> tabs = tabPane.getTabs();
			// Check each tab to see if one already exists with the same name
			Boolean alreadyOpen = false;
			for (Tab t : tabs) {
				alreadyOpen = t.getText().equals(name);
				if (alreadyOpen) {
					break;
				}
			}
			// Add only if not already present
			if (alreadyOpen == false) {
				// Create a new tab
				Tab tab = new Tab(name);
				// Add the new Tab to TabPane
				tabPane.getTabs().add(tab);
				// Set the TabPane's selected tab as the new Tab
				tabPane.getSelectionModel().select(tab);
				// Create a VBox to hold the content of the Tab
				VBox vbox = new VBox();
				// Set styleClass of the VBox
				vbox.getStyleClass().add("tab-content-vbox");
				// Add the VBox to the Tab
				tab.setContent(vbox);
				// Create a ScrollPane to hold the Test Steps Container
				ScrollPane scrollPane = new ScrollPane();
				// Set styleClass of the ScrollPane
				scrollPane.getStyleClass().add("edge-to-edge");
				// Bind the ScrollPane height to that of the Tab's content (VBox)
				scrollPane.prefHeightProperty().bind(vbox.heightProperty());
				// Add the ScrollPane to the VBox
				vbox.getChildren().add(scrollPane);
				// Create a VBox to hold the Test Steps
				VBox testStepsContainer = new VBox();
				// Bind the testStepsContainer (VBox) height to that of the ScrollPane
				testStepsContainer.prefHeightProperty().bind(scrollPane.heightProperty());
				// Bind the testStepsContainer (VBox) width to that of the ScrollPane - 12px (to accommodate the ScrollBar)
				testStepsContainer.prefWidthProperty().bind(scrollPane.widthProperty().subtract(12));
				// Set the ScrollPane's content to the testStepsContainer (VBox)
				scrollPane.setContent(testStepsContainer);
				/* Add any/all Test Steps to the Tab*/
				// Get the XML Document
				Document doc = Utilities.LoadDocumentFromFilePath(IDE.projectFolderPath + "\\ProjectSettings.xml");
				// Get the "file" node whose name attribute matches the Tab's title
				org.w3c.dom.Node fileNode = getFileNodeForCurrentTab(tab, doc);
				// Parse only if there was a matching <file> node
				if (fileNode != null) {
					// Assemble the file path of the Test Case HTML file
					String fileName = IDE.projectFolderPath + "\\TestCase01.html"; // TODO better (not hardcoded)
					// Load the HTML file
					Utilities.LoadSeleneseHtmlFile(fileName);
				}
			}
		}
	}

	/**
	 * Clears the 'console' (TextArea) in the UI
	 */
	public void ClearConsole() {
		UiHelpers.GetConsoleTextAreaNode().clear();
	}

	/**
	 * Collapses a Test Step to only show the first row
	 */
	public static void CollapseTestStep(Button source, ObservableList<Node> rows) {
		// Change the expand/collapse Button text to ➕
		source.setText("➕");
		// Change the ToolTip
		source.getTooltip().setText("Expand All Steps");
		// Make each row (except the first) hidden
		for (int i = 0; i < rows.size(); i++) {
			// Handle the first row
			if (i == 0) {
				// Change the Test Step container class
				VBox testStepContainer = (VBox) rows.get(i).getParent();
				testStepContainer.getStyleClass().remove("test-step-container-collapsed");
				testStepContainer.getStyleClass().remove("test-step-container-expanded");
				testStepContainer.getStyleClass().add("test-step-container-collapsed");
				// Get the Expand/Collapse Button
				Button expandCollpase = (Button) ((HBox) rows.get(i)).getChildren().get(2);
				// Change the expand/collapse Button text to ➕
				expandCollpase.setText("➕");
				// Change the ToolTip
				expandCollpase.getTooltip().setText("Expand Test Step");
			}
			else {
				rows.get(i).setManaged(false);
				rows.get(i).setVisible(false);
			}
		}
	}

	/**
	 * Expands a Test Step to show all rows
	 */
	private static void ExpandTestStep(Button source, ObservableList<Node> rows) {
		// Change the expand/collapse Button text to ➖
		source.setText("➖");
		// Update ToolTip
		source.getTooltip().setText("Collapse All Steps");
		// Make each row visible
		for (int i = 0; i < rows.size(); i++) {
			// Handle the first row
			if (i == 0){
				// Change the Test Step container class
				VBox testStepContainer = (VBox) rows.get(i).getParent();
				testStepContainer.getStyleClass().remove("test-step-container-collapsed");
				testStepContainer.getStyleClass().remove("test-step-container-expanded");
				testStepContainer.getStyleClass().add("test-step-container-expanded");
				// Get the Expand/Collapse Button
				Button expandCollpase = (Button) ((HBox) rows.get(i)).getChildren().get(2);
				// Change the expand/collapse Button text to ➖
				expandCollpase.setText("➖");
				// Change the ToolTip
				expandCollpase.getTooltip().setText("Collapse Test Step");
			}
			rows.get(i).setManaged(true);
			rows.get(i).setVisible(true);
		}
	}

	/**
	 * Gets the <file> Node from ProjectSettings.xml matching the Tab's title/text
	 */
	public static org.w3c.dom.Node getFileNodeForCurrentTab(Tab tab, Document doc) {
		org.w3c.dom.Node node = null;
		// Get all "files"
		NodeList files = doc.getDocumentElement().getElementsByTagName("file");
		// Parse files if there are any to parse
		if (files.getLength() > 0) {
			// Get the Tab's title
			String tabTitle = tab.getText();
			// Parse the <file> nodes
			for (int i = 0; i < files.getLength(); i++) {
				// Get the "name" attribute of the current node
				String name = Utilities.getXmlNodeAttribute(files.item(i), "name");
				// Check if the current name matches the Tab's title
				if (name.equals(tabTitle.trim())) {
					node = files.item(i);
					break;
				}
			}
		}
		return node;
	}

	/***
	 * Opens the Settings menu
	 */
	public void OpenSettingsWindow() throws IOException  {
		// TODO : Check if already opened

		// Create a new application (Stage) window
		Stage settingsStage = new Stage();
		// Application (Stage) Icon
		settingsStage.getIcons().add(new Image(Main.class.getResourceAsStream("/res/drawable/icon.png")));
		// Application (Stage) Title
		settingsStage.setTitle("WATT - Settings");
		// Create a new FXML Loader (loads an object hierarchy from an XML document)
 		FXMLLoader loader = new FXMLLoader();
 		// Give the loader the location of the XML document
 		loader.setLocation(Main.class.getResource("/res/layout/settings.fxml"));
 		// Load an object hierarchy from the FXML document into a generic (base class) node
 		Parent root = loader.load();
		// Load the "scene" into the "stage" and set the "stage"
 		settingsStage.setScene(new Scene(root));
		// Show the application (stage) window
 		settingsStage.show();

 		// Get the Base Address TextField
 		TextField tfBaseAddress = (TextField) settingsStage.getScene().lookup("#base-address");
 		// Set the text value of the TextField
 		tfBaseAddress.setText(IDE.baseAddress);
	}

	/**
	 * Remove the Test Step
	 */
	public static void RemoveStep(MouseEvent mouseEvent) {
		Button source = (Button) mouseEvent.getSource();
		HBox parent = (HBox) source.getParent();
		VBox grandParent = (VBox) parent.getParent();
		VBox greatGrandParent = (VBox) grandParent.getParent();
		greatGrandParent.getChildren().remove(grandParent);
	}

	/**
	 * Add Test Step from Test Step Builder to the 'selected' Tab
	 */
	public void TestStepBuilderAdd() {
		// Get the Description
		String description = UiHelpers.GetTestStepBuilderDescriptionNode().getText();
		// Get the Command
		String command = (String) UiHelpers.GetTestStepBuilderCommandNode().getValue();
		// Get the Target
		String target = UiHelpers.GetTestStepBuilderTargetNode().getText();
		// Get the Value
		String value = UiHelpers.GetTestStepBuilderValueNode().getText();
		// Add the Test Step
		AddStep(true, description, command, target, value, false);
	}

	/**
	 * Clear the Test Step Builder
	 */
	@SuppressWarnings("unchecked")
	public void TestStepBuilderClear() {
		// Clear the Description
		UiHelpers.GetTestStepBuilderDescriptionNode().clear();
		// Clear the Command
		UiHelpers.GetTestStepBuilderCommandNode().setValue(null);
		// Clear the Target
		UiHelpers.GetTestStepBuilderTargetNode().clear();
		// Clear the Value
		UiHelpers.GetTestStepBuilderTargetNode().clear();
		// Clear the Reference
		UiHelpers.GetReferenceWrapperNode().getChildren().clear();
	}

}
