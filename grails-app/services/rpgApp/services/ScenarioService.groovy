package rpgApp.services

import rpgApp.data.ScenarioItem
import rpgApp.persistance.RpgSystem
import rpgApp.persistance.Scenario
import rpgApp.persistance.User

class ScenarioService {
    def securityService
    static transactional = true
    
    List<ScenarioItem> getAllScenarios(){
        return Scenario.findAll([sort: 'name', order: 'asc']).collect{
            new ScenarioItem(
                id: it.id,
                name: it.name,
                type: it.type,
                playersCount: it.playersCount,
                content: it.content,
                owner: it.owner.nickname,
                system: it.system.name
            )
        }
    }
    
    ScenarioItem getScenario(Integer id){
        Scenario s = Scenario.get(id)
        return new ScenarioItem(
            id: id,
            name: s.name,
            type: s.type,
            playersCount: s.playersCount,
            content: s.content,
            owner: s.owner.nickname,
            system: s.system.name
        )
    }
    
    void createScenario(String n, String t, Integer pC, String c, String rpg){
        User u = securityService.getContextUser()
        RpgSystem sys = RpgSystem.findByName(rpg)
        Scenario s = new Scenario(
            name: n,
            type: t,
            playersCount: pC,
            content: c,
            owner: a,
            system: sys
        )
        s.save(failOnError: true, flush: true)
    }
    
    void updateScenario(String n, String t, String pC, String c, String rpg, Long id){
        RpgSystem sys = RpgSystem.findByName(rpg)
        Scenario.executeUpdate('UPDATE Scenario SET name=:name, type=:type, playersCount=:playersCount, content=:content, system=:system WHERE id=:scenarioID', [name: n, type: t, playersCount: pc, content: c, system: sys, scenarioID: id])
    }
    
    void removeScenario(ScenarioItem scenario){
        Scenario s = Scenario.get(scenario.getId())
        if(s)
        {
            s.delete()
        }
    }
}
