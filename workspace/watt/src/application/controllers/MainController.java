package application.controllers;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import application.IDE;
import application.Main;
import application.Settings;
import application.UiHelpers;
import application.Utilities;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainController {

	/* -------- CLICK HANDLERS -------- */

	/**
	 * Handle the "click" on "New Project..."
	 */
	public void newProject(MouseEvent e) throws IOException {
		// Ask where to save project
		String filePath = showNewProjectPrompt();

		// Handle response
		if (filePath.length() > 19) {
			// Get the Main stage
	        Label source = (Label) e.getSource();
			Stage mainStage = (Stage) source.getScene().getWindow();
			// Load the Project
		    loadProject(mainStage, filePath);
		}
	}

	/**
	 * Handle the "click" on "Open Project..."
	 */
	public void openProject(MouseEvent e) throws IOException {
		// Ask what project to load
		String filePath = showOpenProjectPrompt();
		// Handle response
		if (filePath.length() > 0) {
			// Get the Main stage
	        Label source = (Label) e.getSource();
			Stage mainStage = (Stage) source.getScene().getWindow();
			// Load the Project
		    loadProject(mainStage, filePath);
		}
	}

	/* -------- OTHER METHODS -------- */

	/**
	 * Load the Project into the IDE Stage
	 */
	public static void loadProject(Stage mainStage, String filePath) {
		// Set the last opened project
		Settings.SetLastOpenedProject(filePath);
		// Open IDE window
		try {
			// Initialize the IDE stage
			IDE.initIdeLayout();
			// Close Main stage
	        mainStage.close();
		}
		catch (IOException e) { e.printStackTrace(); }
		// Store the project folder path
		IDE.projectFolderPath = filePath.replace("\\ProjectSettings.xml", "");
		// Get TreeView parent
		VBox vbox = (VBox) IDE.ideStage.getScene().lookup("#tree-view-parent");
		// Create TreeView for Project Explorer
		TreeView<String> tv = new TreeView<String>();
		// Set the ContextMenu of the TreeView
		tv.setContextMenu(UiHelpers.GetFolderContextMenu());
		// Add the TreeView to the parent node
		vbox.getChildren().add(tv);
		// Create the root TreeItem for the TreeView
		TreeItem<String> ti = new TreeItem<String>("\uD83D\uDDC0" + " Project Folder");
		// Add the root TreeItem to the TreeView
		tv.setRoot(ti);
		// Load Projects Settings XML file
		Document doc = Utilities.LoadDocumentFromFilePath(filePath);
		// Parse the XML document if there is one to parse
		if (doc != null) {
			// Get the child Node(s) of the root element
			NodeList childNodes = doc.getDocumentElement().getChildNodes();
			// Parse over the root element children
			// TODO: more intelligent recursion that handles folder/file structure
			for (int i = 0; i < childNodes.getLength(); i++) {
				org.w3c.dom.Node item = childNodes.item(i);
				String value = null;
				if (item.getNodeName().equals("file")) {
					value = "\uD83D\uDDCB" + " " + Utilities.getXmlNodeAttribute(item, "name");
				}
				else if (item.getNodeName().equals("folder")) {
					value = "\uD83D\uDDC0" + " " + Utilities.getXmlNodeAttribute(item, "name");
				}
				// Create a child TreeItem of the root TreeItem
				TreeItem<String> treeItem = new TreeItem<String>(value);
				// Add the child TreeItem to the root TreeItem
				ti.getChildren().add(treeItem);
				// Add EventListener to TreeView for double-click
				tv.setOnMouseClicked(new EventHandler<MouseEvent>() {
				    @Override
				    public void handle(MouseEvent mouseEvent) {
				        if(mouseEvent.getClickCount() == 2) {
				            TreeItem<String> item = tv.getSelectionModel().getSelectedItem();
				            IdeController.AddTab(item.getValue());
				        }
				    }
				});
			}
		}
	}

	/**
	 * Shows the "New Project" dialog
	 */
	public static String showNewProjectPrompt() {
		// Create the dialog prompt
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Choose Project Location");
		dialog.setHeaderText("Select a location to store your project");
		dialog.setContentText("Location: ");
		// Get the Stage of the dialog prompt
		Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
		// Add a custom icon to the dialog prompt
		stage.getIcons().add(new Image(Main.class.getResourceAsStream("/res/drawable/icon.png")));
		// Show the prompt and wait for a user response
		Optional<String> result = dialog.showAndWait();
		// Handle the response
		String resultString = "";
		if ( (result.isPresent()) && (result.get().length() > 0) ) {
			// Handle path ending with a trailing slash or not
			if (result.get().endsWith("\\")) {
				resultString = result.get();
		    }
		    else {
		    	resultString = result.get() + "\\";
		    }
			// Check if the folder already exists
			File directory = new File(resultString);
			if (directory.exists() == false) {
				directory.mkdir();
			}
	    	// Create a new Document to hold the project settings
		    org.w3c.dom.Document doc = Utilities.CreateDocument();
		    // Create the root element node
		    org.w3c.dom.Element root = doc.createElement("root");
		    doc.appendChild(root);// Write the Document to an XML File at the specified location
		    Utilities.writeDocumentToFile(doc, new File(resultString + "ProjectSettings.xml"));
		}
		return resultString + "ProjectSettings.xml";
	}

	/**
	 * Shows the "Open Project" dialog
	 */
	public static String showOpenProjectPrompt() {
		// Get the last opened project
		String filePath = Settings.GetLastOpenedProject();
		TextInputDialog dialog = new TextInputDialog(filePath);
		dialog.setTitle("Choose Project Location");
		dialog.setHeaderText("Select a location to store your project");
		dialog.setContentText("Location: ");
		// Get the Stage
		Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
		// Add a custom icon
		stage.getIcons().add(new Image(Main.class.getResourceAsStream("/res/drawable/icon.png")));
		// Traditional way to get the response value.
		Optional<String> result = dialog.showAndWait();
		// Handle the response
		String resultString = "";
		if ( (result.isPresent()) && (result.get().length() > 0) ) {
			resultString = result.get();
		}
		return resultString;
	}
}
