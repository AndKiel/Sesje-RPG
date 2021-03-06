package rpgApp.pages

import rpgApp.main.IndexApplication

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

import rpgApp.data.FAQContainer
import rpgApp.data.FAQItem
import rpgApp.main.IndexApplication
import rpgApp.services.FAQService
import rpgApp.windows.NewFAQWindow
import rpgApp.windows.YesNoDialog

class FAQ extends VerticalLayout implements Property.ValueChangeListener, ClickListener {
    private IndexApplication app
    private FAQService faqService
    private FAQContainer dataSource
    private Table faqTable
	
    private Button addFAQ
    private Button editFAQ
    private Button deleteFAQ
    
    private Label question
    private Label answer
	
    public FAQ(IndexApplication app) {
        this.app = app
        setMargin(true)
        setSpacing(true)
        
        faqService = app.faqService
        dataSource = new FAQContainer(faqService)
        addComponent(createHeader())
        addComponent(createFAQTable())
        Panel info = createFAQInfo()
        addComponent(info)
        setExpandRatio(info, 1.0f)
    }
    
    public fillFAQ(){
        dataSource.fillContainer()
        faqTable.setValue(faqTable.firstItemId())
    }
    private HorizontalLayout createHeader() {
        HorizontalLayout hl = new HorizontalLayout()
        hl.setSpacing(true)
        hl.setMargin(true, false, true, true)
        hl.setWidth("100%")

		Label l = new Label("")
        addFAQ = new Button("Add question")
        addFAQ.addListener((ClickListener) this)
        addFAQ.setIcon(new ThemeResource("icons/folder-add.png"))
        editFAQ = new Button("Edit")
        editFAQ.addListener((ClickListener) this)
        editFAQ.setIcon(new ThemeResource("icons/folder.png"))
        deleteFAQ = new Button("Delete")
        deleteFAQ.addListener((ClickListener) this)
        deleteFAQ.setIcon(new ThemeResource("icons/folder-delete.png"))

		hl.addComponent(l)
        hl.addComponent(addFAQ)
        hl.addComponent(editFAQ)
        hl.addComponent(deleteFAQ)
		hl.setExpandRatio(l, 1.0f)

        return hl
    }
    
    private Table createFAQTable() {
        faqTable = new Table()
        faqTable.setWidth("100%")
        faqTable.setContainerDataSource(dataSource)
        faqTable.setVisibleColumns(dataSource.NATURAL_COL_ORDER)
        faqTable.setColumnHeaders(dataSource.COL_HEADERS_ENGLISH)
        faqTable.setPageLength(10)
        faqTable.setSelectable(true)
        faqTable.setImmediate(true)
        faqTable.setNullSelectionAllowed(false);
        faqTable.addListener((Property.ValueChangeListener) this)
        faqTable.setStyleName(Reindeer.TABLE_STRONG)

        faqTable.setColumnAlignment("id",Table.ALIGN_CENTER);
        faqTable.setColumnAlignment("question",Table.ALIGN_LEFT);
        faqTable.setColumnExpandRatio("question", 1)

        return faqTable
    }
    
    private Panel createFAQInfo(){
        Panel p = new Panel()
        p.setStyleName(Reindeer.PANEL_LIGHT);

        VerticalLayout vl = new VerticalLayout()
        vl.setSizeFull()
        vl.setMargin(true)
        vl.setSpacing(true)

        p.setContent(vl)
        
        question = new Label("", Label.CONTENT_XHTML)
        answer = new Label("", Label.CONTENT_XHTML)

        vl.addComponent(question)
        vl.addComponent(answer)
        
        return p
    }
    
    public Table getFAQTable(){
        return faqTable
    }

    public void buttonClick(ClickEvent clickEvent) {
        final Button source = clickEvent.getButton()
        switch(clickEvent.source){
            case addFAQ:
            app.getMainWindow().addWindow(new NewFAQWindow(app, false, this))
            break
            case editFAQ:
            app.getMainWindow().addWindow(new NewFAQWindow(app, true, this))
            break
            case deleteFAQ:
            app.getMainWindow().addWindow(new YesNoDialog("FAQ delete","Are you sure you want to delete this question?",
                    new YesNoDialog.Callback() {
                        public void onDialogResult(boolean answer) {
                            if(answer) {
                                FAQItem f = (FAQItem)faqTable.getValue()
                                dataSource.removeFAQ(f)
                                fillFAQ()
                            }
                        }
                    }))
            break
        }
    }
    
    public void valueChange(ValueChangeEvent event) {
        Property property = event.getProperty()
        if(property == faqTable) {
            FAQItem faq = (FAQItem)faqTable.getValue()
            question.setValue("<b>Question: </b>"+faq.getQuestion())
            answer.setValue("<b>Answer: </b>"+faq.getAnswer())
        }
    }
}
