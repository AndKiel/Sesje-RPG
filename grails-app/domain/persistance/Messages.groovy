package persistance
/**
 * The Messages entity.
 *
 * @author    
 *
 *
 */
class Messages {
	static mapping = {
		table 'messages'
		// version is set to false, because this isn't available by default for legacy databases
		version false
		id generator:'identity', column:'id'
		sender column: 'sender'
		addressee column: 'addressee'
	}
	Long id
	Date dateCreated
	String topic
	String content
	Boolean wasRead = false
	// Relation
	Users sender
	// Relation
	Users addressee

	static constraints = {
		id()
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
