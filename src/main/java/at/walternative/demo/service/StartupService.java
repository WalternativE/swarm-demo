package at.walternative.demo.service;

import at.walternative.demo.util.DemoLogger;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.jboss.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Singleton
@Startup
public class StartupService {

    @Inject
    @DemoLogger
    private Logger logger;

    @PersistenceContext(unitName = "MyPu")
    private EntityManager em;

    @PostConstruct
    public void startUp() {
        logger.info("StartupService triggered!");

        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(em);
        try {
            fullTextEntityManager.createIndexer().startAndWait();
        } catch (InterruptedException e) {
            logger.error("The index synchronization process was interrupted with an exception!", e);
        }
    }
}
