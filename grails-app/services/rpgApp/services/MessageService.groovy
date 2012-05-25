package rpgApp.services

import rpgApp.data.MessageItem
import rpgApp.persistance.Message
import rpgApp.persistance.User


class MessageService {
	def securityService
	static transactional = true

	List<MessageItem> getAllMessages() {
		User user  = securityService.getContextUser()
		return Message.findAllByAddressee(user, [sort: 'dateCreated', order:'desc']).collect {
			new MessageItem(
					id: it.id,
					dateCreated: it.dateCreated,
					topic: it.topic,
					content: it.content,
					wasRead: it.wasRead,
					sender: it.sender.nickname,
					addressee: it.addressee.nickname,
					)
		}
	}
	
	void addMessage(String topic, String content, String receiver) { 
		User sender  = securityService.getContextUser()
		User addressee = User.findByNickname(receiver)
		new Message(topic: topic, content: content, sender: sender, addressee: addressee).save()
	}
	
	void removeMessage(MessageItem message) {
		Message m = Message.get(message.getId())
		if(m) {
			m.delete()
		}
	}
	
	void removeAllMessages() {
		Message.executeUpdate('delete from Message')
	}
	
	void setWasRead(MessageItem message) {
		Message m = Message.get(message.getId())
		if(m) {
			m.wasRead = true
			m.save()
		}
	}
}
