package application;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class UiHelpers {

	/**
	 * Get the Console (TextArea) Node
	 */
	public static TextArea GetConsoleTextAreaNode() {
		return (TextArea) IDE.ideStage.getScene().lookup("#console-text-area");
	}

	/**
	 * Get the Test Step Builder's Description (TextField) Node
	 */
	public static TextField GetTestStepBuilderDescriptionNode() {
		return (TextField) IDE.ideStage.getScene().lookup("#test-step-builder-description");
	}

	/**
	 * Get the Test Step Builder's Command (ComboBox) Node
	 */
	@SuppressWarnings("rawtypes")
	public static ComboBox GetTestStepBuilderCommandNode() {
		return (ComboBox) IDE.ideStage.getScene().lookup("#test-step-builder-command");
	}

	/**
	 * Get the Test Step Builder's Description Node
	 */
	public static TextField GetTestStepBuilderTargetNode() {
		return (TextField) IDE.ideStage.getScene().lookup("#test-step-builder-target");
	}

	/**
	 * Get the Test Step Builder's Description (TextField) Node
	 */
	public static TextField GetTestStepBuilderValueNode() {
		return (TextField) IDE.ideStage.getScene().lookup("#test-step-builder-value");
	}

	/**
	 * Gets the Test Step Builder's Reference wrapper
	 */
	public static VBox GetReferenceWrapperNode() {
		return (VBox) IDE.ideStage.getScene().lookup("#reference-wrapper");
	}

	/**
	 * Gets the ContextMenu for a File TreeItem
	 */
	public static ContextMenu GetFileContextMenu() {
		ContextMenu contextMenu = new ContextMenu();
		MenuItem mi1 = new MenuItem("Delete Test Case");
		mi1.setOnAction(new EventHandler<ActionEvent>() {
		    public void handle(ActionEvent e) {
		        System.out.println("Delete Test Case selected...");
		        // TODO: Delete Test Case
		    }
		});
		contextMenu.getItems().add(mi1);
		return contextMenu;
	}

	/**
	 * Gets the ContextMenu for a Folder TreeItem
	 */
	public static ContextMenu GetFolderContextMenu() {
		ContextMenu contextMenu = new ContextMenu();
		MenuItem mi1 = new MenuItem("New Test Case");
		mi1.setOnAction(new EventHandler<ActionEvent>() {
		    public void handle(ActionEvent e) {
		        Utilities.CreateNewTestStep();
		    }
		});
		contextMenu.getItems().add(mi1);
		MenuItem mi2 = new MenuItem("New Folder");
		mi2.setOnAction(new EventHandler<ActionEvent>() {
		    public void handle(ActionEvent e) {
		    	Utilities.CreateNewTestFolder();
		    }
		});
		contextMenu.getItems().add(mi2);
		return contextMenu;
	}
}
