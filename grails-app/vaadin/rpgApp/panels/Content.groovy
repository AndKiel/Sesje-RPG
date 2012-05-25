package rpgApp.panels

import rpgApp.content.About
import rpgApp.content.Announcements
import rpgApp.content.Faq
import rpgApp.content.MyPage
import rpgApp.content.StartPage
import rpgApp.content.Systems
import rpgApp.content.Users
import rpgApp.main.IndexApplication

import com.vaadin.ui.HorizontalLayout
import com.vaadin.ui.HorizontalSplitPanel
import com.vaadin.ui.Panel
import com.vaadin.ui.TabSheet
import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.Window
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent
import com.vaadin.ui.TabSheet.Tab
import com.vaadin.ui.Window.Notification
import com.vaadin.ui.themes.Reindeer

class Content extends Panel implements TabSheet.SelectedTabChangeListener {
	private IndexApplication app
	
	private TabSheet tabSheet
	private HorizontalLayout startPage
	private VerticalLayout systems
	private VerticalLayout announcements
	private VerticalLayout users
	private HorizontalSplitPanel myPage
	private VerticalLayout about
	private VerticalLayout faq
	private Window.Notification loginError

	public Content(IndexApplication app) {
		this.app = app

		setWidth("1000px")
		setHeight("630px")
		
		loginError = new Window.Notification("You must log in to enter this page", Window.Notification.TYPE_ERROR_MESSAGE);
		loginError.setDelayMsec(100)

		tabSheet = new TabSheet()
		tabSheet.setHeight("625px")
		tabSheet.setWidth("100%")

		tabSheet.addTab(startPage = new StartPage(app), "Start Page");
		tabSheet.addTab(systems = new Systems(app), "Systems")
		tabSheet.addTab(announcements = new Announcements(app), "Announcements")
		tabSheet.addTab(users = new Users(app), "Users")
		tabSheet.addTab(myPage = new MyPage(app), "My Page")
		tabSheet.addTab(about = new About(app), "About")
		tabSheet.addTab(faq = new Faq(app), "FAQ")
		tabSheet.addListener((TabSheet.SelectedTabChangeListener) this)
		
		VerticalLayout panelLayout = getContent()
		panelLayout.setMargin(false)		
		panelLayout.addComponent(tabSheet)
	}

	public void selectStartPage() {
		tabSheet.setSelectedTab(startPage)
		startPage.refreshContent()
	}

	public void selectedTabChange(SelectedTabChangeEvent event) {
		TabSheet tabsheet = event.getTabSheet();
		Tab tab = tabsheet.getTab(tabsheet.getSelectedTab());
		if (tab != null) {
			if((tab.getCaption()).equals("My Page")) {
				if(app.isSigned == false) {
					tabSheet.setSelectedTab(startPage)
					app.getMainWindow().showNotification(loginError);
				} else {
					myPage.setStartSelection()
				}
			}
			if((tab.getCaption()).equals("Systems")) {
				if(app.isSigned == false) {
					tabSheet.setSelectedTab(startPage)
					app.getMainWindow().showNotification(loginError);
				}
			}
			if((tab.getCaption()).equals("Announcements")) {
				if(app.isSigned == false) {
					tabSheet.setSelectedTab(startPage)
					app.getMainWindow().showNotification(loginError);
				}
			}
			if((tab.getCaption()).equals("Users")) {
				if(app.isSigned == false) {
					tabSheet.setSelectedTab(startPage)
					app.getMainWindow().showNotification(loginError);
				}
			}
		}
	}

}
