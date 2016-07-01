package at.walternative.demo.service;

import at.walternative.demo.entities.Tweet;

import java.util.List;

public interface SearchService {

    void seedDataBaseWithTweets();

    Tweet retrieveFirstTweet();

    List<Tweet> retrieveAllTweets();

    void saveNewTweet(Tweet tweet);

    List<Tweet> findListForKeyWordQuery(String word);

    List<Tweet> findListForFuzzyKeyWordQuery(String fuzzyKeyWord, int editDistance);
}
