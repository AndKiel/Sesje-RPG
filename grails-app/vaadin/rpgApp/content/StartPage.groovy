package rpgApp.content

import rpgApp.data.NewsItem
import rpgApp.data.SessionItem
import rpgApp.data.UserItem
import rpgApp.main.IndexApplication
import rpgApp.persistance.Notification
import rpgApp.windows.SessionJoin
import rpgApp.windows.UserProfile

import com.vaadin.terminal.ThemeResource
import com.vaadin.ui.Alignment
import com.vaadin.ui.Button
import com.vaadin.ui.HorizontalLayout
import com.vaadin.ui.Label
import com.vaadin.ui.Panel
import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.Button.ClickEvent
import com.vaadin.ui.themes.BaseTheme
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

        refreshContent()
    }


    public void refreshContent() {
        vl1.removeAllComponents()
        vl2.removeAllComponents()
        vl3.removeAllComponents()
        List<NewsItem> latestNews = app.newsService.getLatestNews()
        List<UserItem> lastUsers = app.userService.getLastUsers()
        List<SessionItem> incomingSessions = app.sessionService.getIncomingSessions()

        // Latest news
        for(NewsItem news in latestNews){
            Panel p = new Panel()
            p.addComponent(new Label("<b>"+news.getTitle()+"</b>", Label.CONTENT_XHTML))
            p.addComponent(new Label(news.getContent(), Label.CONTENT_XHTML))
            p.addComponent(new Label(news.getAuthor()+", "+news.getDateCreated(), Label.CONTENT_XHTML))
            vl1.addComponent(p)
        }
        
        // Last users
        for(UserItem user in lastUsers) {
            HorizontalLayout hl = new HorizontalLayout()
            Button b = new Button(user.getNickname())
            b.setStyleName(Reindeer.BUTTON_LINK)
            b.addStyleName("black-link")
            UserItem u = user
            b.addListener(new Button.ClickListener() {
                    public void buttonClick(ClickEvent event) {
                        app.getMainWindow().addWindow(new UserProfile(app,u))
                    }
                }
            )
            Label l = new Label(" from <b>"+user.getLocation()+"</b> has registered at: ", Label.CONTENT_XHTML)
            Label l2 = new Label("<b>"+user.getDateCreated().substring(0,10)+" <font color=#80760B>"+user.getDateCreated().substring(11,16)+"</font></b>", Label.CONTENT_XHTML)
            Panel p = new Panel()
            hl.addComponent(b)
            hl.addComponent(l)
            hl.setSpacing(true)
            hl.setComponentAlignment(b, Alignment.MIDDLE_LEFT)
            p.addComponent(hl)
            p.addComponent(l2)
            vl3.addComponent(p)
        }

        // Incoming sessions
        if(app.isSigned == true) {
            for(SessionItem ses in incomingSessions) {
                Panel p = new Panel()
                Label l = new Label("<b>#"+ses.getId()+" "+ses.getSystem()+"</b> session. Players: <b>"+app.sessionService.participantsCount(ses.getId())+"/"+ses.getMaxPlayers()+"</b>", Label.CONTENT_XHTML)
                Label l2 = new Label("Where: <b>"+ses.getLocation()+"</b>", Label.CONTENT_XHTML)
                HorizontalLayout h = new HorizontalLayout()
                h.setWidth("100%")
                Label l3 = new Label("When: <b>"+ses.getTimeStamp().toString().substring(0,10)+" ["+ses.getTimeStamp().toString().substring(11,16)+"]</b>", Label.CONTENT_XHTML)
                h.addComponent(l3)

                if(app.sessionService.checkMembership(ses.getId())) {

                    SessionItem sI = ses
                    String txt = ses.getId().toString()
                    Button b = new Button("Leave")
                    b.addListener(new Button.ClickListener() {
                            public void buttonClick(ClickEvent event) {
                                app.sessionService.playerLeave(sI.getId())
                                app.getMainWindow().showNotification("You have left the session", Notification.TYPE_WARNING_MESSAGE)
                                refreshContent()
                            }
                        }
                    )
                    b.setStyleName(BaseTheme.BUTTON_LINK)
                    b.addStyleName("black-link")
                    h.addComponent(b)
                    p.addStyleName("joined")

                } else {

                    SessionItem sI = ses
                    StartPage startP = this
                    Button b = new Button("Join")
                    b.addListener(new Button.ClickListener() {
                            public void buttonClick(ClickEvent event) {
                                if(app.sessionService.participantsCount(sI.getId()) == sI.getMaxPlayers()) {
                                    app.getMainWindow().showNotification("No slots for this session", Notification.TYPE_ERROR_MESSAGE)
                                } else {
                                    app.getMainWindow().addWindow(new SessionJoin(app, null, startP, sI, app.sessionService))
                                }
                            }
                        }
                    )
                    b.setStyleName(BaseTheme.BUTTON_LINK)
                    b.addStyleName("black-link")
                    h.addComponent(b)

                }

                if(app.sessionService.checkMembership(ses.getId()) == 2) {
                    p.addStyleName("waiting")
                }

                h.setExpandRatio(l3, 1.0f)
                p.addComponent(l)
                p.addComponent(l2)
                p.addComponent(h)
                vl2.addComponent(p)
            }
        } else {
            Label l = new Label("You must log in to see incoming announcements", Label.CONTENT_XHTML)
            Panel p = new Panel()
            p.addComponent(l)
            vl2.addComponent(p)
        }
    }
}

