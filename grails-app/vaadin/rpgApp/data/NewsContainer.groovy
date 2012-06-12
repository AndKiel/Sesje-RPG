package rpgApp.data

import com.vaadin.data.util.BeanItemContainer
import rpgApp.services.NewsService

class NewsContainer extends BeanItemContainer<NewsItem> implements Serializable {
    private NewsService newsService

    public static final Object[] NATURAL_COL_ORDER = [
		"id",
		"title",
		"dateCreated",
		"author"
    ]

    public static final String[] COL_HEADERS_ENGLISH = [
		"ID",
		"Title",
		"Date",
		"Author",
    ];

    public NewsContainer(NewsService newsService) throws InstantiationException, IllegalAccessException {
        super(NewsItem.class)
        this.newsService = newsService
    }

    void fillContainer() {
        removeAllItems()
        addAll(newsService.getAllNews())
    }

    boolean removeNews(NewsItem news) {
        try {
            newsService.removeNews(news)
            return true
        } catch (Exception e) {
            println e
            return false
        }
    }
}
