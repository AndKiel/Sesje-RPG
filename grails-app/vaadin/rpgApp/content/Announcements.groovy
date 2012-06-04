package rpgApp.content

import rpgApp.data.SessionContainer
import rpgApp.data.SessionItem
import rpgApp.main.IndexApplication
import rpgApp.services.SessionService
import rpgApp.windows.NewSessionWindow

import com.vaadin.data.Item
import com.vaadin.data.Property
import com.vaadin.data.Property.ValueChangeEvent
import com.vaadin.terminal.ThemeResource
import com.vaadin.ui.Button
import com.vaadin.ui.Component
import com.vaadin.ui.GridLayout
import com.vaadin.ui.HorizontalLayout
import com.vaadin.ui.Label
import com.vaadin.ui.Panel
import com.vaadin.ui.Table
import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.Button.ClickEvent
import com.vaadin.ui.Button.ClickListener
import com.vaadin.ui.Table.ColumnGenerator
import com.vaadin.ui.themes.Reindeer

class Announcements extends VerticalLayout implements Property.ValueChangeListener, ClickListener {
	private IndexApplication app
	private SessionService sessionService

	private SessionContainer dataSource
	private Button newSession
	private Table sessions
	private Button join

	private Label system
	private Label type
	private Label created
	private Label where
	private Label owner
	private Label when
	private Label master
	private Label players

	public Announcements(IndexApplication app) {
		this.app = app
		this.sessionService = app.sessionService
		dataSource = new SessionContainer(sessionService)

		setMargin(true)
		addComponent(createHeader())
		addComponent(createMessageTable())
		Panel messagePanel = createMessagePanel()
		addComponent(messagePanel)

		setExpandRatio(messagePanel,1.0f)
	}

	private HorizontalLayout createHeader() {
		HorizontalLayout hl = new HorizontalLayout()
		hl.setSizeFull()
		hl.setMargin(true)
		hl.setSpacing(true)

		join = new Button("Join")
		join.addListener((ClickListener) this)
		join.setVisible(false)
		newSession = new Button("New Session")
		newSession.addListener((ClickListener) this)
		newSession.setIcon(new ThemeResource("icons/document-add.png"))

		hl.addComponent(join)
		hl.addComponent(newSession)
		hl.setExpandRatio(join, 1.0f)

		return hl
	}

	private Table createMessageTable() {
		sessions = new Table()
		sessions.setSizeFull()
		sessions.setContainerDataSource(dataSource)
		sessions.setVisibleColumns(dataSource.NATURAL_COL_ORDER)
		sessions.setColumnHeaders(dataSource.COL_HEADERS_ENGLISH)
		sessions.setPageLength(12)
		sessions.setSelectable(true)
		sessions.setImmediate(true)
		sessions.setNullSelectionAllowed(false);
		sessions.addListener((Property.ValueChangeListener) this)
		sessions.setStyleName(Reindeer.TABLE_STRONG)

		// Setting bold rows for unreaded messages
		sessions.setCellStyleGenerator(new Table.CellStyleGenerator() {
					@Override
					public String getStyle(Object itemId, Object propertyId) {
						if (propertyId == null) {
							// Styling for row
							Item item = sessions.getItem(itemId);
							Integer id = (Integer)item.getItemProperty("id").getValue()
							if(sessionService.checkMembership(id)) {
								return "member"
							} else {
								return "notmember"
							}
						} else {
							// styling for column propertyId
							return null;
						}
					}
				})

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

	private Panel createMessagePanel() {
		Panel p = new Panel()
		p.setStyleName(Reindeer.PANEL_LIGHT);
		p.setSizeFull()

		GridLayout gl = new GridLayout(3,6)
		p.setContent(gl)
		gl.setWidth("100%")
		gl.setMargin(true)
		gl.setSpacing(true)

		system = new Label("", Label.CONTENT_XHTML)
		type = new Label("", Label.CONTENT_XHTML)
		created = new Label("", Label.CONTENT_XHTML)
		where = new Label("", Label.CONTENT_XHTML)
		owner = new Label("", Label.CONTENT_XHTML)
		when = new Label("", Label.CONTENT_XHTML)
		master = new Label("", Label.CONTENT_XHTML)
		players = new Label("", Label.CONTENT_XHTML)

		gl.addComponent(system)
		gl.addComponent(where)
		gl.addComponent(created)
		gl.addComponent(type)
		gl.addComponent(when)
		gl.addComponent(owner)
		gl.addComponent(master, 0,4,2,4)
		gl.addComponent(players, 0,5,2,5)
		
		return p
	}

	public fillSessions() {
		// Refresh container
		dataSource.fillContainer()

		join.setVisible(false)
		system.setValue("")
		type.setValue("")
		created.setValue("")
		where.setValue("")
		owner.setValue("")
		when.setValue("")
		master.setValue("")
		players.setValue("")
	}
	
	public Table getSessions() {
		return sessions
	}

	public void valueChange(ValueChangeEvent event) {
		Property property = event.getProperty()
		if (property == sessions) {
			SessionItem s = (SessionItem)sessions.getValue()
			
			system.setValue("<b>System: </b>"+s.getSystem())
			type.setValue("<b>Type: </b>"+s.getType())
			created.setValue("<b>Created: </b>"+s.getDateCreated().toString().substring(0, 10))
			where.setValue("<b>Where: </b>"+s.getLocation())
			owner.setValue("<b>Owner: </b>"+s.getOwner())
			when.setValue("<b>When: </b>"+s.getTimeStamp().toString().substring(0, 16))
			master.setValue("<font size=3><b>Master: </b>"+sessionService.getMaster(s.getId())+"</font>")
			List<String> plrs = sessionService.getPlayers(s.getId())
			int counter = 0
			String txt = "<font size=3><b>Players: </b>"
			for(String player in plrs) {
				txt += "<b> "+player+"</b>,"
				counter++
			}
			for(int i = counter; i < s.getMaxPlayers()-1; i++) {
				txt += "<font size=2>(empty slot)</font>"
			}
			
			players.setValue(txt)
			
			join.setVisible(true)
			if(sessionService.checkMembership(s.getId())) {
				join.setCaption("Leave")
			} else {
				join.setCaption("Join")
			}
		}
	}

	public void buttonClick(ClickEvent event) {
		final Button source = event.getButton()
		switch(source) {
			case newSession:
				app.getMainWindow().addWindow(new NewSessionWindow(app, this))
				break
		}
	}
}
