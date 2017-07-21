package application.controllers;

import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;

import application.IDE;
import application.Main;
import application.Settings;
import application.UiHelpers;
import application.Utilities;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

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
	public static void loadProject(Stage mainStage, String filePath) {
		// Check if file exists
		File file = new File(filePath);
		if (file.exists()) {
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
			// Update the TreeView
			UiHelpers.UpdateTreeView();
		}
		else {
			// Alert user
			String title = "Project Not Found";
			String message = "The project you selected could not be found." + System.lineSeparator() + "Would you like to remove the reference?";
			int reply = JOptionPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_OPTION);
			if (reply == JOptionPane.YES_OPTION) {
				// Remove reference from Settings.XML and refresh the UI
				Settings.RemoveProjectFromPreviousProjects(filePath);
				// Refresh the UI
				UiHelpers.RefreshRecentProjects();
	        }
		}
	}
}
