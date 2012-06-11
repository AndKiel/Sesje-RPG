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
import rpgApp.services.SessionService

class InvitationWindow extends Window implements Button.ClickListener {
	private IndexApplication app
	private SessionItem sessionItem
	private SessionService sessionService
	private Button master
	private Button player
	private Button cancel
	private ComboBox players

	InvitationWindow(IndexApplication app, SessionItem sessionItem, SessionService sessionService) {
		super("Invitation")
		this.app = app
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
		List<String> allNicknames = app.userService.getAllUsersNicknames()
		allNicknames.each {
			players.addItem(it)
		}
		// Removing players who are already in session
		List<String> alreadyIn = sessionService.getParticipants(sessionItem.getId())
		alreadyIn.each {
			players.removeItem(it)
		}

		// Removing contextUser
		players.removeItem(app.security.getContextNickname())
		players.setNullSelectionAllowed(false)
		players.setNewItemsAllowed(false)
		players.setWidth("100%")
		players.setFilteringMode(Filtering.FILTERINGMODE_CONTAINS)
		addComponent(players)

		master = new Button("Invite as a Master")
		master.addListener(this)
		master.setIcon(new ThemeResource("icons/user.png"))
		player = new Button("Invite as a Player")
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
				if(players.getValue() != null) {
					app.sessionService.createNotification(players.getValue(), sessionItem.getId(), true , true)
					app.sessionService.createOtherParticipant(players.getValue(),sessionItem.getId(), true, false)
					app.getMainWindow().showNotification("Your invitation was send", Notification.TYPE_WARNING_MESSAGE)
					this.close()
				}
				break
			case player:
				if(players.getValue() != null) {
					app.sessionService.createNotification(players.getValue(), sessionItem.getId(), true , false)
					sessionService.createOtherParticipant(players.getValue(), sessionItem.getId(), false, false)
					app.getMainWindow().showNotification("Your invitation was send", Notification.TYPE_WARNING_MESSAGE)
					this.close()
				}
				break
			case cancel:
				this.close()
				break
		}
	}
}
