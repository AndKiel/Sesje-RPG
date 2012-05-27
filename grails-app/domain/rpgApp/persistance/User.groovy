package rpgApp.persistance

class User {
	
	transient springSecurityService
	
	String login
	String passMd5
	boolean state = true
	
	String nickname
	String location
	Date birthday
	String homepage
	Boolean showChars = false
	Boolean showScenarios = false
	Boolean commentNotify = false
	Boolean sessionNotify = false
	Boolean messageNotify = false

	static mapping = {
		version false
		table 'users'
		id column:'login', generator:'assigned', type:'string', name: 'login'
		passMd5 column: '`passMd5`'
	}
	
	static constraints = {
		login(size: 1..60, blank: false, unique: true, email: true)
		passMd5(size: 1..128, blank: false)
		nickname(size: 1..30, blank: false, unique: true)
		location(size: 0..60, nullable: true)
		birthday(nullable: true)
		homepage(size: 0..40, nullable: true)
		showChars(nullable: true)
		showScenarios(nullable: true)
		commentNotify(nullable: true)
		sessionNotify(nullable: true)
		messageNotify(nullable: true)
	}
	
	Set<Role> getAuthorities() {
		UserRole.findAllByUser(this).collect { it.role } as Set
	}
	
	def beforeInsert() {
		encodePassword()
	}

	def beforeUpdate() {
		if (isDirty('passMd5')) {
			encodePassword()
		}
	}

	protected void encodePassword() {
		passMd5 = springSecurityService.encodePassword(passMd5)
	}
	
	String toString() {
		return "${login}"
	}
}
