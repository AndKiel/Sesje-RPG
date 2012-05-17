package rpgSession


import com.vaadin.Application
import com.vaadin.ui.Button
import com.vaadin.ui.Label
import com.vaadin.ui.Window
import com.vaadin.ui.Button.ClickListener
import com.vaadin.ui.Window.Notification

class IndexApplication extends Application implements ClickListener {

	private Button login = new Button("Login")
	private Button logout = new Button("Logout")
	private Button show = new Button("Show comment")
	private Label comment = new Label("")

	private SecurityService security = (SecurityService)getBean(SecurityService)
	private CommentService commentService = (CommentService)getBean(CommentService)

	void init() {
		def window = new Window("RPG Sessions")
		setMainWindow window

		login.addListener((Button.ClickListener)this)
		getMainWindow().addComponent(login)
		login.setVisible(!security.isSignedIn())

		logout.addListener((Button.ClickListener)this)
		getMainWindow().addComponent(logout)
		logout.setVisible(security.isSignedIn())

		show.addListener((Button.ClickListener)this)
		getMainWindow().addComponent(show)

		getMainWindow().addComponent(comment)
	}

	public void buttonClick(Button.ClickEvent event) {
		switch (event.getButton()) {
			case login: getMainWindow().addWindow(new LoginWindow(this)); break;
			case logout:
				security.signOut()
				comment.setCaption("")
				refreshToolbar();
			case show:
				String commentS = ""
				try {
					commentS = commentService.getComment(1)
				} catch (Exception e) {
					getMainWindow().showNotification(e.message,Notification.TYPE_ERROR_MESSAGE)
					break;
				}
				comment.setCaption("Komentarz:  "+commentS)
				break;
		}
	}

	boolean login(String username, String password) {
		try {
			security.signIn(username, password)
			refreshToolbar()
			return true
		} catch (SecurityServiceException e) {
			getMainWindow().showNotification(e.message, Notification.TYPE_ERROR_MESSAGE);
			return false
		}
	}

	private void refreshToolbar(){
		login.setVisible(!security.isSignedIn())
		logout.setVisible(security.isSignedIn())
	}
}
