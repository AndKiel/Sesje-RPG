import rpgApp.persistance.Participant
import rpgApp.persistance.Role
import rpgApp.persistance.RpgSystem
import rpgApp.persistance.Session
import rpgApp.persistance.User
import rpgApp.persistance.UserRole


class BootStrap {
	
	def springSecurityService

	def init = { servletContext ->
		User marek = new User(login: "marek@gmail.com", passMd5: "marek", state: true, nickname: "Marek", location: "Polska", homepage: "www.onet.pl" ).save()
		User radek = new User(login: "radek@gmail.com", passMd5: "radek", state: true, nickname: "Radek", location: "Belgia", homepage: "www.interia.pl" ).save()
		User andrzej = new User(login: "andrzej@gmail.com", passMd5: "andrzej", state: true, nickname: "Andrzej", location: "Indie", homepage: "www.wp.pl" ).save()
		User wojtek = new User(login: "wojtek@gmail.com", passMd5: "wojtek", state: true, nickname: "Wojtek", location: "Egipt", homepage: "www.stosowana.pl" ).save()
		User adam = new User(login: "adam@gmail.com", passMd5: "adam", state: true, nickname: "Adam", location: "Polska", homepage: "www.kwejk.pl" ).save()
		
		Role admin = new Role(authority: "Administrator").save()
		Role moderator = new Role(authority: "Moderator").save()
		Role user = new Role(authority: "User").save()
		
		new UserRole(user: marek, role: admin).save()
		new UserRole(user: radek, role: user).save()
		new UserRole(user: andrzej, role: moderator).save()
		new UserRole(user: wojtek, role: user).save()
		new UserRole(user: adam, role: user).save()

		RpgSystem s1 = new RpgSystem(name: "System testowy", year: 2010).save()		
		RpgSystem s2 = new RpgSystem(name: "System testowy2", year: 2012).save()
		
		Session ses = new Session(type: "offline", location: "Krakow", maxPlayers: 6, timeStamp: new Date(2012-1900, 6, 20, 14,30), owner: marek, system: s1).save(failOnError: true)
		Session ses2 = new Session(type: "offline", location: "Warszawa", maxPlayers: 8, timeStamp: new Date(2012-1900, 7, 23, 17,30), owner: radek, system: s1).save(failOnError: true)
		Session ses3 = new Session(type: "offline", location: "Warszawa", maxPlayers: 7, timeStamp: new Date(2012-1900, 7, 24, 18,00), owner: marek, system: s2).save(failOnError: true)
		
		new Participant(user: marek, session: ses, role: true, state: true).save(failOnError: true)
		new Participant(user: marek, session: ses2, role: false, state: true).save(failOnError: true)
		new Participant(user: radek, session: ses3, role: false, state: true).save(failOnError: true)
		new Participant(user: wojtek, session: ses3, role: false, state: true).save(failOnError: true)
	}
	def destroy = {
	}
}