package rpgApp.pages

import com.vaadin.data.Property
import com.vaadin.data.Property.ValueChangeEvent
import com.vaadin.terminal.Resource
import com.vaadin.terminal.ThemeResource
import com.vaadin.ui.Button
import com.vaadin.ui.Embedded
import com.vaadin.ui.HorizontalLayout
import com.vaadin.ui.Label
import com.vaadin.ui.Panel
import com.vaadin.ui.Table
import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.Button.ClickEvent
import com.vaadin.ui.Button.ClickListener
import com.vaadin.ui.themes.BaseTheme
import com.vaadin.ui.themes.Reindeer

import rpgApp.data.NewsContainer
import rpgApp.data.NewsItem
import rpgApp.main.IndexApplication
import rpgApp.services.NewsService

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
        setSizeFull()
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
        hl.setMargin(true, true, false, true)
        hl.setWidth("100%")

        addNews = new Button("Add news")
        addNews.addListener((ClickListener) this)
        addNews.setIcon(new ThemeResource("icons/folder-add.png"))
        editNews = new Button("Edit")
        editNews.addListener((ClickListener) this)
        editNews.setIcon(new ThemeResource("icons/folder.png"))
        deleteNews = new Button("Delete")
        deleteNews.addListener((ClickListener) this)
        deleteNews.setIcon(new ThemeResource("icons/folder-delete.png"))

        hl.addComponent(addNews)
        hl.addComponent(editNews)
        hl.addComponent(deleteNews)

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

        newsTable.setColumnAlignment("id",Table.ALIGN_CENTER);
        newsTable.setColumnAlignment("title",Table.ALIGN_CENTER);
        newsTable.setColumnAlignment("dateCreated",Table.ALIGN_CENTER);
        newsTable.setColumnAlignment("author",Table.ALIGN_CENTER);
        newsTable.setColumnExpandRatio("id", 1)
        newsTable.setColumnExpandRatio("title", 3)
        newsTable.setColumnExpandRatio("dateCreated", 1)
        newsTable.setColumnExpandRatio("author", 1)

        return newsTable
    }
    
    private Panel createNewsInfo(){
        Panel p = new Panel()
        p.setStyleName(Reindeer.PANEL_LIGHT);
        p.setSizeFull()

        VerticalLayout vl = new VerticalLayout()
        vl.setSizeFull()
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
                // TODO: NewNewsWindow
            break
            case editNews:
                // TODO: EditNewsWindow
            break
            case deleteNews:
                // TODO: News Deletion
            break
        }
    }
    
    public void valueChange(ValueChangeEvent event) {
        Property property = event.getProperty()
        if(property == newsTable) {
            NewsItem n = (NewsItem)news.getValue()
            title.setValue("<b>Title: </b>"+n.getTitle())
            date.setValue("<b>Date: </b>"+n.getDateCreated())
            author.setValue("<b>Author: </b>"+n.getAuthor())
            content.setValue("<b>Content: </b>"+n.getContent())
        }
    }
}
