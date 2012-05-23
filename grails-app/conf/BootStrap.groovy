import rpgApp.persistance.Role
import rpgApp.persistance.User
import rpgApp.persistance.UserRole


class BootStrap {
	
	def springSecurityService

	def init = { servletContext ->
		User marek = new User(login: "marek.cbj@gmail.com", passMd5: "marek", state: true, nickname: "Marek Cabaj").save(failOnError: true, flush: true)
		User radek = new User(login: "radoslaw.gabiga@gmail.com", passMd5: "radek", state: false, nickname: "Radoslaw Gabiga").save(failOnError: true, flush: true)
		User andrzej = new User(login: "a.kieltyka@gmail.com", passMd5: "andrzej", state: true, nickname: "Andrzej Kieltyka").save(failOnError: true, flush: true)
		
		Role admin = new Role(authority: "ADMIN").save(failOnError: true, flush: true)
		Role moderator = new Role(authority: "MOD").save(failOnError: true, flush: true)
		Role user = new Role(authority: "USER").save(failOnError: true, flush: true)
		
		new UserRole(user: marek, role: admin).save(failOnError: true, flush: true)
		new UserRole(user: marek, role: user).save(failOnError: true, flush: true)
		new UserRole(user: marek, role: moderator).save(failOnError: true, flush: true)
		new UserRole(user: radek, role: user).save(failOnError: true, flush: true)
		new UserRole(user: andrzej, role: moderator).save(failOnError: true, flush: true)
	}
	def destroy = {
	}
}