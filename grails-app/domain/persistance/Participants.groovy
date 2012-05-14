package persistance
/**
 * The Participants entity.
 *
 * @author    
 *
 *
 */
class Participants implements Serializable {
	static mapping = {
		table 'participants'
		// version is set to false, because this isn't available by default for legacy databases
		version false
		id composite: ['user', 'session'], generator:'assigned'
	}
	Boolean role
	Boolean state = "false"
	// Relation
	Users user
	// Relation
	Sessions session

	static constraints = {
		role()
		state(nullable: true)
		user()
		session()
	}
	String toString() {
		return "${session}"
	}
}
