package rpgApp.persistance

class Session {
	
	Date dateCreated
	Date timeStamp
	String type
	String location
	
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
		type(size: 1..1, blank: false)
		location(size: 0..20, nullable: true)
		owner()
		system()
	}
	
	String toString() {
		return "${id}"
	}
}
