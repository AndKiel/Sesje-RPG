package rpgApp.pages

import com.vaadin.data.Property
import com.vaadin.data.Property.ValueChangeEvent
import com.vaadin.terminal.ThemeResource
import com.vaadin.ui.Button
import com.vaadin.ui.Component
import com.vaadin.ui.HorizontalLayout
import com.vaadin.ui.Label
import com.vaadin.ui.Panel
import com.vaadin.ui.Table
import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.Button.ClickEvent
import com.vaadin.ui.Button.ClickListener
import com.vaadin.ui.themes.Reindeer

import rpgApp.data.ScenarioContainer
import rpgApp.data.ScenarioItem
import rpgApp.main.IndexApplication
import rpgApp.services.ScenarioService
import rpgApp.windows.NewScenarioWindow
import rpgApp.windows.YesNoDialog

class Scenarios extends VerticalLayout implements Property.ValueChangeListener, ClickListener {
    private IndexApplication app
    private ScenarioService scenarioService
    private ScenarioContainer dataSource
    private Table scenarioTable
	
    private Button addScenario
    private Button editScenario
    private Button deleteScenario

    private Label name
    private Label type
    private Label playersCount
    private Label system
    private Label content

    public Scenarios(IndexApplication app) {
        this.app = app
        setMargin(true)
        setSpacing(true)
        
        scenarioService = app.scenarioService
        dataSource = new ScenarioContainer(scenarioService)
        addComponent(createHeader())
        addComponent(createScenarioTable())
        Panel info = createScenarioInfo()
        addComponent(info)
        setExpandRatio(info, 1.0f)
    }

    public fillScenarios(){
        dataSource.fillContainer()
        scenarioTable.setValue(scenarioTable.firstItemId())
    }

    private HorizontalLayout createHeader() {
        HorizontalLayout hl = new HorizontalLayout()
        hl.setSpacing(true)
        hl.setMargin(true, false, true, true)
        hl.setWidth("100%")

        Label l = new Label("")
        addScenario = new Button("Add scenario")
        addScenario.addListener((ClickListener) this)
        addScenario.setIcon(new ThemeResource("icons/folder-add.png"))
        editScenario = new Button("Edit")
        editScenario.addListener((ClickListener) this)
        editScenario.setIcon(new ThemeResource("icons/folder.png"))
        deleteScenario = new Button("Delete")
        deleteScenario.addListener((ClickListener) this)
        deleteScenario.setIcon(new ThemeResource("icons/folder-delete.png"))

        hl.addComponent(l)
        hl.addComponent(addScenario)
        hl.addComponent(editScenario)
        hl.addComponent(deleteScenario)
        hl.setExpandRatio(l, 1.0f)

        return hl
    }
    
    private Table createScenarioTable() {
        scenarioTable = new Table()
        scenarioTable.setWidth("100%")
        scenarioTable.setContainerDataSource(dataSource)
        scenarioTable.setVisibleColumns(dataSource.NATURAL_COL_ORDER)
        scenarioTable.setColumnHeaders(dataSource.COL_HEADERS_ENGLISH)
        scenarioTable.setPageLength(10)
        scenarioTable.setSelectable(true)
        scenarioTable.setImmediate(true)
        scenarioTable.setNullSelectionAllowed(false);
        scenarioTable.addListener((Property.ValueChangeListener) this)
        scenarioTable.setStyleName(Reindeer.TABLE_STRONG)

        scenarioTable.setColumnAlignment("id",Table.ALIGN_CENTER);
        scenarioTable.setColumnAlignment("name",Table.ALIGN_LEFT);
        scenarioTable.setColumnAlignment("type",Table.ALIGN_CENTER);
        scenarioTable.setColumnAlignment("playersCount",Table.ALIGN_CENTER);
        scenarioTable.setColumnAlignment("system",Table.ALIGN_CENTER);
        scenarioTable.setColumnExpandRatio("id", 0.5f)
        scenarioTable.setColumnExpandRatio("name", 6)
        scenarioTable.setColumnExpandRatio("type", 2)
        scenarioTable.setColumnExpandRatio("playersCount", 2)
        scenarioTable.setColumnExpandRatio("system", 4)

        return scenarioTable
    }
    
    private Panel createScenarioInfo(){
        Panel p = new Panel()
        p.setStyleName(Reindeer.PANEL_LIGHT);

        VerticalLayout vl = new VerticalLayout()
        vl.setWidth("100%")
        vl.setMargin(true)
        vl.setSpacing(true)

        p.setContent(vl)
        
        name = new Label("", Label.CONTENT_XHTML)
        type = new Label("", Label.CONTENT_XHTML)
        playersCount = new Label("", Label.CONTENT_XHTML)
        system = new Label("", Label.CONTENT_XHTML)
        content = new Label("", Label.CONTENT_XHTML)
        
        vl.addComponent(name)
        vl.addComponent(type)
        vl.addComponent(playersCount)
        vl.addComponent(system)
        vl.addComponent(content)
        
        return p
    }
    
    public Table getScenarioTable(){
        return scenarioTable
    }

    public void buttonClick(ClickEvent clickEvent) {
        final Button source = clickEvent.getButton()
        switch(clickEvent.source){
            case addScenario:
            app.getMainWindow().addWindow(new NewScenarioWindow(app, false, this))
            break
            case editScenario:
            app.getMainWindow().addWindow(new NewScenarioWindow(app, true, this))
            break
            case deleteScenario:
            app.getMainWindow().addWindow(new YesNoDialog("Scenario delete","Are you sure you want to delete this scenario?",
                    new YesNoDialog.Callback() {
                        public void onDialogResult(boolean answer) {
                            if(answer) {
                                ScenarioItem n = (ScenarioItem)scenarioTable.getValue()
                                dataSource.removeScenario(n)
                                fillScenarios()
                            }
                        }
                    }))
            break
        }
    }
    
    public void valueChange(ValueChangeEvent event) {
        Property property = event.getProperty()
        if(property == scenarioTable) {
            ScenarioItem n = (ScenarioItem)scenarioTable.getValue()
            if(n != null)
            {
                name.setValue("<b>Name: </b>"+n.getName())
                type.setValue("<b>Type: </b>"+n.getType())
                playersCount.setValue("<b>Players count: </b>"+n.getPlayersCount())
                system.setValue("<b>System: </b>"+n.getSystem())
                content.setValue("<b>Content: </b>"+n.getContent())
            }
            else
            {
                name.setValue("")
                type.setValue("")
                playersCount.setValue("")
                system.setValue("")
                content.setValue("")
            }
        }
    }
}
