package rpgApp.pages

import rpgApp.main.IndexApplication

import com.vaadin.ui.Label
import com.vaadin.ui.VerticalLayout

class Scenarios extends VerticalLayout {
	private IndexApplication app
	
	public Scenarios(IndexApplication app) {
		this.app = app
		
		addComponent(new Label("Scenarios in construction..."));
	}
}
