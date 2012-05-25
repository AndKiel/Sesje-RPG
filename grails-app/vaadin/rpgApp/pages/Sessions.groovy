package rpgApp.pages

import rpgApp.main.IndexApplication

import com.vaadin.ui.Label
import com.vaadin.ui.VerticalLayout

class Sessions extends VerticalLayout {
	private IndexApplication app
	
	public Sessions(IndexApplication app) {
		this.app = app
		addComponent(new Label("Sessions in construction..."));
	}
}
