package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
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

import org.apache.commons.io.FileUtils;
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
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class Utilities {

	/**
	 * Creates the compile-script.bat file for the given Test Case
	 */
	public static void CreateBatFileToCompileJavaFiles(String testCaseName) {
		UiHelpers.WriteToIdeConsole("Creating .bat file to compile .java files to .class files... ");
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("cd " + IDE.projectFolderPath + System.lineSeparator());
			// Trim the project file path to go up a level
			String parentFolderOfProject = IDE.projectFolderPath.substring(0, IDE.projectFolderPath.lastIndexOf("\\") + 1);
			sb.append("javac -cp " + parentFolderOfProject +"*; TestBase.java " + testCaseName + ".java");
			// Write File to disk
			CreateFileFromString(sb.toString(), IDE.projectFolderPath + "\\compile-script.bat");
			UiHelpers.WriteToIdeConsole("Complete." + System.lineSeparator());
		}
		catch (Exception e) {
			UiHelpers.WriteToIdeConsole("Failed!" + System.lineSeparator());
			throw e;
		}
	}

	/**
	 * Creates the run-script.bat file for the given Test Case
	 */
	public static void CreateBatFileToRunJunit(String testCaseName) {
		UiHelpers.WriteToIdeConsole("Creating .bat file to run JUnit using the .class files... ");
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("cd " + IDE.projectFolderPath + System.lineSeparator());
			sb.append("cd.."+ System.lineSeparator());
			// Trim the project file path to go up a level
			String parentFolderOfProject = IDE.projectFolderPath.substring(0, IDE.projectFolderPath.lastIndexOf("\\") + 1);
			// Trim the project file path to get the folder name
			String folderOfProject = IDE.projectFolderPath.substring(IDE.projectFolderPath.lastIndexOf("\\") + 1);
			sb.append("java -cp " + parentFolderOfProject +"*;" + folderOfProject + "\\*; org.junit.runner.JUnitCore " + folderOfProject + "." + testCaseName);
			// Write File to disk
			CreateFileFromString(sb.toString(), IDE.projectFolderPath + "\\run-script.bat");
			UiHelpers.WriteToIdeConsole("Complete." + System.lineSeparator());
		}
		catch (Exception e) {
			UiHelpers.WriteToIdeConsole("Failed!" + System.lineSeparator());
			throw e;
		}
	}

	/**
	 * Creates a copy of TestBase.java in the specified destination
	 */
	public static void CreateCopyOfTestBase(String destination){
		UiHelpers.WriteToIdeConsole("Creating a copy of TestBase.java... ");
		try {
			// Trim the project file path to get the folder name
			String folderOfProject = IDE.projectFolderPath.substring(IDE.projectFolderPath.lastIndexOf("\\") + 1);
			// Build out the file's content
			StringBuilder sb = new StringBuilder();
			sb.append("package " + folderOfProject + ";" + System.lineSeparator());
			sb.append(System.lineSeparator());
			sb.append("import org.junit.*;" + System.lineSeparator());
			sb.append("import org.openqa.selenium.*;" + System.lineSeparator());
			sb.append("import org.openqa.selenium.chrome.ChromeDriver;" + System.lineSeparator());
			sb.append(System.lineSeparator());
			sb.append("public class TestBase {" + System.lineSeparator());
			sb.append("	protected WebDriver driver;" + System.lineSeparator());
			String baseAddress = Settings.GetBaseAddress();
			if (baseAddress != null) {
				sb.append("	protected String baseUrl = \"" + baseAddress + "\";" + System.lineSeparator());
			}
			else {
				sb.append("	protected String baseUrl = \"http://timothycope.com/\";" + System.lineSeparator());
			}
			sb.append(System.lineSeparator());
			sb.append("	@Before" + System.lineSeparator());
			sb.append("	public void setUp() throws Exception {" + System.lineSeparator());
			sb.append("		System.setProperty(\"webdriver.chrome.driver\", \"C:\\\\Selenium\\\\chromedriver.exe\");" + System.lineSeparator());
			sb.append("		driver = new ChromeDriver();" + System.lineSeparator());
			sb.append("	}" + System.lineSeparator());
			sb.append(System.lineSeparator());
			sb.append("	@After" + System.lineSeparator());
			sb.append("	public void tearDown() throws Exception {" + System.lineSeparator());
			sb.append("		driver.quit();" + System.lineSeparator());
			sb.append("	}" + System.lineSeparator());
			sb.append("}" + System.lineSeparator());
			sb.append(System.lineSeparator());
			// Write the content to a file
			CreateFileFromString(sb.toString(), destination);
			UiHelpers.WriteToIdeConsole("Complete." + System.lineSeparator());
		}
		catch (Exception e) {
			UiHelpers.WriteToIdeConsole("Failed!" + System.lineSeparator());
			throw e;
		}
	}

	/**
	 * Creates a file at the specified location with the given content
	 */
	public static void CreateFileFromString(String content, String destination) {
		try ( PrintStream ps = new PrintStream(destination) ) {
			ps.println(content);
		}
		catch (FileNotFoundException e) { e.printStackTrace(); }
	}

	/**
	 * Creates a Test Case .html file at the specified location with the given testName
	 */
	private static void CreateNewTestCaseHtmlFile(String destination, String testName){
		try ( PrintStream ps = new PrintStream(destination) ) {
			ps.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			ps.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">");
			ps.println("<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\">");
			ps.println("<head profile=\"http://selenium-ide.openqa.org/profiles/test-case\">");
			ps.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />");
			ps.println("<link rel=\"selenium.base\" href=\"https://www.google.com\" />");
			ps.println("<title>" + testName + "</title>");
			ps.println("</head>");
			ps.println("<body>");
			ps.println("<table cellpadding=\"1\" cellspacing=\"1\" border=\"1\">");
			ps.println("<thead>");
			ps.println("<tr><td rowspan=\"1\" colspan=\"3\">" + testName + "</td></tr>");
			ps.println("</thead>");
			ps.println("<tbody>");
			ps.println("</tbody>");
			ps.println("</table>");
			ps.println("</body>");
			ps.println("</html>");
		}
		catch (FileNotFoundException e) { e.printStackTrace(); }
	}

	/**
	 * Creates a new instance of a org.w3c.dom.Document
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
	 * Shows the directory chooser for where to save the new folder
	 */
	public static void CreateNewTestFolder() {
		System.out.println("New Folder selected...");
        // Show Directory Chooser
        String folderPath = Utilities.ShowDirectoryChooser(IDE.ideStage);
        // With result (String) create the Node in XML to match the file system
        if (folderPath.length() > 0) {
        	System.out.println("File Path: " + folderPath); // DEBUGGING
        }
	}

	/**
	 * Shows the file chooser for where to save the new test
	 */
	public static void CreateNewTestStep() {
		System.out.println("New Test Case selected..."); // DEBUGGING
        // Show File Chooser
        String filePath = Utilities.ShowHtmlFileChooser(IDE.ideStage);
        // With result (String) create the Node in XML to match the file system
        if (filePath.length() > 0) {
        	// Get the directory
        	String directory = filePath.substring(0, filePath.lastIndexOf("\\")+1);
        	// Get the test name
        	String testName = filePath.substring(filePath.lastIndexOf("\\")+1, filePath.indexOf("."));
        	// Create the HTML file
        	CreateNewTestCaseHtmlFile(filePath, testName);

        	// TODO: More than just the <root>

        	// Check if we can add to the <root> Node of the ProjectSettings.xml file
        	if( directory.equals(IDE.projectFolderPath) || directory.equals(IDE.projectFolderPath + "\\") ) {
        		// Get the Project Settings file
        		org.w3c.dom.Document doc = Utilities.LoadDocumentFromFilePath(IDE.projectFolderPath + "\\ProjectSettings.xml");
        		// Get the <root> Node
        		org.w3c.dom.Element root = doc.getDocumentElement();
        		// Create the new element
    		    org.w3c.dom.Element child = doc.createElement("file");
    		    // Give the new element a value
    		    child.setAttribute("name", testName);
    		    // Add the root element to the document
    		    root.appendChild(child);
    		    // Write the Document to an XML File at the specified location
    		    Utilities.writeDocumentToFile(doc, new File(IDE.projectFolderPath + "\\ProjectSettings.xml"));
    		    // Update the TreeView
    		    UiHelpers.UpdateTreeView();
        	}
        }
	}

	/**
	 * Creates a .java file by parsing the given .html file
	 */
	public static void ExportHtmlToJava(String testCaseName, String destination) {
		UiHelpers.WriteToIdeConsole("Creating .java file from Test Case... ");
		try {
			// Trim the project file path to get the folder name
			String folderOfProject = IDE.projectFolderPath.substring(IDE.projectFolderPath.lastIndexOf("\\") + 1);
			// Build out the file's content
			StringBuilder sb = new StringBuilder();
			sb.append("package " + folderOfProject + ";" + System.lineSeparator());
			sb.append(System.lineSeparator());
			sb.append("import org.junit.*;" + System.lineSeparator());
			sb.append("import org.openqa.selenium.*;" + System.lineSeparator());
			sb.append("import " + folderOfProject + ".TestBase;" + System.lineSeparator());
			sb.append(System.lineSeparator());
			sb.append("public class " + testCaseName + " extends TestBase {" + System.lineSeparator());
			sb.append("  @Test" + System.lineSeparator());
			sb.append("  public void " + testCaseName.toLowerCase() + "() throws Exception {" + System.lineSeparator());

			// TODO: Parse the HTML and each test step

			sb.append("  }" + System.lineSeparator());
			sb.append("}" + System.lineSeparator());
			sb.append(System.lineSeparator());
			// Write the content to a file
			CreateFileFromString(sb.toString(), destination);
			UiHelpers.WriteToIdeConsole("Complete." + System.lineSeparator());
		}
		catch (Exception e) {
			UiHelpers.WriteToIdeConsole("Failed!" + System.lineSeparator());
			throw e;
		}
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
	 * Loads a [XML] Document into memory using the given File in memory
	 */
	private static org.w3c.dom.Document LoadDocumentFromFileObject(File file) {
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
	 * Loads a [XML] Document into memory from the given file path
	 */
	public static org.w3c.dom.Document LoadDocumentFromFilePath(String filePath) {
		File file = new File(filePath);
		return LoadDocumentFromFileObject(file);
	}

	/**
	 * Loads a [XML] Document into memory from the given resource (source code) file
	 */
	public static org.w3c.dom.Document LoadDocumentFromResource(String resourcePath) {
		File file = null;
		try { file = File.createTempFile("temp-file-name", ".tmp"); }
		catch (IOException e1) { e1.printStackTrace(); }
		InputStream inputStream = Main.class.getResourceAsStream(resourcePath);
		try { FileUtils.copyInputStreamToFile(inputStream, file); }
		catch (IOException e2) { e2.printStackTrace(); }
		return LoadDocumentFromFileObject(file);
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
				for (int i = 0; i < row.children().size(); i++)
				{
					Element childNode = row.children().get(i);
					if (i == 0) { command = childNode.text(); }
					else if (i==1) { target = childNode.text(); }
					else if (i==2) { value = childNode.text(); }
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
	 * Shows the DirectoryChooser in the given stage
	 * @return
	 */
	public static String ShowDirectoryChooser(Stage stage) {
		DirectoryChooser directoryChooser = new DirectoryChooser();
		directoryChooser.setTitle("Create New Folder");
		if (IDE.projectFolderPath != null) {
			directoryChooser.setInitialDirectory(new File(IDE.projectFolderPath));
		}
		String filePath = "";
		File directory = directoryChooser.showDialog(stage);
		if(directory != null) {
			filePath = directory.getAbsolutePath();
		}
		return filePath;
	}

	/**
	 * Shows the FileChooser (for HTML files) in the given Stage
	 */
	public static String ShowHtmlFileChooser(Stage stage) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Create New Test Case File");
		fileChooser.getExtensionFilters().addAll(
			new ExtensionFilter("HTML File", "*.html")
		);
		if (IDE.projectFolderPath != null) {
			fileChooser.setInitialDirectory(new File(IDE.projectFolderPath));
		}
		String filePath = "";
		File file = fileChooser.showSaveDialog(stage);
		if (file != null) {
			filePath = file.getAbsolutePath();
		}
		return filePath;
	}

	/**
	 * Shows the FileChooser (for XML files) in the given Stage
	 */
	public static String ShowXmlFileChooser(Stage stage) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Choose XML File");
		fileChooser.getExtensionFilters().addAll(
			new ExtensionFilter("XML Files", "*.xml")
		);
		String filePath = "";
		File file = fileChooser.showOpenDialog(stage);
		if (file != null) {
			filePath = file.getAbsolutePath();
		}
		return filePath;
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
