package rpgApp.persistance

class Message {

	Date dateCreated
	String topic
	String content
	Boolean wasRead = false

	// Relation
	User sender
	User addressee

	static mapping = {
		version false
		table 'messages'
		sender column: 'sender'
		addressee column: 'addressee'
	}
	
	static constraints = {
		topic(size: 0..20, nullable: true)
		content(blank: false)
		wasRead(nullable: true)
		sender()
		addressee()
	}
	
	String toString() {
		return "${id}"
	}
}
