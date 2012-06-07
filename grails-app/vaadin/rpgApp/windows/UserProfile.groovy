package rpgApp.windows

import rpgApp.data.UserItem
import rpgApp.main.IndexApplication

import com.vaadin.event.ShortcutAction.KeyCode
import com.vaadin.terminal.Resource
import com.vaadin.terminal.Sizeable
import com.vaadin.terminal.ThemeResource
import com.vaadin.ui.Alignment
import com.vaadin.ui.Button
import com.vaadin.ui.Embedded
import com.vaadin.ui.GridLayout
import com.vaadin.ui.Label
import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.Window
import com.vaadin.ui.Button.ClickEvent

class UserProfile extends Window implements Button.ClickListener {
	private IndexApplication app
	private UserItem userItem

	private Label login
	private Label roles
	private Label nickname
	private Label joined
	private Label state
	private Label location
	private Label birthday
	private Label homepage
	private Button ok

	UserProfile(IndexApplication app, UserItem userItem) {
		super("User info")
		this.app = app
		this.userItem = userItem
		setCaption("User info")
		setModal(true)
		setDraggable(false)
		setResizable(false)
		setWidth(500, Sizeable.UNITS_PIXELS)
		center();

		VerticalLayout mainLayout = getContent()
		mainLayout.setSpacing(true)
		mainLayout.setMargin(true)
		mainLayout.setSizeFull()

		// Creating profile info labels
		GridLayout gl = new GridLayout(2,5)
		gl.setSpacing(true)

		Resource res = new ThemeResource("icons/profile-img.png");
		Embedded e = new Embedded(null, res);
		e.setWidth("112px");
		e.setHeight("117px");

		login = new Label("<b>Login: </b>"+userItem.getLogin())
		login.setContentMode(Label.CONTENT_XHTML)
		roles = new Label("<b>Roles: </b>"+userItem.getRoles().toString())
		roles.setContentMode(Label.CONTENT_XHTML)
		nickname = new Label("<b>Nickname: </b>"+userItem.getNickname())
		nickname.setContentMode(Label.CONTENT_XHTML)
		joined = new Label("<b>Joined </b>: "+userItem.getDateCreated().substring(0,16))
		joined.setContentMode(Label.CONTENT_XHTML)
		state = new Label(userItem.getState() ? "<b>Account status: </b> Active" : "<b>Account status: </b> Inactive")
		state.setContentMode(Label.CONTENT_XHTML)
		gl.addComponent(e,0,0,0,4)
		gl.addComponent(login,1,0,1,0)
		gl.addComponent(nickname,1,1,1,1)
		gl.addComponent(roles,1,2,1,2)
		gl.addComponent(joined,1,3,1,3)
		gl.addComponent(state, 1,4,1,4)
		mainLayout.addComponent(gl)

		String s
		if(userItem.getLocation()) {
			s = userItem.getLocation()
		} else {
			s = ""
		}
		location = new Label("<b>Location: </b>"+s)
		location.setContentMode(Label.CONTENT_XHTML)
		if(userItem.getBirthday()) {
			s = userItem.getBirthday().toString().substring(0,10)
		} else {
			s = ""
		}
		birthday = new Label("<b>Birthday: </b>"+s)
		birthday.setContentMode(Label.CONTENT_XHTML)
		if(userItem.getHomepage()) {
			s = userItem.getHomepage()
		} else {
			s = ""
		}
		homepage = new Label("<b>Homepage: </b><a href="+s+">"+s+"</a>")
		homepage.setContentMode(Label.CONTENT_XHTML)
		ok = new Button("Ok")
		ok.setClickShortcut(KeyCode.ENTER);
		ok.addStyleName("primary");
		ok.setIcon(new ThemeResource("icons/ok.png"))
		ok.addListener((Button.ClickListener)this)
		mainLayout.addComponent(location)
		mainLayout.addComponent(birthday)
		mainLayout.addComponent(homepage)
		mainLayout.addComponent(ok)
		mainLayout.setComponentAlignment(ok, Alignment.MIDDLE_RIGHT)
	}

	void buttonClick(ClickEvent clickEvent) {
		switch(clickEvent.source){
			case ok:
				this.close()
				break
		}
	}
}
