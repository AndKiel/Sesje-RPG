package rpgApp.pages

import rpgApp.data.SessionContainer
import rpgApp.data.SessionItem
import rpgApp.main.IndexApplication
import rpgApp.services.SessionService
import rpgApp.windows.InvitationWindow
import rpgApp.windows.KickWindow
import rpgApp.windows.NewSessionWindow
import rpgApp.windows.YesNoDialog

import com.vaadin.data.Property
import com.vaadin.data.Property.ValueChangeEvent
import com.vaadin.terminal.ThemeResource
import com.vaadin.ui.Button
import com.vaadin.ui.Component
import com.vaadin.ui.HorizontalLayout
import com.vaadin.ui.Label
import com.vaadin.ui.TabSheet
import com.vaadin.ui.Table
import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.Button.ClickEvent
import com.vaadin.ui.Button.ClickListener
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent
import com.vaadin.ui.TabSheet.Tab
import com.vaadin.ui.Table.ColumnGenerator
import com.vaadin.ui.themes.Reindeer
import com.vaadin.ui.Window.Notification

class Sessions extends VerticalLayout implements TabSheet.SelectedTabChangeListener, ClickListener, Property.ValueChangeListener{
	private IndexApplication app
	private SessionService sessionService
	private SessionContainer dataSource

	private TabSheet tabSheet
	private VerticalLayout tab1
	private VerticalLayout tab2
	private VerticalLayout tab3
	private Button newSession
	private Button editSession
	private Button deleteSession
	private Button refresh
	private Button leave
	private Button leave2
	private Button invite
	private Button kick

	private Label master
	private Label players
	private Label master2
	private Label players2
	private Label master3
	private Label players3

	private Table mySessions
	private Table joinedSessions
	private Table waitingSessions

	public Sessions(IndexApplication app) {
		this.app = app
		setMargin(true)
		setSpacing(true)

		this.sessionService = app.sessionService
		dataSource = new SessionContainer(sessionService)
		mySessions = createTable()
		joinedSessions = createTable()
		waitingSessions = createTable()
		tab1 = createMySessions()
		tab2 = createJoinedSession()
		tab3 = createWaitingSession()

		tabSheet = new TabSheet()
		tabSheet.setStyleName(Reindeer.TABSHEET_SMALL)
		tabSheet.addStyleName("sessions")
		tabSheet.addListener((TabSheet.SelectedTabChangeListener) this)
		tabSheet.setWidth("100%")
		tabSheet.addTab(tab1 ,"My sessions")
		tabSheet.addTab(tab2 ,"Joined session")
		tabSheet.addTab(tab3 ,"Waiting for acceptation")
                tabSheet.setStyleName(Reindeer.TABSHEET_MINIMAL)
		addComponent(createActionBar())
		addComponent(tabSheet)
	}

	HorizontalLayout createActionBar() {
		HorizontalLayout hl = new HorizontalLayout()
		hl.setWidth("100%")
		hl.setSpacing(true)

		Label l = new Label("")
		refresh = new Button("Refresh")
		refresh.addListener((ClickListener) this)
		refresh.setIcon(new ThemeResource("icons/reload.png"))

		hl.addComponent(l)
		hl.addComponent(refresh)
		hl.setExpandRatio(l, 1.0f)

		return hl
	}

	VerticalLayout createMySessions() {
		VerticalLayout vl = new VerticalLayout()
		vl.setMargin(true)
		vl.setSpacing(true)
		HorizontalLayout hl = new HorizontalLayout()
		hl.setWidth("100%")
		hl.setSpacing(true)

		invite = new Button("Invite")
		invite.addListener((ClickListener)this)
		kick = new Button("Kick")
		kick.addListener((ClickListener)this)
		newSession = new Button("New session")
		newSession.addListener((ClickListener)this)
		editSession = new Button("Edit")
		editSession.addListener((ClickListener)this)
		deleteSession = new Button("Delete")
		deleteSession.addListener((ClickListener)this)

		hl.addComponent(invite)
		hl.addComponent(kick)
		hl.addComponent(newSession)
		hl.addComponent(editSession)
		hl.addComponent(deleteSession)
		hl.setExpandRatio(kick, 1.0f)

		vl.addComponent(hl)
		vl.addComponent(mySessions)
		master = new Label("", Label.CONTENT_XHTML)
		vl.addComponent(master)
		players = new Label("", Label.CONTENT_XHTML)
		vl.addComponent(players)

		return vl
	}

	VerticalLayout createJoinedSession() {
		VerticalLayout vl = new VerticalLayout()
		vl.setMargin(true)
		vl.setSpacing(true)
		HorizontalLayout hl = new HorizontalLayout()
		hl.setWidth("100%")
		hl.setSpacing(true)

		leave = new Button("Leave")
		leave.addListener((ClickListener)this)

		hl.addComponent(leave)

		vl.addComponent(hl)
		vl.addComponent(joinedSessions)
		master2 = new Label("", Label.CONTENT_XHTML)
		vl.addComponent(master2)
		players2 = new Label("", Label.CONTENT_XHTML)
		vl.addComponent(players2)

		return vl
	}

