package persistance
/**
 * The Users entity.
 *
 * @author    
 *
 *
 */
class Users {
	static mapping = {
		table 'users'
		// version is set to false, because this isn't available by default for legacy databases
		version false
		id column:'login', generator:'assigned', type:'string', name: 'login'
	}
	String login
	String passMd5
	String state = "i"
	String level = "0"
	String nickname
	String location
	Date birthday
	String homepage
	Boolean showChars = false
	Boolean showScenarios = false
	Boolean commentNotify = false
	Boolean sessionNotify = false
	Boolean messageNotify = false

	static constraints = {
		login(size: 1..20, blank: false)
		passMd5(size: 1..30, blank: false)
		state(size: 0..1)
		level(size: 0..1)
		nickname(size: 1..30, blank: false, unique: true)
		location(size: 0..20, nullable: true)
		birthday(nullable: true)
		homepage(size: 0..40, nullable: true)
		showChars(nullable: true)
		showScenarios(nullable: true)
		commentNotify(nullable: true)
		sessionNotify(nullable: true)
		messageNotify(nullable: true)
	}
	String toString() {
		return "${login}"
	}
}
