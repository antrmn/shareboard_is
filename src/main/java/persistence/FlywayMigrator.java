package persistence;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.FluentConfiguration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.*;
import javax.sql.DataSource;

@Singleton
@Startup
@TransactionManagement(TransactionManagementType.BEAN)
public class FlywayMigrator {

    @Resource(name = "jdbc/shareboardDB")
    private DataSource dataSource;

    @PostConstruct
    private void onStartup(){
        if (dataSource == null) {
            throw new EJBException("Cannot migrate, no datasource configured!");
        }
        FluentConfiguration config = Flyway.configure()
                .dataSource(dataSource)
                .createSchemas(true)
                .validateOnMigrate(false)
                .locations("classpath:db/migration");
        Flyway flyway = new Flyway(config);
        flyway.migrate();
    }



}
