package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

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
	 * Creates a "Demo" project
	 */
	public static org.w3c.dom.Document CreateDemo(org.w3c.dom.Document doc, String folderPath) {
		// Get the <root> element of the Document
		org.w3c.dom.Element root = doc.getDocumentElement();
		// Create a "Demo" test case element in the ProjectSettings.xml
		org.w3c.dom.Element demoTestCase = doc.createElement("file");
	    demoTestCase.setAttribute("name", "TestCase01");
	    root.appendChild(demoTestCase);

	    // Create a "Demo" test step element as a child of the "Demo" test case
	    org.w3c.dom.Element demoTestStep = doc.createElement("test-step");
	    demoTestStep.setAttribute("name", "TestStep01");
	    demoTestCase.appendChild(demoTestStep);
	    // Create a matching HTML file in the project's folder
	    CreateDemoFile("/demo/TestCase01.html", folderPath + "TestCase01.html");
	    // Create a matching JAVA file in the project's folder
	    CreateDemoFile("/demo/TestCase01.txt", folderPath + "TestCase01.java");

	    /* DEPENDENCIES */
	    CreateDemoFile("/demo/TestBase.txt", folderPath + "TestBase.java");
	    CreateDemoFile("/demo/client-combined-3.0.1-nodeps.jar", folderPath + "client-combined-3.0.1-nodeps.jar");
	    CreateDemoFile("/demo/hamcrest-core-1.3.jar", folderPath + "hamcrest-core-1.3.jar");
	    CreateDemoFile("/demo/junit-4.12.jar", folderPath + "junit-4.12.jar");
	    CreateDemoFile("/demo/selenium-server-standalone-3.0.1.jar", folderPath + "selenium-server-standalone-3.0.1.jar");
	    CreateDemoFile("/demo/compile-script.bat", folderPath + "compile-script.bat");
	    // Create "Compile" script
	    CreateTxtFileFromString("cd " + folderPath
	    		+ System.lineSeparator()
	    		+ "javac -cp *; TestBase.java TestCase01.java"
	    		, folderPath + "compile-script.bat");
	    // Create "Run" script
	    CreateTxtFileFromString("cd " + folderPath
	    		+ System.lineSeparator()
	    		+ "cd.."
	    		+ System.lineSeparator()
	    		+ "java -cp demo\\*; org.junit.runner.JUnitCore demo.TestCase01"
	    		, folderPath + "run-script.bat");
	    // Return the modified Document
	    return doc;
	}

	public static void CreateTxtFileFromString(String content, String destination) {
		try ( PrintStream ps = new PrintStream(destination) ) {
			ps.println(content);
		}
		catch (FileNotFoundException e) { e.printStackTrace(); }
	}

	/**
	 *
	 * @param origin The source code file path
	 * @param destination The destination file path
	 */
	public static void CreateDemoFile(String origin, String destination) {
		// Load the contents of the source file into a FileStream
        InputStream inputStream = Utilities.class.getResourceAsStream(origin); // "/demo/TestCase01.txt"
        // Save the FileStream as a File
        try { FileUtils.copyInputStreamToFile(inputStream, new File(destination)); }
        catch (IOException e) { e.printStackTrace(); }
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
}
