package rpgApp.services

import org.apache.commons.lang.RandomStringUtils

import rpgApp.exeptions.ValidationException
import rpgApp.persistance.Role
import rpgApp.persistance.User
import rpgApp.persistance.UserRole


class UserService {

	static transactional = true
	def emailService
	def securityService
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

	void updatePerson(String loc, Date bday, String home, boolean sC, boolean sS, boolean cN, boolean sN, boolean mN) {
		User u  = securityService.getContextUser()
		String login = u.login
		User.executeUpdate('UPDATE User SET homepage=:homepage, showChars=:sC, showScenarios=:sS, commentNotify=:cN, sessionNotify=:sN, messageNotify=:mN WHERE login=:email', [homepage: home, sC: sC, sS: sS, cN: cN, sN: sN, mN: mN , email: login])
		if(bday != null) {
			User.executeUpdate('UPDATE User SET birthday=:birthday WHERE login=:email', [birthday: bday, email: login])
		} else {
			User.executeUpdate('UPDATE User SET birthday=null WHERE login=:email', [email: login])
		}
		if(loc != null) {
			User.executeUpdate('UPDATE User SET location=:location WHERE login=:email', [location: loc, email: login])
		} else {
			User.executeUpdate('UPDATE User SET location=null WHERE login=:email', [email: login])
		}
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

	// Read data functions
	String getLogin() {
		User u  = securityService.getContextUser()
		return u.getLogin()
	}

	String getNickname() {
		User u  = securityService.getContextUser()
		return u.getNickname()
	}

	Set <String> getRoles() {
		securityService.getContextRoles()
	}
	
	Date getDateCreated() {
		User u  = securityService.getContextUser()
		return u.getDateCreated()
	}

	String getLocation() {
		User u  = securityService.getContextUser()
		return u.getLocation()
	}

	Date getBirthday() {
		User u  = securityService.getContextUser()
		return u.getBirthday()
	}

	String getHomepage() {
		User u  = securityService.getContextUser()
		return u.getHomepage()
	}

	boolean getShowChars() {
		User u  = securityService.getContextUser()
		return u.getShowChars()
	}

	boolean getShowScenarios() {
		User u  = securityService.getContextUser()
		return u.getShowScenarios()
	}

	boolean getCommentNotify() {
		User u  = securityService.getContextUser()
		return u.getCommentNotify()
	}

	boolean getSessionNotify() {
		User u  = securityService.getContextUser()
		return u.getSessionNotify()
	}

	boolean getMessageNotify() {
		User u  = securityService.getContextUser()
		return u.getMessageNotify()
	}
}
