package rpgApp.persistance

class FAQ {
    
    String question
    String answer
    
    static mapping = {
        version false
        table 'faq'
        question column: 'question'
        answer column: 'answer'
    }

    static constraints = {
        question(nullable: false)
        answer(nullable: false)
    }

    String toString(){
        return "${id}"
    }
}
