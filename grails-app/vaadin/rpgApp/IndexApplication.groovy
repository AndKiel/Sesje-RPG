package rpgApp

import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


import com.vaadin.Application
import com.vaadin.terminal.gwt.server.HttpServletRequestListener
import com.vaadin.ui.Alignment
import com.vaadin.ui.Button
import com.vaadin.ui.Label
import com.vaadin.ui.Panel
import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.Window
import com.vaadin.ui.Button.ClickListener
import com.vaadin.ui.Window.Notification
import com.vaadin.ui.themes.Reindeer


class IndexApplication extends Application implements ClickListener, HttpServletRequestListener{

	private Button login
	private Button logout
	private Button register
	private Label who

	private VerticalLayout layout	// Main window layout
	private Panel main				// Main window panel
	private Panel header
	private Panel menuBar
	private Panel content
	private Panel footer

	HttpServletResponse response

	private SecurityService security = (SecurityService)getBean(SecurityService)
	private UserService userService = (UserService)getBean(UserService)

	public void onRequestStart(HttpServletRequest request, HttpServletResponse response) {
		String username = null
		String password = null

		this.response = response

		Cookie[] cookies = request.getCookies()
		for(i in cookies) {
			if("username".equals(i.getName())) {
				username = i.getValue()
			}
			else if("password".equals(i.getName())) {
				password = i.getValue()
			}
		}

		if(password != null && username != null) {
			security.signIn(username, password)
		}
	}

	public void onRequestEnd(HttpServletRequest request, HttpServletResponse response) {
	}

	void init() {
		def window = new Window("RPG Sessions")
		setMainWindow(window)
		
		// Creating main panel (FullSize)
		main = new Panel()
		main.setSizeFull()
		main.setStyleName(Reindeer.PANEL_LIGHT);
		window.setContent(main)
		
		// Creating main layout (FullSize)
		layout = new VerticalLayout()
		layout.setSizeFull()
		main.addComponent(layout)

		// Main window layout settings
		layout.addComponent(createHeader())
		layout.addComponent(createMenuBar())
		layout.addComponent(createContent())
		layout.addComponent(createFooter())
		layout.setComponentAlignment(header, Alignment.MIDDLE_CENTER)
		layout.setComponentAlignment(menuBar, Alignment.MIDDLE_CENTER)
		layout.setComponentAlignment(content, Alignment.MIDDLE_CENTER)
		layout.setComponentAlignment(footer, Alignment.MIDDLE_CENTER)
		layout.setMargin(true)
		layout.setSpacing(true)
	}

	Panel createHeader() {
		header = new Panel()
		header.setWidth("50%")
		header.setHeight("120px")
		login = new Button("Login")
		logout = new Button("Logout")
		register = new Button("Register")
		who = new Label("")

		boolean isSigned = security.isSignedIn()

		login.addListener((Button.ClickListener)this)
		login.setVisible(!isSigned)

		logout.addListener((Button.ClickListener)this)
		logout.setVisible(isSigned)

		register.addListener((Button.ClickListener)this)
		register.setVisible(!isSigned)

		who.setVisible(isSigned)
		who.setCaption("Your are logged in as: "+security.getContextNickname())
		
		header.addComponent(logout)
		header.addComponent(login)
		header.addComponent(register)
		header.addComponent(who)

		return header
	}

	Panel createMenuBar() {
		menuBar = new Panel()
		menuBar.setWidth("50%")
		menuBar.setHeight("50px")
		return menuBar
	}

	Panel createContent() {
		content = new Panel()
		content.setWidth("50%")
		content.setHeight("600px")
		return content
	}

	Panel createFooter() {
		footer = new Panel()
		footer.setWidth("50%")
		footer.setHeight("70px")
		return footer
	}

	// Buttons clicks listner
	public void buttonClick(Button.ClickEvent event) {
		final Button source = event.getButton()
		if(source == login) {
			getMainWindow().addWindow(new LoginWindow(this))
		}
		else if(source == logout) {
			security.signOut()
			setLoginCookies("","",0)
			refreshToolbar()
		}
		else if(source == register) {
			getMainWindow().addWindow(new RegisterWindow(this))
		}
	}

	boolean login(String username, String password) {
		try {
			security.signIn(username, password)
			who.setCaption("You are logged in as: "+security.getContextNickname())
			refreshToolbar()
			return true
		} catch (Exception e) {
			getMainWindow().showNotification(e.message, Notification.TYPE_ERROR_MESSAGE);
			return false
		}
	}

	boolean register(String login, String password, String nickname, String location, Date birthday, String homepage) {
		try{
			userService.createPerson(login, password, nickname, location, birthday, homepage)
			return true
		} catch(ValidationException e) {
			getMainWindow().showNotification(e.message, Notification.TYPE_ERROR_MESSAGE);
			return false
		}
	}

	private void refreshToolbar(){
		boolean isSigned = security.isSignedIn()

		login.setVisible(!isSigned)
		logout.setVisible(isSigned)
		register.setVisible(!isSigned)
		who.setVisible(isSigned)
	}

	public void setLoginCookies(String username, String password, int maxAge) {
		Cookie cookie = new Cookie("username", username)
		cookie.setMaxAge(maxAge)
		response.addCookie(cookie)

		cookie = new Cookie("password", password)
		cookie.setMaxAge(maxAge)
		cookie.setHttpOnly(true)
		response.addCookie(cookie)
	}
}
