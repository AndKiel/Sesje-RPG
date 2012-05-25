package rpgApp.pages

import rpgApp.main.IndexApplication

import com.vaadin.ui.Label
import com.vaadin.ui.VerticalLayout

class Chars extends VerticalLayout {
	private IndexApplication app
	
	public Chars(IndexApplication app) {
		this.app = app
		
		addComponent(new Label("Character sheets in construction..."));
	}
}
