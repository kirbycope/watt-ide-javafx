package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.xml.sax.SAXException;

import application.controllers.IdeController;
import application.models.TestStep;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Utilities {

	/**
	 * Creates a .txt file at the specified location with the given content
	 */
	public static void CreateTxtFileFromString(String content, String destination) {
		try ( PrintStream ps = new PrintStream(destination) ) {
			ps.println(content);
		}
		catch (FileNotFoundException e) { e.printStackTrace(); }
	}

	/**
	 * Creates a new instance of a [org.w3c.dom.]Document
	 */
	public static org.w3c.dom.Document CreateDocument() {
		// Create a new Document
	    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	    DocumentBuilder builder = null;
		try { builder = dbf.newDocumentBuilder(); }
		catch (ParserConfigurationException e1) { e1.printStackTrace();	}
	    org.w3c.dom.Document doc = builder.newDocument();
		return doc;
	}

	/**
	 * Gets the currently selected Tab
	 */
	public static Tab GetCurrentTab() {
		// Get TabPane
		TabPane tabPane = (TabPane) IDE.ideStage.getScene().lookup("#tab-pane");
		// Get/Record the selected Tab
		return tabPane.getSelectionModel().getSelectedItem();
	}

	/**
	 * Gets the current OS
	 */
	public static String GetOS() {
		// Get the OS name
		String osName = System.getProperty("os.name");
		if (osName.toLowerCase().contains("")) {
			return "windows";
		}
		else if (osName.toLowerCase().contains("mac")) {
			return "osx";
		}
		else if (osName.toLowerCase().contains("nux")) {
			return "linux";
		}
		else {
			return "unknown";
		}
	}

	/**
	 * Get the Test Step details from the given VBox (Test Step container)
	 */
	@SuppressWarnings("rawtypes")
	public static TestStep GetTestStepDetails(VBox testStepContainer) {
		// Get the first row of the Test Step container
		HBox firstRow = (HBox) testStepContainer.getChildren().get(0);
		// Get Execute Step
		boolean executeStep = ((CheckBox) firstRow.getChildren().get(0)).isSelected();
		// Get the Description
		 String description = ((TextField) firstRow.getChildren().get(1)).getText();
		// Get the second row of the Test Step container
		HBox secondRow = (HBox) testStepContainer.getChildren().get(1);
		// Get the Command
		String command = (String) ((ComboBox) secondRow.getChildren().get(0)).getValue();
		// Get the third row of the Test Step container
		HBox thirdRow = (HBox) testStepContainer.getChildren().get(2);
		// Get the Target
		String target = ((TextField) thirdRow.getChildren().get(0)).getText();
		// Get the fourth row of the Test Step container
		HBox fourthRow = (HBox) testStepContainer.getChildren().get(3);
		// Get the Value
		String value =  ((TextField) fourthRow.getChildren().get(0)).getText();
		// Get the fifth row of the Test Step container
		HBox fifthRow = (HBox) testStepContainer.getChildren().get(4);
		// Get the Continue on Failure
		boolean continueOnFailure = ((CheckBox) fifthRow.getChildren().get(1)).isSelected();
		// Return the new Test Step object
		return new TestStep(executeStep, description, command, target, value, continueOnFailure);
	}

	/**
	 * Gets the attribute of the XML Node
	 */
	public static String getXmlNodeAttribute(org.w3c.dom.Node item, String attribute){
		return ((org.w3c.dom.Node) item).getAttributes().getNamedItem(attribute).getNodeValue();
	}

	/**
	 * Loads a [XML] Document into memory from the given file path
	 */
	public static org.w3c.dom.Document LoadDocumentFromFilePath(String filePath) {
		File file = new File(filePath);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = null;
		try { dBuilder = dbFactory.newDocumentBuilder(); }
		catch (ParserConfigurationException e) { e.printStackTrace(); }
		org.w3c.dom.Document doc = null;
		try { doc = dBuilder.parse(file); }
		catch (SAXException e) { e.printStackTrace(); }
		catch (IOException e) { e.printStackTrace(); }
		return doc;
	}

	/**
	 * Load the HTML file and each Test Step to the 'selected' Tab
	 */
	public static void LoadSeleneseHtmlFile(String filePath) {
		// Read the HTML file and convert to a String
		StringBuilder contentBuilder = new StringBuilder();
		try
		{
			// Create the Buffered Reader for the File Reader with the given file name
		    BufferedReader in = new BufferedReader(new FileReader(filePath));
		    String str;
		    // REad each line and add to the string builder
		    while ((str = in.readLine()) != null)
		    {
		        contentBuilder.append(str);
		    }
		    // Close the Buffered Reader
		    in.close();
		    // Convert the string builder to a string
		    String content = contentBuilder.toString();
		    // Get/Set the Base Address
			IDE.baseAddress = content.substring( content.indexOf("<link")+32, content.lastIndexOf(" />")-1 );
			// Get the table of test steps
			Document doc = Jsoup.parse(content);
			Element table = doc.select("table").first();
			// Get the rows in the table
			Elements rows = table.select("tr");
			// Ignore the first row (the Test Case header)
			rows.remove(0);
			for (Element row : rows)
			{
				boolean execute;
				String description = "";
				String command = "";
				String target = "";
				String value = "";
				boolean continueOnFailure;
				// Get the previous node/element; the one before this table row (Looking for a HTML comment)
				Node previousSiblingNode = row.previousSibling();
				// Assure that the previous node...
				if 	(
					( previousSiblingNode != null ) && // is not null
					( row.previousSibling().getClass().getName() == "org.jsoup.nodes.Comment" ) && // is a Comment
					( ((Comment) previousSiblingNode).getData().length() > 0 ) // has some value
					)
				{
					description = ((Comment)previousSiblingNode).getData();
				}
				// Handle the <tr>
				String dataContinueOnFailure = row.attr("data-continueOnFailure");
				continueOnFailure = Boolean.parseBoolean(dataContinueOnFailure);
				String dataExecute = row.attr("data-execute");
				execute = Boolean.parseBoolean(dataExecute);
				// Handle each <td> in the <tr>
				for (int i = 0; i < row.childNodes().size(); i++)
				{
					Node childNode = row.childNodes().get(i);

					if (i == 1) { command = ((Element) childNode).text(); }
					else if (i==3) { target = ((Element) childNode).text(); }
					else if (i==5) { value = ((Element) childNode).text(); }
				}
				// Finally, Add the test step to the Test Steps Pane
				IdeController.AddStep(execute, description, command, target, value, continueOnFailure);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			/* do nothing */
		}
	}

	/**
	 * Run the given string as a command (in a separate thread)
	 * @param command String to run as command
	 */
	public static void RunCommand(String command) {
		ProcessBuilder pb = new ProcessBuilder(command);
		pb.redirectErrorStream(true);
		// Run in a new thread
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Process process = pb.start();
					BufferedReader inStreamReader = new BufferedReader( new InputStreamReader(process.getInputStream()) );
					while(inStreamReader.readLine() != null) {
					    // Write to "console" in the IDE
						TextArea taConsole = (TextArea) IDE.ideStage.getScene().lookup("#console-text-area");
						taConsole.appendText(inStreamReader.readLine() + System.lineSeparator());
					}
				}
				catch (IOException e) { e.printStackTrace(); }
			}
		}).start();
	}

	/**
	 * Writes the Document object to a file
	 */
	public static void writeDocumentToFile(org.w3c.dom.Document document, File file) {
        // Make a transformer factory to create the Transformer
        TransformerFactory tFactory = TransformerFactory.newInstance();
        // Make the Transformer
        Transformer transformer = null;
		try { transformer = tFactory.newTransformer(); }
		catch (TransformerConfigurationException e) { e.printStackTrace(); }
        // Mark the document as a DOM (XML) source
        DOMSource source = new DOMSource(document);
        // Say where we want the XML to go
        StreamResult result = new StreamResult(file);
        // Write the XML to file
        try { transformer.transform(source, result); }
        catch (TransformerException e) { e.printStackTrace(); }
    }
}
