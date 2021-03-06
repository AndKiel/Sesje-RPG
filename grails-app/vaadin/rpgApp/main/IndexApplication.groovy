package rpgApp.main

import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import rpgApp.exeptions.ValidationException
import rpgApp.panels.Content
import rpgApp.panels.Footer
import rpgApp.panels.Header
import rpgApp.services.AboutService
import rpgApp.services.EmailService
import rpgApp.services.FAQService
import rpgApp.services.MessageService
import rpgApp.services.NewsService
import rpgApp.services.NotificationService
import rpgApp.services.ScenarioService
import rpgApp.services.SecurityService;
import rpgApp.services.SessionService
import rpgApp.services.SystemService
import rpgApp.services.UserService;
import rpgApp.utils.ChatEntry
import rpgApp.utils.UrlParameter
import rpgApp.windows.LoginWindow;
import rpgApp.windows.RegisterWindow;


import com.github.wolfie.refresher.Refresher
import com.github.wolfie.refresher.Refresher.RefreshListener
import com.vaadin.Application
import com.vaadin.terminal.gwt.server.HttpServletRequestListener
import com.vaadin.ui.Alignment
import com.vaadin.ui.Button
import com.vaadin.ui.Label
import com.vaadin.ui.Panel
import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.Window
import com.vaadin.ui.Button.ClickEvent
import com.vaadin.ui.Button.ClickListener
import com.vaadin.ui.Window.Notification
import com.vaadin.ui.themes.Reindeer


class IndexApplication extends Application implements ClickListener, HttpServletRequestListener{

	public Button login
	public Button logout
	public Button register
	public Label who
	public Button notifications
	public Button unreadMessages
	public boolean isSigned
	
	public static final List<List<ChatEntry>> chatEntries = [[],[]]
	public static final List<Integer> roomIndexes = [5,6]

	private VerticalLayout layout	// Main window layout
	private Panel main				// Main window panel
	private Panel header
	private Panel content
	private Panel footer
	
	HttpServletResponse response

	public SecurityService security = (SecurityService)getBean(SecurityService)
	public UserService userService = (UserService)getBean(UserService)
	public AboutService aboutService = (AboutService)getBean(AboutService)
	public EmailService emailService = (EmailService)getBean(EmailService)
        public FAQService faqService = (FAQService)getBean(FAQService)
	public MessageService messageService = (MessageService)getBean(MessageService)
        public NewsService newsService = (NewsService)getBean(NewsService)
        public ScenarioService scenarioService = (ScenarioService)getBean(ScenarioService)
	public SystemService systemService = (SystemService)getBean(SystemService)
	public SessionService sessionService = (SessionService)getBean(SessionService)
	public NotificationService notificationService = (NotificationService)getBean(NotificationService)
	
	
	public class ContentRefreshListener implements RefreshListener {
		public void refresh(final Refresher source) {
			refreshContent()
		}
	}
	

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
		isSigned = security.isSignedIn()
		// Setting custom theme
		this.setTheme("rpg-theme")
		
		// Url parameters getting
		UrlParameter urlParameter = new UrlParameter(this)
		window.addParameterHandler(urlParameter);
		window.addURIHandler(urlParameter);

		// Creating main panel (FullSize)
		main = new Panel()
		main.setSizeFull()
		main.setStyleName(Reindeer.PANEL_LIGHT);
		window.setContent(main)

		// Creating main layout (FullSize)
		layout = main.getContent()
		layout.setWidth("100%")
		
		
		// Main window layout settings
		layout.addComponent(header = new Header(this))
		layout.addComponent(content = new Content(this))
		layout.addComponent(footer = new Footer(this))
		layout.setComponentAlignment(header, Alignment.MIDDLE_CENTER)
		layout.setComponentAlignment(content, Alignment.MIDDLE_CENTER)
		layout.setComponentAlignment(footer, Alignment.MIDDLE_CENTER)
		layout.setMargin(true, false, true, false)
		layout.setSpacing(true)
		
		final Refresher contentRefresher = new Refresher();
		contentRefresher.setRefreshInterval(5000);
		contentRefresher.addListener(new ContentRefreshListener());
		layout.addComponent(contentRefresher);
                layout.setStyleName(Reindeer.LAYOUT_BLACK);
	}
	
	
	public void refreshContent() {
		notifications.setCaption("You've got: "+notificationService.getNotificationsCount()+" notifications")
		unreadMessages.setCaption("You've got: "+messageService.getUnreadCount()+" new messages")
		content.getMyPage().getNotifications()
		content.getStartPage().refreshContent()
	}

	
	// Buttons clicks listner
	public void buttonClick(Button.ClickEvent event) {
		final Button source = event.getButton()
		if(source == login) {
			getMainWindow().addWindow(new LoginWindow(this))
		}
		else if(source == logout) {
			security.signOut()
			isSigned = security.isSignedIn()
			setLoginCookies("","",0)
			content.selectStartPage()
			refreshToolbar()
		}
		else if(source == register) {
			getMainWindow().addWindow(new RegisterWindow(this))
		}
		else if(source == unreadMessages) {
			content.goToMessages()
		} else if(source == notifications) {
			content.goToNotifications()
		}
	}

	boolean login(String username, String password) {
		try {
			security.signIn(username, password)
			isSigned = security.isSignedIn()
			who.setCaption("Hello "+security.getContextNickname()+" !")
			notifications.setCaption("You've got: "+notificationService.getNotificationsCount()+" notifications")
			unreadMessages.setCaption("You've got: "+messageService.getUnreadCount()+" new messages")
			content.selectStartPage()
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
			content.selectStartPage()
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
		notifications.setVisible(isSigned)
		unreadMessages.setVisible(isSigned)
	}

	private void setLoginPanel() {
		login.addListener((Button.ClickListener)this)
		login.setVisible(!isSigned)
		logout.addListener((Button.ClickListener)this)
		logout.setVisible(isSigned)
		register.addListener((Button.ClickListener)this)
		register.setVisible(!isSigned)
		who.setVisible(isSigned)
		who.setCaption("Hello "+security.getContextNickname()+" !")
		notifications.setVisible(isSigned)
		notifications.setCaption("You've got: "+notificationService.getNotificationsCount()+" notifications")
		notifications.addListener((Button.ClickListener)this)
		unreadMessages.setVisible(isSigned)
		unreadMessages.setCaption("You've got: "+messageService.getUnreadCount()+" new messages")
		unreadMessages.addListener((Button.ClickListener)this)
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
