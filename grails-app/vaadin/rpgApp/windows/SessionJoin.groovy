package rpgApp.windows

import rpgApp.content.Announcements
import rpgApp.content.StartPage
import rpgApp.data.SessionItem
import rpgApp.main.IndexApplication
import rpgApp.services.SessionService

import com.vaadin.terminal.ThemeResource
import com.vaadin.ui.Button.ClickEvent
import com.vaadin.ui.Button
import com.vaadin.ui.Window
import com.vaadin.ui.Window.Notification
import com.vaadin.ui.themes.Reindeer

class SessionJoin extends Window implements Button.ClickListener {
	private IndexApplication app
	private Announcements ann
	private StartPage startPage
	private SessionItem sessionItem
	private SessionService sessionService
	private Button master
	private Button player
	private Button cancel
	
	SessionJoin(IndexApplication app, Announcements ann, StartPage startPage, SessionItem sessionItem, SessionService sessionService) {
		super("Join session")
		this.app = app
                this.setStyleName(Reindeer.WINDOW_BLACK)
		this.ann = ann
		this.startPage = startPage
		this.sessionItem = sessionItem
		this.sessionService = sessionService
		setCaption("Join session")
		setModal(true)
		setResizable(false)
		setDraggable(false)
		setWidth("185px")
		getContent().setMargin(true)
		getContent().setSpacing(true)

		master = new Button("Join as a Master")
		master.addListener(this)
		master.setIcon(new ThemeResource("icons/user.png"))
		player = new Button("Join as a Player")
		player.addListener(this)
		player.setIcon(new ThemeResource("icons/users.png"))
		cancel = new Button("Cancel")
		cancel.addListener(this)
		cancel.setIcon(new ThemeResource("icons/cancel.png"))

		if(sessionService.isMasterSlot(sessionItem.getId())) {
			addComponent(master)
		}
		if(sessionService.isPlayerSlot(sessionItem.getId(), sessionItem.getMaxPlayers())) {
			addComponent(player)
		}
		addComponent(cancel)
	}

	public void buttonClick(ClickEvent event) {
		final Button source = event.getButton()
		switch(source) {
			case master:
				if(sessionService.isMineSession(sessionItem.getOwner())) {
					sessionService.createParticipant(sessionItem.getId(), true, true)
					app.getMainWindow().showNotification("You have joined the session", Notification.TYPE_WARNING_MESSAGE)
				} else {
					sessionService.createNotification(sessionItem.getOwner(), sessionItem.getId(), false, true)
					sessionService.createParticipant(sessionItem.getId(), true, false)
					app.getMainWindow().showNotification("You must wait for owner acceptation", Notification.TYPE_WARNING_MESSAGE)
				}
				if(ann) {
					ann.fillSessions()
				}
				if(startPage) {
					startPage.refreshContent()
				}
				this.close()
				break
			case player:
				if(sessionService.isMineSession(sessionItem.getOwner())) {
					sessionService.createParticipant(sessionItem.getId(), false, true)
					app.getMainWindow().showNotification("You have joined the session", Notification.TYPE_WARNING_MESSAGE)
				} else {
					sessionService.createNotification(sessionItem.getOwner(), sessionItem.getId(), false, false)
					sessionService.createParticipant(sessionItem.getId(), false, false)
					app.getMainWindow().showNotification("You must wait for owner acceptation", Notification.TYPE_WARNING_MESSAGE)
				}
				if(ann) {
					ann.fillSessions()
				}
				if(startPage) {
					startPage.refreshContent()
				}
				this.close()
				break
			case cancel:
				this.close()
				break
		}
	}
}
