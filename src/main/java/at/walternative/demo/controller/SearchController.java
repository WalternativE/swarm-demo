package at.walternative.demo.controller;

import at.walternative.demo.entities.Tweet;
import at.walternative.demo.service.PrimerService;
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
    private PrimerService primerService;

    private TableModel tableModel;

    private String searchWord;

    private List<Tweet> foundTweets;

    @PostConstruct
    public void init() {
        this.tableModel = new DefaultTableModel();
    }

    public void doSearch() {
        this.foundTweets = primerService.findListForQueryWord(this.searchWord);
    }

    public String getSearchWord() {
        return searchWord;
    }

    public void setSearchWord(String searchWord) {
        this.searchWord = searchWord;
    }

    public List<Tweet> getFoundTweets() {
        return foundTweets;
    }

    public TableModel getTableModel() {
        return tableModel;
    }
}
