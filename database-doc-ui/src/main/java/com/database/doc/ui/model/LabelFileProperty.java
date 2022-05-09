package com.database.doc.ui.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.CheckBox;

/**
 * Model class for a Table.
 *
 * @author Marco Jakob
 */
public class LabelFileProperty {

    private final CheckBox checkBox = new CheckBox();
    private final StringProperty name;
    private final StringProperty dateStr;

    /**
     * Default constructor.
     */
    public LabelFileProperty() {
        this(null, null);
    }

    /**
     * Constructor with some initial data.
     *
     * @param name
     * @param comment
     */
    public LabelFileProperty(String name, String comment) {
        this.name = new SimpleStringProperty(name);
        this.dateStr = new SimpleStringProperty(comment);
    }

    public CheckBox getCheckBox() {
        return checkBox;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public StringProperty nameProperty() {
        return name;
    }

    public String getDateStr() {
        return dateStr.get();
    }

    public StringProperty dateStrProperty() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr.set(dateStr);
    }
}