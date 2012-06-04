package rpgApp.content



import com.vaadin.data.Property
import com.vaadin.data.Property.ValueChangeEvent
import com.vaadin.terminal.Resource
import com.vaadin.terminal.ThemeResource
import com.vaadin.ui.Button
import com.vaadin.ui.Embedded
import com.vaadin.ui.GridLayout
import com.vaadin.ui.HorizontalLayout
import com.vaadin.ui.Label
import com.vaadin.ui.Panel
import com.vaadin.ui.Table
import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.Button.ClickEvent
import com.vaadin.ui.Button.ClickListener
import com.vaadin.ui.themes.BaseTheme
import com.vaadin.ui.themes.Reindeer

import rpgApp.data.SystemContainer
import rpgApp.data.SystemItem
import rpgApp.main.IndexApplication
import rpgApp.services.SystemService
import rpgApp.windows.NewSystemWindow
import rpgApp.windows.YesNoDialog

class Systems extends VerticalLayout implements Property.ValueChangeListener, ClickListener {
	private IndexApplication app
	private SystemService systemService
	private SystemContainer dataSource
	private Table systems

	private Button addSystem
	private Button editSystem
	private Button deleteSystem
	private Button showAll
	private Button letter
	
	private Label name
	private Label description
	private Label genre
	private Label designer
	private Label publisher
	private Label year

	private char[] alphabet = ['A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z']

	public Systems(IndexApplication app) {
		this.app = app
		setSizeFull()
		setMargin(true)
		setSpacing(true)

		systemService = app.systemService
		dataSource = new SystemContainer(systemService)
		addComponent(createHeader())
		addComponent(createSearchBar())
		addComponent(createSystemTable())
		Panel info = createSystemInfo()
		addComponent(info)
		
		setExpandRatio(info, 1.0f)
	}

	public fillSystems() {
		// Refresh container
		dataSource.fillContainer()
		systems.setValue(systems.firstItemId())

		if(app.security.checkRole("Administrator") || app.security.checkRole("Moderator")) {
			addSystem.setVisible(true)
			editSystem.setVisible(true)
			deleteSystem.setVisible(true)
		} else {
			addSystem.setVisible(false)
			editSystem.setVisible(false)
			deleteSystem.setVisible(false)
		}
	}

	private HorizontalLayout createHeader() {
		HorizontalLayout hl = new HorizontalLayout()
		hl.setSpacing(true)
		hl.setMargin(true, true, false, true)
		hl.setWidth("100%")

		addSystem = new Button("New system")
		addSystem.addListener((ClickListener) this)
		addSystem.setIcon(new ThemeResource("icons/folder-add.png"))
		editSystem = new Button("Edit")
		editSystem.addListener((ClickListener) this)
		editSystem.setIcon(new ThemeResource("icons/folder.png"))
		deleteSystem = new Button("Delete")
		deleteSystem.addListener((ClickListener) this)
		deleteSystem.setIcon(new ThemeResource("icons/folder-delete.png"))
		showAll = new Button("Show all")
		showAll.addListener((ClickListener) this)

		hl.addComponent(showAll)
		hl.addComponent(addSystem)
		hl.addComponent(editSystem)
		hl.addComponent(deleteSystem)
		hl.setExpandRatio(showAll, 1.0f)

		return hl
	}

	private HorizontalLayout createSearchBar() {
		HorizontalLayout hl = new HorizontalLayout()
		hl.setWidth("100%")
		hl.setMargin(true)
		for(char let : alphabet) {
			letter = new Button(let.toString())
			letter.addListener(new Button.ClickListener() {
						public void buttonClick(ClickEvent event) {
							dataSource.removeAllContainerFilters()
							dataSource.addContainerFilter("name",event.source.getCaption(),true,true)
						}
					}
					)
			letter.setStyleName(BaseTheme.BUTTON_LINK)
			hl.addComponent(letter)

		}
		
		return hl
	}

