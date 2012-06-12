package rpgApp.pages

import rpgApp.main.IndexApplication

import com.vaadin.ui.Label
import com.vaadin.ui.VerticalLayout

class News extends VerticalLayout {
	private IndexApplication app
	
	public News(IndexApplication app) {
		this.app = app
		
		addComponent(new Label("News in construction..."));
	}
}
