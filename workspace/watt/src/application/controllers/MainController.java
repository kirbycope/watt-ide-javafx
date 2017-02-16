package application.controllers;

import java.io.File;
import java.io.IOException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import application.IDE;
import application.Main;
import application.Settings;
import application.Utilities;
import application.models.TreeCellImpl;
import application.models.TreeItemFile;
import application.models.TreeItemFolder;
import javafx.event.EventHandler;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

public class MainController {

	/* -------- CLICK HANDLERS -------- */

	/**
	 * Handle the "click" on "New Project..."
	 */
	public void newProject(MouseEvent e) throws IOException {
		// Ask where to save project
		//String filePath = showNewProjectPrompt();
		String filePath = Utilities.ShowDirectoryChooser(Main.mainStage);
		// Handle response
		if (filePath.length() > 0) {
			// Handle path ending with a trailing slash or not
			if (filePath.endsWith("\\") == false) {
				filePath = filePath + "\\";
		    }
			// Check if the folder already exists
			File directory = new File(filePath);
			if (directory.exists() == false) {
				directory.mkdir();
			}
	    	// Create a new Document to hold the project settings
		    org.w3c.dom.Document doc = Utilities.CreateDocument();
		    // Create the root element node
		    org.w3c.dom.Element root = doc.createElement("root");
		    // Add the root element to the document
		    doc.appendChild(root);
		    // Write the Document to an XML File at the specified location
		    Utilities.writeDocumentToFile(doc, new File(filePath + "ProjectSettings.xml"));
			// Load the Project
		    loadProject(Main.mainStage, filePath + "ProjectSettings.xml");
		}
	}

	/**
	 * Handle the "click" on "Open Project..."
	 */
	public void openProject(MouseEvent e) throws IOException {
		// Ask what project to load
		String filePath = Utilities.ShowXmlFileChooser(Main.mainStage);
		// Handle response
		if (filePath.length() > 0) {
			// Load the Project
		    loadProject(Main.mainStage, filePath);
		}
	}

	/* -------- OTHER METHODS -------- */

	/**
	 * Load the Project into the IDE Stage
	 */
	@SuppressWarnings("unchecked")
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
		/* Give the TreeView a ContextMenu handler for its TreeCells
		 * Source - http://stackoverflow.com/a/20695628/1106708
		*/
		tv.setCellFactory(new Callback<TreeView<String>,TreeCell<String>>(){
	        @Override
	        public TreeCell<String> call(TreeView<String> p) {
	            return new TreeCellImpl();
	        }
	    });
		// Add the TreeView to the parent node
		vbox.getChildren().add(tv);
		// Create the root TreeItem for the TreeView
		TreeItemFolder ti = new TreeItemFolder("\uD83D\uDDC0" + " Project Folder");
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
					// Create a TreeItem for File
					TreeItemFile treeItemFile = new TreeItemFile(value);
					// Add the TreeItem to the root TreeItem
					ti.getChildren().add(treeItemFile);
				}
				else if (item.getNodeName().equals("folder")) {
					value = "\uD83D\uDDC0" + " " + Utilities.getXmlNodeAttribute(item, "name");
					// Create a TreeItem for Folder
					TreeItemFolder treeItemFolder = new TreeItemFolder(value);
					// Add the TreeItem to the root TreeItem
					ti.getChildren().add(treeItemFolder);
				}
			}
		}
	}
}
