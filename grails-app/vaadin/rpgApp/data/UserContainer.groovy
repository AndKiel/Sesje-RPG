package rpgApp.data

import rpgApp.services.UserService

import com.vaadin.data.util.BeanItemContainer

class UserContainer extends BeanItemContainer<MessageItem> implements Serializable {
	private UserService userService
	public static final Object[] NATURAL_COL_ORDER = [
		"nickname",
		"dateCreated",
		"location",
		"state"
	]

	public static final String[] COL_HEADERS_ENGLISH = [
		"Nickname",
		"Joined",
		"Location",
		"Account status"
	];

	public UserContainer(UserService userService) throws InstantiationException, IllegalAccessException {
		super(UserItem.class)
		this.userService = userService
	}

	void fillContainer() {
		removeAllItems()
		addAll(userService.getAllUsers())
	}
	
	void setState(String login, boolean state) {
		userService.setState(login, state)
	}
}