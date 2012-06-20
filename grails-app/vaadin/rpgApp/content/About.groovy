package rpgApp.content

import rpgApp.data.AboutItem
import rpgApp.main.IndexApplication
import rpgApp.services.AboutService;

import com.vaadin.terminal.ThemeResource
import com.vaadin.ui.Alignment
import com.vaadin.ui.Button
import com.vaadin.ui.Button.ClickEvent
import com.vaadin.ui.Button.ClickListener
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout
import com.vaadin.ui.Label
import com.vaadin.ui.RichTextArea
import com.vaadin.ui.VerticalLayout

class About extends VerticalLayout implements ClickListener {
    private IndexApplication app
    private AboutService aboutService
    
    private Form aboutForm = new Form()
    private Button edit
    private Button save
    private Button cancel
	
    public About(IndexApplication app) {
        this.app = app
        aboutService = app.aboutService
        setMargin(true)
        setSpacing(true)
        
        RichTextArea rta = new RichTextArea("About: ");
        rta.setWidth("100%")
        rta.setHeight("300px")
        rta.setRequired(true)
        aboutForm.addField("rta", rta)

        HorizontalLayout footer = new HorizontalLayout()
        footer.setSpacing(true);
        footer.setWidth("100%")
        save = new Button("Save", (Button.ClickListener) this)
        save.setIcon(new ThemeResource("icons/ok.png"))
        cancel = new Button("Cancel", (Button.ClickListener)this)
        cancel.setIcon(new ThemeResource("icons/cancel.png"))
        footer.addComponent(save);
        footer.setComponentAlignment(save, Alignment.MIDDLE_CENTER)
        footer.addComponent(cancel);
        footer.setComponentAlignment(cancel, Alignment.MIDDLE_CENTER)

        aboutForm.setFooter(footer)
        aboutForm.setWidth("100%")

        refreshAbout()
    }
    
    public void refreshAbout() {
        removeAllComponents()
        AboutItem a = aboutService.getAbout(1)
        addComponent(new Label(a.getContent(), Label.CONTENT_XHTML));
        if(app.security.checkRole("Administrator") || app.security.checkRole("Moderator"))
        {
            edit = new Button("Edit", (Button.ClickListener) this)
            edit.setIcon(new ThemeResource("icons/folder.png"))
            addComponent(edit)
        }
    }

    public void buttonClick(ClickEvent clickEvent) {
        final Button source = clickEvent.getButton()
        AboutItem a = aboutService.getAbout(1)
        switch(clickEvent.source){
            case edit:
            removeAllComponents()
            addComponent(aboutForm)

            aboutForm.getField("rta").setValue(a.getContent())

            break
            case save:
            if(aboutForm.isValid())
            {
                aboutService.updateAbout(
                    (String)(aboutForm.getField("rta").getValue()),
                    a.getId()
                )
                refreshAbout()
            }
            break
            case cancel:
            refreshAbout()
            break
        }
    }
}