	VerticalLayout createWaitingSession() {
		VerticalLayout vl = new VerticalLayout()
		vl.setMargin(true)
		vl.setSpacing(true)
		HorizontalLayout hl = new HorizontalLayout()
		hl.setWidth("100%")
		hl.setSpacing(true)

		leave2 = new Button("Leave")
		leave2.addListener((ClickListener)this)

		hl.addComponent(leave2)

		vl.addComponent(hl)
		vl.addComponent(waitingSessions)
		master3 = new Label("", Label.CONTENT_XHTML)
		vl.addComponent(master3)
		players3 = new Label("", Label.CONTENT_XHTML)
		vl.addComponent(players3)

		return vl
	}

	Table createTable() {
		Table sessions = new Table()
		sessions.setWidth("100%")
		sessions.setContainerDataSource(dataSource)
		sessions.setVisibleColumns(dataSource.NATURAL_COL_ORDER)
		sessions.setColumnHeaders(dataSource.COL_HEADERS_ENGLISH)
		sessions.setPageLength(15)
		sessions.setSelectable(true)
		sessions.setImmediate(true)
		sessions.setNullSelectionAllowed(false);
		sessions.addListener((Property.ValueChangeListener) this)
		sessions.setStyleName(Reindeer.TABLE_STRONG)


		// Formating Date string in Date column
		sessions.addGeneratedColumn("timeStamp", new ColumnGenerator() {
					public Component generateCell(Table source, Object itemId, Object columnId) {
						SessionItem s = (SessionItem) itemId;
						String d = s.getTimeStamp().toString().substring(0, 16)
						Label l = new Label(d);
						return l;
					}
				});

		// Add players column
		sessions.addGeneratedColumn("players", new ColumnGenerator() {
					public Component generateCell(Table source, Object itemId, Object columnId) {
						SessionItem s = (SessionItem) itemId;
						String max = s.getMaxPlayers().toString()
						String joined = sessionService.participantsCount(s.getId()).toString()
						Label l = new Label(joined+"/"+max);
						return l;
					}
				});

		sessions.setColumnExpandRatio("system",1.0f)
		sessions.setColumnExpandRatio("location",1.0f)
		sessions.setColumnExpandRatio("timeStamp",0.5f);
		sessions.setColumnAlignment("id", Table.ALIGN_CENTER)
		sessions.setColumnAlignment("timeStamp", Table.ALIGN_CENTER)
		sessions.setColumnAlignment("type", Table.ALIGN_CENTER)
		sessions.setColumnAlignment("players", Table.ALIGN_CENTER)

		return sessions
	}

	public Table getMySessions() {
		return mySessions
	}

	public void fillSessions() {
		dataSource.fillWithMySessions()
		master.setValue("")
		players.setValue("")
		invite.setEnabled(false)
		kick.setEnabled(false)
		editSession.setEnabled(false)
		deleteSession.setEnabled(false)
	}

	public void selectedTabChange(SelectedTabChangeEvent event) {
		TabSheet tabsheet = event.getTabSheet();
		Tab tab = tabsheet.getTab(tabsheet.getSelectedTab());
		if (tab != null) {
			if((tab.getCaption()).equals("My sessions")) {
				dataSource.fillWithMySessions()
				master.setValue("")
				players.setValue("")
				invite.setEnabled(false)
				kick.setEnabled(false)
				editSession.setEnabled(false)
				deleteSession.setEnabled(false)
			}
			else if((tab.getCaption()).equals("Joined session")) {
				dataSource.fillWithJoinedSessions()
				master2.setValue("")
				players2.setValue("")
				leave.setEnabled(false)
			}
			else if((tab.getCaption()).equals("Waiting for acceptation")) {
				dataSource.fillWithWaitingSessions()
				master3.setValue("")
				players3.setValue("")
				leave2.setEnabled(false)
			}
		}
	}

