package rpgSession

import grails.plugins.springsecurity.Secured
import persistance.Comments

class CommentService {

    static transactional = true
	
	@Secured(["ADMIN"])
	String getComment(int id) {
		return Comments.get(id).getComment()
	}
}
