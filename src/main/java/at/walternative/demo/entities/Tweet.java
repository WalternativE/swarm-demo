package at.walternative.demo.entities;

import org.hibernate.search.annotations.*;
import org.hibernate.search.annotations.Index;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table
@Indexed
public class Tweet implements Serializable {

    private Long id;
    private String text;
    private String author;

    @Id
    @GeneratedValue
    @Column
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column
    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Column
    @Field(index = Index.YES, analyze = Analyze.NO, store = Store.NO)
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
