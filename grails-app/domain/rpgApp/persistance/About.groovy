package rpgApp.persistance

class About {
    
    String content

    static mapping = {
        version false
        table 'about'
        id generator: 'increment'
        content column: 'content'
    }
    
    static constraints = {
        content(maxSize: 10000, nullable: false)
    }

    String toString(){
        return "${id}"
    }
}
