package rpgApp.data

import rpgApp.services.SessionService

import com.vaadin.data.util.BeanItemContainer

class SessionContainer extends BeanItemContainer<SessionItem> implements Serializable {
	private SessionService sessionService
	public static final Object[] NATURAL_COL_ORDER = [
		"id",
		"system",
		"timeStamp",
		"type",
		"location",
	]

	public static final String[] COL_HEADERS_ENGLISH = [
		"ID",
		"System",
		"Date",
		"Type",
		"Location",
	];

	public SessionContainer(SessionService sessionService) throws InstantiationException, IllegalAccessException {
		super(SessionItem.class)
		this.sessionService = sessionService
	}

	void fillContainer() {
		removeAllItems()
		addAll(sessionService.getAllSessions())
	}

	void fillWithMySessions() {
		removeAllItems()
		addAll(sessionService.getMySessions())
	}

	void fillWithJoinedSessions() {
		removeAllItems()
		addAll(sessionService.getJoinedSessions())
	}

	void fillWithWaitingSessions() {
		removeAllItems()
		addAll(sessionService.getWaitingSessions())
	}
}