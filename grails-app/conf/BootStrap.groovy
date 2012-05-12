import persistance.Messages
import persistance.Users

class BootStrap {

    def init = { servletContext ->
		Users u1 = new Users(login: "Mareczek", passMd5: "testowe", nickname: "Marcos")
		u1.save(failOnError: true, flush: true)
		Messages m1 = new Messages(addressee: u1, sender: u1, topic: "Jest fajnie",content: "Bardzo fajnie jest")
		m1.save(failOnError: true, flush: true)
    }
    def destroy = {
    }
}
