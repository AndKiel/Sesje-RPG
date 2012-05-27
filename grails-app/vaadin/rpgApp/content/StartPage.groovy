package rpgApp.content

import rpgApp.main.IndexApplication

import com.vaadin.ui.HorizontalLayout
import com.vaadin.ui.Label

class StartPage extends HorizontalLayout {
	private IndexApplication app
	
	public StartPage(IndexApplication app) {
		this.app = app
		
		if(app.isSigned) {
			addComponent(new Label("Start Page in construction..."));
		} else {
			addComponent(new Label("Hello Guest ! You must log in to enter this page"));
		}
	}
	
	public void refreshContent() {
		removeAllComponents()
		if(app.isSigned) {
			addComponent(new Label("Start Page in construction..."));
		} else {
			addComponent(new Label("Hello Guest !  You must log in to enter this page"));
		}
	}


}
