package persistance

import org.apache.commons.lang.builder.HashCodeBuilder

class UsersRoles implements Serializable {

	Users user
	Roles role

	boolean equals(other) {
		if (!(other instanceof UsersRoles)) {
			return false
		}

		other.user?.id == user?.id &&
			other.role?.id == role?.id
	}

	int hashCode() {
		def builder = new HashCodeBuilder()
		if (user) builder.append(user.id)
		if (role) builder.append(role.id)
		builder.toHashCode()
	}

	static UsersRoles get(long userId, long roleId) {
		find 'from UserRole where user.id=:userId and role.id=:roleId',
			[userId: userId, roleId: roleId]
	}

	static UsersRoles create(Users user, Roles role, boolean flush = false) {
		new UsersRoles(user: user, role: role).save(flush: flush, insert: true)
	}

	static boolean remove(Users user, Roles role, boolean flush = false) {
		UsersRoles instance = UsersRoles.findByUserAndRole(user, role)
		instance ? instance.delete(flush: flush) : false
	}

	static void removeAll(Users user) {
		executeUpdate 'DELETE FROM UserRole WHERE user=:user', [user: user]
	}

	static void removeAll(Roles role) {
		executeUpdate 'DELETE FROM UserRole WHERE role=:role', [role: role]
	}

	static mapping = {
		id composite: ['role', 'user']
		version false
	}
}