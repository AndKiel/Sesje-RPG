dataSource {
	pooled = true
	driverClassName = "org.postgresql.Driver"
	// dialect = org.hibernate.dialect.PostgreSQLDialect
	dialect = net.sf.hibernate.dialect.PostgreSQLDialect
}
hibernate {
	cache.use_second_level_cache = true
	cache.use_query_cache = true
	cache.region.factory_class = 'net.sf.ehcache.hibernate.EhCacheRegionFactory'
}
// environment specific settings
environments {
	development {
		dataSource {
			// one of 'create', 'create-drop','update'
			dbCreate = "create-drop"
			url="jdbc:postgresql://localhost:5432/rpg_app"
			username = "postgres"
			password = "postgres"
		}
	}
	test {
		dataSource {
			dbCreate = "update"
			url="jdbc:postgresql://localhost:5432/rpg_app"
			driverClassName = "org.postgresql.Driver"
			username = "rpg"
			password = "rpg"
		}
	}
	production {
		dataSource {
			dbCreate = "update"
			url="jdbc:postgresql://localhost:5432/rpg_app"
			username = "rpg"
			password = "rpg"
		}
	}
}