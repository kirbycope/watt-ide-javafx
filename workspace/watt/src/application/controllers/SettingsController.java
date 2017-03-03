package application.controllers;

import java.io.IOException;

import application.Settings;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class SettingsController {

	/* -------- CLICK HANDLERS -------- */

	/**
	 * Handle the "click" on "Save" for BaseAddress
	 */
	public void SaveBaseAddress(MouseEvent e) throws IOException {
		// Get the Base Address TextField
 		TextField tfBaseAddress = (TextField) application.Settings.applicationSettingsStage.getScene().lookup("#base-address");
 		// Set the Base Address in the settings file
		Settings.SetBaseAddress(tfBaseAddress.getText());
	}
}
