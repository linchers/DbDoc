package com.database.doc.ui.model;

import com.database.doc.domain.DBColumn;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.CheckBox;

/**
 * Model class for a Table.
 *
 * @author Marco Jakob
 */
public class DbColumnProperty {

    private final StringProperty name;
    private final StringProperty comment;
    private final StringProperty type;
    private final IntegerProperty size;
    private final StringProperty nullAble;

    /**
     * Default constructor.
     */
    public DbColumnProperty() {
        this(null, null);
    }

    /**
     * Constructor with some initial data.
     *
     * @param name
     * @param comment
     */
    public DbColumnProperty(String name, String comment) {
        this.name = new SimpleStringProperty(name);
        this.comment = new SimpleStringProperty(comment);
        this.type = new SimpleStringProperty("");
        this.size = new SimpleIntegerProperty();
        this.nullAble = new SimpleStringProperty();
    }

    public DbColumnProperty(DBColumn column) {
        this(column.getName(), column.getComment());
        this.setName(column.getName());
        this.setComment(column.getComment());
        this.setSize(column.getLength());
        this.setType(column.getType());
        this.setNullAble(column.getNullable());
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

    public String getType() {
        return type.get();
    }

    public StringProperty typeProperty() {
        return type;
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public int getSize() {
        return size.get();
    }

    public IntegerProperty sizeProperty() {
        return size;
    }

    public void setSize(int size) {
        this.size.set(size);
    }

    public String getNullAble() {
        return nullAble.get();
    }

    public StringProperty nullAbleProperty() {
        return nullAble;
    }

    public void setNullAble(String nullAble) {
        this.nullAble.set(nullAble);
    }
}