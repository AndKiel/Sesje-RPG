package rpgApp.services

import rpgApp.data.SessionItem
import rpgApp.persistance.Notification
import rpgApp.persistance.Participant
import rpgApp.persistance.RpgSystem
import rpgApp.persistance.Session
import rpgApp.persistance.User



class SessionService {

	static transactional = true
	def securityService

	List<SessionItem> getAllSessions() {
		return Session.findAll([sort: 'dateCreated', order:'desc']).collect {
			new SessionItem(
					id: it.id,
					dateCreated: it.dateCreated,
					timeStamp: it.timeStamp,
					type: it.type,
					location: it.location,
					maxPlayers: it.maxPlayers,
					owner: it.owner.nickname,
					system: it.system.name,
					)
		}
	}

	void createSession(Date ts, String type, String loc, Integer mP, String sysName, String role) {
		User owner = securityService.getContextUser()
		RpgSystem system = RpgSystem.findByName(sysName)
		Session s = new Session(
				timeStamp: ts,
				type: type,
				location: loc,
				maxPlayers: mP,
				owner: owner,
				system: system,
				)

		s.save(failOnError: true)

		if(role.equals("Master")) {
			new Participant(user: owner, session: s, role: true, state: true).save()
		} else if(role.equals("Player")) {
			new Participant(user: owner, session: s, role: false, state: true).save()
		}
	}

	// 0 - not member , 1 - member, 2 - waiting
	int checkMembership(Integer id) {
		User contextUser = securityService.getContextUser()
		Session sessionS = Session.get(id)
		Participant p = Participant.findByUserAndSession(contextUser, sessionS)
		if(p) {
			if(p.getState() == true) {
				return 1
			} else {
				return 2
			}
		}

		return 0
	}

	Integer participantsCount(Integer id) {
		Session sessionS = Session.get(id)
		return Participant.countBySessionAndState(sessionS, true)
	}

	String getMaster(Integer id)  {
		Session sessionS = Session.get(id)
		Participant p = Participant.findBySessionAndRole(sessionS, true)
		if(p) {
			if(p.getState() == true) {
				return "<b>"+p.getUser().getNickname()+"</b>"
			}
		}

		return "<font size=2>(empty slot)</font>"
	}

	List<String> getPlayers(Integer id) {
		List<String> players = []
		Session sessionS = Session.get(id)
		Participant.findAllBySessionAndRole(sessionS, false).collect() {
			if(it.getState() == true) {
				players.add(it.getUser().getNickname())
			}
		}

		return players
	}
	
	void createNotification(String to, Integer id, Boolean type, Boolean role) {
		User contextUser = securityService.getContextUser()
		User rec = User.findByNickname(to)
		Session sessionS = Session.get(id)
		new Notification(sender: contextUser, receiver: rec, session: sessionS, type: type, role: role).save(failOnError: true)
	}
	
	void createParticipant(Integer id, Boolean role, Boolean state) {
		User u = securityService.getContextUser()
		new Participant(
			user: u,
			session: Session.get(id),
			role: role,
			state: state,
			).save(failOnError: true)
		
	}
	
	void setParticipantActive(String who, Integer sessionId) {
		User u = User.findByNickname(who)
		Session s = Session.get(sessionId)
		Participant p = Participant.findByUserAndSession(u,s)
		p.setState(true)
		p.save()
	}
	
	void deleteParticipant(String who, Integer sessionId) {
		User u = User.findByNickname(who)
		Session s = Session.get(sessionId)
		Participant p = Participant.findByUserAndSession(u,s)
		p.delete()
	}
	
	void playerLeave(Integer id) {
		User contextUser = securityService.getContextUser()
		Session sessionS = Session.get(id)
		Participant p = Participant.findByUserAndSession(contextUser, sessionS)
		if(p) {
			p.delete()
		}
		
		Notification n = Notification.findBySenderAndSession(contextUser, sessionS)
		if(n) {
			n.delete()
		}
	}
	
	boolean isMasterSlot(Integer id) {
		Session sessionS = Session.get(id)
		Participant p = Participant.findBySessionAndRole(sessionS, true)
		if(p != null) {
			if(p.getState() == true) {
				return false
			}
		}
		
		return true
	}
	
	boolean isPlayerSlot(Integer id, Integer maxPlayers) {
		Session sessionS = Session.get(id)
		int counter = 0
		Participant.findAllBySessionAndRole(sessionS, false).collect() {
			if(it.getState() == true) {
				counter++
			}
		}
		
		if(counter < maxPlayers-1) {
			return true
		} else {
			return false
		}
	}
	
	int getNotificationsCount() {
		User contextUser = securityService.getContextUser()
		return Notification.countByReceiver(contextUser)
	}
	
	boolean isMineSession(String owner) {
		if(securityService.getContextNickname().equals(owner)) {
			return true
		} else {
			return false
		}
	}
}
