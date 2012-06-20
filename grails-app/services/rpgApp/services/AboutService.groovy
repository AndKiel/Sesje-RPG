package rpgApp.services

import rpgApp.data.AboutItem
import rpgApp.persistance.About

class AboutService {
    static transactional = true
    
    AboutItem getAbout(Integer id){
        About a = About.get(id)
        return new AboutItem(
            id: id,
            content: a.content
        )
    }
    
    void updateAbout(String c, Long i){
        About.executeUpdate('UPDATE About SET content=:content WHERE id=:id', [content: c, id: i])
    }
}
