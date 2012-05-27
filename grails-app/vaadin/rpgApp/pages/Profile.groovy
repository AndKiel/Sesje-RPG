package rpgApp.pages

import rpgApp.main.IndexApplication

import com.vaadin.ui.Label
import com.vaadin.ui.VerticalLayout

class Profile extends VerticalLayout {
	private IndexApplication app
	
	public Profile(IndexApplication app) {
		this.app = app
		addComponent(new Label("Profile in construction..."));
	}
}
