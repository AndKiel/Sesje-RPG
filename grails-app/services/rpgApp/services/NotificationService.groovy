package rpgApp.services

import rpgApp.data.NotificationItem
import rpgApp.persistance.Notification
import rpgApp.persistance.User

class NotificationService {

    def securityService
	static transactional = true

	List<NotificationItem> getInvitationNotifications() {
		User user  = securityService.getContextUser()
		return Notification.findAllByReceiverAndType(user, true).collect() {
			new NotificationItem(
				id: it.id,
				sender: it.getSender().getNickname(),
				receiver: it.getReceiver().getNickname(),
				session: it.getSession().getId(),
				role: it.getRole(),
				type: it.getType()
				)
		}
	}
	
	List<NotificationItem> getAcceptationNotifications() {
		User user  = securityService.getContextUser()
		return Notification.findAllByReceiverAndType(user, false).collect() {
			new NotificationItem(
				id: it.id,
				sender: it.getSender().getNickname(),
				receiver: it.getReceiver().getNickname(),
				session: it.getSession().getId(),
				role: it.getRole(),
				type: it.getType()
				)
		}
	}
	
	void deleteNotification(Integer id) {
		Notification n = Notification.get(id)
		if(n) {
			n.delete()
		}
	}
	
	int getNotificationsCount() {
		User contextUser = securityService.getContextUser()
		if(contextUser == null) {
			return 0
		}
		return Notification.countByReceiver(contextUser)
	}
}
