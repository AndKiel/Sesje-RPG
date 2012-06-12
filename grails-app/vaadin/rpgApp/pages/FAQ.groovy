package rpgApp.pages

import rpgApp.main.IndexApplication

import com.vaadin.ui.Label
import com.vaadin.ui.VerticalLayout

class FAQ extends VerticalLayout {
	private IndexApplication app
	
	public FAQ(IndexApplication app) {
		this.app = app
		
		addComponent(new Label("FAQ administration in construction..."));
	}
}
