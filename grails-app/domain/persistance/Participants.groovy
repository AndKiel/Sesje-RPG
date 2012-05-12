package persistance
/**
 * The Participants entity.
 *
 * @author    
 *
 *
 */
class Participants {
	static mapping = {
		table 'participants'
		// version is set to false, because this isn't available by default for legacy databases
		version false
		id generator:'identity', column:'id'
		login column: 'login'
		session column: 'session'
	}
	Long id
	Boolean role
	Boolean state = "false"
	// Relation
	Users login
	// Relation
	Sessions session

	static constraints = {
		role()
		state(nullable: true)
		login()
		session()
	}
	String toString() {
		return "${session}"
	}
}
