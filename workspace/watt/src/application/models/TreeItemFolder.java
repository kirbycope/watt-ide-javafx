package application.models;

import application.UiHelpers;
import javafx.scene.control.ContextMenu;

public class TreeItemFolder extends AbstractTreeItem {

	@SuppressWarnings("unchecked")
	public TreeItemFolder(String value) {
		this.setValue(value);
	}

	@Override
	public ContextMenu getMenu() {
		return UiHelpers.GetFolderContextMenu();
	}
}
