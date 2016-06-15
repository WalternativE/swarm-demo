package at.walternative.demo.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table
public class Tweet implements Serializable {

    private Long id;
    private String text;

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
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
