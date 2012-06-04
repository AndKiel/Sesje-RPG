package rpgApp.services

import rpgApp.data.SessionItem
import rpgApp.persistance.Session
import rpgApp.persistance.Participant
import rpgApp.persistance.User
import rpgApp.persistance.RpgSystem

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

	boolean checkMembership(Integer id) {
		User contextUser = securityService.getContextUser()
		Session sessionS = Session.get(id)
		Participant p = Participant.findByUserAndSession(contextUser, sessionS)
		if(p) {
			if(p.getState() == true) {
				return true
			}
		}

		return false
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
}
