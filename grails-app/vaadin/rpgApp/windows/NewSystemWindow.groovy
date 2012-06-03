package rpgApp.windows;

import rpgApp.content.Systems
import rpgApp.data.SystemItem
import rpgApp.exeptions.ValidationException
import rpgApp.main.IndexApplication;
import rpgApp.services.SystemService;

import com.vaadin.data.validator.RegexpValidator
import com.vaadin.data.validator.StringLengthValidator
import com.vaadin.terminal.Sizeable
import com.vaadin.terminal.ThemeResource
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Alignment
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout
import com.vaadin.ui.RichTextArea
import com.vaadin.ui.TextField
import com.vaadin.ui.Window
import com.vaadin.ui.Window.Notification

public class NewSystemWindow extends Window implements Button.ClickListener {
	private IndexApplication app
	private Systems systems
	private SystemService systemService
	private boolean editMode

	private Button save
	private Button cancel
	private Form systemForm = new Form()

	NewSystemWindow(IndexApplication app, boolean editMode, Systems systems) {
		super("New RPG System")
		this.app = app
		this.systems = systems
		this.editMode = editMode
		systemService = app.systemService
		this.setCaption("New RPG System")
		setModal(true)
		setDraggable(false)
		setResizable(false)

		TextField name = new TextField("Name: ")
		name.setWidth("100%")
		name.addValidator(new StringLengthValidator("Name must be less than 30 signs", 0, 30, false))
		name.setRequired(true)
		name.focus()
		systemForm.addField("name", name)

		RichTextArea description = new RichTextArea("Description: ");
		description.setWidth("100%")
		description.setHeight("300px")
		systemForm.addField("description", description)

		TextField genre = new TextField("Genre: ")
		genre.setWidth("100%")
		genre.addValidator(new StringLengthValidator("Genre must be less than 30 signs", 0, 30, false))
		systemForm.addField("genre", genre)

		TextField designer = new TextField("Designer: ")
		designer.setWidth("100%")
		designer.addValidator(new StringLengthValidator("Designer must be less than 40 signs", 0, 40, false))
		systemForm.addField("designer", designer)

		TextField publisher = new TextField("Publisher")
		publisher.setWidth("100%")
		publisher.addValidator(new StringLengthValidator("Publisher must be less than 50 signs", 0, 40, false))
		systemForm.addField("publisher", publisher)

		TextField year = new TextField("Year: ")
		year.setWidth("20%")
		year.addValidator(new RegexpValidator("[0-9]{4}","Invalid year format"))
		year.setRequired(true)
		systemForm.addField("year", year)

		HorizontalLayout footer = new HorizontalLayout()
		footer.setSpacing(true);
		footer.setWidth("100%")
		save = new Button("Create", (Button.ClickListener)this)
		if(editMode) {
			save.setCaption("Save")
		}
		save.setIcon(new ThemeResource("icons/ok.png"))
		cancel = new Button("Cancel", (Button.ClickListener)this)
		cancel.setIcon(new ThemeResource("icons/cancel.png"))
		footer.addComponent(save);
		footer.setComponentAlignment(save, Alignment.MIDDLE_CENTER)
		footer.addComponent(cancel);
		footer.setComponentAlignment(cancel, Alignment.MIDDLE_CENTER)

		systemForm.setFooter(footer)
		systemForm.setWidth("100%")

		addComponent(systemForm);

		setWidth(650, Sizeable.UNITS_PIXELS)
		center();

		if(editMode) {
			this.setCaption("Edit RPG System")
			
			SystemItem s = (SystemItem)systems.getSystems().getValue()
			name.setValue(s.getName())
			name.setEnabled(false)
			description.setValue(s.getDescription())
			description.setNullRepresentation("")
			genre.setValue(s.getGenre())
			genre.setNullRepresentation("")
			designer.setValue(s.getDesigner())
			designer.setNullRepresentation("")
			publisher.setValue(s.getPublisher())
			publisher.setNullRepresentation("")
			year.setValue(s.getYear())
		}
	}

	void buttonClick(ClickEvent clickEvent) {
		switch(clickEvent.source){
			case save:
				if(systemForm.isValid()){
					if(!editMode) {
						try{
							systemService.createSystem(
									(String)(systemForm.getField("name").getValue()),
									(String)(systemForm.getField("description").getValue()),
									(String)(systemForm.getField("genre").getValue()),
									(String)(systemForm.getField("designer").getValue()),
									(String)(systemForm.getField("publisher").getValue()),
									new Integer(systemForm.getField("year").getValue())
									)
							systems.fillSystems()
							this.close()
						} catch(ValidationException e) {
							app.getMainWindow().showNotification(e.message, Notification.TYPE_ERROR_MESSAGE);
						}
					} else {
						systemService.updateSystem(
									(String)(systemForm.getField("name").getValue()),
									(String)(systemForm.getField("description").getValue()),
									(String)(systemForm.getField("genre").getValue()),
									(String)(systemForm.getField("designer").getValue()),
									(String)(systemForm.getField("publisher").getValue()),
									new Integer(systemForm.getField("year").getValue())
									)
						systems.fillSystems()
						this.close()
					}
				}
				break;
			case cancel:
				this.close()
				break;
		}
	}
}
