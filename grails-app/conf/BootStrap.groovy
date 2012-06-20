import rpgApp.persistance.About
import rpgApp.persistance.FAQ
import rpgApp.persistance.News
import rpgApp.persistance.Participant
import rpgApp.persistance.Role
import rpgApp.persistance.RpgSystem
import rpgApp.persistance.Session
import rpgApp.persistance.User
import rpgApp.persistance.UserRole

class BootStrap {
    def springSecurityService

    def init = { servletContext ->
        User marek = new User(login: "marek@gmail.com", passMd5: "password", state: true, nickname: "Marek", location: "Polska", homepage: "www.onet.pl" ).save()
        User radek = new User(login: "radek@gmail.com", passMd5: "password", state: true, nickname: "Radek", location: "Belgia", homepage: "www.interia.pl" ).save()
        User andrzej = new User(login: "andrzej@gmail.com", passMd5: "password", state: true, nickname: "Andrzej", location: "Indie", homepage: "www.wp.pl" ).save()
        User wojtek = new User(login: "wojtek@gmail.com", passMd5: "password", state: true, nickname: "Wojtek", location: "Egipt", homepage: "www.stosowana.pl" ).save()
        User adam = new User(login: "adam@gmail.com", passMd5: "password", state: true, nickname: "Adam", location: "Polska", homepage: "www.kwejk.pl" ).save()
        User krzysztof = new User(login: "krzysztof@gmail.com", passMd5: "password", state: true, nickname: "Krzysztof", location: "Francja", homepage: "www.stosowana.pl" ).save()
        User piotrek = new User(login: "piotrek@gmail.com", passMd5: "password", state: true, nickname: "Piotrek", location: "Polska", homepage: "www.kwejk.pl" ).save()
		
        Role admin = new Role(authority: "Administrator").save()
        Role moderator = new Role(authority: "Moderator").save()
        Role user = new Role(authority: "User").save()
		
        new UserRole(user: marek, role: admin).save()
        new UserRole(user: radek, role: user).save()
        new UserRole(user: andrzej, role: moderator).save()
        new UserRole(user: wojtek, role: user).save()
        new UserRole(user: adam, role: user).save()
        new UserRole(user: krzysztof, role: user).save()
        new UserRole(user: piotrek, role: user).save()

        RpgSystem s1 = new RpgSystem(name: "Call of Cthulhu", year: 1981, genre: "Horror", designer: "Sandy Petersen", publisher: "Chaosium", description: "The definitive horror roleplaying game, based on the writing of H.P. Lovecraft and other contributors to the Lovecraft mythos, such as August Derleth, Brian Lumley, and Ramsey Campbell.").save()		
        RpgSystem s2 = new RpgSystem(name: "Dungeons & Dragons", year: 1974, genre: "Fantasy", designer: "Gary Gygax, Dave Arneson", publisher: "TSR, Wizards of the Coast", description: "The Dungeons & Dragons Roleplaying Game has defined the medieval fantasy genre and the tabletop RPG industry for more than 30 years. In the D&D game, players create characters that band together to explore dungeons, slay monsters, and find treasure.").save()
        RpgSystem s3 = new RpgSystem(name: "Vampire: The Masquerade", year: 1991, genre: "Horror", designer: "Mark Rein·Hagen", publisher: "White Wolf Publishing", description: "The game of personal horror, where you pretend to be vampires in modern times.").save()
        RpgSystem s4 = new RpgSystem(name: "Monastyr", year: 2004, genre: "Dark fantasy", designer: "I. Trzewiczek, M. Oracz, M. Blacha", publisher: "Wydawnictwo Portal", description: "Monastyr is a Polish role-playing game set in a dark fantasy world of Dominium. Its setting features a fantastic equivalent of the Age of Enlightenment. Most often, the game plays in a cloak and dagger mood and involves plotting, intrigue, but also struggle in the name of honour. The game has been inspired by such authors as Alexandre Dumas, père, Michael Moorcock and H. P. Lovecraft.").save()
		
        Session ses = new Session(type: "offline", location: "Kraków", maxPlayers: 6, timeStamp: new Date(2012-1900, 6, 20, 14,30), owner: marek, system: s2).save(failOnError: true)
        Session ses2 = new Session(type: "offline", location: "Warszawa", maxPlayers: 8, timeStamp: new Date(2012-1900, 7, 23, 17,30), owner: radek, system: s1).save(failOnError: true)
        Session ses3 = new Session(type: "offline", location: "Gdańsk", maxPlayers: 7, timeStamp: new Date(2012-1900, 7, 24, 18,00), owner: wojtek, system: s3).save(failOnError: true)
        Session ses4 = new Session(type: "offline", location: "Kraków", maxPlayers: 5, timeStamp: new Date(2012-1900, 9, 21, 14,30), owner: marek, system: s3).save(failOnError: true)
        Session ses5 = new Session(type: "online", location: "online", maxPlayers: 5, timeStamp: new Date(2012-1900, 6, 23, 17,30), owner: radek, system: s4).save(failOnError: true)
        Session ses6 = new Session(type: "online", location: "online", maxPlayers: 7, timeStamp: new Date(2012-1900, 6, 31, 15,00), owner: marek, system: s2).save(failOnError: true)
        Session ses7 = new Session(type: "offline", location: "Kraków", maxPlayers: 3, timeStamp: new Date(2012-1900, 6, 30, 17,30), owner: marek, system: s1).save(failOnError: true)

        new News(title: "Website Start", content: "Today at long last website start", author: marek).save()
        new News(title: "Updates", content: "First updates", author: marek).save()
        
        new FAQ(question: "Jaki jest sens życia?", answer: "42").save()
        new FAQ(question: "O'rly?", answer: "Ya'rly").save()
        
        new About(content: "<b>About not set.</b>").save()
    }
        
    def destroy = {
    }
}