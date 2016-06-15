package at.walternative.demo.service;

import at.walternative.demo.entities.Tweet;

public interface PrimerService {

    void seedDataBaseWithTweets();

    Tweet retrieveFirstTweet();

}
