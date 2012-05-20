package rpgApp

import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


import com.vaadin.Application
import com.vaadin.terminal.Sizeable
import com.vaadin.terminal.gwt.server.HttpServletRequestListener
import com.vaadin.ui.Alignment
import com.vaadin.ui.Button
import com.vaadin.ui.HorizontalLayout
import com.vaadin.ui.Label
import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.Window
import com.vaadin.ui.Button.ClickListener
import com.vaadin.ui.Window.Notification


class IndexApplication extends Application implements ClickListener, HttpServletRequestListener{

	private Button login = new Button("Login")
	private Button logout = new Button("Logout")
	private Button register = new Button("Register")
	private Label who = new Label("")
	private VerticalLayout layout = new VerticalLayout()
	private HorizontalLayout toolbar = new HorizontalLayout()

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
		window.setContent(layout)	
		layout.setWidth(300, Sizeable.UNITS_PIXELS)
		layout.setHeight(100, Sizeable.UNITS_PIXELS)
		layout.addComponent(toolbar)
		layout.setComponentAlignment(toolbar, Alignment.MIDDLE_CENTER)
		toolbar.setSpacing(true)
		
		boolean isSigned = security.isSignedIn()

		login.addListener((Button.ClickListener)this)
		toolbar.addComponent(login)
		login.setVisible(!isSigned)
		
		logout.addListener((Button.ClickListener)this)
		toolbar.addComponent(logout)
		logout.setVisible(isSigned)

		register.addListener((Button.ClickListener)this)
		toolbar.addComponent(register)
		register.setVisible(!isSigned)
		
		toolbar.addComponent(who)
		who.setVisible(isSigned)
		who.setCaption("You are logged in as: "+security.getContextNickname())
		
//		toolbar.setComponentAlignment(login, Alignment.MIDDLE_RIGHT)
//		toolbar.setComponentAlignment(logout, Alignment.MIDDLE_RIGHT)
//		toolbar.setComponentAlignment(register, Alignment.MIDDLE_LEFT)
//		toolbar.setComponentAlignment(who, Alignment.MIDDLE_LEFT)
		
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
