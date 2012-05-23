package rpgApp.services

import rpgApp.exeptions.ValidationException
import rpgApp.persistance.User


class UserService {

	static transactional = true

	void createPerson(String lgn, String pass, String nick, String loc, Date bday, String home) {
		User u = new User(
				login: lgn,
				passMd5: pass,
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
			u.save(flush: true)
		}
	}
}
