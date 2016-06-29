package at.walternative.demo;

import org.jboss.modules.Module;
import org.jboss.modules.ModuleIdentifier;
import org.wildfly.swarm.ContainerFactory;
import org.wildfly.swarm.Swarm;
import org.wildfly.swarm.container.Container;
import org.wildfly.swarm.datasources.DatasourcesFraction;
import org.wildfly.swarm.jpa.JPAFraction;

import java.util.Iterator;
import java.util.ServiceLoader;

public class App {

    public static void main(String[] args) throws Exception {
        if (System.getProperty("boot.module.loader") == null) {
            System.setProperty("boot.module.loader", "org.wildfly.swarm.bootstrap.modules.BootModuleLoader");
        }

        Module bootstrap = Module.getBootModuleLoader().loadModule(ModuleIdentifier.create("swarm.application"));
        ServiceLoader factory = bootstrap.loadService(ContainerFactory.class);
        Iterator factoryIter = factory.iterator();
        if (!factoryIter.hasNext()) {
            simpleMain(args);
        } else {
            factoryMain((ContainerFactory) factoryIter.next(), args);
        }
    }

    private static void simpleMain(String[] args) throws Exception {
        Container container = new Swarm(args);

        container = configureContainer(container);

        container.start();
        container.deploy();
    }

    private static void factoryMain(ContainerFactory factory, String[] args) throws Exception {
        Container container = factory.newContainer(args);

        container = configureContainer(container);

        container.start();
        container.deploy();
    }

    private static Container configureContainer(Container container) {
        container.fraction(
                new DatasourcesFraction()
                        .jdbcDriver("h2", (d) -> {
                            d.driverClassName("org.h2.Driver");
                            d.xaDatasourceClass("org.h2.jdbcx.JdbcDataSource");
                            d.driverModuleName("com.h2database.h2");
                        })
                        .dataSource("ZeDS", (ds) -> {
                            ds.driverName("h2");
                            ds.connectionUrl("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE");
                            ds.userName("sa");
                            ds.password("sa");
                        })
        );

        container.fraction(new JPAFraction()
                .inhibitDefaultDatasource()
                .defaultDatasource("ZeDS")
        );

        return container;
    }
}