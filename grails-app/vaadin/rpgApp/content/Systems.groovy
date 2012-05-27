package rpgApp.content

import rpgApp.main.IndexApplication

import com.vaadin.ui.Label
import com.vaadin.ui.VerticalLayout

class Systems extends VerticalLayout {
	private IndexApplication app
	
	public Systems(IndexApplication app) {
		this.app = app
		addComponent(new Label("Systems in construction..."));
	}
}
