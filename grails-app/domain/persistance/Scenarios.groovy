package persistance
/**
 * The Scenarios entity.
 *
 * @author    
 *
 *
 */
class Scenarios {
	static mapping = {
		table 'scenarios'
		// version is set to false, because this isn't available by default for legacy databases
		version false
		id generator:'identity', column:'id'
		owner column: 'owner'
		system column: 'system'
	}
	Long id
	String type
	Integer playersCount
	String content
	// Relation
	Users owner
	// Relation
	RpgSystems system

	static constraints = {
		id()
		type(size: 1..1, blank: false)
		playersCount(max: 99999)
		content(blank: false)
		owner()
		system()
	}
	String toString() {
		return "${id}"
	}
}
