package application;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;

import application.controllers.MainController;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

	public static Stage mainStage;

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
		CheckDependencies();
		// Get the applications's settings file and load it into memory
		Settings.GetApplicationSettingsFile();
		// Set the main stage
		try { initMainLayout(primaryStage); }
		catch (IOException e) { e.printStackTrace(); }
	}

	/**
	 * Initialize the Main stage
	 */
	private void initMainLayout(Stage primaryStage) throws IOException {
		// Set the stage reference
		mainStage = primaryStage;
		// Application (Stage) Icon
		mainStage.getIcons().add(new Image(Main.class.getResourceAsStream("/res/drawable/icon.png")));
		// Application (Stage) Title
		mainStage.setTitle("WATT");
		// Create a new FXML Loader (loads an object hierarchy from an XML document)
		FXMLLoader loader = new FXMLLoader();
		// Give the loader the location of the XML document
		loader.setLocation(Main.class.getResource("/res/layout/main.fxml"));
		// Load an object hierarchy from the FXML document into a generic (base class) node
		Parent root = loader.load();
		// Load the "scene" into the "stage" and set the "stage"
		mainStage.setScene(new Scene(root));
		// Show the application (stage) window
		mainStage.show();
		// Disable resize
		mainStage.setResizable(false);
		// Add Recent Projects
		AddRecentProjects(mainStage);
	}

	/**
	 * Adds recent projects to the main menu
	 */
	public static void AddRecentProjects(Stage primaryStage) {
		// Get the Recent Projects container
		VBox vbRecentProjects = (VBox) primaryStage.getScene().lookup("#recent-projects");
		// Start with the last opened project
		String lastProject = Settings.GetLastOpenedProject();
		// Truncate if needed
		String labelText = lastProject;
		if (labelText.length() > 40) {
			labelText = labelText.substring(0, 37) + "...";
		}
		// Create a Label
		Label labelLastProject = new Label(labelText);
		labelLastProject.setTooltip(new Tooltip(lastProject));
		labelLastProject.getStyleClass().add("label-item");
		labelLastProject.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent e) {
                MainController.loadProject(primaryStage, lastProject);
            }
        });
		// Add the project to the container
		vbRecentProjects.getChildren().add(labelLastProject);

		// TODO: Add more 'recent' projects
	}

	/**
	 * Checks for the .JAR files needed to compile and run the tests
	 */
	private static void CheckDependencies() {
		// Get the path of the first root drive
		String rootPath = File.listRoots()[0].getPath();
		// Create the WATT folder if not present
		try { Files.createDirectories(Paths.get(rootPath + "WATT")); }
		catch (Exception e) { e.printStackTrace(); }
		// Hamcrest
		String hamcrest = rootPath + "WATT" + "\\hamcrest-core-1.3.jar";
		File f = new File(hamcrest);
		if (f.exists() == false){
			CopyDependency("/lib/hamcrest-core-1.3.jar", hamcrest);
		}
		// JUnit
		String junit = rootPath + "WATT" + "\\junit-4.12.jar";
		f = new File(junit);
		if (f.exists() == false){
			CopyDependency("/lib/junit-4.12.jar", junit);
		}
		// Selenium
		String selenium = rootPath + "WATT" + "\\client-combined-3.0.1-nodeps.jar";
		f = new File(selenium);
		if (f.exists() == false){
			CopyDependency("/lib/client-combined-3.0.1-nodeps.jar", selenium);
		}
		// Selenium Server
		String seleniumServer = rootPath + "WATT" + "\\selenium-server-standalone-3.0.1.jar";
		f = new File(seleniumServer);
		if (f.exists() == false){
			CopyDependency("/lib/selenium-server-standalone-3.0.1.jar", seleniumServer);
		}
	}

	/**
	 * Copies a resource file to the local file system
	 */
	private static void CopyDependency(String origin, String destination) {
		// Load the contents of the source file into a FileStream
		InputStream inputStream = Utilities.class.getResourceAsStream(origin); // "/demo/TestCase01.txt"
		// Save the FileStream as a File
		try { FileUtils.copyInputStreamToFile(inputStream, new File(destination)); }
		catch (IOException e) { e.printStackTrace(); }
	}
}
