package rpgApp.windows;

import rpgApp.pages.News
import rpgApp.data.NewsItem
import rpgApp.exeptions.ValidationException
import rpgApp.main.IndexApplication;
import rpgApp.services.NewsService;

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
import com.vaadin.ui.themes.Reindeer

public class NewNewsWindow extends Window implements Button.ClickListener {
    private IndexApplication app
    private News news
    private NewsService newsService
    private boolean editMode

    private Button save
    private Button cancel
    private Form newsForm = new Form()

    NewNewsWindow(IndexApplication app, boolean editMode, News news) {
        super("New news")
        this.app = app
        this.setStyleName(Reindeer.WINDOW_BLACK)
        this.news = news
        this.editMode = editMode
        newsService = app.newsService
        this.setCaption("New News")
        setModal(true)
        setDraggable(false)
        setResizable(false)
        
        TextField id = new TextField("Id: ")
        id.setWidth("100%")
        id.setVisible(false)
        newsForm.addField("id", id)

        TextField title = new TextField("Title: ")
        title.setWidth("100%")
        title.setRequired(true)
        title.focus()
        newsForm.addField("title", title)

        RichTextArea content = new RichTextArea("Content: ");
        content.setWidth("100%")
        content.setHeight("300px")
        content.setRequired(true)
        newsForm.addField("content", content)

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

        newsForm.setFooter(footer)
        newsForm.setWidth("100%")

        addComponent(newsForm);

        setWidth(650, Sizeable.UNITS_PIXELS)
        center();

        if(editMode) {
            this.setCaption("Edit news")
			
            NewsItem f = (NewsItem)news.getNewsTable().getValue()
            id.setValue(f.getId())
            title.setValue(f.getTitle())
            content.setValue(f.getContent())
        }
    }

    void buttonClick(ClickEvent clickEvent) {
        switch(clickEvent.source){
            case save:
            if(newsForm.isValid()){
                if(!editMode) {
                    try{
                        newsService.createNews(
                            (String)(newsForm.getField("title").getValue()),
                            (String)(newsForm.getField("content").getValue())
                        )
                        news.fillNews()
                        this.close()
                    } catch(ValidationException e) {
                        app.getMainWindow().showNotification(e.message, Notification.TYPE_ERROR_MESSAGE);
                    }
                } else {
                    newsService.updateNews(
                        (String)(newsForm.getField("title").getValue()),
                        (String)(newsForm.getField("content").getValue()),
                        (Integer)(newsForm.getField("id").getValue())
                    )
                    news.fillNews()
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
