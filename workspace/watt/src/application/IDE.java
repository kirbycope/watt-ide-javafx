package application;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class IDE {

	public static Stage ideStage;
	public static String projectFolderPath;
	public static String baseAddress;


	/**
	 * Initialize the IDE stage
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void initIdeLayout() throws IOException {
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
}
