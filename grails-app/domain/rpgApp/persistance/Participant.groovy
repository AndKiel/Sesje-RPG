package rpgApp.persistance

class Participant implements Serializable {
	Boolean role	// true - master , false - player
	Boolean state = false

	// Relation
	User user
	Session session

	static mapping = {
		version false
		table 'participants'
		id composite: ['user', 'session'], generator:'assigned'
	}
	
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
