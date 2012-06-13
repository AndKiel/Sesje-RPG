package rpgApp.windows;

import rpgApp.pages.FAQ
import rpgApp.data.FAQItem
import rpgApp.exeptions.ValidationException
import rpgApp.main.IndexApplication;
import rpgApp.services.FAQService;

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
import com.vaadin.ui.themes.Reindeer

public class NewFAQWindow extends Window implements Button.ClickListener {
    private IndexApplication app
    private FAQ faq
    private FAQService faqService
    private boolean editMode

    private Button save
    private Button cancel
    private Form faqForm = new Form()

    NewFAQWindow(IndexApplication app, boolean editMode, FAQ faq) {
        super("New FAQ")
        this.app = app
        this.faq = faq
        this.editMode = editMode
        faqService = app.faqService
        this.setCaption("New FAQ")
        setModal(true)
        setDraggable(false)
        setResizable(false)
        
        TextField id = new TextField("Id: ")
        id.setWidth("100%")
        id.setVisible(false)
        faqForm.addField("id", id)

        TextField question = new TextField("Question: ")
        question.setWidth("100%")
        question.setRequired(true)
        question.focus()
        faqForm.addField("question", question)

        RichTextArea answer = new RichTextArea("Answer: ");
        answer.setWidth("100%")
        answer.setHeight("300px")
        answer.setRequired(true)
        faqForm.addField("answer", answer)

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

        faqForm.setFooter(footer)
        faqForm.setWidth("100%")

        addComponent(faqForm);

        setWidth(650, Sizeable.UNITS_PIXELS)
        center();

        if(editMode) {
            this.setCaption("Edit RPG System")
			
            FAQItem f = (FAQItem)faq.getFAQTable().getValue()
            id.setValue(f.getId())
            question.setValue(f.getQuestion())
            answer.setValue(f.getAnswer())
        }
    }

    void buttonClick(ClickEvent clickEvent) {
        switch(clickEvent.source){
            case save:
            if(faqForm.isValid()){
                if(!editMode) {
                    try{
                        faqService.createFAQ(
                            (String)(faqForm.getField("question").getValue()),
                            (String)(faqForm.getField("answer").getValue())
                        )
                        faq.fillFAQ()
                        this.close()
                    } catch(ValidationException e) {
                        app.getMainWindow().showNotification(e.message, Notification.TYPE_ERROR_MESSAGE);
                    }
                } else {
                    faqService.updateFAQ(
                        (String)(faqForm.getField("question").getValue()),
                        (String)(faqForm.getField("answer").getValue()),
                        (Integer)(faqForm.getField("id").getValue())
                    )
                    faq.fillFAQ()
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
