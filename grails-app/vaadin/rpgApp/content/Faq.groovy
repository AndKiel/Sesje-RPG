package rpgApp.content

import rpgApp.data.FAQItem
import rpgApp.main.IndexApplication

import com.vaadin.ui.Label
import com.vaadin.ui.Panel
import com.vaadin.ui.VerticalLayout

class Faq extends VerticalLayout {
    private IndexApplication app
	
    public Faq(IndexApplication app) {
        this.app = app
        setMargin(true)
        setSpacing(true)
		
        refreshFaq()
    }
	
	public void refreshFaq() {
		this.removeAllComponents()
		addComponent(new Label("<b>Frequently Asked Questions</b>", Label.CONTENT_XHTML));
		List<FAQItem> questions = app.faqService.getAllFAQ()
		int i = 0
		for(FAQItem faq in questions)
		{
			i++
			Panel p = new Panel()
			p.addComponent(new Label("<b>"+i+". "+faq.getQuestion()+"</b>", Label.CONTENT_XHTML))
			p.addComponent(new Label(faq.getAnswer(), Label.CONTENT_XHTML))
			addComponent(p)
		}
	}
}
