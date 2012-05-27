package rpgApp.content

import rpgApp.main.IndexApplication

import com.vaadin.ui.Label
import com.vaadin.ui.VerticalLayout

class Faq extends VerticalLayout {
	private IndexApplication app
	
	public Faq(IndexApplication app) {
		this.app = app
		
		addComponent(new Label("FAQ in construction..."));
	}
}
