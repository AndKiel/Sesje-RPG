package rpgApp.panels

import rpgApp.main.IndexApplication

import com.vaadin.ui.Alignment
import com.vaadin.ui.Button
import com.vaadin.ui.HorizontalLayout
import com.vaadin.ui.Label
import com.vaadin.ui.Panel

class Header extends Panel {
	private IndexApplication app
	private HorizontalLayout headerLayout
	private Panel loginPanel
	
	
	public Header(IndexApplication app) {
		this.app = app
		
		addStyleName("header-panel")
		setWidth("1000px")
		setHeight("150px")
		app.login = new Button("Login")
		app.logout = new Button("Logout")
		app.register = new Button("Register")
		app.who = new Label("")
	
		app.setLoginPanel()
	
		headerLayout = new HorizontalLayout()
		loginPanel = new Panel()
		loginPanel.addStyleName("login-panel")
		
		headerLayout.setSizeFull()
		headerLayout.setMargin(true)
		headerLayout.addComponent(loginPanel)
		headerLayout.setComponentAlignment(loginPanel, Alignment.MIDDLE_RIGHT)
				
		loginPanel.getContent().setSpacing(true)
		loginPanel.setWidth("300px")
		loginPanel.addComponent(app.logout)
		loginPanel.addComponent(app.login)
		loginPanel.addComponent(app.register)
		loginPanel.addComponent(app.who)
		
		setContent(headerLayout)
	}
}
