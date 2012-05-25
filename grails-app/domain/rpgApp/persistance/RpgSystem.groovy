package rpgApp.persistance

class RpgSystem {

	String name
	String description
	String genre
	String designer
	String publisher
	Date year
	String charSheetDtd

	static mapping = {
		version false
		table 'rpg_systems'
	}
	
	static constraints = {
		name(size: 1..20, blank: false, unique: true)
		description(maxSize: 5000, nullable: true)
		genre(size: 0..20, nullable: true)
		designer(size: 0..20, nullable: true)
		publisher(size: 0..20, nullable: true)
		year(nullable: true)
		charSheetDtd(maxSize: 5000, nullable: true)
	}
	
	String toString() {
		return "${id}"
	}
}
