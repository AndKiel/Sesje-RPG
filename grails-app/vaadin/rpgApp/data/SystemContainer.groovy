package rpgApp.data

import com.vaadin.data.util.BeanItemContainer

import rpgApp.services.SystemService

class SystemContainer extends BeanItemContainer<MessageItem> implements Serializable {
	private SystemService systemService
	public static final Object[] NATURAL_COL_ORDER = [
		"name",
		"genre",
		"year",
	]

	public static final String[] COL_HEADERS_ENGLISH = [
		"System name",
		"Genre",
		"Year",
	];

	public SystemContainer(SystemService systemService) throws InstantiationException, IllegalAccessException {
		super(SystemItem.class)
		this.systemService = systemService
	}

	void fillContainer() {
		removeAllItems()
		addAll(systemService.getAllSystems())
	}
	
	boolean removeSystem(SystemItem system) {
		try {
			systemService.removeSystem(system)
			return true
		} catch (Exception e) {
			println e
			return false
		}
	}
}