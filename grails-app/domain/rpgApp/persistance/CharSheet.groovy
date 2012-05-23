package rpgApp.persistance

class CharSheet {
	
	String xmlData

	// Relations
	User owner
	RpgSystem system

	static mapping = {
		version false
		table 'char_sheets'
		owner column: 'owner'
		system column: 'system'
	}
	
	static constraints = {
		xmlData()
		owner()
		system()
	}
	
	String toString() {
		return "${id}"
	}
}