	public void buttonClick(ClickEvent event) {
		final Button source = event.getButton()
		switch(source) {
			case refresh:
				if(tabSheet.getTab(tabSheet.getSelectedTab()).getCaption().equals("My sessions")) {
					fillSessions()
				} else if(tabSheet.getTab(tabSheet.getSelectedTab()).getCaption().equals("Joined session")) {
					dataSource.fillWithJoinedSessions()
					master2.setValue("")
					players2.setValue("")
					leave.setEnabled(false)
				} else if(tabSheet.getTab(tabSheet.getSelectedTab()).getCaption().equals("Waiting for acceptation")) {
					dataSource.fillWithWaitingSessions()
					master3.setValue("")
					players3.setValue("")
					leave2.setEnabled(false)
				}
				break
			case leave:
				SessionItem s = (SessionItem)joinedSessions.getValue()
				sessionService.playerLeave(s.getId())
				app.getMainWindow().showNotification("You have left the session", Notification.TYPE_WARNING_MESSAGE)
				dataSource.fillWithJoinedSessions()
				master2.setValue("")
				players2.setValue("")
				leave.setEnabled(false)
				break
			case leave2:
				SessionItem s = (SessionItem)waitingSessions.getValue()
				sessionService.playerLeave(s.getId())
				app.getMainWindow().showNotification("You have left the session", Notification.TYPE_WARNING_MESSAGE)
				dataSource.fillWithWaitingSessions()
				master3.setValue("")
				players3.setValue("")
				leave2.setEnabled(false)
				break
			case newSession:
				app.getMainWindow().addWindow(new NewSessionWindow(app, null, this, false))
				break
			case editSession:
				app.getMainWindow().addWindow(new NewSessionWindow(app, null, this, true))
				break
			case deleteSession:
				app.getMainWindow().addWindow(new YesNoDialog("Session delete","Are you sure you want to delete this session?",
				new YesNoDialog.Callback() {
					public void onDialogResult(boolean answer) {
						if(answer) {
							SessionItem s = (SessionItem)mySessions.getValue()
							sessionService.deleteSession(s.getId())

							// Removing room
							if(s.getType().equals("online")) {
								int index = Collections.binarySearch(app.roomIndexes, s.getId())
								app.chatEntries.remove(index)
								app.roomIndexes.remove(index)
							}

							fillSessions()
						}
					}
				}
				))
				break
			case invite:
				SessionItem s = (SessionItem)mySessions.getValue()
				if(sessionService.participantsCount(s.getId()) == s.getMaxPlayers()) {
					app.getMainWindow().showNotification("No slots for this session", Notification.TYPE_ERROR_MESSAGE)
				} else {
					app.getMainWindow().addWindow(new InvitationWindow(app, s, sessionService))
				}
				break
			case kick:
				SessionItem s = (SessionItem)mySessions.getValue()
				if(sessionService.participantsCount(s.getId()) == 0) {
					app.getMainWindow().showNotification("Session has no players", Notification.TYPE_ERROR_MESSAGE)
				} else {
					app.getMainWindow().addWindow(new KickWindow(app, this, s, sessionService))
				}
				break
		}
	}

	public void valueChange(ValueChangeEvent event) {
		Property property = event.getProperty()
		if (property == mySessions) {
			SessionItem s = (SessionItem)mySessions.getValue()

			master.setValue("<font size=3><b>Master: <font color=#786D3C>"+sessionService.getMaster(s.getId())+"</font></b></font>")
			List<String> plrs = sessionService.getPlayers(s.getId())
			int counter = 0
			String txt = "<font size=3><b>Players: </b>"
			for(String player in plrs) {
				txt += "<b> <font color=#786D3C>"+player+"</b></font>,"
				counter++
			}
			for(int i = counter; i < s.getMaxPlayers()-1; i++) {
				txt += "<font size=2 color=#786D3C>(empty slot)</font>"
			}

			players.setValue(txt)

			invite.setEnabled(true)
			kick.setEnabled(true)
			editSession.setEnabled(true)
			deleteSession.setEnabled(true)
		}
		else if (property == joinedSessions) {
			SessionItem s = (SessionItem)joinedSessions.getValue()

			master2.setValue("<font size=3><b>Master: <font color=#786D3C>"+sessionService.getMaster(s.getId())+"</font></b></font>")
			List<String> plrs = sessionService.getPlayers(s.getId())
			int counter = 0
			String txt = "<font size=3><b>Players: </b>"
			for(String player in plrs) {
				txt += "<b> <font color=#786D3C>"+player+"</b></font>,"
				counter++
			}
			for(int i = counter; i < s.getMaxPlayers()-1; i++) {
				txt += "<font size=2 color=#786D3C>(empty slot)</font>"
			}

			players2.setValue(txt)

			leave.setEnabled(true)
		}
		else if (property == waitingSessions) {
			SessionItem s = (SessionItem)waitingSessions.getValue()

			master3.setValue("<font size=3><b>Master: <font color=#786D3C>"+sessionService.getMaster(s.getId())+"</font></b></font>")
			List<String> plrs = sessionService.getPlayers(s.getId())
			int counter = 0
			String txt = "<font size=3><b>Players: </b>"
			for(String player in plrs) {
				txt += "<b> <font color=#786D3C>"+player+"</b></font>,"
				counter++
			}
			for(int i = counter; i < s.getMaxPlayers()-1; i++) {
				txt += "<font size=2 color=#786D3C>(empty slot)</font>"
			}

			players3.setValue(txt)

			leave2.setEnabled(true)
		}
	}

}
