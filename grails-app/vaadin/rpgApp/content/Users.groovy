package rpgApp.content


import rpgApp.data.UserContainer
import rpgApp.data.UserItem
import rpgApp.main.IndexApplication
import rpgApp.services.UserService
import rpgApp.windows.UserProfile
import rpgApp.windows.YesNoDialog

import com.vaadin.data.Property
import com.vaadin.data.Property.ValueChangeEvent
import com.vaadin.terminal.ThemeResource
import com.vaadin.ui.Alignment
import com.vaadin.ui.Button
import com.vaadin.ui.Component
import com.vaadin.ui.HorizontalLayout
import com.vaadin.ui.Label
import com.vaadin.ui.Table
import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.Button.ClickEvent
import com.vaadin.ui.Button.ClickListener
import com.vaadin.ui.Table.ColumnGenerator
import com.vaadin.ui.themes.Reindeer

class Users extends VerticalLayout implements Property.ValueChangeListener, ClickListener {
	private IndexApplication app
	private UserService userService
	private UserContainer dataSource
	private Table users

	private Button showDetails
	private Button activate

	public Users(IndexApplication app) {
		this.app = app
		setSizeFull()
		setMargin(true)
		setSpacing(true)

		userService = app.userService
		dataSource = new UserContainer(userService)

		addComponent(createHeader())
		addComponent(createUserTable())
	}

	public fillUsers() {
		// Refresh container
		dataSource.fillContainer()
		users.setValue(users.firstItemId())
		if(!app.security.checkRole("Administrator")) {
			activate.setVisible(false)
		} else {
			activate.setVisible(true)
		}
	}

	private HorizontalLayout createHeader() {
		HorizontalLayout hl = new HorizontalLayout()
		hl.setSpacing(true)
		hl.setMargin(true)
		hl.setWidth("100%")

		showDetails = new Button("Show details")
		showDetails.addListener((ClickListener) this)
		showDetails.setIcon(new ThemeResource("icons/details.png"))
		activate = new Button("Disactivate account")
		activate.addListener((ClickListener) this)
		activate.setIcon(new ThemeResource("icons/lock.png"))

		hl.addComponent(showDetails)
		hl.addComponent(activate)
		hl.setComponentAlignment(showDetails, Alignment.BOTTOM_RIGHT)
		hl.setExpandRatio(showDetails, 1.0f)

		return hl
	}

	private Table createUserTable() {
		users = new Table()
		users.setWidth("100%")
		users.setContainerDataSource(dataSource)
		users.setVisibleColumns(dataSource.NATURAL_COL_ORDER)
		users.setColumnHeaders(dataSource.COL_HEADERS_ENGLISH)
		users.setSortDisabled(true)
		users.setPageLength(20)
		users.setSelectable(true)
		users.setImmediate(true)
		users.setNullSelectionAllowed(false);
		users.addListener((Property.ValueChangeListener) this)
		users.setStyleName(Reindeer.TABLE_STRONG)


		// Formating Date string in Date column
		users.addGeneratedColumn("dateCreated", new ColumnGenerator() {
					public Component generateCell(Table source, Object itemId, Object columnId) {
						UserItem u = (UserItem) itemId;
						String d = u.getDateCreated().substring(0, 10)
						Label l = new Label(d);
						return l;
					}
				});

		users.addGeneratedColumn("state", new ColumnGenerator() {
					public Component generateCell(Table source, Object itemId, Object columnId) {
						UserItem u = (UserItem) itemId;
						Label l = new Label("");
						if(u.getState() == true) {
							l.setValue("Active")
						} else {
							l.setValue("Inactive")
						}
						return l;
					}
				});


		users.setColumnAlignment("dateCreated",Table.ALIGN_CENTER);
		users.setColumnAlignment("location",Table.ALIGN_CENTER);
		users.setColumnAlignment("nickname",Table.ALIGN_CENTER);
		users.setColumnAlignment("state",Table.ALIGN_CENTER);
		users.setColumnExpandRatio("nickname", 1)
		users.setColumnExpandRatio("dateCreated", 1)
		users.setColumnExpandRatio("location", 1)

		return users
	}

	public void valueChange(ValueChangeEvent event) {
		Property property = event.getProperty()
		if(property == users) {
			UserItem u = (UserItem)users.getValue()
			if(u.getState()) {
				activate.setCaption("Disactivate account")
			} else {
				activate.setCaption("Activate account")
			}
		}
	}

	public void buttonClick(ClickEvent event) {
		final Button source = event.getButton()
		if(source == showDetails) {
			app.getMainWindow().addWindow(new UserProfile(app, (UserItem)users.getValue()))
		} else if(source == activate) {
			app.getMainWindow().addWindow(new YesNoDialog("Account status change","Are you sure you want change account status?",
					new YesNoDialog.Callback() {
						public void onDialogResult(boolean answer) {
							if(answer) {
								UserItem u = (UserItem)users.getValue()
								u.setState(!u.getState())
								if(u.getState()) {
									activate.setCaption("Disactivate account")
								} else {
									activate.setCaption("Activate account")
								}
								dataSource.setState(u.login, u.getState())
								dataSource.fireItemSetChange()
							}
						}
					}))
		}
	}
}
