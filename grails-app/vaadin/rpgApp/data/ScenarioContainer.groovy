package rpgApp.data

import com.vaadin.data.util.BeanItemContainer
import rpgApp.services.ScenarioService

class ScenarioContainer extends BeanItemContainer<ScenarioItem> implements Serializable {
    private ScenarioService scenarioService
    
    public static final Object[] NATURAL_COL_ORDER = [
	"id",
	"name",
	"type",
	"playersCount",
	"system"
    ]

    public static final String[] COL_HEADERS_ENGLISH = [
	"ID",
	"Name",
	"Type",
	"Players Count",
	"System",
    ];

    public ScenarioContainer(ScenarioService scenarioService) throws InstantiationException, IllegalAccessException {
        super(ScenarioItem.class)
        this.scenarioService = scenarioService
    }

    void fillContainer() {
        removeAllItems()
        addAll(scenarioService.getAllScenarios())
    }

    boolean removeScenario(ScenarioItem scenario) {
        try {
            scenarioService.removeScenario(scenario)
            return true
        } catch (Exception e) {
            println e
            return false
        }
    }
}