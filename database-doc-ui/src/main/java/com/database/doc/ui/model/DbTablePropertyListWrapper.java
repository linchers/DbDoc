package com.database.doc.ui.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Helper class to wrap a list of persons. This is used for saving the
 * list of persons to XML.
 * 
 * @author Marco Jakob
 */
@XmlRootElement(name = "tables")
public class DbTablePropertyListWrapper {

    private List<DbTableProperty> dbTableProperties;

    @XmlElement(name = "table")
    public List<DbTableProperty> getPersons() {
        return dbTableProperties;
    }

    public void setPersons(List<DbTableProperty> dbTableProperties) {
        this.dbTableProperties = dbTableProperties;
    }
}