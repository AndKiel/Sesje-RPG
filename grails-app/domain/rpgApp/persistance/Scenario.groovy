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
        id generator: 'increment'
        name column: 'name'
        type column: 'type'
        playersCount column: 'playersCount'
        content column: 'content'
        owner column: 'owner'
        system column: 'system'
    }
	
    static constraints = {
        name()
        type(size: 1..8, blank: false)
        playersCount(max: 10)
        content(maxSize: 10000, blank: false)
        owner()
        system()
    }
	
    String toString() {
        return "${id}"
    }
}
