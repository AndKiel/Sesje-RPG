package rpgApp.windows

import grails.validation.ValidationException
import rpgApp.main.IndexApplication
import rpgApp.services.MessageService
import rpgApp.services.UserService

import com.vaadin.data.validator.StringLengthValidator
import com.vaadin.event.ShortcutAction.KeyCode
import com.vaadin.terminal.Sizeable
import com.vaadin.ui.Alignment
import com.vaadin.ui.Button
import com.vaadin.ui.ComboBox
import com.vaadin.ui.Form
import com.vaadin.ui.HorizontalLayout
import com.vaadin.ui.RichTextArea
import com.vaadin.ui.TextField
import com.vaadin.ui.Window
import com.vaadin.ui.AbstractSelect.Filtering
import com.vaadin.ui.Button.ClickEvent

class NewMessageWindow extends Window implements Button.ClickListener {
	private IndexApplication app
	private MessageService messageService
	private UserService userService
	
	private Button send 
	private Button cancel 
	private Form messageForm = new Form()

	NewMessageWindow(IndexApplication app) {
		super("New Message")
		this.app = app
		messageService = app.messageService
		userService = app.userService
		this.setCaption("New Message")
		setModal(true)
		setDraggable(false)
		setResizable(false)
		
		ComboBox addressee = new ComboBox("To: ")
		List<String> allNicknames = userService.getAllUsersNicknames()
		allNicknames.each {
			addressee.addItem(it)
		}
		addressee.setNullSelectionAllowed(false)
		addressee.setNewItemsAllowed(false)
		addressee.setWidth("100%")
		addressee.setFilteringMode(Filtering.FILTERINGMODE_CONTAINS)
		addressee.focus()
		addressee.setRequired(true)
		messageForm.addField("addressee", addressee)

		TextField topicField = new TextField("Topic: ")
		topicField.setWidth("100%")
		topicField.addValidator(new StringLengthValidator("Topic must be less than 50 signs", 0, 50, false))
		messageForm.addField("topic", topicField)

		RichTextArea content = new RichTextArea("Content: ");
		content.setWidth("100%")
		content.setHeight("400px")
		content.setRequired(true)
		messageForm.addField("content", content)

		HorizontalLayout footer = new HorizontalLayout()
		footer.setSpacing(true);
		footer.setWidth("100%")
		send = new Button("Send", (Button.ClickListener)this)
		cancel = new Button("Cancel", (Button.ClickListener)this)
		footer.addComponent(send);
		footer.setComponentAlignment(send, Alignment.MIDDLE_CENTER)
		footer.addComponent(cancel);
		footer.setComponentAlignment(cancel, Alignment.MIDDLE_CENTER)

		messageForm.setFooter(footer)
		messageForm.setWidth("100%")

		addComponent(messageForm);

		setWidth(800, Sizeable.UNITS_PIXELS)
		center();
	}

	void buttonClick(ClickEvent clickEvent) {
		switch(clickEvent.source){
			case send:
				if(messageForm.isValid()){
					try{
						messageService.addMessage((String)(messageForm.getField("topic").getValue()), (String)(messageForm.getField("content").getValue()), (String)(messageForm.getField("addressee").getValue()))
						this.close()
					} catch(ValidationException e) {
						app.getMainWindow().showNotification(e.message, Notification.TYPE_ERROR_MESSAGE);
					}
				}
				break;
			case cancel:
				this.close()
				break;
		}
	}
}
