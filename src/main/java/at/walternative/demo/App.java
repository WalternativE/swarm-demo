package at.walternative.demo;

import org.jboss.modules.Module;
import org.jboss.modules.ModuleIdentifier;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.asset.ClassLoaderAsset;
import org.wildfly.swarm.ContainerFactory;
import org.wildfly.swarm.Swarm;
import org.wildfly.swarm.container.Container;
import org.wildfly.swarm.datasources.DatasourcesFraction;
import org.wildfly.swarm.jpa.JPAFraction;
import org.wildfly.swarm.spi.api.StageConfig;
import org.wildfly.swarm.undertow.WARArchive;

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

        Archive<?> archive = configureDefaultDeployment(container);
        container.deploy(archive);
    }

    private static void factoryMain(ContainerFactory factory, String[] args) throws Exception {
        Container container = factory.newContainer(args);

        container = configureContainer(container);

        container.start();

        Archive<?> archive = configureDefaultDeployment(container);
        container.deploy(archive);
    }

    private static Archive<?> configureDefaultDeployment(Container container) {
        Archive<?> archive = container.createDefaultDeployment();

        if (archive instanceof WARArchive) {
            WARArchive war = (WARArchive) archive;

            String persistenceResource = container
                    .stageConfig()
                    .resolve("database.persistenceConf.resourceName")
                    .getValue();

            war.addAsWebInfResource(new ClassLoaderAsset(persistenceResource,
                    App.class.getClassLoader()), "classes/META-INF/persistence.xml");
        }

        return archive;
    }

    private static Container configureContainer(Container container) {
        StageConfig config = container.stageConfig();
        String connectionUrl = config.resolve("database.connection.url").getValue();
        String driverClassName = config.resolve("jdbcDriver.driverClassName").getValue();
        String xaDatasourceClass = config.resolve("jdbcDriver.xaDatasourceClass").getValue();
        String driverModuleName = config.resolve("jdbcDriver.driverModuleName").getValue();

        String userName = config.resolve("database.connection.userName").getValue();
        String userPassword = config.resolve("database.connection.userPassword").getValue();

        container.fraction(
                new DatasourcesFraction()
                        .jdbcDriver("ZeDriver", (d) -> {
                            d.driverClassName(driverClassName);
                            d.xaDatasourceClass(xaDatasourceClass);
                            d.driverModuleName(driverModuleName);
                        })
                        .dataSource("ZeDS", (ds) -> {
                            ds.driverName("ZeDriver");
                            ds.connectionUrl(connectionUrl);
                            ds.userName(userName);
                            ds.password(userPassword);
                        })
        );

        container.fraction(new JPAFraction()
                .inhibitDefaultDatasource()
                .defaultDatasource("ZeDS")
        );

        return container;
    }
}