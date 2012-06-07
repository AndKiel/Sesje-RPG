package rpgApp.services

import rpgApp.data.SystemItem
import rpgApp.exeptions.ValidationException
import rpgApp.persistance.RpgSystem

class SystemService {

	static transactional = true
	
    List<SystemItem> getAllSystems() {
		return RpgSystem.findAll([sort: 'name', order:'asc']).collect {
			new SystemItem(
				id: it.id,
				name: it.name,
				description: it.description,
				genre: it.genre,
				designer: it.designer,
				publisher: it.publisher,
				year: it.year,
				)
		}
	}
	
	List<String> getAllSystemsNames() {
		return RpgSystem.findAll([sort: 'name', order:'asc']).collect {
			new String(it.name)
		}
	}
	
	SystemItem getSystem(Integer id) {
		RpgSystem s = RpgSystem.get(id)
		return new SystemItem(
			id: id,
			name: s.name,
			description: s.description,
			genre: s.genre,
			designer: s.designer,
			publisher: s.publisher,
			year: s.year,
			)
	}
	
	void createSystem(String n, String d, String g, String des, String p, Integer y) {
		RpgSystem s = new RpgSystem(
				name: n,
				description: d,
				genre: g,
				designer: des,
				publisher: p,
				year: y,
				)

		if(!s.validate()) {
			if(s.errors.getFieldError("name")) {
				throw new ValidationException("System name is already taken")
				return
			}
		} else {
			s.save(failOnError: true, flush: true)
		}
	}
	
	void updateSystem(String name, String d, String g, String des, String p, Integer y) {
		if(d ==null) {
			d = ""
		}
		if(g ==null) {
			g = ""
		}
		if(des ==null) {
			des = ""
		}
		if(p ==null) {
			p = ""
		}
		RpgSystem.executeUpdate('UPDATE RpgSystem SET description=:d, genre=:g , designer=:des, publisher=:p, year=:y WHERE name=:name', [d: d, g: g, des: des, p: p, y: y, name: name])
	}
	
	void removeSystem(SystemItem system) {
		RpgSystem s = RpgSystem.get(system.getId())
		if(s) {
			s.delete()
		}
	}
}
