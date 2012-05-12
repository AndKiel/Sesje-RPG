package persistance
/**
 * The Comments entity.
 *
 * @author    
 *
 *
 */
class Comments {
	static mapping = {
		table 'comments'
		// version is set to false, because this isn't available by default for legacy databases
		version false
		id generator:'identity', column:'id'
		commentee column: 'commentee'
		commentator column: 'commentator'
	}
	Long id
	Integer grade
	String comment
	Date dateCreated
	// Relation
	Users commentee
	// Relation
	Users commentator

	static constraints = {
		id()
		grade(max: 99999)
		comment(blank: false)
		commentee()
		commentator()
	}
	String toString() {
		return "${id}"
	}
}
