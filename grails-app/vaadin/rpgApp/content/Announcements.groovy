package rpgApp.content

import rpgApp.data.SessionContainer
import rpgApp.data.SessionItem
import rpgApp.main.IndexApplication
import rpgApp.services.SessionService
import rpgApp.windows.NewSessionWindow
import rpgApp.windows.SessionJoin

import com.vaadin.data.Item
import com.vaadin.data.Property
import com.vaadin.data.Container.Filter
import com.vaadin.data.Property.ValueChangeEvent
import com.vaadin.data.util.filter.Compare.Equal
import com.vaadin.data.util.filter.Compare.GreaterOrEqual
import com.vaadin.data.util.filter.Compare.LessOrEqual
import com.vaadin.terminal.Resource
import com.vaadin.terminal.ThemeResource
import com.vaadin.ui.Alignment
import com.vaadin.ui.Button
import com.vaadin.ui.ComboBox
import com.vaadin.ui.Component
import com.vaadin.ui.Embedded
import com.vaadin.ui.GridLayout
import com.vaadin.ui.HorizontalLayout
import com.vaadin.ui.Label
import com.vaadin.ui.Panel
import com.vaadin.ui.Table
import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.AbstractSelect.Filtering
import com.vaadin.ui.Button.ClickEvent
import com.vaadin.ui.Button.ClickListener
import com.vaadin.ui.Table.ColumnGenerator
import com.vaadin.ui.themes.Reindeer
import com.vaadin.ui.Window.Notification

class Announcements extends VerticalLayout implements Property.ValueChangeListener, ClickListener {
	private IndexApplication app
	private SessionService sessionService

	private SessionContainer dataSource
	private Button newSession
	private Button refresh
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

	private ComboBox systemNames
	private ComboBox plrs
	private ComboBox plrs2
	private ComboBox tp
	private Button removeAllFilters

	private Filter systemFilter
	private Filter minPlayersFilter
	private Filter maxPlayersFilter
	private Filter typeFilter

	public Announcements(IndexApplication app) {
		this.app = app
		this.sessionService = app.sessionService
		dataSource = new SessionContainer(sessionService)

		setMargin(true)
		addComponent(createFilterBar())
		addComponent(createHeader())
		addComponent(createMessageTable())
		Panel messagePanel = createMessagePanel()
		addComponent(messagePanel)

		setExpandRatio(messagePanel,1.0f)
	}

	private HorizontalLayout createFilterBar() {
		HorizontalLayout hl = new HorizontalLayout()
		hl.setSizeFull()
		hl.setSpacing(true)
		hl.setMargin(false, true , false, false)

		systemNames = new ComboBox("System: ")
		List<String> allNames = app.systemService.getAllSystemsNames()
		systemNames.addItem("(none)")
		allNames.each {
			systemNames.addItem(it)
		}
		systemNames.setNullSelectionAllowed(false)
		systemNames.setNewItemsAllowed(false)
		systemNames.select("(none)")
		systemNames.setImmediate(true)
		systemNames.addListener((Property.ValueChangeListener) this)
		systemNames.setWidth("100%")
		systemNames.setFilteringMode(Filtering.FILTERINGMODE_CONTAINS)

		plrs = new ComboBox("Min. players #: ")
		plrs.addItem("(none)")
		for(int i = 3; i < 10; i ++) {
			plrs.addItem(i)
		}
		plrs.setNullSelectionAllowed(false)
		plrs.setNewItemsAllowed(false)
		plrs.select("(none)")
		plrs.setImmediate(true)
		plrs.addListener((Property.ValueChangeListener) this)
		plrs.setWidth("100%")

		plrs2 = new ComboBox("Max. players #: ")
		plrs2.addItem("(none)")
		for(int i = 3; i < 10; i ++) {
			plrs2.addItem(i)
		}
		plrs2.setNullSelectionAllowed(false)
		plrs2.setNewItemsAllowed(false)
		plrs2.select("(none)")
		plrs2.setImmediate(true)
		plrs2.addListener((Property.ValueChangeListener) this)
		plrs2.setWidth("100%")

		tp = new ComboBox("Type: ")
		tp.addItem("(none)")
		tp.addItem("offline")
		tp.addItem("online")
		tp.setNullSelectionAllowed(false)
		tp.setNewItemsAllowed(false)
		tp.select("(none)")
		tp.setImmediate(true)
		tp.addListener((Property.ValueChangeListener) this)
		tp.setWidth("100%")

		removeAllFilters = new Button("Clear all filters")
		removeAllFilters.addListener((ClickListener) this)

		hl.addComponent(systemNames)
		hl.addComponent(plrs)
		hl.addComponent(plrs2)
		hl.addComponent(tp)
		Label l = new Label("")
		hl.addComponent(l)
		hl.addComponent(removeAllFilters)
		hl.setExpandRatio(l,0.2f)
		hl.setExpandRatio(systemNames,0.3f)
		hl.setExpandRatio(plrs,0.1f)
		hl.setExpandRatio(plrs2,0.1f)
		hl.setExpandRatio(tp,0.1f)
		hl.setExpandRatio(removeAllFilters, 0.2f)
		hl.setComponentAlignment(removeAllFilters, Alignment.MIDDLE_RIGHT)

		return hl
	}

