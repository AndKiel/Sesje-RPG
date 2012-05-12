package persistance
/**
 * The RpgSystems entity.
 *
 * @author    
 *
 *
 */
class RpgSystems {
	static mapping = {
		table 'rpg_systems'
		// version is set to false, because this isn't available by default for legacy databases
		version false
		id generator:'identity', column:'id'
	}
	Long id
	String name
	String description
	String genre
	String designer
	String publisher
	Date year
	String charSheetDtd

	static constraints = {
		id()
		name(size: 1..20, blank: false, unique: true)
		description(nullable: true)
		genre(size: 0..20, nullable: true)
		designer(size: 0..20, nullable: true)
		publisher(size: 0..20, nullable: true)
		year(nullable: true)
		charSheetDtd(nullable: true)
	}
	String toString() {
		return "${id}"
	}
}
