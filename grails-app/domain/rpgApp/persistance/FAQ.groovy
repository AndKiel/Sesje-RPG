package rpgApp.persistance

class FAQ {
    
    String question
    String answer
    
    static mapping = {
        version false
        table 'faq'
		id generator: 'increment'
        question column: 'question'
        answer column: 'answer'
    }

    static constraints = {
        question(nullable: false)
        answer(maxSize: 5000, nullable: false)
    }

    String toString(){
        return "${id}"
    }
}
