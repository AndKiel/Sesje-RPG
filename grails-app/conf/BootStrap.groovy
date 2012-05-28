import rpgApp.persistance.Message
import rpgApp.persistance.Role
import rpgApp.persistance.User
import rpgApp.persistance.UserRole


class BootStrap {
	
	def springSecurityService

	def init = { servletContext ->
		User marek = new User(login: "marek.cbj@gmail.com", passMd5: "marek", state: true, nickname: "Marek").save()
		User radek = new User(login: "radek@gmail.com", passMd5: "radek", state: false, nickname: "Radek").save()
		User andrzej = new User(login: "andrzej@gmail.com", passMd5: "andrzej", state: false, nickname: "Andrzej").save()
		User wojtek = new User(login: "wojtek@gmail.com", passMd5: "wojtek", state: false, nickname: "Wojtek").save()
		User adam = new User(login: "adam@gmail.com", passMd5: "adam", state: false, nickname: "Adam").save()
		
		Role admin = new Role(authority: "ADMIN").save()
		Role moderator = new Role(authority: "MOD").save()
		Role user = new Role(authority: "USER").save()
		
		new UserRole(user: marek, role: admin).save()
		new UserRole(user: radek, role: user).save()
		new UserRole(user: andrzej, role: moderator).save()
		new UserRole(user: wojtek, role: user).save()
		new UserRole(user: adam, role: user).save()
	}
	def destroy = {
	}
}