package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Commands {
	public static ObservableList<String> options =
	    FXCollections.observableArrayList(
	        "click"
	        ,"clickAndWait"
	        ,"deleteAllVisibleCookies"
	        ,"echo"
	        ,"goBack"
	        ,"goBackAndWait"
	        ,"goForward"
	        ,"goForwardAndWait"
	        ,"open"
	        ,"pause"
	        ,"refresh"
	        ,"refreshAndWait"
	        ,"runScript"
	        ,"select"
	        ,"store"
	        ,"submit"
	        ,"submitAndWait"
	        ,"type"
	        ,"verifyChecked"
	        ,"verifyElementNotPresent"
	        ,"verifyElementPresent"
	        ,"verifyLocation"
	        ,"verifyNotChecked"
	        ,"verifyTable"
	        ,"verifyText"
	        ,"verifyTitle"
	        ,"verifyValue"
	        ,"waitForLocation"
		)
    ;
}
