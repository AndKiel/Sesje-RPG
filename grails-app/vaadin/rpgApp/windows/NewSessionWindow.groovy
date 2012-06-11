package rpgApp.windows


import rpgApp.content.Announcements
import rpgApp.data.SessionItem
import rpgApp.main.IndexApplication
import rpgApp.pages.Sessions
import rpgApp.services.SessionService
import rpgApp.utils.ChatEntry

import com.vaadin.data.Property
import com.vaadin.data.Property.ValueChangeEvent
import com.vaadin.data.validator.StringLengthValidator
import com.vaadin.terminal.Sizeable
import com.vaadin.terminal.ThemeResource
import com.vaadin.ui.Alignment
import com.vaadin.ui.Button
import com.vaadin.ui.ComboBox
import com.vaadin.ui.Form
import com.vaadin.ui.HorizontalLayout
import com.vaadin.ui.OptionGroup
import com.vaadin.ui.PopupDateField
import com.vaadin.ui.TextField
import com.vaadin.ui.Window
import com.vaadin.ui.AbstractSelect.Filtering
import com.vaadin.ui.Button.ClickEvent

class NewSessionWindow extends Window implements Button.ClickListener, Property.ValueChangeListener {
	private IndexApplication app
	private SessionService sessionService
	private Announcements announcements
	private Sessions sessions
	private boolean editMode

	private Button create
	private Button cancel
	private TextField location
	private Form sessionForm = new Form()

	NewSessionWindow(IndexApplication app, Announcements announcements, Sessions sessions, boolean editMode) {
		super("New Session")
		this.app = app
		this.announcements = announcements
		this.sessions = sessions
		this.editMode = editMode
		sessionService = app.sessionService
		this.setCaption("New Session")
		setModal(true)
		setDraggable(false)
		setResizable(false)

		ComboBox system = new ComboBox("System: ")
		List<String> allNames = app.systemService.getAllSystemsNames()
		allNames.each {
			system.addItem(it)
		}
		system.setNullSelectionAllowed(false)
		system.setNewItemsAllowed(false)
		system.setWidth("100%")
		system.setFilteringMode(Filtering.FILTERINGMODE_CONTAINS)
		system.focus()
		system.setRequired(true)
		sessionForm.addField("system", system)

		PopupDateField time = new PopupDateField()
		time.setInputPrompt("mm.dd.rr hh:min")
		time.setCaption("Session Date: ")
		time.setResolution(PopupDateField.RESOLUTION_MIN)
		time.setRequired(true)
		time.setImmediate(true)
		sessionForm.addField("time", time)

		OptionGroup type = new OptionGroup("Type: ")
		type.addItem("offline")
		type.addItem("online")
		type.setImmediate(true)
		type.addListener((Property.ValueChangeListener) this)
		type.setNullSelectionAllowed(false)
		sessionForm.addField("type", type)

		location = new TextField("Location: ")
		location.setWidth("100%")
		location.addValidator(new StringLengthValidator("Location must be less than 40 signs", 0, 40, false))
		location.setRequired(true)
		sessionForm.addField("location", location)
		type.select("offline")
		ComboBox maxPlayers = new ComboBox("Max. players: ")
		if(!editMode) {
			for(int i = 3; i < 10; i++) {
				maxPlayers.addItem(i)
			}
		} else {
			SessionItem s = (SessionItem)sessions.getMySessions().getValue()
			int players = sessionService.participantsCount(s.getId())
			if(sessionService.isMasterSlot(s.getId())) {
				players++
			}
			for(int i = players; i < 10; i++) {
				maxPlayers.addItem(i)
			}
		}
		maxPlayers.setNullSelectionAllowed(false)
		maxPlayers.setNewItemsAllowed(false)
		maxPlayers.setWidth("20%")
		maxPlayers.setRequired(true)
		sessionForm.addField("maxPlayers", maxPlayers)

		OptionGroup role = new OptionGroup("Role: ")
		role.addItem("Master")
		role.addItem("Player")
		role.addItem("None")
		role.setNullSelectionAllowed(false)
		sessionForm.addField("role", role)
		role.select("Master")


		HorizontalLayout footer = new HorizontalLayout()
		footer.setSpacing(true);
		footer.setWidth("100%")
		create = new Button("Create", (Button.ClickListener)this)
		create.setIcon(new ThemeResource("icons/document-txt.png"))
		if(editMode) {
			create.setCaption("Save")
			create.setIcon(new ThemeResource("icons/ok.png"))
		}
		cancel = new Button("Cancel", (Button.ClickListener)this)
		cancel.setIcon(new ThemeResource("icons/cancel.png"))
		footer.addComponent(create);
		footer.setComponentAlignment(create, Alignment.MIDDLE_CENTER)
		footer.addComponent(cancel);
		footer.setComponentAlignment(cancel, Alignment.MIDDLE_CENTER)

		sessionForm.setFooter(footer)
		addComponent(sessionForm);

		setWidth(400, Sizeable.UNITS_PIXELS)
		center();

		if(editMode) {
			this.setCaption("Edit Session")

			SessionItem s = (SessionItem)sessions.getMySessions().getValue()
			system.setValue(s.getSystem())
			time.setValue(s.getTimeStamp())
			type.setValue(s.getType())
			location.setValue(s.getLocation())
			maxPlayers.setValue(s.getMaxPlayers())
			role.setVisible(false)
		}
	}

	void buttonClick(ClickEvent clickEvent) {
		switch(clickEvent.source){
			case create:
				if(sessionForm.isValid()) {
					if(!editMode) {
						Integer sessionId
						sessionId = sessionService.createSession(
								(Date)(sessionForm.getField("time").getValue()),
								(String)(sessionForm.getField("type").getValue()),
								(String)(sessionForm.getField("location").getValue()),
								new Integer(sessionForm.getField("maxPlayers").getValue()),
								(String)(sessionForm.getField("system").getValue()),
								(String)(sessionForm.getField("role").getValue())
								)

						// Adding room
						if(sessionForm.getField("type").getValue().equals("online")) {
							List<ChatEntry> entries = new ArrayList<ChatEntry>()
							app.chatEntries.add(entries)
							app.roomIndexes.add(sessionId)
						}

						if(announcements) {
							announcements.fillSessions()
						}
						if(sessions) {
							sessions.fillSessions()
						}
						this.close()
					} else {
						SessionItem s = (SessionItem)sessions.getMySessions().getValue()
						sessionService.updateSession(
								(Date)(sessionForm.getField("time").getValue()),
								(String)(sessionForm.getField("type").getValue()),
								(String)(sessionForm.getField("location").getValue()),
								new Integer(sessionForm.getField("maxPlayers").getValue()),
								(String)(sessionForm.getField("system").getValue()),
								(Long)s.getId()
								)
						if(announcements) {
							announcements.fillSessions()
						}
						if(sessions) {
							sessions.fillSessions()
						}
						this.close()
					}
				}
				break;
			case cancel:
				this.close()
				break;
		}
	}

	public void valueChange(ValueChangeEvent event) {
		if(event.getProperty().toString().equals("offline")) {
			location.setEnabled(true)
			location.setValue("")
		} else if(event.getProperty().toString().equals("online")) {
			location.setEnabled(false)
			location.setValue("online")
		}
	}
}
