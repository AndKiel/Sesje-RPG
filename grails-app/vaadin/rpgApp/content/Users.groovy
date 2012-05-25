package rpgApp.content

import rpgApp.main.IndexApplication

import com.vaadin.ui.Label
import com.vaadin.ui.VerticalLayout

class Users extends VerticalLayout {
	private IndexApplication app
	
	public Users(IndexApplication app) {
		this.app = app
		
		addComponent(new Label("Users in construction..."));
	}
}
