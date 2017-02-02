package application;

import java.io.IOException;

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
		// Add Recent Projects
		AddRecentProjects(primaryStage);
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
}
