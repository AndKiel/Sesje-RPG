package rpgApp.content

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

	public MyPage() {
		menu = new Tree()

		setFirstComponent(menu)
		setSecondComponent(getProfile())
		setSplitPosition(15)
		setLocked(true)

		menu.setItemStyleGenerator(new TreeItemStyleGenerator())
		menu.addItem(profileN)
		menu.addItem(messagesN)
		menu.addItem(scenariosN)
		menu.addItem(charsN)
		menu.addItem(sessionsN)
		menu.setSelectable(true)
		menu.setNullSelectionAllowed(false)
		menu.addListener((ItemClickListener) this)
		menu.select(profileN)
	}

	public void itemClick(ItemClickEvent event) {
		Object itemId = event.getItemId()
		if(itemId != null) {
			if(profileN.equals(itemId)) {
				setSecondComponent(getProfile())
			} else if(messagesN.equals(itemId)) {
				setSecondComponent(getMessages())
			} else if(scenariosN.equals(itemId)) {
				setSecondComponent(getScenarios())
			} else if(charsN.equals(itemId)) {
				setSecondComponent(getChars())
			} else if(sessionsN.equals(itemId)) {
				setSecondComponent(getSessions())
			}
		}
	}

	private VerticalLayout getMessages() {
		if(messages == null) {
			messages = new Messages()
		}
		return messages
	}

	private VerticalLayout getProfile() {
		if(profile == null) {
			profile = new Profile()
		}
		return profile
	}
	
	private VerticalLayout getScenarios() {
		if(scenarios == null) {
			scenarios = new Scenarios()
		}
		return scenarios
	}
	
	private VerticalLayout getChars() {
		if(chars == null) {
			chars = new Chars()
		}
		return chars
	}
	
	private VerticalLayout getSessions() {
		if(sessions == null) {
			sessions = new Sessions()
		}
		return sessions
	}

	public void setStartSelection() {
		menu.select(profileN)
		setSecondComponent(getProfile())
	}
}