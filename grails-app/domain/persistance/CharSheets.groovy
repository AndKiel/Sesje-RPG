package persistance
/**
 * The CharSheets entity.
 *
 * @author    
 *
 *
 */
class CharSheets {
	static mapping = {
		table 'char_sheets'
		// version is set to false, because this isn't available by default for legacy databases
		version false
		id generator:'identity', column:'id'
		owner column: 'owner'
		system column: 'system'
	}
	Long id
	String xmlData
	// Relation
	Users owner
	// Relation
	RpgSystems system

	static constraints = {
		id()
		xmlData()
		owner()
		system()
	}
	String toString() {
		return "${id}"
	}
}
