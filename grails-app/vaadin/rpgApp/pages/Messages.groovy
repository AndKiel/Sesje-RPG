package rpgApp.pages


import rpgApp.data.MessageContainer
import rpgApp.data.MessageItem
import rpgApp.main.IndexApplication
import rpgApp.services.MessageService
import rpgApp.services.UserService
import rpgApp.windows.NewMessageWindow
import rpgApp.windows.YesNoDialog

import com.vaadin.data.Item
import com.vaadin.data.Property
import com.vaadin.data.Property.ValueChangeEvent
import com.vaadin.terminal.ThemeResource
import com.vaadin.ui.Alignment
import com.vaadin.ui.Button
import com.vaadin.ui.CheckBox
import com.vaadin.ui.Component
import com.vaadin.ui.HorizontalLayout
import com.vaadin.ui.Label
import com.vaadin.ui.Panel
import com.vaadin.ui.Table
import com.vaadin.ui.Tree
import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.Button.ClickEvent
import com.vaadin.ui.Button.ClickListener
import com.vaadin.ui.Table.ColumnGenerator
import com.vaadin.ui.themes.Reindeer

class Messages extends VerticalLayout implements Property.ValueChangeListener, ClickListener {
	private IndexApplication app

	private MessageService messageService
	private UserService userService
	private MessageContainer dataSource
	private CheckBox onlyUnread
	private Button newMessage
	private Table messages
	private Button reply
	private Button deleteMessage
	private Button refresh
	private Button deleteAll

	private Label topic
	private Label from
	private Label grid
	private Label messageContent
	private Label time

	public Messages(IndexApplication app) {
		this.app = app
		this.setMargin(true)
		messageService = app.messageService
		userService = app.userService
		dataSource = new MessageContainer(messageService)

		addComponent(createHeader())
		addComponent(createMessageTable())
		addComponent(createDeleteBar())
		Panel messagePanel = createMessagePanel()
		addComponent(messagePanel)
		
		setExpandRatio(messagePanel,1.0f)
	}

	private HorizontalLayout createHeader() {
		HorizontalLayout hl = new HorizontalLayout()
		hl.setWidth("100%")
		hl.setMargin(false, false, true, false)
		hl.setSpacing(true)

		onlyUnread = new CheckBox("Show only unread")
		onlyUnread.setValue(false)
		onlyUnread.setImmediate(true)
		onlyUnread.addListener((ClickListener) this)
		newMessage = new Button("New Message")
		newMessage.addListener((ClickListener) this)
		newMessage.setIcon(new ThemeResource("icons/document-add.png"))
		refresh = new Button("Refresh")
		refresh.addListener((ClickListener) this)
		refresh.setIcon(new ThemeResource("icons/reload.png"))
		deleteAll = new Button("Delete All")
		deleteAll.addListener((ClickListener) this)
		deleteAll.setIcon(new ThemeResource("icons/folder-delete.png"))

		hl.addComponent(onlyUnread)
		hl.addComponent(newMessage)
		hl.addComponent(refresh)
		hl.addComponent(deleteAll)
		hl.setExpandRatio(onlyUnread,1.0f)

		return hl
	}

	private Table createMessageTable() {
		messages = new Table()
		messages.setWidth("100%")
		messages.setContainerDataSource(dataSource)
		messages.setVisibleColumns(dataSource.NATURAL_COL_ORDER)
		messages.setColumnHeaders(dataSource.COL_HEADERS_ENGLISH)
		messages.setPageLength(10)
		messages.setSelectable(true)
		messages.setImmediate(true)
		messages.setNullSelectionAllowed(false);
		messages.addListener((Property.ValueChangeListener) this)
		messages.setStyleName(Reindeer.TABLE_STRONG)

		// Setting bold rows for unreaded messages
		messages.setCellStyleGenerator(new Table.CellStyleGenerator() {
					@Override
					public String getStyle(Object itemId, Object propertyId) {
						if (propertyId == null) {
							// Styling for row
							Item item = messages.getItem(itemId);
							Boolean wasRead = (Boolean) item.getItemProperty("wasRead").getValue();
							if (!wasRead) {
								return "unread";
							} else {
								return "read";
							}
						} else {
							// styling for column propertyId
							return null;
						}
					}
				})

		// Formating Date string in Date column
		messages.addGeneratedColumn("dateCreated", new ColumnGenerator() {
					public Component generateCell(Table source, Object itemId, Object columnId) {
						MessageItem m = (MessageItem) itemId;
						String d = m.getDateCreated().substring(0, 10)
						Label l = new Label(d);
						return l;
					}
				});

		messages.setColumnAlignment("dateCreated",Table.ALIGN_RIGHT);
		messages.setColumnExpandRatio("sender", 2)
		messages.setColumnExpandRatio("topic", 5)
		messages.setColumnExpandRatio("dateCreated", 1)

		return messages
	}

