package rpgSession

import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder as SCH

import persistance.Users


class SecurityService {

    static transactional = true

    def springSecurityService
    def authenticationManager

    void signIn(String username, String password) {
        try {
            def authentication = new UsernamePasswordAuthenticationToken(username, password)
            SCH.context.setAuthentication(authenticationManager.authenticate(authentication))
        } catch (BadCredentialsException e) {
            throw new SecurityServiceException("Invalid username/password")
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
		Users user = Users.get(login)
		return user.getNickname()
	}
}
