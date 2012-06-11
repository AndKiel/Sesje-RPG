package rpgApp.services

import org.apache.commons.lang.time.DateUtils

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
	
	List<SessionItem> getIncomingSessions() {
		Date actualDate = new Date()
		int maximum = 5
		if(User.count() < 5) {
			maximum = Session.count()
		}
		return Session.findAllByTimeStampGreaterThan(actualDate, [sort: 'timeStamp', order:'asc', max: maximum]).collect {
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


	List<SessionItem> getMySessions() {
		User owner = securityService.getContextUser()
		return Session.findAllByOwner(owner, [sort: 'dateCreated', order:'desc']).collect {
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

	List<SessionItem> getJoinedSessions() {
		User user = securityService.getContextUser()
		List<Session> sessions = []
		Participant.findAllByUserAndState(user, true).collect() {
			sessions.add(it.getSession())
		}

		List<SessionItem> joinedSessions = []
		for(Session ses in sessions) {
			joinedSessions.add(new SessionItem(
					id: ses.id,
					dateCreated: ses.dateCreated,
					timeStamp: ses.timeStamp,
					type: ses.type,
					location: ses.location,
					maxPlayers: ses.maxPlayers,
					owner: ses.owner.nickname,
					system: ses.system.name,
					))
		}

		return joinedSessions
	}

	List<SessionItem> getWaitingSessions() {
		User user = securityService.getContextUser()
		List<Session> sessions = []
		Participant.findAllByUserAndState(user, false).collect() {
			sessions.add(it.getSession())
		}

		List<SessionItem> waitingSessions = []
		for(Session ses in sessions) {
			waitingSessions.add(new SessionItem(
					id: ses.id,
					dateCreated: ses.dateCreated,
					timeStamp: ses.timeStamp,
					type: ses.type,
					location: ses.location,
					maxPlayers: ses.maxPlayers,
					owner: ses.owner.nickname,
					system: ses.system.name,
					))
		}

		return waitingSessions
	}

	Integer createSession(Date ts, String type, String loc, Integer mP, String sysName, String role) {
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

		s.save(failOnError: true, flush: true)

		if(role.equals("Master")) {
			new Participant(user: owner, session: s, role: true, state: true).save(flush: true)
		} else if(role.equals("Player")) {
			new Participant(user: owner, session: s, role: false, state: true).save(flush: true)
		}
		
		return s.id
	}

	void updateSession(Date ts, String type, String loc, Integer mP, String sysName, Long id) {
		RpgSystem rpgsystem = RpgSystem.findByName(sysName)
		Session.executeUpdate('UPDATE Session SET timeStamp=:timeStamp, type=:type, location=:location, maxPlayers=:maxPlayers, system=:system WHERE id=:sessionId', [timeStamp: ts, type: type, location: loc, maxPlayers: mP, system: rpgsystem, sessionId: id])
	}

	void deleteSession(Integer id) {
		Session sessionS = Session.get(id)
		Participant.findAllBySession(sessionS).collect() {
			it.delete()
		}

		sessionS.delete()
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

	List<String> getParticipants(Integer id) {
		Session sessionS = Session.get(id)
		return Participant.findAllBySession(sessionS).collect() {
			new String(it.getUser().getNickname()) 	
		}
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
				return p.getUser().getNickname()
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
		new Notification(sender: contextUser, receiver: rec, session: sessionS, type: type, role: role).save(failOnError: true, flush: true)
	}

	void createParticipant(Integer id, Boolean role, Boolean state) {
		User u = securityService.getContextUser()
		new Participant(
				user: u,
				session: Session.get(id),
				role: role,
				state: state,
				).save(failOnError: true, flush: true)

	}
	
	void createOtherParticipant(String user, Integer id, Boolean role, Boolean state) {
		User u = User.findByNickname(user)
		new Participant(
				user: u,
				session: Session.get(id),
				role: role,
				state: state,
				).save(failOnError: true, flush: true)

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

	boolean isMineSession(String owner) {
		if(securityService.getContextNickname().equals(owner)) {
			return true
		} else {
			return false
		}
	}
}
