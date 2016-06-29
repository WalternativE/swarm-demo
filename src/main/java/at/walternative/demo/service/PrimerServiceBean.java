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
    public List<Tweet> findListForQueryWord(String word) {

        FullTextEntityManager fem = Search.getFullTextEntityManager(em);

        QueryBuilder builder = fem.getSearchFactory().buildQueryBuilder().forEntity(Tweet.class).get();

        Query query = builder.keyword()
                            .onFields("author", "text")
                            .matching(word)
                            .createQuery();

        javax.persistence.Query q = fem.createFullTextQuery(query, Tweet.class);

        List tweets = q.getResultList();

        List<Tweet> tweetResults = new ArrayList<>();

        //noinspection unchecked
        tweets.forEach(tweet -> {
            if (!(tweet instanceof Tweet)) {
                throw new RuntimeException("This is shit");
            } else {
                tweetResults.add((Tweet) tweet);
            }
        });

        logger.info("Search is done");

        return tweetResults;
    }
}
