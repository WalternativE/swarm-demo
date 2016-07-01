package at.walternative.demo.controller;

import at.walternative.demo.entities.Tweet;
import at.walternative.demo.service.SearchService;
import at.walternative.demo.util.DemoLogger;
import de.larmic.butterfaces.model.table.DefaultTableModel;
import de.larmic.butterfaces.model.table.TableModel;
import org.jboss.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@ViewScoped
@Named
public class IndexController implements Serializable {

    @Inject
    @DemoLogger
    private Logger logger;

    @Inject
    private SearchService searchService;

    private Tweet model;

    private TableModel tableModel;

    @PostConstruct
    public void init() {
        this.model = new Tweet();
        this.tableModel = new DefaultTableModel();
    }

    public void doSave() {
        logger.info("Do save was used!");
        searchService.saveNewTweet(this.model);

        this.model = new Tweet();
    }

    public String getFirstTweetText() {
        logger.info("Priming database if necessary");
        searchService.seedDataBaseWithTweets();

        logger.info("Retrieving first tweet");
        Tweet tweet = searchService.retrieveFirstTweet();

        return tweet.getText();
    }

    public Tweet getModel() {
        return model;
    }

    public TableModel getTableModel() {
        return tableModel;
    }

    public void setTableModel(TableModel tableModel) {
        this.tableModel = tableModel;
    }

    public List<Tweet> getAllTweets() {
        return searchService.retrieveAllTweets();
    }
}
