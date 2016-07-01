package at.walternative.demo.service;

import at.walternative.demo.entities.Tweet;
import at.walternative.demo.util.DemoLogger;
import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.jboss.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class SearchServiceBean implements SearchService, Serializable {

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

    @Override
    public List<Tweet> retrieveAllTweets() {
        TypedQuery<Tweet> query = em.createQuery("select t from Tweet t", Tweet.class);
        return query.getResultList();
    }

    @Override
    public void saveNewTweet(Tweet tweet) {
        em.persist(tweet);
    }

    @Override
    public List<Tweet> findListForKeyWordQuery(String statement) {
        logger.info("Key word search statement incoming: " + statement);

        FullTextEntityManager fem = Search.getFullTextEntityManager(em);
        QueryBuilder builder = fem.getSearchFactory().buildQueryBuilder().forEntity(Tweet.class).get();

        Query query = builder
                .keyword()
                .onFields("author", "text")
                .matching(statement)
                .createQuery();

        javax.persistence.Query q = fem.createFullTextQuery(query, Tweet.class);
        return returnTypedListForQuery(q, Tweet.class);
    }

    @Override
    public List<Tweet> findListForFuzzyKeyWordQuery(String fuzzyKeyWord, int editDistance) {
        logger.info("Fuzzy key word search statement incoming: " + fuzzyKeyWord);

        FullTextEntityManager fem = Search.getFullTextEntityManager(em);
        QueryBuilder builder = fem.getSearchFactory().buildQueryBuilder().forEntity(Tweet.class).get();

        Query query = builder
                .keyword()
                .fuzzy()
                .withEditDistanceUpTo(editDistance)
                .withPrefixLength(1)
                .onFields("author", "text")
                .matching(fuzzyKeyWord)
                .createQuery();

        javax.persistence.Query q = fem.createFullTextQuery(query, Tweet.class);
        return returnTypedListForQuery(q, Tweet.class);
    }

    private <T> List<T> returnTypedListForQuery(javax.persistence.Query query, Class<T> clazz) {
        List entities = query.getResultList();
        List<T> entityResults = new ArrayList<>();

        // this is as safe as it can be...
        //noinspection unchecked
        entities.forEach(entity -> {
            try {
                entityResults.add(clazz.cast(entity));
            } catch (ClassCastException e) {
                logger.error("You might miscalculated your class cast her, laddy...");
            }
        });

        logger.info("Results are in");
        return entityResults;
    }
}