	private Table createSystemTable() {
		systems = new Table()
		systems.setWidth("100%")
		systems.setContainerDataSource(dataSource)
		systems.setVisibleColumns(dataSource.NATURAL_COL_ORDER)
		systems.setColumnHeaders(dataSource.COL_HEADERS_ENGLISH)
		systems.setPageLength(10)
		systems.setSelectable(true)
		systems.setImmediate(true)
		systems.setNullSelectionAllowed(false);
		systems.addListener((Property.ValueChangeListener) this)
		systems.setStyleName(Reindeer.TABLE_STRONG)

		systems.setColumnAlignment("name",Table.ALIGN_CENTER);
		systems.setColumnAlignment("genre",Table.ALIGN_CENTER);
		systems.setColumnAlignment("year",Table.ALIGN_CENTER);
		systems.setColumnExpandRatio("name", 2)
		systems.setColumnExpandRatio("genre", 1)
		systems.setColumnExpandRatio("year", 1)

		return systems
	}
	
	private Panel createSystemInfo() {
		Panel p = new Panel()
		p.setSizeFull()
		HorizontalLayout hl = new HorizontalLayout()
		hl.setSizeFull()
		hl.setMargin(true)
		GridLayout vl = new GridLayout(3,3)
		vl.setSpacing(true)
		
		p.setContent(hl)
		hl.addComponent(vl)
		Resource res = new ThemeResource("icons/empty-image.jpg");
		Embedded e = new Embedded(null, res);
		e.setWidth("130px");
		e.setHeight("130px");
		hl.addComponent(e)
		hl.setExpandRatio(vl,1.0f)
		vl.setSizeUndefined()
		
		name = new Label("")
		name.setContentMode(Label.CONTENT_XHTML)
		description = new Label("")
		description.setContentMode(Label.CONTENT_XHTML)
		genre = new Label("")
		genre.setContentMode(Label.CONTENT_XHTML)
		designer = new Label("")
		designer.setContentMode(Label.CONTENT_XHTML)
		publisher = new Label("")
		publisher.setContentMode(Label.CONTENT_XHTML)
		year = new Label("")
		year.setContentMode(Label.CONTENT_XHTML)
		
		vl.addComponent(name)
		vl.addComponent(genre)
		vl.addComponent(year)
		vl.addComponent(designer)
		vl.addComponent(publisher)
		vl.addComponent(description,0,2,2,2)
		
		return p
	}

	public Table getSystems() {
		return systems
	}

	public void valueChange(ValueChangeEvent event) {
		Property property = event.getProperty()
		if(property == systems) {
			SystemItem s = (SystemItem)systems.getValue()
			name.setValue("<b>System name: </b>"+s.getName())
			if(s.getDescription() == null) {
				s.setDescription("")
			}
			description.setValue("<b>Description: </b>"+s.getDescription())
			if(s.getGenre() == null) {
				s.setGenre("")
			}
			genre.setValue("<b>Genre: </b>"+s.getGenre())
			if(s.getDesigner() == null) {
				s.setDesigner("")
			}
			designer.setValue("<b>Designer: </b>"+s.getDesigner())
			if(s.getPublisher() == null) {
				s.setPublisher("")
			}
			publisher.setValue("<b>Publisher: </b>"+s.getPublisher())
			year.setValue("<b>Year: </b>"+s.getYear())
		}
	}

	public void buttonClick(ClickEvent clickEvent) {
		final Button source = clickEvent.getButton()
		switch(clickEvent.source){
			case addSystem:
				app.getMainWindow().addWindow(new NewSystemWindow(app, false, this))
				break
			case editSystem:
				app.getMainWindow().addWindow(new NewSystemWindow(app, true, this))
				break
			case deleteSystem:
				app.getMainWindow().addWindow(new YesNoDialog("RPG System delete","Are you sure you want to delete this system?",
				new YesNoDialog.Callback() {
					public void onDialogResult(boolean answer) {
						if(answer) {
							SystemItem s = (SystemItem)systems.getValue()
							dataSource.removeSystem(s)
							fillSystems()
						}
					}
				}))
				break
			case showAll:
				dataSource.removeAllContainerFilters()
				break
		}
	}
}