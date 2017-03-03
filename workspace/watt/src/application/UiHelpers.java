package application;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import application.controllers.IdeController;
import application.models.TreeCellImpl;
import application.models.TreeItemFile;
import application.models.TreeItemFolder;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

public class UiHelpers {

	/**
	 * Get the Console (TextArea) Node
	 */
	public static TextArea GetConsoleTextAreaNode() {
		return (TextArea) IDE.ideStage.getScene().lookup("#console-text-area");
	}

	/**
	 * Get the Test Step Builder's Description (TextField) Node
	 */
	public static TextField GetTestStepBuilderDescriptionNode() {
		return (TextField) IDE.ideStage.getScene().lookup("#test-step-builder-description");
	}

	/**
	 * Get the Test Step Builder's Command (ComboBox) Node
	 */
	@SuppressWarnings("rawtypes")
	public static ComboBox GetTestStepBuilderCommandNode() {
		return (ComboBox) IDE.ideStage.getScene().lookup("#test-step-builder-command");
	}

	/**
	 * Get the Test Step Builder's Description Node
	 */
	public static TextField GetTestStepBuilderTargetNode() {
		return (TextField) IDE.ideStage.getScene().lookup("#test-step-builder-target");
	}

	/**
	 * Get the Test Step Builder's Description (TextField) Node
	 */
	public static TextField GetTestStepBuilderValueNode() {
		return (TextField) IDE.ideStage.getScene().lookup("#test-step-builder-value");
	}

	/**
	 * Gets the Test Step Builder's Reference wrapper
	 */
	public static VBox GetReferenceWrapperNode() {
		return (VBox) IDE.ideStage.getScene().lookup("#reference-wrapper");
	}

	/**
	 * Gets the ContextMenu for a File TreeItem
	 */
	public static ContextMenu GetFileContextMenu() {
		ContextMenu contextMenu = new ContextMenu();
		MenuItem mi1 = new MenuItem("Delete Test Case");
		mi1.setOnAction(new EventHandler<ActionEvent>() {
		    public void handle(ActionEvent e) {
		        System.out.println("Delete Test Case selected...");
		        // TODO: Delete Test Case
		    }
		});
		contextMenu.getItems().add(mi1);
		return contextMenu;
	}

	/**
	 * Gets the ContextMenu for a Folder TreeItem
	 */
	public static ContextMenu GetFolderContextMenu() {
		ContextMenu contextMenu = new ContextMenu();
		MenuItem mi1 = new MenuItem("New Test Case");
		mi1.setOnAction(new EventHandler<ActionEvent>() {
		    public void handle(ActionEvent e) {
		        Utilities.CreateNewTestStep();
		    }
		});
		contextMenu.getItems().add(mi1);
		MenuItem mi2 = new MenuItem("New Folder");
		mi2.setOnAction(new EventHandler<ActionEvent>() {
		    public void handle(ActionEvent e) {
		    	Utilities.CreateNewTestFolder();
		    }
		});
		contextMenu.getItems().add(mi2);
		return contextMenu;
	}

	/**
	 * Updates the TreeView in the IDE Stage
	 */
	@SuppressWarnings("unchecked")
	public static void UpdateTreeView(){
		// Get the TreeView
		TreeView<String> tv = (TreeView<String>) IDE.ideStage.getScene().lookup("#tree-view");
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
		// Create the root TreeItem for the TreeView
		TreeItemFolder ti = new TreeItemFolder("\uD83D\uDDC0" + " Project Folder");
		// Add the root TreeItem to the TreeView
		tv.setRoot(ti);
		// Load Projects Settings XML file
		Document doc = Utilities.LoadDocumentFromFilePath(IDE.projectFolderPath + "\\ProjectSettings.xml");
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

	/**
	 * Writes the given text to the IDE's Console
	 */
	public static void WriteToIdeConsole(String text){
		TextArea console = UiHelpers.GetConsoleTextAreaNode();
		console.appendText(text);
	}
}
