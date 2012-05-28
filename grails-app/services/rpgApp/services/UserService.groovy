package rpgApp.services

import org.apache.commons.lang.RandomStringUtils

import rpgApp.exeptions.ValidationException
import rpgApp.persistance.Role
import rpgApp.persistance.User
import rpgApp.persistance.UserRole


class UserService {

	static transactional = true
	def emailService
	def springSecurityService

	void createPerson(String lgn, String pass, String nick, String loc, Date bday, String home) {
		User u = new User(
				login: lgn,
				passMd5: pass,
				state: false,
				nickname: nick,
				location: loc,
				birthday: bday,
				homepage: home
				)

		if(!u.validate()) {
			if(u.errors.getFieldError("login")) {
				throw new ValidationException("Login is already taken")
				return
			}
			if(u.errors.getFieldError("nickname")) {
				throw new ValidationException("Nickname is already taken")
				return
			}
		} else {
			u.save(failOnError: true)
		}
		
		// Adding USER role to newly created user
		Role user = Role.findByAuthority("USER")
		new UserRole(user: u, role: user).save()
	}
	
	List<String> getAllUsersNicknames() {
		return User.findAll().collect {
			new String(it.nickname)
		}
	}
	
	boolean resetPassword(String login) {
		User u = User.get(login)
		if(u) {
			String newPass = RandomStringUtils.randomAlphanumeric(8);
			User.executeUpdate('UPDATE User SET passMd5=:newpass WHERE login=:email', [newpass: springSecurityService.encodePassword(newPass),email: login])
			emailService.sendPasswordResetMail(login, newPass)
			return true
		} else {
			return false
		}
	}
	
	void activateAccount(String login) {
		User.executeUpdate('UPDATE User SET state=true WHERE login=:email', [email: login])
	}
	
	String getEncodedPassword(String login) {
		User u = User.get(login)
		if(u) {
			return u.getPassMd5()
		} else {
			return null
		}
	}
	
	boolean getState(String login) {
		User u = User.get(login)
		if(u) {
			return u.getState()
		}
	}
}
