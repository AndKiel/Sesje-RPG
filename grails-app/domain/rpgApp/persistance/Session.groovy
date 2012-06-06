package rpgApp.persistance

class Session {
	
	Date dateCreated
	Date timeStamp
	String type
	String location
	Integer maxPlayers
	
	// Relation
	User owner
	RpgSystem system

	static mapping = {
		version false
		table 'sessions'
		owner column: 'owner'
		system column: 'system'
	}
	
	static constraints = {
		timeStamp()
		type(size: 1..7, blank: false)
		location(size: 0..40, nullable: true)
		owner()
		system()
	}
	
	String toString() {
		return "${id}"
	}
}
