package rpgApp.services

import org.apache.commons.lang.RandomStringUtils

import rpgApp.data.UserItem
import rpgApp.exeptions.ValidationException
import rpgApp.persistance.Role
import rpgApp.persistance.User
import rpgApp.persistance.UserRole


class UserService {

	static transactional = true
	def emailService
	def securityService
	def springSecurityService
	
	List<UserItem> getAllUsers() {
		return User.findAll([sort: 'nickname', order:'asc']).collect {
			Set<Role> roles = it.getAuthorities()
			Set<String> roleNames = []
			for(role in roles) {
				roleNames.add(role.getAuthority())
			}
			new UserItem(
				login: it.login,
				state: it.state,
				roles: roleNames,
				dateCreated: it.dateCreated,
				nickname: it.nickname,
				location: it.location,
				birthday: it.birthday,
				homepage: it.homepage
				)
		}
	}
	
	List<UserItem> getLastUsers() {
		int maximum = 6
		if(User.count() < 6) {
			maximum = User.count()
		}
		return User.findAll([sort: 'dateCreated', order:'desc', max: maximum]).collect {
			Set<Role> roles = it.getAuthorities()
			Set<String> roleNames = []
			for(role in roles) {
				roleNames.add(role.getAuthority())
			}
			new UserItem(
				login: it.login,
				state: it.state,
				roles: roleNames,
				dateCreated: it.dateCreated,
				nickname: it.nickname,
				location: it.location,
				birthday: it.birthday,
				homepage: it.homepage
				)
		}
	}

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
			u.save(failOnError: true, flush: true)
		}

		// Adding USER role to newly created user
		Role user = Role.findByAuthority("USER")
		new UserRole(user: u, role: user).save()
	}

	void updatePerson(String loc, Date bday, String home, boolean sC, boolean sS, boolean cN, boolean sN, boolean mN) {
		User u  = securityService.getContextUser()
		String login = u.login
		User.executeUpdate('UPDATE User SET showChars=:sC, showScenarios=:sS, commentNotify=:cN, sessionNotify=:sN, messageNotify=:mN WHERE login=:email', [sC: sC, sS: sS, cN: cN, sN: sN, mN: mN , email: login])
		if(loc != null) {
			User.executeUpdate('UPDATE User SET location=:location WHERE login=:email', [location: loc, email: login])
		} else {
			User.executeUpdate('UPDATE User SET location=null WHERE login=:email', [email: login])
		}
		if(bday != null) {
			User.executeUpdate('UPDATE User SET birthday=:birthday WHERE login=:email', [birthday: bday, email: login])
		} else {
			User.executeUpdate('UPDATE User SET birthday=null WHERE login=:email', [email: login])
		}
		if(home != null) {
			User.executeUpdate('UPDATE User SET homepage=:homepage WHERE login=:email', [homepage: home, email: login])
		} else {
			User.executeUpdate('UPDATE User SET homepage=null WHERE login=:email', [email: login])
		}

	}

	List<String> getAllUsersNicknames() {
		return User.findAll([sort: 'nickname', order:'asc']).collect() {
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
		setState(login, true)
	}

	String getEncodedPassword(String login) {
		User u = User.get(login)
		if(u) {
			return u.getPassMd5()
		} else {
			return null
		}
	}
	
	void setState(String login, boolean state) {
		User.executeUpdate('UPDATE User SET state=:state WHERE login=:email', [state: state, email: login])
	}

	// Read data functions
	boolean getState() {
		User u  = securityService.getContextUser()
		return u.getState()
	}

	String getLogin() {
		User u  = securityService.getContextUser()
		return u.getLogin()
	}

	String getNickname() {
		User u  = securityService.getContextUser()
		return u.getNickname()
	}

	Set<String> getRoles() {
		return securityService.getContextRoles()
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
