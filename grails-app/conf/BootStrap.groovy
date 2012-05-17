import persistance.Comments
import persistance.Roles
import persistance.Users
import persistance.UsersRoles

class BootStrap {
	
	def springSecurityService

	def init = { servletContext ->
		Users marek = new Users(login: "Marcos", passMd5: springSecurityService.encodePassword("marcos5"), state: true, nickname: "Marek Cabaj", accountExpired: false, accountLocked: false, passwordExpired: false).save(failOnError: true, flush: true)
		Users radek = new Users(login: "RGabiga", passMd5: springSecurityService.encodePassword("radek"), state: true, nickname: "Radoslaw Gabiga", accountExpired: false, accountLocked: false, passwordExpired: false).save(failOnError: true, flush: true)
		Users andrzej = new Users(login: "AKieltyka", passMd5: springSecurityService.encodePassword("andrzej"), state: true, nickname: "Andrzej Kieltyka", accountExpired: false, accountLocked: false, passwordExpired: false).save(failOnError: true, flush: true)
		
		Roles admin = new Roles(authority: "ADMIN").save(failOnError: true, flush: true)
		Roles moderator = new Roles(authority: "MOD").save(failOnError: true, flush: true)
		Roles user = new Roles(authority: "USER").save(failOnError: true, flush: true)
		
		new UsersRoles(users: marek, roles: admin).save(failOnError: true, flush: true)
		new UsersRoles(users: radek, roles: user).save(failOnError: true, flush: true)
		new UsersRoles(users: andrzej, roles: moderator).save(failOnError: true, flush: true)
		
		new Comments(grade: 5, comment: "Jest to komentarz testowy", commentee: marek, commentator: radek).save(failOnError: true, flush: true)
	}
	def destroy = {
	}
}