dataSource {
    pooled = true
    driverClassName = "org.postgresql.Driver"
    dialect = net.sf.hibernate.dialect.PostgreSQLDialect
    username = "postgres"
    password = "postgres"
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
        }
    }
    test {
        dataSource {
            dbCreate = "update"
            url="jdbc:postgresql://localhost:5432/rpg_app"
        }
    }
    production {
        dataSource {
            dbCreate = "update"
            url="jdbc:postgresql://localhost:5432/rpg_app"
        }
    }
}