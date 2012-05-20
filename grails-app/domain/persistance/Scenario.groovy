package persistance

class Scenario {
	
	String type
	Integer playersCount
	String content
	
	// Relation
	User owner
	RpgSystem system

	static mapping = {
		version false
		table 'scenarios'
		owner column: 'owner'
		system column: 'system'
	}
	
	static constraints = {
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
