package persistance
/**
 * The Sessions entity.
 *
 * @author    
 *
 *
 */
class Sessions {
	static mapping = {
		table 'sessions'
		// version is set to false, because this isn't available by default for legacy databases
		version false
		id generator:'identity', column:'id'
		owner column: 'owner'
		system column: 'system'
	}
	Long id
	Date dateCreated
	Date timeStamp
	String type
	String location
	// Relation
	Users owner
	// Relation
	RpgSystems system

	static constraints = {
		id()
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
