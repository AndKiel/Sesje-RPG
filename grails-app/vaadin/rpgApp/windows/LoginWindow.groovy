package rpgApp.windows

import rpgApp.main.IndexApplication;

import com.vaadin.ui.Alignment
import com.vaadin.ui.CheckBox
import com.vaadin.ui.GridLayout
import com.vaadin.ui.PasswordField
import com.vaadin.ui.Window
import com.vaadin.ui.Form
import com.vaadin.ui.TextField
import com.vaadin.ui.Button
import com.vaadin.ui.Button.ClickEvent
import com.vaadin.event.ShortcutAction.KeyCode
import com.vaadin.terminal.Sizeable
import com.vaadin.terminal.ThemeResource

class LoginWindow extends Window implements Button.ClickListener {
	private IndexApplication app
	
	private Button login
	private Button cancel
	private Form loginForm
	private CheckBox rememberMe
	
	LoginWindow(IndexApplication app) {
		// Window settings
		super("Login")
		this.app = app
		this.setCaption("Login")
		setModal(true)
		setDraggable(false)
		setResizable(false)

		// Creating login form
		loginForm = new Form()
		TextField loginField = new TextField("Email: ")
		loginField.setWidth("100%")
		loginField.setRequired(true)
		loginField.focus()
		loginForm.addField("login", loginField)

		PasswordField passwordField = new PasswordField("Password: ")
		passwordField.setWidth("100%")
		passwordField.setRequired(true)
		loginForm.addField("password", passwordField)

		rememberMe = new CheckBox("Remember me")
		rememberMe.setValue(true)

		login = new Button("Login", (Button.ClickListener)this)
		login.setClickShortcut(KeyCode.ENTER);
		login.addStyleName("primary");
		login.setIcon(new ThemeResource("icons/ok.png"))
	
		cancel = new Button("Cancel", (Button.ClickListener)this)
		cancel.setIcon(new ThemeResource("icons/cancel.png"))
	
		// Creating login form footer 
		GridLayout footer = new GridLayout(2,2);
		footer.setSpacing(true);
		footer.setWidth(100, Sizeable.UNITS_PERCENTAGE)
		footer.addComponent(rememberMe, 0, 0, 1, 0)
		footer.setComponentAlignment(rememberMe, Alignment.MIDDLE_CENTER)
		footer.addComponent(login, 0, 1, 0, 1);
		footer.setComponentAlignment(login, Alignment.MIDDLE_CENTER)
		footer.addComponent(cancel, 1, 1, 1, 1);
		footer.setComponentAlignment(cancel, Alignment.MIDDLE_CENTER)

		loginForm.setFooter(footer)
		loginForm.setWidth("100%")

		addComponent(loginForm);

		setWidth(300, Sizeable.UNITS_PIXELS)
		center();
	}

	void buttonClick(ClickEvent clickEvent) {
		switch(clickEvent.source){
			case login:
				if(loginForm.isValid()){
					if(app.login((String)(loginForm.getField("login").getValue()), (String)(loginForm.getField("password").getValue()))){
						if(rememberMe.getValue()) {
							app.setLoginCookies((String)(loginForm.getField("login").getValue()), (String)(loginForm.getField("password").getValue()), 604800) 	// Cookie lifetime = 1 week
						}
						this.close()
					}
				} 
				break;
			case cancel:
				this.close()
				break;
		}
	}
}
