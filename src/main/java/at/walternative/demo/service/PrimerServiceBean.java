package at.walternative.demo.service;

import at.walternative.demo.entities.Tweet;
import at.walternative.demo.util.DemoLogger;
import org.jboss.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.io.Serializable;
import java.util.List;

@Stateless
public class PrimerServiceBean implements PrimerService, Serializable {

    @PersistenceContext(unitName = "MyPu")
    private EntityManager em;

    @Inject
    @DemoLogger
    private Logger logger;

    @Override
    public void seedDataBaseWithTweets() {
        TypedQuery<Tweet> query = em.createQuery("select t from Tweet t", Tweet.class);
        List<Tweet> tweets = query.getResultList();

        if (tweets.isEmpty()) {
            Tweet firstTweet = new Tweet();
            firstTweet.setText("This is my first tweet!");

            em.persist(firstTweet);

            logger.info("Persisted tweet with id: " + firstTweet.getId());
        }
    }

    @Override
    public Tweet retrieveFirstTweet() {
        return em.find(Tweet.class, 1L);
    }

}
