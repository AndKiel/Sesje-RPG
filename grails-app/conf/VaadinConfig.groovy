vaadin {
    applicationClass = "rpgApp.main.IndexApplication"

    // This is optional, GrailsAwareApplicationServlet is provided by default. Use this if you need to add or change application servlet. 
    // You should extend GrailsAwareApplicationServlet or GrailsAwareGAEApplicationServlet (from com.vaadin.grails.terminal.gwt.server package).
    // servletClass = "com.mycompany.MyGrailsAwareApplicationServlet"
	
    autowire = "byName" //how should dependencies be injected? other option is 'byType'

    // The context relative path where you want to access your Vaadin UI. Default is the context root.
    contextRelativePath = "/"
              
    productionMode = false

    googleAppEngineMode = false
	
//	widgetset = "com.SesjeRpg.ui.vaadin.SesjeRpgWidgetset"
}

environments {
    production {
        vaadin {
            productionMode = true
        }
    }
}
