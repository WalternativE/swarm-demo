package at.walternative.demo;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.wildfly.swarm.Swarm;
import org.wildfly.swarm.datasources.DatasourcesFraction;
import org.wildfly.swarm.jpa.JPAFraction;
import org.wildfly.swarm.undertow.WARArchive;

public class App {
    public static void main(String[] args) throws Exception {

        Swarm container = new Swarm(args);
        container.fraction(new DatasourcesFraction()
                .jdbcDriver("h2", (d) -> {
                    d.driverClassName("org.h2.Driver");
                    d.xaDatasourceClass("org.h2.jdbcx.JdbcDataSource");
                    d.driverModuleName("com.h2database.h2");
                })
                .dataSource("MyDS", (ds) -> {
                    ds.driverName("h2");
                    ds.connectionUrl("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE");
                    ds.userName("sa");
                    ds.password("sa");
                })
        );

        container.fraction(new JPAFraction()
                .inhibitDefaultDatasource()
                .defaultDatasource("jboss/datasources/MyDS")
        );

        WARArchive deployment = ShrinkWrap.create(WARArchive.class, "demo.war");
        deployment.addPackages(true, "at.walternative.demo.controller",
                "at.walternative.demo.util",
                "at.walternative.demo.entities",
                "at.walternative.demo.service");

        deployment.addAsWebInfResource("WEB-INF/beans.xml")
                .addAsWebInfResource("WEB-INF/web.xml")
                .addAsWebInfResource("WEB-INF/faces-config.xml")
                .addAsWebInfResource("META-INF/persistence.xml", "classes/META-INF/persistence.xml");

        deployment.addAsWebResource("index.xhtml");
        deployment.addAllDependencies();

        container.start().deploy(deployment);
    }
}
