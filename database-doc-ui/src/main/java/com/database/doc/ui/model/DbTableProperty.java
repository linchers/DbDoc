package com.database.doc.ui.model;

import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckBox;

/**
 * Model class for a Table.
 *
 * @author Marco Jakob
 */
public class DbTableProperty {

    private final CheckBox checkBox = new CheckBox();
    private final StringProperty name;
    private final StringProperty comment;
    private final SimpleListProperty<DbColumnProperty> columns;

    /**
     * Default constructor.
     */
    public DbTableProperty() {
        this(null, null);
    }

    /**
     * Constructor with some initial data.
     * 
     * @param name
     * @param comment
     */
    public DbTableProperty(String name, String comment) {
        this.name = new SimpleStringProperty(name);
        this.comment = new SimpleStringProperty(comment);
        this.columns = new SimpleListProperty();
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

    public String getComment() {
        return comment.get();
    }

    public void setComment(String comment) {
        this.comment.set(comment);
    }

    public StringProperty commentProperty() {
        return comment;
    }

    public ObservableList<DbColumnProperty> getColumns() {
        return columns.get();
    }

    public SimpleListProperty<DbColumnProperty> columnsProperty() {
        return columns;
    }

    public void setColumns(ObservableList<DbColumnProperty> columns) {
        this.columns.set(columns);
    }
}