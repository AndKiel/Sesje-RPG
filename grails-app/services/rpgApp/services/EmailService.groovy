package rpgApp.services

class EmailService {

	def mailService
	def springSecurityService

	public void sendActivationMail(String email, String pass, String path) {
		mailService.sendMail {
			to email
			subject "Activation link from RPG Sessions"
			html "To activate your account: <b>"+email+"</b> click the link below. <br/><br/>"+path+"activation?email="+email+"&code="+springSecurityService.encodePassword(pass)
		}
	}
	
	public void sendPasswordResetMail(String email, String newPass) {
		mailService.sendMail {
			to email
			subject "Password reset from RPG Sessions"
			html "Your new password to account: <b>"+email+"</b> <br/><br/> New password :"+newPass
		}
	}
}