	private HorizontalLayout createDeleteBar() {
		HorizontalLayout hl = new HorizontalLayout()
		hl.setWidth("100%")
		hl.setMargin(true, true, false, true)

		time = new Label("", Label.CONTENT_XHTML)
		reply = new Button("Reply")
		reply.addListener((ClickListener) this)
		reply.setIcon(new ThemeResource("icons/email-reply.png"))
		deleteMessage = new Button("Delete Message")
		deleteMessage.addListener((ClickListener) this)
		deleteMessage.setIcon(new ThemeResource("icons/document-delete.png"))

		hl.addComponent(time)
		hl.addComponent(reply)
		hl.addComponent(deleteMessage)
		hl.setSpacing(true)
		hl.setExpandRatio(time,1.0f)

		return hl
	}

	private Panel createMessagePanel() {
		Panel p = new Panel()
		p.setStyleName(Reindeer.PANEL_LIGHT);
		p.setWidth("100%")

		VerticalLayout panelLayout = p.getContent()
		panelLayout.setSizeUndefined()
		panelLayout.setMargin(false, true, true, true)

		topic = new Label("", Label.CONTENT_XHTML)
		from = new Label("", Label.CONTENT_XHTML)
		grid = new Label("", Label.CONTENT_XHTML)
		messageContent = new Label("", Label.CONTENT_XHTML)
		messageContent.setWidth("750px")

		panelLayout.addComponent(topic)
		panelLayout.addComponent(from)
		panelLayout.addComponent(grid)
		panelLayout.addComponent(messageContent)

		return p
	}

	public fillMessages() {
		// Refresh container
		dataSource.fillContainer()
		app.unreadMessages.setCaption("You've got: "+messageService.getUnreadCount()+" new messages")

		deleteMessage.setVisible(false)
		reply.setVisible(false)
		topic.setValue("")
		from.setValue("")
		grid.setValue("")
		messageContent.setValue("")
		time.setValue("")
	}

	public void valueChange(ValueChangeEvent event) {
		Property property = event.getProperty()
		if (property == messages) {
			MessageItem m = (MessageItem)messages.getValue()

			if(m.getTopic() != null) {
				topic.setValue("<b>Subject: </b>"+m.getTopic())
			} else {
				topic.setValue("<b>Subject: </b>")
			}
			from.setValue("<b>From: </b>"+m.getSender())
			grid.setValue("<hr/>")
			messageContent.setValue(m.getContent())
			time.setValue("<b>"+m.getDateCreated().substring(0, 19)+"</b>")

			deleteMessage.setVisible(true)
			reply.setVisible(true)

			// If message clicked then change wasRead to true
			if(!m.getWasRead()) {
				m.setWasRead(true)
				dataSource.setWasRead(m)
				dataSource.fireItemSetChange()
				app.unreadMessages.setCaption("You've got: "+messageService.getUnreadCount()+" new messages")
			}
		}
	}

	public void buttonClick(ClickEvent event) {
		final Button source = event.getButton()
		if(source == newMessage) {
			app.getMainWindow().addWindow(new NewMessageWindow(app, null))
		} else if(source == onlyUnread) {
			if(source.booleanValue()) {
				dataSource.addContainerFilter("wasRead", "false", true, false);
			} else {
				dataSource.removeContainerFilters("wasRead")
			}
		} else if(source == deleteAll) {
			app.getMainWindow().addWindow(new YesNoDialog("All messages delete !","Are you sure you want to delete all messages?",
					new YesNoDialog.Callback() {
						public void onDialogResult(boolean answer) {
							if(answer) {
								dataSource.removeAllMessages()
								fillMessages()
							}
						}
					}))
		} else if(source == reply) {
			MessageItem m = (MessageItem)messages.getValue()
			app.getMainWindow().addWindow(new NewMessageWindow(app, m.getSender()))
		} else if(source == deleteMessage) {
			app.getMainWindow().addWindow(new YesNoDialog("Message delete","Are you sure you want to delete this message?",
					new YesNoDialog.Callback() {
						public void onDialogResult(boolean answer) {
							if(answer) {
								MessageItem m = (MessageItem)messages.getValue()
								dataSource.removeMessage(m)
								fillMessages()
							}
						}
					}))
		} else if(source == refresh) {
			fillMessages()
		}
	}
}
