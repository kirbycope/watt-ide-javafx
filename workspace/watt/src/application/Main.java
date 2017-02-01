package application;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

	/* Mouse Event Handlers */

	/**
	 * Handle the "click" on "New Project..."
	 */
	public void newProject(MouseEvent e) throws IOException {
		// Ask where to save project
		String filePath = showNewProjectPrompt();
		// Handle response
		if (filePath.length() > 0) {
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

	/* Main Methods */

	/**
	 * Called when application is opened
	 */
	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * Overrides the start() method
	 */
	@Override
	public void start(Stage primaryStage) {
		// Set the main stage
		try { initMainLayout(primaryStage); }
		catch (IOException e) { e.printStackTrace(); }
	}

	/* Other Methods */

	/**
	 * Initialize the IDE stage
	 */
	public void initIdeLayout() throws IOException {
		// Create a new application (Stage) window
		IDE.ideStage = new Stage();
        // Application (Stage) Icon
        IDE.ideStage.getIcons().add(new Image(Main.class.getResourceAsStream("/res/drawable/icon.png")));
		// Application (Stage) Title
        IDE.ideStage.setTitle("WATT");
		// Create a new FXML Loader (loads an object hierarchy from an XML document)
 		FXMLLoader loader = new FXMLLoader();
 		// Give the loader the location of the XML document
 		loader.setLocation(Main.class.getResource("/res/layout/ide.fxml"));
 		// Load an object hierarchy from the FXML document into a generic (base class) node
 		Parent root = loader.load();
		// Load the "scene" into the "stage" and set the "stage"
 		IDE.ideStage.setScene(new Scene(root));
		// Show the application (stage) window
 		IDE.ideStage.show();
 		// Set the options for the Test Step Builder commands
 		((ComboBox) IDE.ideStage.getScene().lookup("#test-step-builder-command")).setItems(Commands.options);
	}

	/**
	 * Initialize the Main stage
	 */
	private void initMainLayout(Stage primaryStage) throws IOException {
		// Application (Stage) Icon
		primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("/res/drawable/icon.png")));
		// Application (Stage) Title
		primaryStage.setTitle("WATT");
		// Create a new FXML Loader (loads an object hierarchy from an XML document)
		FXMLLoader loader = new FXMLLoader();
		// Give the loader the location of the XML document
		loader.setLocation(Main.class.getResource("/res/layout/main.fxml"));
		// Load an object hierarchy from the FXML document into a generic (base class) node
		Parent root = loader.load();
		// Load the "scene" into the "stage" and set the "stage"
		primaryStage.setScene(new Scene(root));
		// Show the application (stage) window
		primaryStage.show();
		// Disable resize
		primaryStage.setResizable(false);
	}

	/**
	 * Shows the "New Project" dialog
	 */
	public String showNewProjectPrompt() {
		// Create the dialog prompt
		TextInputDialog dialog = new TextInputDialog("C:\\WATT\\Demo\\");
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
		if ( (result.isPresent()) && (result.get().length() > 0) ) {
	    	// Create a new Document to hold the project settings
		    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		    DocumentBuilder builder = null;
			try { builder = dbf.newDocumentBuilder(); }
			catch (ParserConfigurationException e1) { e1.printStackTrace();	}
		    Document doc = builder.newDocument();
		    // Create the root element node
		    Element root = doc.createElement("root");
		    doc.appendChild(root);

		    /********************************************/
		    // DEBUGGING - Create a DEMO project
		    doc = Utilities.CreateDemo(doc, result.get());
		    /********************************************/

		    // Write the Document to an XML File at the specified location
		    try { writeDocumentToFile(doc, new File(result.get() + "ProjectSettings.xml")); }
		    catch (TransformerException e2) { e2.printStackTrace(); }
		}
		return result.get() + "ProjectSettings.xml";
	}

	/**
	 * Shows the "Open Project" dialog
	 */
	public String showOpenProjectPrompt() {
		TextInputDialog dialog = new TextInputDialog("C:\\WATT\\Demo\\ProjectSettings.xml");
		dialog.setTitle("Choose Project Location");
		dialog.setHeaderText("Select a location to store your project");
		dialog.setContentText("Location: ");
		// Get the Stage
		Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
		// Add a custom icon
		stage.getIcons().add(new Image(Main.class.getResourceAsStream("/res/drawable/icon.png")));
		// Traditional way to get the response value.
		Optional<String> result = dialog.showAndWait();
		return result.get();
	}

	/**
	 * Writes the Document object to a file
	 */
	public static void writeDocumentToFile(Document document, File file) throws TransformerException {
        // Make a transformer factory to create the Transformer
        TransformerFactory tFactory = TransformerFactory.newInstance();
        // Make the Transformer
        Transformer transformer = tFactory.newTransformer();
        // Mark the document as a DOM (XML) source
        DOMSource source = new DOMSource(document);
        // Say where we want the XML to go
        StreamResult result = new StreamResult(file);
        // Write the XML to file
        transformer.transform(source, result);
    }

	/**
	 * Gets the attribute of the XML Node
	 */
	public static String getXmlNodeAttribute(Node node, String attribute){
		return node.getAttributes().getNamedItem(attribute).getNodeValue();
	}

	/**
	 * Load the Project into the IDE Stage
	 */
	public void loadProject(Stage mainStage, String filePath) {
		// Open IDE window
		try {
			// Initialize the IDE stage
			initIdeLayout();
			// Close Main stage
	        mainStage.close();
		}
		catch (IOException e) { /* e.printStackTrace(); */ }
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
		TreeItem<String> ti = new TreeItem<String>("🗀 Project Folder");
		// Add the root TreeItem to the TreeView
		tv.setRoot(ti);
		// Load Projects Settings XML file
		Document doc = loadProjectSettingsFile(filePath);
		// Parse the XML document if there is one to parse
		if (doc != null) {
			// Get the child Node(s) of the root element
			NodeList childNodes = doc.getDocumentElement().getChildNodes();
			// Parse over the root element children
			// TODO: more intelligent recursion that handles folder/file structure
			for (int i = 0; i < childNodes.getLength(); i++) {
				Node item = childNodes.item(i);
				String value = null;
				if (item.getNodeName().equals("file")) {
					value = "🗋 " + getXmlNodeAttribute(item, "name");
				}
				else if (item.getNodeName().equals("folder")) {
					value = "🗀 " + getXmlNodeAttribute(item, "name");
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
				            IDE.AddTab(item.getValue());
				        }
				    }
				});
			}
		}
	}

	/**
	 * Load the ProjectSettings.xml file into an Object
	 */
	public static Document loadProjectSettingsFile(String filePath) {
		File file = new File(filePath);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = null;
		try { dBuilder = dbFactory.newDocumentBuilder(); }
		catch (ParserConfigurationException e) { e.printStackTrace(); }
		Document doc = null;
		try { doc = dBuilder.parse(file); }
		catch (SAXException e) { e.printStackTrace(); }
		catch (IOException e) { e.printStackTrace(); }
		return doc;
	}
}
