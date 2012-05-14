import persistance.Roles
import persistance.Users
import persistance.UsersRoles

class BootStrap {
	
	def springSecurityService

    def init = { servletContext ->
		Users marek = new Users(login: "Marcos", passMd5: springSecurityService.encodePassword("marcos5"), state: true, nickname: "Marek Cabaj").save(failOnError: true, flush: true)
		Users radek = new Users(login: "RGabiga", passMd5: springSecurityService.encodePassword("radek"), state: true, nickname: "Radoslaw Gabiga").save(failOnError: true, flush: true)
		Users andrzej = new Users(login: "AKieltyka", passMd5: springSecurityService.encodePassword("andrzej"), state: true, nickname: "Andrzej Kieltyka").save(failOnError: true, flush: true)
		
		Roles admin = new Roles(authority: "ADMIN_ROLE").save(failOnError: true, flush: true)
		Roles moderator = new Roles(authority: "MOD_ROLE").save(failOnError: true, flush: true)
		Roles user = new Roles(authority: "USER_ROLE").save(failOnError: true, flush: true)
		
		new UsersRoles(user: marek, role: admin).save(failOnError: true, flush: true)
		new UsersRoles(user: radek, role: user).save(failOnError: true, flush: true)
		new UsersRoles(user: andrzej, role: moderator).save(failOnError: true, flush: true)
    }
    def destroy = {
    }
}