	private HorizontalLayout createHeader() {
		HorizontalLayout hl = new HorizontalLayout()
		hl.setSizeFull()
		hl.setMargin(true)
		hl.setSpacing(true)

		join = new Button("Join")
		join.addListener((ClickListener) this)
		join.setVisible(false)
		Label l = new Label("Joined")
		Label l2 = new Label("Waiting for acceptation")
		newSession = new Button("New Session")
		newSession.addListener((ClickListener) this)
		newSession.setIcon(new ThemeResource("icons/document-add.png"))
		refresh = new Button("Refresh")
		refresh.addListener((ClickListener) this)
		refresh.setIcon(new ThemeResource("icons/reload.png"))

		Resource res = new ThemeResource("icons/member-color.png");
		Embedded e = new Embedded(null, res);
		e.setWidth("16px");
		e.setHeight("16px");
		Resource res2 = new ThemeResource("icons/waiting-color.png");
		Embedded e2 = new Embedded(null, res2);
		e2.setWidth("16px");
		e2.setHeight("16px");

		hl.addComponent(join)
		hl.addComponent(e)
		hl.addComponent(l)
		hl.addComponent(e2)
		hl.addComponent(l2)
		hl.addComponent(newSession)
		hl.addComponent(refresh)
		hl.setExpandRatio(join, 0.6f)
		hl.setComponentAlignment(e, Alignment.MIDDLE_CENTER)
		hl.setComponentAlignment(e2, Alignment.MIDDLE_CENTER)
		hl.setComponentAlignment(l, Alignment.MIDDLE_CENTER)
		hl.setComponentAlignment(l2, Alignment.MIDDLE_CENTER)
		hl.setExpandRatio(l,0.1f)
		hl.setExpandRatio(l2,0.3f)

		return hl
	}

	private Table createMessageTable() {
		sessions = new Table()
		sessions.setSizeFull()
		sessions.setContainerDataSource(dataSource)
		sessions.setVisibleColumns(dataSource.NATURAL_COL_ORDER)
		sessions.setColumnHeaders(dataSource.COL_HEADERS_ENGLISH)
		sessions.setPageLength(11)
		sessions.setSelectable(true)
		sessions.setImmediate(true)
		sessions.setNullSelectionAllowed(false);
		sessions.addListener((Property.ValueChangeListener) this)
		sessions.setStyleName(Reindeer.TABLE_STRONG)

		// Setting row colors generator
		sessions.setCellStyleGenerator(new Table.CellStyleGenerator() {
					@Override
					public String getStyle(Object itemId, Object propertyId) {
						if (propertyId == null) {
							// Styling for row
							Item item = sessions.getItem(itemId);
							Integer id = (Integer)item.getItemProperty("id").getValue()
							if(sessionService.checkMembership(id) == 1) {
								return "member"
							} else if(sessionService.checkMembership(id) == 0) {
								return "notmember"
							} else if(sessionService.checkMembership(id) == 2) {
								return "waiting"
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

	public void fillSystemNamesFilter() {
		systemNames.removeAllItems()
		List<String> allNames = app.systemService.getAllSystemsNames()
		systemNames.addItem("(none)")
		allNames.each {
			systemNames.addItem(it)
		}
		systemNames.select("(none)")
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

			join.setVisible(true)
			if(sessionService.checkMembership(s.getId())) {
				join.setCaption("Leave")
			} else {
				join.setCaption("Join")
			}
		} else if(property == systemNames) {
			if(systemFilter) {
				dataSource.removeContainerFilter(systemFilter)
			}
			if(!systemNames.getValue().equals("(none)")) {
				systemFilter = new Equal("system", systemNames.getValue().toString())
				dataSource.addContainerFilter(systemFilter)
			}
		} else if(property == plrs) {
			if(minPlayersFilter) {
				dataSource.removeContainerFilter(minPlayersFilter)
			}
			if(!plrs.getValue().equals("(none)")) {
				minPlayersFilter = new GreaterOrEqual("maxPlayers", plrs.getValue())
				dataSource.addContainerFilter(minPlayersFilter)
			}
		} else if(property == plrs2) {
			if(maxPlayersFilter) {
				dataSource.removeContainerFilter(maxPlayersFilter)
			}
			if(!plrs2.getValue().equals("(none)")) {
				maxPlayersFilter = new LessOrEqual("maxPlayers", plrs2.getValue())
				dataSource.addContainerFilter(maxPlayersFilter)
			}
		} else if(property == tp) {
			if(typeFilter) {
				dataSource.removeContainerFilter(typeFilter)
			}
			if(!tp.getValue().equals("(none)")) {
				typeFilter = new Equal("type", tp.getValue().toString())
				dataSource.addContainerFilter(typeFilter)
			}
		}
	}

	public void buttonClick(ClickEvent event) {
		final Button source = event.getButton()
		switch(source) {
			case join:
				if(join.getCaption().equals("Join")) {
					SessionItem s = (SessionItem)sessions.getValue()
					if(sessionService.participantsCount(s.getId()) == s.getMaxPlayers()) {
						app.getMainWindow().showNotification("No slots for this session", Notification.TYPE_ERROR_MESSAGE)
					} else {
						app.getMainWindow().addWindow(new SessionJoin(app, this, s, sessionService))
					}
				} else if(join.getCaption().equals("Leave")) {
					SessionItem s = (SessionItem)sessions.getValue()
					sessionService.playerLeave(s.getId())
					app.getMainWindow().showNotification("You have left the session", Notification.TYPE_WARNING_MESSAGE)
					fillSessions()
				}
				break
			case newSession:
				app.getMainWindow().addWindow(new NewSessionWindow(app, this, null, false))
				break
			case refresh:
				fillSessions()
				break
			case removeAllFilters:
				dataSource.removeAllContainerFilters()
				systemNames.select("(none)")
				plrs.select("(none)")
				plrs2.select("(none)")
				tp.select("(none)")
				break
		}
	}
}
