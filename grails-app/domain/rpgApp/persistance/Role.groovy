package rpgApp.persistance

class Role {

	String authority

	static mapping = {
		cache true
		version false
		table 'roles'
	}

	static constraints = {
		authority blank: false, unique: true
	}
}
