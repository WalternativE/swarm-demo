package at.walternative.demo.controller;

import at.walternative.demo.entities.Tweet;
import at.walternative.demo.service.PrimerService;
import at.walternative.demo.util.DemoLogger;
import org.jboss.logging.Logger;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@ViewScoped
@Named
public class IndexController implements Serializable {
//
//    @Inject
//    @DemoLogger
//    private Logger logger;

    @Inject
    private PrimerService primerService;

    public String getFirstTweetText() {
//        logger.info("Priming database if necessary");
        primerService.seedDataBaseWithTweets();

//        logger.info("Retrieving first tweet");
        Tweet tweet = primerService.retrieveFirstTweet();

        return tweet.getText();
    }

}
