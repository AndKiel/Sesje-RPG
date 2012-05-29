package rpgApp.windows


import rpgApp.main.IndexApplication
import rpgApp.services.UserService

import com.vaadin.event.ShortcutAction.KeyCode
import com.vaadin.terminal.Sizeable
import com.vaadin.terminal.ThemeResource
import com.vaadin.ui.Alignment
import com.vaadin.ui.Button
import com.vaadin.ui.HorizontalLayout
import com.vaadin.ui.Label
import com.vaadin.ui.TextField
import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.Window
import com.vaadin.ui.Button.ClickEvent
import com.vaadin.ui.Window.Notification

class PasswordReset extends Window implements Button.ClickListener {
	private UserService userService
	private IndexApplication app

	private Button send
	private Button cancel
	private TextField email
	private Label txt
	
	PasswordReset(IndexApplication app) {
		// Window settings
		super("Forgotten password")
		this.app = app
		userService = app.userService
		this.setCaption("Forgotten password")
		setModal(true)
		setDraggable(false)
		setResizable(false)
		
		txt = new Label("Enter your e-mail to reset your password. New password will be sent to your e-mail")
		
		email = new TextField("Your e-mail: ")
		email.setWidth("100%")
		email.setRequired(true)
		
		send = new Button("Send", (Button.ClickListener)this)
		send.setClickShortcut(KeyCode.ENTER);
		send.addStyleName("primary");
		send.setIcon(new ThemeResource("icons/ok.png"))
		
		cancel = new Button("Cancel", (Button.ClickListener)this)
		cancel.setIcon(new ThemeResource("icons/cancel.png"))
		
		addComponent(txt)
		addComponent(email)
		HorizontalLayout hl = new HorizontalLayout();
		hl.addComponent(send)
		hl.addComponent(cancel)
		hl.setSpacing(true)
		addComponent(hl)
		VerticalLayout mainLayout = this.getContent()
		mainLayout.setSpacing(true)
		mainLayout.setComponentAlignment(hl, Alignment.BOTTOM_RIGHT);
		mainLayout.setMargin(true)

		setWidth(400, Sizeable.UNITS_PIXELS)
		center();
	}

	void buttonClick(ClickEvent clickEvent) {
		switch(clickEvent.source){
			case send:
				if(userService.resetPassword((String)email.getValue())) {
					Window.Notification emailNotif = new Window.Notification("New password was sent to: "+email.getValue(), Notification.TYPE_WARNING_MESSAGE)
					emailNotif.setDelayMsec(2500)
					app.getMainWindow().showNotification(emailNotif)
					this.close()
				} else {
					app.getMainWindow().showNotification("There is no such user", Notification.TYPE_ERROR_MESSAGE)
				}
				break;
			case cancel:
				this.close()
				break;
		}
	}
}
