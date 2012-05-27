package rpgApp.utils

import com.vaadin.ui.Tree.ItemStyleGenerator

class TreeItemStyleGenerator implements ItemStyleGenerator {
	protected static final String TREE_NODE_WITHOUT_CHILDREN_STYLE="no-children";


	public String getStyle(Object itemId) {
		return TREE_NODE_WITHOUT_CHILDREN_STYLE;
	}

}
