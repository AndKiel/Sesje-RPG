package rpgApp.data

import com.vaadin.data.util.BeanItemContainer
import rpgApp.services.FAQService

class FAQContainer extends BeanItemContainer<NewsItem> implements Serializable {
    private FAQService faqService

    public static final Object[] NATURAL_COL_ORDER = [
		"id",
		"question"
    ]

    public static final String[] COL_HEADERS_ENGLISH = [
		"ID",
		"Question"
    ];

    public FAQContainer(FAQService faqService) throws InstantiationException, IllegalAccessException {
        super(FAQItem.class)
        this.faqService = faqService
    }

    void fillContainer() {
        removeAllItems()
        addAll(faqService.getAllFAQ())
    }

    boolean removeFAQ(FAQItem faq) {
        try {
            faqService.removeFAQ(faq)
            return true
        } catch (Exception e) {
            println e
            return false
        }
    }
}
