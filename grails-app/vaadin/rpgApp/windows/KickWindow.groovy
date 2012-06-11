package rpgApp.windows

import com.vaadin.terminal.ThemeResource
import com.vaadin.ui.Button
import com.vaadin.ui.ComboBox
import com.vaadin.ui.Window
import com.vaadin.ui.AbstractSelect.Filtering
import com.vaadin.ui.Button.ClickEvent
import com.vaadin.ui.Window.Notification

import rpgApp.data.SessionItem
import rpgApp.main.IndexApplication
import rpgApp.pages.Sessions
import rpgApp.services.SessionService

class KickWindow extends Window implements Button.ClickListener {
	private IndexApplication app
	private Sessions sessions
	private SessionItem sessionItem
	private SessionService sessionService
	private Button kick
	private Button cancel
	private ComboBox players

	KickWindow(IndexApplication app, Sessions sessions, SessionItem sessionItem, SessionService sessionService) {
		super("Invitation")
		this.app = app
		this.sessions = sessions
		this.sessionItem = sessionItem
		this.sessionService = sessionService
		setCaption("Invitation")
		setModal(true)
		setResizable(false)
		setDraggable(false)
		setWidth("225px")
		getContent().setMargin(true)
		getContent().setSpacing(true)

		players = new ComboBox("Player: ")
		// Add players
		List<String> alreadyIn = sessionService.getPlayers(sessionItem.getId())
		alreadyIn.each {
			players.addItem(it)
		}
		// Add master
		if(!sessionService.getMaster(sessionItem.getId()).equals("<font size=2>(empty slot)</font>")) {
			players.addItem(sessionService.getMaster(sessionItem.getId()))
		}
		// Removing contextUser
		players.removeItem(app.security.getContextNickname())
		players.setNullSelectionAllowed(false)
		players.setNewItemsAllowed(false)
		players.setWidth("100%")
		players.focus()
		players.setFilteringMode(Filtering.FILTERINGMODE_CONTAINS)
		addComponent(players)

		kick = new Button("Kick player")
		kick.addListener(this)
		cancel = new Button("Cancel")
		cancel.addListener(this)
		cancel.setIcon(new ThemeResource("icons/cancel.png"))

		addComponent(kick)
		addComponent(cancel)
	}

	public void buttonClick(ClickEvent event) {
		final Button source = event.getButton()
		switch(source) {
			case kick:
				if(players.getValue() != null) {
					sessionService.deleteParticipant(players.getValue(), sessionItem.getId())
					app.getMainWindow().showNotification(players.getValue()+" has been kicked", Notification.TYPE_WARNING_MESSAGE)
					sessions.fillSessions()
					this.close()
				}
				break
			case cancel:
				this.close()
				break
		}
	}
}
