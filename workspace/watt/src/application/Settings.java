package application;

import java.io.File;
import java.net.URISyntaxException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javafx.stage.Stage;

public class Settings {

	public static String applicationSettingsFilePath;
	public static Document applicationSettingsFile;
	public static Stage applicationSettingsStage;

	/**
	 * Loads the Settings.xml file into memory from the file system
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
				applicationSettingsFile = Utilities.LoadDocumentFromFilePath(path + "settings.xml");
			}
			else {
				// Create the file
				applicationSettingsFile = Utilities.CreateDocument();
				// Create the root element node
			    org.w3c.dom.Element root = applicationSettingsFile.createElement("root");
			    applicationSettingsFile.appendChild(root);
			    // Write the document to disk
			    Utilities.writeDocumentToFile(applicationSettingsFile, new File(path + "settings.xml"));
			}
			// Store a reference to the file path
		    applicationSettingsFilePath = path + "settings.xml";
		}
	}

	/**
	 * Gets the Base Address
	 */
	public static String GetBaseAddress() {
		// Load Projects Settings XML file
		Document doc = Utilities.LoadDocumentFromFilePath(IDE.projectFolderPath + "\\ProjectSettings.xml");
		String baseAddress = "";
		if (doc != null) {
			// Get all "files"
			NodeList baseAddressNode = doc.getDocumentElement().getElementsByTagName("baseAddress");
			// Handle result
			if (baseAddressNode.getLength() > 0) {
				baseAddress = Utilities.getXmlNodeAttribute(baseAddressNode.item(0), "value");
			}
		}
		return baseAddress;
	}

	/**
	 * Gets the path of the last opened project location
	 */
	public static String GetLastOpenedProject() {
		String lastOpenedProjectPath = "";
		if (applicationSettingsFile != null) {
			// Get all "files"
			NodeList lastProject = applicationSettingsFile.getDocumentElement().getElementsByTagName("lastProject");
			// Handle result
			if (lastProject.getLength() > 0) {
				lastOpenedProjectPath = Utilities.getXmlNodeAttribute(lastProject.item(0), "value");
			}
		}
		return lastOpenedProjectPath;
	}

	/**
	 * Gets the value of the "previousProjects" node
	 */
	public static String GetPreviousProjects() {
		String previousProjects = "";
		if (applicationSettingsFile != null) {
			// Get all "files"
			NodeList lastProject = applicationSettingsFile.getDocumentElement().getElementsByTagName("previousProjects");
			// Handle result
			if (lastProject.getLength() > 0) {
				previousProjects = Utilities.getXmlNodeAttribute(lastProject.item(0), "value");
			}
		}
		return previousProjects;
	}

	/**
	 * Removes the given project from the previousProjects
	 * @param projectToRemove
	 */
	public static void RemoveProjectFromPreviousProjects(String projectToRemove) {
		// Get the <root> element of the Document
		org.w3c.dom.Element root = applicationSettingsFile.getDocumentElement();

		// Handle the "previousProject"
		Node previousLastProject = null;
		String previousLastProjectValue = null;
		try {
			// Get the previous last opened project
			previousLastProject = root.getElementsByTagName("lastProject").item(0);
			// Get the previous last opened project's value
			previousLastProjectValue = previousLastProject.getAttributes().getNamedItem("value").getNodeValue();
			// Remove the previous last opened project
			root.removeChild(previousLastProject);
		}
		catch (Exception e) { /* do nothing */ }
		// Create a child node of root: <lastProject value="{path}">
		org.w3c.dom.Element lastProjectNode = applicationSettingsFile.createElement("lastProject");
		if (previousLastProjectValue == projectToRemove) {
			lastProjectNode.setAttribute("value", "");
		}
		else {
			lastProjectNode.setAttribute("value", previousLastProjectValue);
		}
		// Add the node to the root element
	    root.appendChild(lastProjectNode);

		// Handle the "previousProjects"
		Node previousProjects = null;
	    String previousProjectsValue = null;
		try {
			// Get the previous projects
	    	previousProjects = root.getElementsByTagName("previousProjects").item(0);
	    	// Get the previous projects' value
	    	previousProjectsValue = previousProjects.getAttributes().getNamedItem("value").getNodeValue();
	    	// Remove the previous last projects
	    	root.removeChild(previousProjects);
		}
		catch (Exception e) { /* do nothing*/ }
		// Remove the projectToRemove from previousProjectsValue
		if (previousProjectsValue != null) {
			if (previousProjectsValue.contains(projectToRemove)) {
				previousProjectsValue = previousProjectsValue.replace(projectToRemove, "");
				previousProjectsValue = previousProjectsValue.replace(",,", ",");
			}
			// Remove any leading commas
        	if (previousProjectsValue.startsWith(",")) {
    			previousProjectsValue = previousProjectsValue.substring(1);
    		}
        	// Remove any trailing commas
        	if (previousProjectsValue.endsWith(",")) {
        		previousProjectsValue = previousProjectsValue.substring(0, previousProjectsValue.length()-1);
        	}
		}
		// Create a child node of root: <lastProject value="{path},{path},etc">
    	org.w3c.dom.Element previousProjectsNode = applicationSettingsFile.createElement("previousProjects");
    	previousProjectsNode.setAttribute("value", previousProjectsValue);
    	// Add the node to the root element
	    root.appendChild(previousProjectsNode);

		// Save document to disk
	    Utilities.writeDocumentToFile(applicationSettingsFile, new File(applicationSettingsFilePath));
	}

