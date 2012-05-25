package rpgApp.services

import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.DisabledException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder as SCH

import rpgApp.exeptions.SecurityServiceException
import rpgApp.persistance.Role
import rpgApp.persistance.User

class SecurityService {

	static transactional
	
	def springSecurityService
	def authenticationManager

	void signIn(String username, String password) {
		try {
			def authentication = new UsernamePasswordAuthenticationToken(username, password)
			SCH.context.authentication = authenticationManager.authenticate(authentication)
		} catch (BadCredentialsException e) {
			throw new SecurityServiceException("Invalid login or password")
		} catch (DisabledException e) {
			throw new SecurityServiceException("This account is disabled")
		}
	}

	void signOut(){
		SCH.context.authentication = null
	}

	boolean isSignedIn(){
		return springSecurityService.isLoggedIn()
	}

	String getContextNickname() {
		String login = SCH.getContext().getAuthentication().name
		User user = User.get(login)
		if(user == null) {
			return ""
		} else {
			return user.getNickname()
		}
	}
	
	User getContextUser() {
		String login = SCH.getContext().getAuthentication().name
		return User.get(login)
	}

	boolean checkRole(String role) {
		Set<String> roles = getContextRoles()
		if(role in roles) {
			return true
		}
		else {
			return false
		}
	}

	private Set<String> getContextRoles() {
		String login = SCH.getContext().getAuthentication().name

		if(login.equals("anonymousUser")) {
			Set<String> roles = ["GUEST"]
			return roles
		} else {
			User user = User.get(login)
			Set<Role> roles = user.getAuthorities()
			Set<String> roleNames = []
			for(role in roles) {
				roleNames.add(role.getAuthority())
			}

			return roleNames
		}
	}
}


