package rpgApp

import java.text.DateFormat

import com.vaadin.data.Property.ValueChangeEvent
import com.vaadin.data.Property.ValueChangeListener
import com.vaadin.data.validator.EmailValidator
import com.vaadin.data.validator.StringLengthValidator
import com.vaadin.event.ShortcutAction.KeyCode
import com.vaadin.terminal.Sizeable
import com.vaadin.ui.Alignment
import com.vaadin.ui.Button
import com.vaadin.ui.ComboBox
import com.vaadin.ui.Form
import com.vaadin.ui.HorizontalLayout
import com.vaadin.ui.PasswordField
import com.vaadin.ui.PopupDateField
import com.vaadin.ui.TextField
import com.vaadin.ui.Window
import com.vaadin.ui.Button.ClickEvent
import com.vaadin.ui.Window.Notification

class RegisterWindow extends Window implements Button.ClickListener, ValueChangeListener {

	private Button register = new Button("Create account", (Button.ClickListener)this)
	private Button cancel = new Button("Cancel", (Button.ClickListener)this)
	private Form registerForm = new Form()
	private IndexApplication app

	RegisterWindow(IndexApplication app) {
		super("Registration")
		this.app = app
		this.setCaption("Registration")
		setModal(true)
		setDraggable(false)
		setResizable(false)

		TextField loginField = new TextField("Email: ")
		loginField.addValidator(new EmailValidator("Wrong e-mail format"))
		loginField.setWidth("100%")
		loginField.setRequired(true)
		registerForm.addField("login", loginField)

		PasswordField passwordField = new PasswordField("Password: ")
		loginField.addValidator(new StringLengthValidator("Password must be 6 to 30 signs", 6, 30, false))
		passwordField.setWidth("100%")
		passwordField.setRequired(true)	
		registerForm.addField("password", passwordField)

		PasswordField password2Field = new PasswordField("Repeat password: ")
		password2Field.setWidth("100%")
		password2Field.setRequired(true)
		registerForm.addField("password2", password2Field)

		TextField nicknameField = new TextField("Nickname: ")
		nicknameField.addValidator(new StringLengthValidator("Nickname must be less than 30 signs", 1, 30, false))
		nicknameField.setWidth("100%")
		nicknameField.setRequired(true)
		registerForm.addField("nickname", nicknameField)

		ComboBox location = new ComboBox("Location: ")
		def countries = [] as SortedSet
		// Getting list of all countries
		Locale.availableLocales.displayCountry.each {
			if (it) {
				countries << it
			}
		}
		// Adding list of countries to ComboBox
		countries.each {
			location.addItem(it)
		}
		location.setNewItemsAllowed(false)
		location.setWidth("100%")
		registerForm.addField("location", location)

		PopupDateField birthday = new PopupDateField()
		birthday.setInputPrompt("mm.dd.rr")
		birthday.setCaption("Birthday: ")
		birthday.setResolution(PopupDateField.RESOLUTION_DAY)
		birthday.addListener(this)
		birthday.setImmediate(true)
		registerForm.addField("birthday", birthday)


		TextField homepageField = new TextField("Homepage: ")
		homepageField.addValidator(new StringLengthValidator("Homepage must be less than 40 signs", 0, 40, true))
		homepageField.setWidth("100%")
		registerForm.addField("homepage", homepageField)


		register.setClickShortcut(KeyCode.ENTER);
		register.addStyleName("primary");

		// Adding form footer
		HorizontalLayout footer = new HorizontalLayout();
		footer.setSpacing(true);
		footer.setWidth(100, Sizeable.UNITS_PERCENTAGE)
		footer.addComponent(register);
		footer.setComponentAlignment(register, Alignment.MIDDLE_CENTER)
		footer.addComponent(cancel);
		footer.setComponentAlignment(cancel, Alignment.MIDDLE_CENTER)
		registerForm.setFooter(footer)
		registerForm.setWidth("100%")

		addComponent(registerForm);

		setWidth(20, Sizeable.UNITS_PERCENTAGE)
		center();
	}

	void buttonClick(ClickEvent clickEvent) {
		switch(clickEvent.source){
			case register:
				if(registerForm.isValid()) {
					if(((String)(registerForm.getField("password").getValue())).equals((String)(registerForm.getField("password2").getValue()))) {
						if(app.register(
						(String)(registerForm.getField("login").getValue()),
						(String)(registerForm.getField("password").getValue()),
						(String)(registerForm.getField("nickname").getValue()),
						(String)(registerForm.getField("location").getValue()),
						(Date)(registerForm.getField("birthday").getValue()),
						(String)(registerForm.getField("homepage").getValue())
						)) {
							this.close()
						}
					} else {
						app.getMainWindow().showNotification("Passwords are not the same", Notification.TYPE_ERROR_MESSAGE);
					}
				} 
				break;
			case cancel:
				this.close()
				break;
		}
	}

	public void valueChange(ValueChangeEvent event) {
		// Get the new value and format it to the current locale
		DateFormat dateFormatter = DateFormat.getDateInstance(DateFormat.SHORT);
		Object value = event.getProperty().getValue();
		if (value == null || !(value instanceof Date)) {
			getWindow().showNotification("Invalid date entered");
		}
	}
}