	/**
	 * Sets the Base Address
	 */
	public static void SetBaseAddress(String baseAddress) {
		// Load Projects Settings XML file
		Document doc = Utilities.LoadDocumentFromFilePath(IDE.projectFolderPath + "\\ProjectSettings.xml");
		// Get the <root> element of the Document
		org.w3c.dom.Element root = doc.getDocumentElement();
		// Remove previous entry
		Node previousBaseAddress = null;
		try {
			previousBaseAddress = root.getElementsByTagName("baseAddress").item(0);
		}
		catch (Exception ex) { /* do nothing */ }
		if (previousBaseAddress != null) {
			root.removeChild(previousBaseAddress);
		}
		// Create a child node for <baseAddress value="{baseAddress}">
		org.w3c.dom.Element baseAddressNode = doc.createElement("baseAddress");
		baseAddressNode.setAttribute("value", baseAddress);
	    root.appendChild(baseAddressNode);
	    // Save document to disk
	    Utilities.writeDocumentToFile(doc, new File(IDE.projectFolderPath + "\\ProjectSettings.xml"));
	}

	/**
	 * Sets the last project opened (or created)
	 */
	public static void SetLastOpenedProject(String path) {
		// Get the <root> element of the Document
		org.w3c.dom.Element root = applicationSettingsFile.getDocumentElement();

		// Handle the "previousProject"
		Node previousLastProject = null;
		String previousLastProjectValue = null;
		try {
			// Get the previous last opened project
			previousLastProject = root.getElementsByTagName("lastProject").item(0);
			// Get the previous last opened project's value
			previousLastProjectValue = previousLastProject.getAttributes().getNamedItem("value").getNodeValue();
			// Remove the previous last opened project
			root.removeChild(previousLastProject);
		}
		catch (Exception e) { /* do nothing */ }
		// Create a child node of root: <lastProject value="{path}">
		org.w3c.dom.Element lastProjectNode = applicationSettingsFile.createElement("lastProject");
		lastProjectNode.setAttribute("value", path);
		// Add the node to the root element
	    root.appendChild(lastProjectNode);

		// Handle the "previousProjects"
		Node previousProjects = null;
	    String previousProjectsValue = null;
		try {
			// Get the previous projects
	    	previousProjects = root.getElementsByTagName("previousProjects").item(0);
	    	// Get the previous projects' value
	    	previousProjectsValue = previousProjects.getAttributes().getNamedItem("value").getNodeValue();
	    	// Remove the previous last projects
	    	root.removeChild(previousProjects);
		}
		catch (Exception e) { /* do nothing*/ }
		// Move the old previousProject to the previousProjects
		if (previousLastProjectValue != null) {
			previousProjectsValue = previousProjectsValue + "," +  previousLastProjectValue;
		}
		// Remove the [new] previousProject if contained in previousProjectsValue
    	if (previousProjectsValue != null) {
    		if(previousProjectsValue.contains(path)) {
    			previousProjectsValue = previousProjectsValue.replace(path, "");
    			previousProjectsValue = previousProjectsValue.replace(",,", ",");
    		}
    		// Remove any leading commas
        	if (previousProjectsValue.startsWith(",")) {
    			previousProjectsValue = previousProjectsValue.substring(1);
    		}
    	}
    	// Create a child node of root: <lastProject value="{path},{path},etc">
    	org.w3c.dom.Element previousProjectsNode = applicationSettingsFile.createElement("previousProjects");
    	previousProjectsNode.setAttribute("value", previousProjectsValue);
    	// Add the node to the root element
	    root.appendChild(previousProjectsNode);

		// Save document to disk
	    Utilities.writeDocumentToFile(applicationSettingsFile, new File(applicationSettingsFilePath));
	}
}
