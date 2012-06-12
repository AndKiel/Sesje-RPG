package rpgApp.persistance

class News {
    
    Date dateCreated
    String title
    String content
    User author

    static mapping = {
        version false
        table 'news'
        title column: 'title'
        content column: 'content'
        author column: 'author'
    }
    
    static constraints = {
        title(nullable: false)
        content(nullable: false)
        author()
    }
    
    String toString(){
        return "${id}"
    }
}
