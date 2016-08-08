package models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by nek on 08.08.16.
 */
public class Message {
    final private StringProperty text;

    public Message(String text) {
        this.text = new SimpleStringProperty(text);
    }

    String getText () {
        return  text.get();
    }
}
