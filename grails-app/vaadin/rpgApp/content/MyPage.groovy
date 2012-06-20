package rpgApp.content

import rpgApp.main.IndexApplication
import rpgApp.pages.Chars
import rpgApp.pages.Messages
import rpgApp.pages.News
import rpgApp.pages.Notifications
import rpgApp.pages.Profile
import rpgApp.pages.FAQ
import rpgApp.pages.Scenarios
import rpgApp.pages.Sessions
import rpgApp.utils.TreeItemStyleGenerator

import com.vaadin.event.ItemClickEvent
import com.vaadin.event.ItemClickEvent.ItemClickListener
import com.vaadin.ui.HorizontalLayout
import com.vaadin.ui.Tree
import com.vaadin.ui.VerticalLayout

class MyPage extends HorizontalLayout implements ItemClickListener {
	private IndexApplication app

	private Tree menu
	private static final Object profileN = "Profile"
	private static final Object messagesN = "Messages"
	private static final Object notificationsN = "Notifications"
	private static final Object sessionsN = "Sessions"
	private static final Object scenariosN = "Scenarios"
	private static final Object charsN = "Character sheets"
	private static final Object newsN = "News"
	private static final Object faqN = "FAQ"

	private VerticalLayout rightLayout
	private VerticalLayout messages
	private VerticalLayout profile
	private VerticalLayout notifications
	private VerticalLayout sessions
	private VerticalLayout scenarios
	private VerticalLayout chars
	private VerticalLayout news
	private VerticalLayout faq

	public MyPage(IndexApplication app) {
		this.app = app
		setWidth("100%")
		
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
		addComponent(menu)
		rightLayout = new VerticalLayout()
		rightLayout.addStyleName("blabla")
		addComponent(rightLayout)
		setExpandRatio(menu, 0.05f)
		setExpandRatio(rightLayout, 0.95f)
	}

	public void itemClick(ItemClickEvent event) {
		Object itemId = event.getItemId()
		if(itemId != null) {
			if(profileN.equals(itemId)) {
				rightLayout.removeAllComponents()
				rightLayout.addComponent(getProfile())
			} else if(messagesN.equals(itemId)) {
				rightLayout.removeAllComponents()
				rightLayout.addComponent(getMessages())
			} else if(notificationsN.equals(itemId)) {
				rightLayout.removeAllComponents()
				rightLayout.addComponent(getNotifications())
			} else if(sessionsN.equals(itemId)) {
				rightLayout.removeAllComponents()
				rightLayout.addComponent(getSessions())
			} else if(scenariosN.equals(itemId)) {
				rightLayout.removeAllComponents()
				rightLayout.addComponent(getScenarios())
			} else if(charsN.equals(itemId)) {
				rightLayout.removeAllComponents()
				rightLayout.addComponent(getChars())
			} else if(newsN.equals(itemId)) {
				rightLayout.removeAllComponents()
				rightLayout.addComponent(getNews())
			} else if(faqN.equals(itemId)) {
				rightLayout.removeAllComponents()
				rightLayout.addComponent(getFAQ())
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
		sessions.fillSessions()
		return sessions
	}

	private VerticalLayout getScenarios() {
		if(scenarios == null) {
			scenarios = new Scenarios(app)
		}
                scenarios.fillScenarios()
		return scenarios
	}

	private VerticalLayout getChars() {
		if(chars == null) {
			chars = new Chars(app)
		}
		return chars
	}

	private VerticalLayout getNews() {
		if(news == null) {
			news = new News(app)
		}
		news.fillNews()
		return news
	}

	private VerticalLayout getFAQ() {
		if(faq == null) {
			faq = new FAQ(app)
		}
		faq.fillFAQ()
		return faq
	}

	public void setStartSelection() {
		menu.select(profileN)
		rightLayout.removeAllComponents()
		rightLayout.addComponent(getProfile())
		
		menu.removeItem(newsN)
		menu.removeItem(faqN)
		if(app.security.checkRole("Administrator") || app.security.checkRole("Moderator"))
		{
			menu.addItem(newsN)
		}
		if(app.security.checkRole("Administrator"))
		{
			menu.addItem(faqN)
		}

	}

	public void setMessagesSelection() {
		menu.select(messagesN)
		rightLayout.removeAllComponents()
		rightLayout.addComponent(getMessages())
	}

	public void setNotificationsSelection() {
		menu.select(notificationsN)
		rightLayout.removeAllComponents()
		rightLayout.addComponent(getNotifications())
	}
}