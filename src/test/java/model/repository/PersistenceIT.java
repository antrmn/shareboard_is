package model.repository;

import ch.vorburger.exec.ManagedProcessException;
import ch.vorburger.mariadb4j.DB;
import ch.vorburger.mariadb4j.DBConfigurationBuilder;
import org.apache.openejb.jee.jpa.unit.PersistenceUnit;
import org.apache.openejb.junit5.ExtensionMode;
import org.apache.openejb.junit5.RunWithApplicationComposer;
import org.apache.openejb.testing.Configuration;
import org.apache.openejb.testing.Module;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import rocks.limburg.cdimock.CdiMocking;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Status;
import javax.transaction.UserTransaction;
import java.util.Properties;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.mockito.quality.Strictness.LENIENT;

@ExtendWith({MockitoExtension.class, CdiMocking.class})
@MockitoSettings(strictness = LENIENT)
@RunWithApplicationComposer(mode = ExtensionMode.PER_ALL)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.Random.class)
public abstract class PersistenceIT {

    @FunctionalInterface
    protected interface Worker {
        void run();
    }

    @PersistenceContext
    private EntityManager entityManager;

    @Resource
    private UserTransaction userTransaction;

    private DB db;

    protected EntityManager getEntityManager(){
        return entityManager;
    }

    protected DB getDb(){
        return db;
    }

    protected UserTransaction getUserTransaction(){
        return userTransaction;
    };

    private void startDb() throws ManagedProcessException {
        DBConfigurationBuilder config = DBConfigurationBuilder.newBuilder();
        config.setPort(0); // random port
        db = DB.newEmbeddedDB(config.build());
        db.start();
        db.createDB("testdb", "root", "root");
        db.source("schemaonly.sql", "root", "root", "testdb");
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

    protected void doThenRollback(Consumer<EntityManager> consumer) throws Exception{
        userTransaction.begin();
        try {
            consumer.accept(entityManager);
        } finally {
            userTransaction.rollback();
        }
    }

    protected <T> T doThenRollback(Function<EntityManager, T> function) throws Exception{
        userTransaction.begin();
        try {
            return function.apply(entityManager);
        } finally {
            userTransaction.rollback();
        }
    }

    protected void doThenRollback(Worker worker) throws Exception{
        userTransaction.begin();
        try {
            worker.run();
        } finally {
            userTransaction.rollback();
        }
    }

    protected <T> T doThenRollback(Supplier<T> supplier) throws Exception{
        userTransaction.begin();
        try {
            return supplier.get();
        } finally {
            userTransaction.rollback();
        }
    }

    protected void doTransactional(Worker worker) throws Exception{
        userTransaction.begin();
        try {
            worker.run();
        } catch (RuntimeException e){
            userTransaction.setRollbackOnly();
            throw e;
        } finally {
            if (userTransaction.getStatus() == Status.STATUS_MARKED_ROLLBACK)
                userTransaction.rollback();
            else
                userTransaction.commit();
        }
    }

    protected <T> T doTransactional(Function<EntityManager, T> function) throws Exception{
        userTransaction.begin();
        try {
            return function.apply(entityManager);
        } catch (RuntimeException e){
            userTransaction.setRollbackOnly();
            throw e;
        } finally {
            if (userTransaction.getStatus() == Status.STATUS_MARKED_ROLLBACK)
                userTransaction.rollback();
            else
                userTransaction.commit();
        }
    }

    protected <T> T doTransactional(Supplier<T> supplier) throws Exception{
        userTransaction.begin();
        try {
            return supplier.get();
        } catch (RuntimeException e){
            userTransaction.setRollbackOnly();
            throw e;
        } finally {
            if (userTransaction.getStatus() == Status.STATUS_MARKED_ROLLBACK)
                userTransaction.rollback();
            else
                userTransaction.commit();
        }
    }

    protected void doTransactional(Consumer<EntityManager> consumer) throws Exception{
        userTransaction.begin();
        try {
            consumer.accept(entityManager);
        } catch (RuntimeException e){
            userTransaction.setRollbackOnly();
            throw e;
        } finally {
            if (userTransaction.getStatus() == Status.STATUS_MARKED_ROLLBACK)
                userTransaction.rollback();
            else
                userTransaction.commit();
        }
    }
}