package application;

import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

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
	 * Get the Test Step Builder's Description (TextField) Node
	 */
	public static ContextMenu GetFolderContextMenu() {
		ContextMenu contextMenu = new ContextMenu();
		MenuItem mi1 = new MenuItem("New Test Case");
		contextMenu.getItems().add(mi1);
		MenuItem mi2 = new MenuItem("New Folder");
		contextMenu.getItems().add(mi2);

		return contextMenu;
	}
}
