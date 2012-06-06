package rpgApp.services

import rpgApp.data.NotificationItem
import rpgApp.persistance.Notification
import rpgApp.persistance.User

class NotificationService {

    def securityService
	static transactional = true

	List<NotificationItem> getAllNotifications() {
		User user  = securityService.getContextUser()
		return Notification.findAllByReceiver(user).collect() {
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
}
