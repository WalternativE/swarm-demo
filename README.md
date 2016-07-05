# This is a a demo

Nothing special to see here. Have to work on a college exercise and wanted to test Hibernate Search with Lucene,
Wildfly Swarm with different fractions including JPA, CDI, EJB, JTA and JSF as well as Butterfaces.

The application will break if you don't add a project-stages.yml file. A sufficient project-stages file
would look like this:

```yaml
jdbcDriver:
    driverClassName: "org.h2.Driver"
    xaDatasourceClass: "org.h2.jdbcx.JdbcDataSource"
    driverModuleName: "com.h2database.h2"
database:
    connection:
        url: "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE"
        userName: "sa"
        userPassword: "sa"
    persistenceConf:
        resourceName: "persistence-dev.xml"
```