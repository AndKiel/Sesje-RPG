package rpgApp.persistance

class Notification {

	Boolean role	// true - master , false - player
	Boolean type	// true - invintation, false - acceptation
	
	// Relation
	User sender
	User receiver
	Session session
	
	static mapping = {
		version false
		table 'notifications'
		sender column: 'sender'
		receiver column: 'receiver'
	}
	
	static constraints = {

	}
	
	String toString() {
		return "${id}"
	}
}
