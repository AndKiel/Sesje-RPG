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
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent
import com.vaadin.ui.TabSheet.Tab

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
	
	public Content(IndexApplication app) {
		this.app = app
		
		setWidth("1000px")
		setHeight("630px")
		VerticalLayout panelLayout = getContent()
		panelLayout.setMargin(false)
		
		tabSheet = new TabSheet()
		tabSheet.setHeight("625px")
		tabSheet.setWidth("100%")

		tabSheet.addTab(startPage = new StartPage(), "Start Page");
		tabSheet.addTab(systems = new Systems(), "Systems")
		tabSheet.addTab(announcements = new Announcements(), "Announcements")
		tabSheet.addTab(users = new Users(), "Users")
		tabSheet.addTab(myPage = new MyPage(), "My Page")
		tabSheet.addTab(about = new About(), "About")
		tabSheet.addTab(faq = new Faq(), "FAQ")
		tabSheet.addListener((TabSheet.SelectedTabChangeListener) this)
		panelLayout.addComponent(tabSheet)
	}
	
	public void selectedTabChange(SelectedTabChangeEvent event) {
		TabSheet tabsheet = event.getTabSheet();
		Tab tab = tabsheet.getTab(tabsheet.getSelectedTab());
		if (tab != null) {
			if((tab.getCaption()).equals("My Page")) {
				myPage.setStartSelection()
			}
		}
	}

}
