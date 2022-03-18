# Shareboard 

* [Dependencies](https://ra-c.github.io/shareboard_is/dependencies.html)
* [Generated reports](https://ra-c.github.io/shareboard_is/project-reports.html)
  + Javadoc, Jacoco, Surefire, Failsafe
* [Releases](https://github.com/ra-c/shareboard_is/releases)


## What's needed

* TomEE 8.0.9
* JDK 11
* Hibernate 5.6
* MySQL/MariaDB  


## Build

Build war artifact:  
```
mvn package
```
    
To skip tests:
```    
mvn package -Dmaven.skip.test=true
```


The resulting ``shareboard.war`` file is in ``./out/``


## Run

* Move ``shareboard.war`` in ``<<tomee-home>>/webapps/``  
* Edit (create if not exists) ``<<tomee-home>>/conf/system.properties`` to set DB connection parameters:  
```
shareboardDB.jdbcUrl = jdbc:mysql://<host>:<port>/<schemaName>?createDatabaseIfNotExist=true
shareboardDB.password = <db user password>
shareboardDB.userName = <db user name>
```
* Run TomEE
  + If it's a first run, the web app fills the db schema with the appropriate tables and sample data
