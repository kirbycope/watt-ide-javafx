package application;

import java.io.File;
import java.net.URISyntaxException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Settings {

	public static String settingsFilePath;
	public static Document settingsFile;

	/**
	 * Loads the ProjectSettings.xml file into memory from the file system
	 */
	public static void GetApplicationSettingsFile() {
		// Get the current path where the application (.jar) was launched from
		String path = "";
		try {
			path = Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
		}
		catch (URISyntaxException e) { e.printStackTrace(); }
		// Handle result
		if (path.length() > 0) {
			// Handle leading "/"
			if (path.startsWith("/")) {
				path = path.substring(1);
			}
			// Check for settings file
			Boolean exists = new File(path + "settings.xml").exists();
			// Handle existence
			if (exists) {
				// Load the file into memory
				settingsFile = Utilities.LoadDocumentFromFilePath(path + "settings.xml");
			}
			else {
				// Create the file
				settingsFile = Utilities.CreateDocument();
				// Create the root element node
			    org.w3c.dom.Element root = settingsFile.createElement("root");
			    settingsFile.appendChild(root);
			    // Write the document to disk
			    Utilities.writeDocumentToFile(settingsFile, new File(path + "settings.xml"));
			}
			// Store a reference to the file path
		    settingsFilePath = path + "settings.xml";
		}
	}

	/**
	 * Gets the path of the last opened project location
	 */
	public static String GetLastOpenedProject() {
		String lastOpenedProjectPath = "";
		if (settingsFile != null) {
			// Get all "files"
			NodeList lastProject = settingsFile.getDocumentElement().getElementsByTagName("lastProject");
			// Handle result
			if (lastProject.getLength() > 0) {
				lastOpenedProjectPath = Utilities.getXmlNodeAttribute(lastProject.item(0), "value");
			}
		}
		return lastOpenedProjectPath;
	}

	/**
	 * Sets the last project opened (or created)
	 */
	public static void SetLastOpenedProject(String path) {
		// Get the <root> element of the Document
		org.w3c.dom.Element root = settingsFile.getDocumentElement();
		// Remove previous entry
		Node previousLastProject = null;
		try {
			previousLastProject = root.getElementsByTagName("lastProject").item(0);
		}
		catch (Exception e) { /* do nothing */ }
		if (previousLastProject != null) {
			root.removeChild(previousLastProject);
		}
		// Create a child node for <lastProject value="{path}">
		org.w3c.dom.Element lastProjectNode = settingsFile.createElement("lastProject");
		lastProjectNode.setAttribute("value", path);
	    root.appendChild(lastProjectNode);
	    // Save document to disk
	    Utilities.writeDocumentToFile(settingsFile, new File(settingsFilePath));
	}
}
