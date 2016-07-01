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
public class SearchController implements Serializable {

    @Inject
    @DemoLogger
    private Logger logger;

    @Inject
    private SearchService searchService;

    private TableModel tableModel;

    private String keyWord;

    private List<Tweet> foundTweets;
    private String fuzzyKeyWord;
    private Integer editDistance = 1;

    @PostConstruct
    public void init() {
        this.tableModel = new DefaultTableModel();
    }

    public void doSearch() {
        this.foundTweets = searchService.findListForKeyWordQuery(this.keyWord);
    }

    public void doFuzzySearch() {
        this.foundTweets = searchService.findListForFuzzyKeyWordQuery(this.fuzzyKeyWord, this.editDistance);
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public List<Tweet> getFoundTweets() {
        return foundTweets;
    }

    public TableModel getTableModel() {
        return tableModel;
    }

    public void setFuzzyKeyWord(String fuzzyKeyWord) {
        this.fuzzyKeyWord = fuzzyKeyWord;
    }

    public String getFuzzyKeyWord() {
        return fuzzyKeyWord;
    }

    public void setEditDistance(Integer editDistance) {
        this.editDistance = editDistance;
    }

    public Integer getEditDistance() {
        return editDistance;
    }
}
