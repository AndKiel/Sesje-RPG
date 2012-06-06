package rpgApp.pages

import com.vaadin.ui.Alignment
import com.vaadin.ui.Button
import com.vaadin.ui.HorizontalLayout
import com.vaadin.ui.Label
import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.Button.ClickEvent

import rpgApp.data.NotificationItem
import rpgApp.main.IndexApplication

class Notifications extends VerticalLayout {
	private IndexApplication app

	public Notifications(IndexApplication app) {
		this.app = app
		setMargin(true)
		setSpacing(true)
	}

	public void fillNotifications() {
		removeAllComponents()
		List<NotificationItem> notifications = app.notificationService.getAllNotifications()
		if(notifications.size() == 0) {
			VerticalLayout vl = new VerticalLayout()
			Label l = new Label("<font size=4>NO NOTIFICATIONS</font>",Label.CONTENT_XHTML)
			vl.addComponent(l)
			vl.setComponentAlignment(l, Alignment.MIDDLE_CENTER)
			addComponent(vl)
		}
		for(NotificationItem notif in notifications) {
			String role = notif.getRole() ? "Master" : "Player"

			HorizontalLayout hl = new HorizontalLayout()
			hl.setSpacing(true)
			hl.setSizeFull()
			// Invintation
			if(notif.getType()) {

				
			} else { // Acceptation
				Label l = new Label(notif.getSender()+" wants to join session nr "+notif.getSession()+" as a "+role)
				hl.addComponent(l)
				Button b1 = new Button("Accept")
				b1.addListener(new Button.ClickListener() {
						public void buttonClick(ClickEvent event) {
							app.sessionService.setParticipantActive(notif.getSender(),notif.getSession())
							app.notificationService.deleteNotification(notif.getId())
							this.fillNotifications()
						}
					}
					)
				hl.addComponent(b1)
				Button b2 = new Button("Refuse")
				b2.addListener(new Button.ClickListener() {
						public void buttonClick(ClickEvent event) {
							app.sessionService.deleteParticipant(notif.getSender(),notif.getSession())
							app.notificationService.deleteNotification(notif.getId())
							this.fillNotifications()
						}
					}
					)
				hl.addComponent(b2)
				hl.setExpandRatio(l, 1.0f)
				hl.setComponentAlignment(l, Alignment.MIDDLE_CENTER)
				addComponent(hl)
			}
			
		}
	}
}
