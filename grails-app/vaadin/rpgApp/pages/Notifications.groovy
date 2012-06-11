package rpgApp.pages

import com.vaadin.ui.Alignment
import com.vaadin.ui.Button
import com.vaadin.ui.HorizontalLayout
import com.vaadin.ui.Label
import com.vaadin.ui.Panel
import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.Button.ClickEvent

import rpgApp.data.NotificationItem
import rpgApp.main.IndexApplication

class Notifications extends VerticalLayout {
	private IndexApplication app

	private Panel p1
	private Panel p2
	private VerticalLayout vl1
	private VerticalLayout vl2
	private Button b
	private Button b2

	public Notifications(IndexApplication app) {
		this.app = app
		setMargin(true)
		setSpacing(true)

		p1 = new Panel("Invitations")
		p2  = new Panel("Acceptations")
		vl1 = p1.getContent()
		vl1.setSpacing(true)
		vl2 = p2.getContent()
		vl2.setSpacing(true)
		addComponent(p1)
		addComponent(p2)
	}

	public void fillNotifications() {
		vl1.removeAllComponents()
		vl2.removeAllComponents()
		List<NotificationItem> invNotifications = app.notificationService.getInvitationNotifications()
		if(invNotifications.size() == 0) {
			Label l = new Label("<font size=3>You've got 0 invitations</font>",Label.CONTENT_XHTML)
			vl1.addComponent(l)
			vl1.setComponentAlignment(l, Alignment.MIDDLE_CENTER)
		}
		for(NotificationItem notif in invNotifications) {
			String role = notif.getRole() ? "Master" : "Player"

			HorizontalLayout hl = new HorizontalLayout()
			hl.setSpacing(true)
			hl.setSizeFull()

			Label l = new Label("<b>"+notif.getSender()+"</b> invites you to join session <b>#"+notif.getSession()+"</b> as a "+role, ,Label.CONTENT_XHTML)
			hl.addComponent(l)
			b = new Button("Join")
			String receiver = notif.getReceiver()
			Integer sesId = notif.getSession()
			Integer notifId = notif.getId()
			b.addListener(new Button.ClickListener() {
						public void buttonClick(ClickEvent event) {
							app.sessionService.setParticipantActive(receiver,sesId)
							app.notificationService.deleteNotification(notifId)
							app.notifications.setCaption("You've got: "+app.notificationService.getNotificationsCount()+" notifications")
							this.fillNotifications()
						}
					}
					)
			hl.addComponent(b)
			b2 = new Button("Refuse")
			receiver = notif.getReceiver()
			sesId = notif.getSession()
			notifId = notif.getId()
			b2.addListener(new Button.ClickListener() {
						public void buttonClick(ClickEvent event) {
							app.sessionService.deleteParticipant(receiver,sesId)
							app.notificationService.deleteNotification(notifId)
							app.notifications.setCaption("You've got: "+app.notificationService.getNotificationsCount()+" notifications")
							this.fillNotifications()
						}
					}
					)
			hl.addComponent(b2)
			hl.setExpandRatio(l, 1.0f)
			hl.setComponentAlignment(l, Alignment.MIDDLE_CENTER)
			vl1.addComponent(hl)

		}
		List<NotificationItem> accNotifications = app.notificationService.getAcceptationNotifications()
		if(accNotifications.size() == 0) {
			Label l = new Label("<font size=3>You've got 0 acceptation requests</font>",Label.CONTENT_XHTML)
			vl2.addComponent(l)
			vl2.setComponentAlignment(l, Alignment.MIDDLE_CENTER)
		}
		for(NotificationItem notif in accNotifications) {
			String role = notif.getRole() ? "Master" : "Player"

			HorizontalLayout hl = new HorizontalLayout()
			hl.setSpacing(true)
			hl.setSizeFull()

			Label l = new Label("<b>"+notif.getSender()+"</b> wants to join session <b>#"+notif.getSession()+"</b> as a "+role,Label.CONTENT_XHTML)
			hl.addComponent(l)
			b = new Button("Accept")
			String sender = notif.getSender()
			Integer sesId = notif.getSession()
			Integer notifId = notif.getId()
			b.addListener(new Button.ClickListener() {
						public void buttonClick(ClickEvent event) {
							app.sessionService.setParticipantActive(sender,sesId)
							app.notificationService.deleteNotification(notifId)
							app.notifications.setCaption("You've got: "+app.notificationService.getNotificationsCount()+" notifications")
							this.fillNotifications()
						}
					}
					)
			hl.addComponent(b)
			b2 = new Button("Refuse")
			sender = notif.getSender()
			sesId = notif.getSession()
			notifId = notif.getId()
			b2.addListener(new Button.ClickListener() {
						public void buttonClick(ClickEvent event) {
							app.sessionService.deleteParticipant(sender, sesId)
							app.notificationService.deleteNotification(notifId)
							app.notifications.setCaption("You've got: "+app.notificationService.getNotificationsCount()+" notifications")
							this.fillNotifications()
						}
					}
					)
			hl.addComponent(b2)
			hl.setExpandRatio(l, 1.0f)
			hl.setComponentAlignment(l, Alignment.MIDDLE_CENTER)
			vl2.addComponent(hl)
		}


	}
}
