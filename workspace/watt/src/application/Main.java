package application;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
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
	}
}
