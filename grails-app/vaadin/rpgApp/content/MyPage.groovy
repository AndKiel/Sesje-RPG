package rpgApp.content

import rpgApp.main.IndexApplication
import rpgApp.pages.Chars
import rpgApp.pages.Messages
import rpgApp.pages.Notifications
import rpgApp.pages.Profile
import rpgApp.pages.Scenarios
import rpgApp.pages.Sessions
import rpgApp.utils.TreeItemStyleGenerator

import com.vaadin.event.ItemClickEvent
import com.vaadin.event.ItemClickEvent.ItemClickListener
import com.vaadin.ui.HorizontalSplitPanel
import com.vaadin.ui.Tree
import com.vaadin.ui.VerticalLayout

class MyPage extends HorizontalSplitPanel implements ItemClickListener {
	private IndexApplication app

	private Tree menu
	private static final Object profileN = "Profile"
	private static final Object messagesN = "Messages"
	private static final Object notificationsN = "Notifications"
	private static final Object sessionsN = "Sessions"
	private static final Object scenariosN = "Scenarios"
	private static final Object charsN = "Character sheets"

	private VerticalLayout messages
	private VerticalLayout profile
	private VerticalLayout notifications
	private VerticalLayout sessions
	private VerticalLayout scenarios
	private VerticalLayout chars

	public MyPage(IndexApplication app) {
		this.app = app

		menu = new Tree()
		menu.setItemStyleGenerator(new TreeItemStyleGenerator())
		menu.addItem(profileN)
		menu.addItem(messagesN)
		menu.addItem(notificationsN)
		menu.addItem(sessionsN)
		menu.addItem(scenariosN)
		menu.addItem(charsN)
		menu.setNullSelectionAllowed(false)
		menu.setMultiSelect(false)
		menu.addListener((ItemClickListener) this)
		menu.select(profileN)

		setFirstComponent(menu)
		setSplitPosition(15)
		setLocked(true)

	}

	public void itemClick(ItemClickEvent event) {
		Object itemId = event.getItemId()
		if(itemId != null) {
			if(profileN.equals(itemId)) {
				setSecondComponent(getProfile())
			} else if(messagesN.equals(itemId)) {
				setSecondComponent(getMessages())
			} else if(notificationsN.equals(itemId)) {
				setSecondComponent(getNotifications())
			} else if(sessionsN.equals(itemId)) {
				setSecondComponent(getSessions())
			} else if(scenariosN.equals(itemId)) {
				setSecondComponent(getScenarios())
			} else if(charsN.equals(itemId)) {
				setSecondComponent(getChars())
			}
		}
	}


	// LAZY LOADS
	private VerticalLayout getMessages() {
		if(messages == null) {
			messages = new Messages(app)
		}
		messages.fillMessages()
		return messages
	}

	private VerticalLayout getProfile() {
		if(profile == null) {
			profile = new Profile(app)
		}
		profile.setProfileInfo()
		return profile
	}

	private VerticalLayout getNotifications() {
		if(notifications == null) {
			notifications = new Notifications(app)
		}
		notifications.fillNotifications()
		return notifications
	}
	
	private VerticalLayout getSessions() {
		if(sessions == null) {
			sessions = new Sessions(app)
		}
		return sessions
	}
	
	private VerticalLayout getScenarios() {
		if(scenarios == null) {
			scenarios = new Scenarios(app)
		}
		return scenarios
	}

	private VerticalLayout getChars() {
		if(chars == null) {
			chars = new Chars(app)
		}
		return chars
	}

	public void setStartSelection() {
		menu.select(profileN)
		setSecondComponent(getProfile())
	}

	public void setMessagesSelection() {
		menu.select(messagesN)
		setSecondComponent(getMessages())
	}
	
	public void setNotificationsSelection() {
		menu.select(notificationsN)
		setSecondComponent(getNotifications())
	}
}