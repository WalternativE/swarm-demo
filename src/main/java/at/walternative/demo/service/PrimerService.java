package at.walternative.demo.service;

import at.walternative.demo.entities.Tweet;

import java.util.List;

public interface PrimerService {

    void seedDataBaseWithTweets();

    Tweet retrieveFirstTweet();

    List<Tweet> retrieveAllTweets();

    void saveNewTweet(Tweet tweet);

    List<Tweet> findListForQueryWord(String word);

}
