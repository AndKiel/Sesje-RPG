package rpgApp.content

import rpgApp.main.IndexApplication

import com.vaadin.ui.Label
import com.vaadin.ui.VerticalLayout

class About extends VerticalLayout {
	private IndexApplication app
	
	public About(IndexApplication app) {
		this.app = app
		setMargin(true)
		
		addComponent(new Label("About in construction..."));
	}
}
