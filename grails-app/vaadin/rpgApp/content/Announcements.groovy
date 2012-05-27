package rpgApp.content

import rpgApp.main.IndexApplication

import com.vaadin.ui.Label
import com.vaadin.ui.VerticalLayout

class Announcements extends VerticalLayout {
	private IndexApplication app
	
	public Announcements(IndexApplication app) {
		this.app = app
		addComponent(new Label("Announcments sheets in construction..."));
	}
}
