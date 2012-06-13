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
import com.vaadin.ui.Table.ColumnGenerator
import com.vaadin.ui.themes.Reindeer

import rpgApp.data.NewsContainer
import rpgApp.data.NewsItem
import rpgApp.main.IndexApplication
import rpgApp.services.NewsService
import rpgApp.windows.NewNewsWindow
import rpgApp.windows.YesNoDialog

class News extends VerticalLayout implements Property.ValueChangeListener, ClickListener {
    private IndexApplication app
    private NewsService newsService
    private NewsContainer dataSource
    private Table newsTable
	
    private Button addNews
    private Button editNews
    private Button deleteNews
    
    private Label title
    private Label date
    private Label author
    private Label content

    public News(IndexApplication app) {
        this.app = app
        setMargin(true)
        setSpacing(true)
        
        newsService = app.newsService
        dataSource = new NewsContainer(newsService)
        addComponent(createHeader())
        addComponent(createNewsTable())
        Panel info = createNewsInfo()
        addComponent(info)
        setExpandRatio(info, 1.0f)
    }
    
    public fillNews(){
        dataSource.fillContainer()
        newsTable.setValue(newsTable.firstItemId())
    }
    
    private HorizontalLayout createHeader() {
        HorizontalLayout hl = new HorizontalLayout()
        hl.setSpacing(true)
        hl.setMargin(true, false, true, true)
        hl.setWidth("100%")

		Label l = new Label("")
        addNews = new Button("Add news")
        addNews.addListener((ClickListener) this)
        addNews.setIcon(new ThemeResource("icons/folder-add.png"))
        editNews = new Button("Edit")
        editNews.addListener((ClickListener) this)
        editNews.setIcon(new ThemeResource("icons/folder.png"))
        deleteNews = new Button("Delete")
        deleteNews.addListener((ClickListener) this)
        deleteNews.setIcon(new ThemeResource("icons/folder-delete.png"))

		hl.addComponent(l)
        hl.addComponent(addNews)
        hl.addComponent(editNews)
        hl.addComponent(deleteNews)
		hl.setExpandRatio(l, 1.0f)

        return hl
    }
    
    private Table createNewsTable() {
        newsTable = new Table()
        newsTable.setWidth("100%")
        newsTable.setContainerDataSource(dataSource)
        newsTable.setVisibleColumns(dataSource.NATURAL_COL_ORDER)
        newsTable.setColumnHeaders(dataSource.COL_HEADERS_ENGLISH)
        newsTable.setPageLength(10)
        newsTable.setSelectable(true)
        newsTable.setImmediate(true)
        newsTable.setNullSelectionAllowed(false);
        newsTable.addListener((Property.ValueChangeListener) this)
        newsTable.setStyleName(Reindeer.TABLE_STRONG)

        // Formating Date string in Date column
        newsTable.addGeneratedColumn("dateCreated", new ColumnGenerator() {
        	public Component generateCell(Table source, Object itemId, Object columnId) {
        		NewsItem n = (NewsItem) itemId;
        		String d = n.getDateCreated().toString().substring(0, 16)
        				Label l = new Label(d);
        		return l;
        	}
        });
	
        newsTable.setColumnAlignment("id",Table.ALIGN_CENTER);
        newsTable.setColumnAlignment("title",Table.ALIGN_CENTER);
        newsTable.setColumnAlignment("dateCreated",Table.ALIGN_CENTER);
        newsTable.setColumnAlignment("author",Table.ALIGN_CENTER);
        newsTable.setColumnExpandRatio("id", 0.5f)
        newsTable.setColumnExpandRatio("title", 6)
        newsTable.setColumnExpandRatio("dateCreated", 2)
        newsTable.setColumnExpandRatio("author", 2)
		

        return newsTable
    }
    
    private Panel createNewsInfo(){
        Panel p = new Panel()
        p.setStyleName(Reindeer.PANEL_LIGHT);

        VerticalLayout vl = new VerticalLayout()
        vl.setWidth("100%")
        vl.setMargin(true)
        vl.setSpacing(true)

        p.setContent(vl)
        
        title = new Label("", Label.CONTENT_XHTML)
        date = new Label("", Label.CONTENT_XHTML)
        author = new Label("", Label.CONTENT_XHTML)
        content = new Label("", Label.CONTENT_XHTML)

        vl.addComponent(title)
        vl.addComponent(date)
        vl.addComponent(author)
        vl.addComponent(content)
        
        return p
    }
    
    public Table getNewsTable(){
        return newsTable
    }

    public void buttonClick(ClickEvent clickEvent) {
        final Button source = clickEvent.getButton()
        switch(clickEvent.source){
            case addNews:
            app.getMainWindow().addWindow(new NewNewsWindow(app, false, this))
            break
            case editNews:
            app.getMainWindow().addWindow(new NewNewsWindow(app, true, this))
            break
            case deleteNews:
            app.getMainWindow().addWindow(new YesNoDialog("News delete","Are you sure you want to delete this news?",
                    new YesNoDialog.Callback() {
                        public void onDialogResult(boolean answer) {
                            if(answer) {
                                NewsItem n = (NewsItem)newsTable.getValue()
                                dataSource.removeNews(n)
                                fillNews()
                            }
                        }
                    }))
            break
        }
    }
    
    public void valueChange(ValueChangeEvent event) {
        Property property = event.getProperty()
        if(property == newsTable) {
            NewsItem n = (NewsItem)newsTable.getValue()
            title.setValue("<b>Title: </b>"+n.getTitle())
            date.setValue("<b>Date: </b>"+n.getDateCreated().toString().substring(0,16))
            author.setValue("<b>Author: </b>"+n.getAuthor())
            content.setValue("<b>Content: </b>"+n.getContent())
        }
    }
}
