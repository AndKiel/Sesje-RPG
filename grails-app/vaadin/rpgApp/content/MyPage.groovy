package rpgApp.content

import rpgApp.main.IndexApplication
import rpgApp.pages.Chars
import rpgApp.pages.Messages
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
	private static final Object scenariosN = "Scenarios"
	private static final Object charsN = "Character sheets"
	private static final Object sessionsN = "Sessions"

	private VerticalLayout messages
	private VerticalLayout profile
	private VerticalLayout scenarios
	private VerticalLayout chars
	private VerticalLayout sessions

	public MyPage(IndexApplication app) {
		this.app = app

		menu = new Tree()
		menu.setItemStyleGenerator(new TreeItemStyleGenerator())
		menu.addItem(profileN)
		menu.addItem(messagesN)
		menu.addItem(scenariosN)
		menu.addItem(charsN)
		menu.addItem(sessionsN)
		menu.setNullSelectionAllowed(false)
		menu.setMultiSelect(false)
		menu.addListener((ItemClickListener) this)
		menu.select(profileN)

		setFirstComponent(menu)
		setSecondComponent(getProfile())
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
				getMessages().fillMessages()
			} else if(scenariosN.equals(itemId)) {
				setSecondComponent(getScenarios())
			} else if(charsN.equals(itemId)) {
				setSecondComponent(getChars())
			} else if(sessionsN.equals(itemId)) {
				setSecondComponent(getSessions())
			}
		}
	}


	// LAZY LOADS
	private VerticalLayout getMessages() {
		if(messages == null) {
			messages = new Messages(app)
		}
		return messages
	}

	private VerticalLayout getProfile() {
		if(profile == null) {
			profile = new Profile(app)
		}
		return profile
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

	private VerticalLayout getSessions() {
		if(sessions == null) {
			sessions = new Sessions(app)
		}
		return sessions
	}

	public void setStartSelection() {
		menu.select(profileN)
		setSecondComponent(getProfile())
	}
	
	public void setMessagesSelection() {
		menu.select(messagesN)
		getMessages().fillMessages()
		setSecondComponent(getMessages())
	}
}