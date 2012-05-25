package rpgApp.data

import rpgApp.services.MessageService

import com.vaadin.data.util.BeanItemContainer

class MessageContainer extends BeanItemContainer<MessageItem> implements Serializable {
	private MessageService messageService
	public static final Object[] NATURAL_COL_ORDER = [
		"sender",
		"topic",
		"dateCreated",
	]

	public static final String[] COL_HEADERS_ENGLISH = [
		"From",
		"Topic",
		"Date",
	];

	public MessageContainer(MessageService messageService) throws InstantiationException, IllegalAccessException {
		super(MessageItem.class)
		this.messageService = messageService
	}

	void fillContainer() {
		removeAllItems()
		addAll(messageService.getAllMessages())
	}

	boolean removeMessage(MessageItem message) {
		try {
			messageService.removeMessage(message)
			return true
		} catch (Exception e) {
			println e
			return false
		}
	}

	boolean removeAllMessages() {
		try {
			messageService.removeAllMessages()
			return true
		} catch (Exception e) {
			println e
			return false
		}
	}

	boolean setWasRead(MessageItem message) {
		try {
			messageService.setWasRead(message)
			return true
		} catch (Exception e) {
			println e
			return false
		}
	}
}