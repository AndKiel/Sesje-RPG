package rpgApp.windows;

import rpgApp.pages.Scenarios
import rpgApp.data.ScenarioItem
import rpgApp.exeptions.ValidationException
import rpgApp.main.IndexApplication;
import rpgApp.services.ScenarioService;

import com.vaadin.terminal.Sizeable
import com.vaadin.terminal.ThemeResource
import com.vaadin.ui.AbstractSelect.Filtering
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComboBox
import com.vaadin.ui.Alignment
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout
import com.vaadin.ui.RichTextArea
import com.vaadin.ui.TextField
import com.vaadin.ui.OptionGroup
import com.vaadin.ui.Window
import com.vaadin.ui.Window.Notification
import com.vaadin.ui.themes.Reindeer

public class NewScenarioWindow extends Window implements Button.ClickListener {
    private IndexApplication app
    private Scenarios scenarios
    private ScenarioService scenarioService
    private boolean editMode

    private Button save
    private Button cancel
    private Form scenarioForm = new Form()

    NewScenarioWindow(IndexApplication app, boolean editMode, Scenarios scenarios) {
        super("New scenario")
        this.app = app
        this.setStyleName(Reindeer.WINDOW_BLACK)
        this.scenarios = scenarios
        this.editMode = editMode
        scenarioService = app.scenarioService
        this.setCaption("New Scenario")
        setModal(true)
        setDraggable(false)
        setResizable(false)
        
        TextField id = new TextField("Id: ")
        id.setWidth("100%")
        id.setVisible(false)
        scenarioForm.addField("id", id)

        TextField name = new TextField("Name: ")
        name.setWidth("100%")
        name.setRequired(true)
        name.focus()
        scenarioForm.addField("name", name)

        OptionGroup type = new OptionGroup("Type: ")
        type.addItem("campaign")
        type.addItem("one-shot")
        type.setImmediate(true)
        type.setNullSelectionAllowed(false)
        scenarioForm.addField("type", type)

        ComboBox playersCount = new ComboBox("Players count: ")
        for(int i = 1; i < 10; i++) {
            playersCount.addItem(i)
        }
        playersCount.setNullSelectionAllowed(false)
        playersCount.setNewItemsAllowed(false)
        playersCount.setWidth("20%")
        playersCount.setRequired(true)
        scenarioForm.addField("playersCount", playersCount)

        ComboBox system = new ComboBox("System: ")
        List<String> allNames = app.systemService.getAllSystemsNames()
        allNames.each {
            system.addItem(it)
        }
        system.setNullSelectionAllowed(false)
        system.setNewItemsAllowed(false)
        system.setWidth("100%")
        system.setFilteringMode(Filtering.FILTERINGMODE_CONTAINS)
        system.focus()
        system.setRequired(true)
        scenarioForm.addField("system", system)

        RichTextArea content = new RichTextArea("Content: ");
        content.setWidth("100%")
        content.setHeight("300px")
        content.setRequired(true)
        scenarioForm.addField("content", content)

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

        scenarioForm.setFooter(footer)
        scenarioForm.setWidth("100%")

        addComponent(scenarioForm);

        setWidth(650, Sizeable.UNITS_PIXELS)
        center();

        if(editMode) {
            this.setCaption("Edit scenario")
			
            ScenarioItem f = (ScenarioItem)scenarios.getScenarioTable().getValue()
            id.setValue(f.getId())
            name.setValue(f.getName())
            type.setValue(f.getType())
            playersCount.setValue(f.getPlayersCount())
            system.setValue(f.getSystem())
            content.setValue(f.getContent())
        }
    }

    void buttonClick(ClickEvent clickEvent) {
        switch(clickEvent.source){
            case save:
            if(scenarioForm.isValid()){
                if(!editMode) {
                    try{
                        scenarioService.createScenario(
                            (String)(scenarioForm.getField("name").getValue()),
                            (String)(scenarioForm.getField("type").getValue()),
                            new Integer(scenarioForm.getField("playersCount").getValue()),
                            (String)(scenarioForm.getField("content").getValue()),
                            (String)(scenarioForm.getField("system").getValue())
                        )
                        scenarios.fillScenarios()
                        this.close()
                    } catch(ValidationException e) {
                        app.getMainWindow().showNotification(e.message, Notification.TYPE_ERROR_MESSAGE);
                    }
                } else {
                    scenarioService.updateScenario(
                        (String)(scenarioForm.getField("name").getValue()),
                        (String)(scenarioForm.getField("type").getValue()),
                        new Integer(scenarioForm.getField("playersCount").getValue()),
                        (String)(scenarioForm.getField("content").getValue()),
                        (String)(scenarioForm.getField("system").getValue()),
                        (Long)(scenarioForm.getField("id").getValue())
                    )
                    scenarios.fillScenarios()
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
