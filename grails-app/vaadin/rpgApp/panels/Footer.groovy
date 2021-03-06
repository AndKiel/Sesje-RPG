package rpgApp.panels

import rpgApp.main.IndexApplication

import com.vaadin.ui.Alignment
import com.vaadin.ui.HorizontalLayout
import com.vaadin.ui.Label
import com.vaadin.ui.Panel

class Footer extends Panel {
	private IndexApplication app
	private HorizontalLayout footerLayout
	private Label copyrightLabel

	public Footer(IndexApplication app) {
		this.app = app
		
		addStyleName("footer-panel")
		setWidth("1000px")
		setHeight("50px")
		addStyleName("footer-panel")

		// Create layout
		footerLayout = new HorizontalLayout();

		// Add copyright notice in the center
		copyrightLabel = new Label();
		copyrightLabel.setContentMode(Label.CONTENT_XHTML);
		copyrightLabel.setValue("RPG-Sessions v0.1 &copy; Copyright 2012 &nbsp;" + "Marek Cabaj, Andrzej Kiełtyka, Radosław Gabiga" + ". All Rights Reserved.");
		copyrightLabel.setWidth(null)
		
		footerLayout.addComponent(copyrightLabel);
		footerLayout.setComponentAlignment(copyrightLabel, Alignment.MIDDLE_CENTER);
		footerLayout.setSizeFull()

		setContent(footerLayout)
	}
}
