package rpgApp.persistance

class Scenario {
	
        String name
	String type
	Integer playersCount
	String content
	
	// Relation
	User owner
	RpgSystem system

	static mapping = {
		version false
		table 'scenarios'
                name column: 'name'
                type column: 'type'
                playersCount column: 'playersCount'
                content column: 'content'
		owner column: 'owner'
		system column: 'system'
	}
	
	static constraints = {
                name()
		type(size: 1..1, blank: false)
		playersCount(max: 10)
		content(maxSize: 10000, blank: false)
		owner()
		system()
	}
	
	String toString() {
		return "${id}"
	}
}
