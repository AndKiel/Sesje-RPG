package rpgApp.content

import rpgApp.data.SessionItem
import rpgApp.data.UserItem
import rpgApp.main.IndexApplication

import com.vaadin.terminal.Sizeable
import com.vaadin.terminal.ThemeResource
import com.vaadin.ui.HorizontalLayout
import com.vaadin.ui.Label
import com.vaadin.ui.Panel
import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.themes.Reindeer

class StartPage extends HorizontalLayout {
	private IndexApplication app

	private Panel p1
	private Panel p2
	private Panel p3
	private VerticalLayout vl1
	private VerticalLayout vl2
	private VerticalLayout vl3

	public StartPage(IndexApplication app) {
		this.app = app
		setMargin(true, true, false, true)
		setSpacing(true)
		setWidth("100%")
		setHeight("100%")

		p1 = new Panel("News")
		p1.setStyleName(Reindeer.PANEL_LIGHT)
		p1.setIcon(new ThemeResource("icons/news-icon.png"))
		p2 = new Panel("Incoming sessions")
		p2.setStyleName(Reindeer.PANEL_LIGHT)
		p2.setIcon(new ThemeResource("icons/announcement-icon.png"))
		p3 = new Panel("New users")
		p3.setStyleName(Reindeer.PANEL_LIGHT)
		p3.setIcon(new ThemeResource("icons/users-icon.png"))

		vl1 = p1.getContent()
		vl1.setMargin(true, true, false ,false )
		vl1.setSpacing(true)
		vl2 = p2.getContent()
		vl2.setMargin(true, true, false ,false )
		vl2.setSpacing(true)
		vl3 = p3.getContent()
		vl3.setMargin(true, true, false ,false )
		vl3.setSpacing(true)

		addComponent(p1)
		addComponent(p2)
		addComponent(p3)

		Panel pan1 = new Panel()
		Label l = new Label("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Quisque quis nulla quis lectus mollis tempor sit amet eget libero."
				+"Sed vulputate ligula id urna interdum laoreet. Cras interdum leo eget metus mattis at dapibus justo semper.")
		Label l2 = new Label("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Quisque quis nulla quis lectus mollis tempor sit amet eget libero."
			+"Sed vulputate ligula id urna interdum laoreet. Cras interdum leo eget metus mattis at dapibus justo semper.")
		Label l3 = new Label("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Quisque quis nulla quis lectus mollis tempor sit amet eget libero."
			+"Sed vulputate ligula id urna interdum laoreet. Cras interdum leo eget metus mattis at dapibus justo semper.")
		pan1.addComponent(l)
		Panel pan2 = new Panel()
		pan2.addComponent(l2)
		Panel pan3 = new Panel()
		pan3.addComponent(l3)
		vl1.addComponent(pan1)
		vl1.addComponent(pan2)
		vl1.addComponent(pan3)

		refreshContent()
	}


	public void refreshContent() {
		vl2.removeAllComponents()
		vl3.removeAllComponents()
		List<UserItem> lastUsers = app.userService.getLastUsers()
		List<SessionItem> incomingSessions = app.sessionService.getIncomingSessions()

		for(UserItem user in lastUsers) {
			Label l = new Label("<b>"+user.getNickname()+"</b> from <b>"+user.getLocation()+"</b> has registered at: ", Label.CONTENT_XHTML)
			Label l2 = new Label("<b>"+user.getDateCreated().substring(0,10)+" <font color=#80760B>"+user.getDateCreated().substring(11,16)+"</font></b>", Label.CONTENT_XHTML)
			Panel p = new Panel()
			p.addComponent(l)
			p.addComponent(l2)
			vl3.addComponent(p)
		}
		
		for(SessionItem ses in incomingSessions) {
			
			Label l = new Label("<b>#"+ses.getId()+" "+ses.getSystem()+"</b> session. Players: <b>"+app.sessionService.participantsCount(ses.getId())+"/"+ses.getMaxPlayers()+"</b>", Label.CONTENT_XHTML)
			Label l2 = new Label("Where: <b>"+ses.getLocation()+"</b>", Label.CONTENT_XHTML)
			Label l3 = new Label("When: <b>"+ses.getTimeStamp().toString().substring(0,10)+" <font color=#80760B>"+ses.getTimeStamp().toString().substring(11,16)+"</font></b>", Label.CONTENT_XHTML)
			Panel p = new Panel()
			p.addComponent(l)
			p.addComponent(l2)
			p.addComponent(l3)
			vl2.addComponent(p)
		}
	}
}

