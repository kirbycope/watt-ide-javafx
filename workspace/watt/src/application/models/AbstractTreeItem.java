package application.models;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.TreeItem;

@SuppressWarnings("rawtypes")
public abstract class AbstractTreeItem extends TreeItem {
	public abstract ContextMenu getMenu();
}
