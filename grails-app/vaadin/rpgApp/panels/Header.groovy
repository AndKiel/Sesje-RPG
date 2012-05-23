package rpgApp.panels

import rpgApp.main.IndexApplication

import com.vaadin.ui.Alignment
import com.vaadin.ui.Button
import com.vaadin.ui.HorizontalLayout
import com.vaadin.ui.Label
import com.vaadin.ui.Panel

class Header extends Panel {
	private IndexApplication app
	private HorizontalLayout hl
	private Panel p
	
	
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
	
		hl = new HorizontalLayout()
		p = new Panel()
		p.addStyleName("login-panel")
		
		setContent(hl)
		hl.setSizeFull()
		hl.setMargin(true)
		hl.addComponent(p)
		hl.setComponentAlignment(p, Alignment.MIDDLE_RIGHT)
				
		p.getContent().setSpacing(true)
		p.setWidth("300px")
		p.addComponent(app.logout)
		p.addComponent(app.login)
		p.addComponent(app.register)
		p.addComponent(app.who)
	}
}
