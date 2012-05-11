class UrlMappings {

	static mappings = {
		"/$controller/$action?/$id?"{
			constraints {
				// apply constraints here
			}
		}

        // commented for vaadin to work properly
		// "/"(view:"/index")
		"500"(view:'/error')
	}
}
