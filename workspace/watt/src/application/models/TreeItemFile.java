package application.models;

import application.UiHelpers;
import javafx.scene.control.ContextMenu;

public class TreeItemFile extends AbstractTreeItem {

	@SuppressWarnings("unchecked")
	public TreeItemFile(String value) {
		this.setValue(value);
	}

	@Override
	public ContextMenu getMenu() {
		return UiHelpers.GetFileContextMenu();
	}

}
