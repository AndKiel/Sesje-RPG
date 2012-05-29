package rpgApp.pages



import rpgApp.main.IndexApplication

import com.vaadin.data.validator.StringLengthValidator
import com.vaadin.terminal.Resource
import com.vaadin.terminal.Sizeable
import com.vaadin.terminal.ThemeResource
import com.vaadin.ui.Alignment
import com.vaadin.ui.Button
import com.vaadin.ui.CheckBox
import com.vaadin.ui.ComboBox
import com.vaadin.ui.Embedded
import com.vaadin.ui.Form
import com.vaadin.ui.GridLayout
import com.vaadin.ui.HorizontalLayout
import com.vaadin.ui.Label
import com.vaadin.ui.PopupDateField
import com.vaadin.ui.TextField
import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.Button.ClickEvent

class Profile extends VerticalLayout implements Button.ClickListener {
	private IndexApplication app

	private Button edit
	private Button save
	private Button discard
	private Form profileForm

	private Label login
	private Label roles
	private Label nickname
	private Label joined

	private ComboBox location
	private PopupDateField birthday
	private TextField homepage
	private CheckBox showChars
	private CheckBox showScenarios
	private CheckBox commentNotify
	private CheckBox sessionNotify
	private CheckBox messageNotify

	Profile(IndexApplication app) {
		this.app = app
		setSpacing(true)
		setMargin(true)
		addStyleName("left75")
		setWidth("60%")

		// Creating profile info labels
		GridLayout gl = new GridLayout(2,6)
		gl.setSpacing(true)

		Resource res = new ThemeResource("icons/profile-img.png");
		Embedded e = new Embedded(null, res);
		e.setWidth("112px");
		e.setHeight("117px");

		login = new Label("")
		login.setContentMode(Label.CONTENT_XHTML)
		roles = new Label("")
		roles.setContentMode(Label.CONTENT_XHTML)
		nickname = new Label("")
		nickname.setContentMode(Label.CONTENT_XHTML)
		joined = new Label("")
		joined.setContentMode(Label.CONTENT_XHTML)
		gl.addComponent(e,0,0,0,5)
		gl.addComponent(login,1,0,1,0)
		gl.addComponent(nickname,1,1,1,1)
		gl.addComponent(roles,1,2,1,2)
		gl.addComponent(joined,1,4,1,4)
		addComponent(gl)

		// Creating register form
		profileForm = new Form()

		location = new ComboBox("Location: ")
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
		profileForm.addField("location", location)

		birthday = new PopupDateField()
		birthday.setInputPrompt("mm.dd.rr")
		birthday.setCaption("Birthday: ")
		birthday.setResolution(PopupDateField.RESOLUTION_DAY)
		birthday.setImmediate(true)
		profileForm.addField("birthday", birthday)

		homepage = new TextField("Homepage: ")
		homepage.addValidator(new StringLengthValidator("Homepage must be less than 40 signs", 0, 40, true))
		homepage.setWidth("100%")
		homepage.setNullRepresentation("")
		profileForm.addField("homepage", homepage)

		showChars = new CheckBox("Show my character sheets")
		showScenarios = new CheckBox("Show my scenarios")
		commentNotify = new CheckBox("Notify me about new comments")
		sessionNotify = new CheckBox("Notify me about new sessions")
		messageNotify = new CheckBox("Notify me about new messages")
		profileForm.addField("showChars", showChars)
		profileForm.addField("showScenarios", showScenarios)
		profileForm.addField("commentNotify", commentNotify)
		profileForm.addField("sessionNotify", sessionNotify)
		profileForm.addField("messageNotify", messageNotify)

		// Creating footer
		edit = new Button("Edit", (Button.ClickListener)this)
		edit.setIcon(new ThemeResource("icons/document-txt.png"))
		save = new Button("Save", (Button.ClickListener)this)
		save.setIcon(new ThemeResource("icons/ok.png"))
		discard = new Button("Discard", (Button.ClickListener)this)
		discard.setIcon(new ThemeResource("icons/cancel.png"))

		// Adding form footer
		HorizontalLayout footer = new HorizontalLayout();
		footer.setSpacing(true);
		footer.setWidth(100, Sizeable.UNITS_PERCENTAGE)
		footer.addComponent(edit);
		footer.setComponentAlignment(edit, Alignment.MIDDLE_LEFT)
		footer.addComponent(save);
		footer.setComponentAlignment(save, Alignment.MIDDLE_LEFT)
		footer.addComponent(discard);
		footer.setComponentAlignment(discard, Alignment.MIDDLE_LEFT)
		profileForm.setFooter(footer)

		addComponent(profileForm);
		this.setExpandRatio(profileForm, 1.0f)
		setProfileInfo()
	}

	void buttonClick(ClickEvent clickEvent) {
		switch(clickEvent.source){
			case edit:
				setReadOnly(false)
				break;
			case save:
				if(profileForm.isValid()) {
					app.userService.updatePerson(location.getValue(), (Date)birthday.getValue(), homepage.getValue(), showChars.getValue(), showScenarios.getValue(), commentNotify.getValue(), sessionNotify.getValue(), messageNotify.getValue())
					setReadOnly(true)
				}
				break;
			case discard:
				setProfileInfo()
				break;
		}
	}

	public void setReadOnly(boolean readOnly) {
		profileForm.setReadOnly(readOnly)
		edit.setVisible(readOnly)
		save.setVisible(!readOnly)
		discard.setVisible(!readOnly)
	}

	public void setProfileInfo() {
		setReadOnly(false)
		login.setValue("<b>Login: </b>"+app.userService.getLogin())
		roles.setValue("<b>Your roles: </b>"+app.userService.getRoles().toString())
		nickname.setValue("<b>Nickname: </b>"+app.userService.getNickname())
		joined.setValue("<b>Joined </b>: "+app.userService.getDateCreated().toLocaleString())

		location.setValue(app.userService.getLocation())
		birthday.setValue(app.userService.getBirthday())
		homepage.setValue(app.userService.getHomepage())
		showChars.setValue(app.userService.getShowChars())
		showScenarios.setValue(app.userService.getShowScenarios())
		commentNotify.setValue(app.userService.getCommentNotify())
		sessionNotify.setValue(app.userService.getSessionNotify())
		messageNotify.setValue(app.userService.getMessageNotify())
		setReadOnly(true)
	}
}
