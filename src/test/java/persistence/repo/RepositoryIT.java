package persistence.repo;

import ch.vorburger.exec.ManagedProcessException;
import ch.vorburger.mariadb4j.DB;
import ch.vorburger.mariadb4j.DBConfigurationBuilder;
import ch.vorburger.mariadb4j.MariaDBOutputStreamLogDispatcher;
import org.apache.bval.cdi.BValInterceptor;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.apache.openejb.jee.jpa.unit.PersistenceUnit;
import org.apache.openejb.junit5.ExtensionMode;
import org.apache.openejb.junit5.RunWithApplicationComposer;
import org.apache.openejb.testing.Classes;
import org.apache.openejb.testing.Configuration;
import org.apache.openejb.testing.Module;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;

import javax.annotation.Resource;

import javax.persistence.*;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import java.util.function.Consumer;

import static org.hibernate.testing.transaction.TransactionUtil.doInJPA;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.quality.Strictness.LENIENT;

@ExtendWith({MockitoExtension.class})
@MockitoSettings(strictness = LENIENT)
@RunWithApplicationComposer(mode = ExtensionMode.PER_ALL)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.Random.class)
public class RepositoryIT {

    @PersistenceContext
    protected EntityManager em;

    @Resource
    protected UserTransaction utx;

    private DB db;

    protected void doTransactional(Consumer<UserTransaction> utxConsumer) throws Exception {
        utx.begin();
        try{
            utxConsumer.accept(utx);
            utx.commit();
        } catch(Exception e) {
            utx.rollback();
            throw e;
        }
    }

    private void startDb() throws ManagedProcessException {
        DBConfigurationBuilder config = DBConfigurationBuilder.newBuilder();
        config.setPort(0); // random port
        db = DB.newEmbeddedDB(config.build());
        db.start();
        db.createDB("testdb", "root", "root");
    }

    //resources.xml equivalent
    @Configuration
    public Properties config() throws ManagedProcessException {
        startDb();
        Properties p = new Properties();
        p.put("db", "new://Resource?type=DataSource");
        p.put("db.JdbcDriver", "com.mysql.cj.jdbc.Driver");
        p.put("db.JdbcUrl", "jdbc:mysql://localhost:"+db.getConfiguration().getPort()+"/testdb?user=root&password=root");
        return p;
    }

    //persistence.xml equivalent
    @Module
    public PersistenceUnit setupPU(){
        PersistenceUnit pu = new PersistenceUnit("testPU");
        pu.setProvider(HibernatePersistenceProvider.class);
        pu.setJtaDataSource("db");
        pu.setProperty("tomee.jpa.factory.lazy", "true");
        pu.setProperty("hibernate.format_sql" ,"true");
        pu.setProperty("hibernate.use_sql_comments" ,"true");
        pu.setProperty("hibernate.dialect" ,"org.hibernate.dialect.MySQL8Dialect");
        pu.setProperty("hibernate.physical_naming_strategy",
                       "org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy");
        return pu;
    }

    @BeforeEach
    void cleanDatabase() throws ManagedProcessException {
        db.run("DROP DATABASE IF EXISTS testdb", "root", "root", "testdb", false, false);
        db.createDB("testdb", "root", "root");
        db.source("schemaonly.sql", "root", "root", "testdb");
    }

    @AfterEach
    void tearDown() {
    }


}