package rpgApp.services

import rpgApp.data.NewsItem
import rpgApp.exeptions.ValidationException
import rpgApp.persistance.News
import rpgApp.persistance.User

class NewsService {
    def securityService
    static transactional = true
    
    List<NewsItem> getAllNews(){
        return News.findAll([sort: 'dateCreated', order: 'dsc']).collect{
            new NewsItem(
                id: it.id,
                dateCreated: it.dateCreated,
                title: it.title,
                content: it.content,
                author: it.author
            )
        }
    }
    
    List<NewsItem> getLatestNews(){
        int maximum = 3;
        if(News.count() < 3)
        {
            maximum = News.count();
        }
        return News.findAll([sort: 'dateCreated', order: 'dsc', max: maximum]).collect{
            new NewsItem(
                id: it.id,
                dateCreated: it.dateCreated,
                title: it.title,
                content: it.content,
                author: it.author
            )
        }
    }

    NewsItem getNews(Integer id){
        News n = News.get(id)
        return new NewsItem(
            id: id,
            dateCreated: n.dateCreated,
            title: n.title,
            content: n.content,
            author: n.author
        )
    }
    
    void createNews(String t, String c){
        User a = securityService.getContextUser()
        News n = new News(
            title: t,
            content: c,
            author: a
        )
        n.save(failOnError: true, flush: true)
    }
    
    void updateNews(String t, String c, Long id){
        News.executeUpdate('UPDATE News SET title=:title, content=:content WHERE id=:newsID', [title: t, content: c, newsID: id])
    }
    
    void removeNews(NewsItem news){
        News n = News.get(news.getId())
        if(n)
        {
            n.delete()
        }
    }
}
