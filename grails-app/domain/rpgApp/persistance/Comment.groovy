package rpgApp.persistance

class Comment {

	Integer grade
	String comment
	Date dateCreated

	// Relation
	User commentee
	User commentator

	static mapping = {
		version false
		table 'comments'
		commentee column: 'commentee'
		commentator column: 'commentator'
	}
	
	static constraints = {
		grade(max: 99999)
		comment(blank: false)
		commentee()
		commentator()
	}
	
	String toString() {
		return "${id}"
	}
}
