package rpgApp.services

import rpgApp.data.FAQItem
import rpgApp.persistance.FAQ

class FAQService {
    static transactional = true
    
    List<FAQItem> getAllFAQ(){
        return FAQ.findAll([sort: 'question', order: 'asc']).collect{
            new FAQItem(
                id: it.id,
                question: it.question,
                answer: it.answer
            )
        }
    }
    
    FAQItem getFAQ(Integer id){
        FAQ f = FAQ.get(id)
        return new FAQItem(
            id: id,
            question: f.question,
            answer: f.answer
        )
    }
    
    void createFAQ(String q, String a){
        FAQ f = new FAQ(
            question: q,
            answer: a
        )
        f.save(failOnError: true, flush: true)
    }
    
    void updateFAQ(String q, String a, Long id){
        FAQ.executeUpdate('UPDATE FAQ SET question=:question, answer=:answer WHERE id=:FAQID', [question: q, answer: a, FAQID: id])
    }
    
    void removeFAQ(FAQItem faq){
        FAQ f = FAQ.get(faq.getId())
        if(f)
        {
            f.delete()
        }
    }
}
