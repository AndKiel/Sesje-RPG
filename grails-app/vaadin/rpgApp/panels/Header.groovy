package rpgApp.panels

import rpgApp.main.IndexApplication

import com.vaadin.terminal.ThemeResource
import com.vaadin.ui.Alignment
import com.vaadin.ui.Button
import com.vaadin.ui.HorizontalLayout
import com.vaadin.ui.Label
import com.vaadin.ui.Panel
import com.vaadin.ui.themes.BaseTheme

class Header extends Panel {
	private IndexApplication app
	private HorizontalLayout headerLayout
	private Panel loginPanel
	
	
	public Header(IndexApplication app) {
		this.app = app
		
		addStyleName("header-panel")
		setWidth("1000px")
		setHeight("200px")
		app.login = new Button("Login")
		app.logout = new Button("Logout")
		app.register = new Button("Register")
		app.who = new Label("")
		app.notifications = new Button("")
		app.notifications.setStyleName(BaseTheme.BUTTON_LINK)
		app.notifications.addStyleName("white-link")
		app.unreadMessages = new Button("")
		app.unreadMessages.setStyleName(BaseTheme.BUTTON_LINK)
		app.unreadMessages.addStyleName("white-link")
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
		loginPanel.addComponent(app.notifications)
		loginPanel.addComponent(app.unreadMessages)
		
		setContent(headerLayout)
	}
}
