package com.database.doc.ui;

import com.database.doc.config.DatabaseConfig;
import com.database.doc.domain.DBColumn;
import com.database.doc.domain.DBTable;
import com.database.doc.exception.CommonException;
import com.database.doc.processor.DatabaseProcessor;
import com.database.doc.processor.TableProcessor;
import com.database.doc.ui.model.DbColumnProperty;
import com.database.doc.ui.model.DbSettingProperty;
import com.database.doc.ui.model.DbTableProperty;
import com.database.doc.ui.model.DbTablePropertyListWrapper;
import com.database.doc.ui.task.FeatureTask;
import com.database.doc.ui.task.TaskResult;
import com.database.doc.ui.util.DbConfigUtil;
import com.database.doc.ui.util.ProgressUtil;
import com.database.doc.ui.util.ResourceLoader;
import com.database.doc.ui.view.BirthdayStatisticsController;
import com.database.doc.ui.view.DbSettingDialogController;
import com.database.doc.ui.view.DbTableEditDialogController;
import com.database.doc.ui.view.DbTableOverviewController;
import com.database.doc.ui.view.LabelFileDialogController;
import com.database.doc.ui.view.RootLayoutController;
import com.database.doc.util.DatabaseUtil;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.prefs.Preferences;
import java.util.stream.Collectors;

public class DocFXApplication extends Application {

    private String appTitle = "DatabaseDoc";
    private Stage primaryStage;
    private BorderPane rootLayout;

    private DbSettingProperty dbSettingProperty = new DbSettingProperty();

    /**
     * The data as an observable list of Persons.
     */
    private ObservableList<DbTableProperty> tableData = FXCollections.observableArrayList();

    private List<DBTable> originData = new ArrayList<>();

    /**
     * Constructor
     */
    public DocFXApplication() {
    }

    public String getAppTitle() {
        return appTitle;
    }

    /**
     * Returns the data as an observable list of Persons.
     *
     * @return
     */
    public ObservableList<DbTableProperty> getTableData() {
        return tableData;
    }

    public List<DBTable> getOriginData() {
        return originData;
    }

    public void setOriginData(List<DBTable> originData) {
        this.originData = originData;
    }

    public DbSettingProperty getDbSetting() {
        return dbSettingProperty;
    }

    public BorderPane getRootLayout() {
        return rootLayout;
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle(appTitle);

        // Set the application icon.
        this.primaryStage.getIcons().add(new Image(ResourceLoader.getResource("baseline_history_edu_white_36.png").toString()));

        initRootLayout();

        showTableOverview();

    }

    /**
     * Initializes the root layout and tries to load the last opened
     * table file.
     */
    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ResourceLoader.getFxmlResource("RootLayout.fxml"));
            rootLayout = loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);

            // Give the controller access to the main app.
            RootLayoutController controller = loader.getController();
            controller.setMainApp(this);

            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Try to load last opened table file.
        File file = getPersonFilePath();
        if (file != null) {
            loadTableDataFromFile(file);
        }
    }

    public void reloadDataTask() {
        DatabaseConfig config = DbConfigUtil.getFromSetting(getDbSetting());
        primaryStage.setTitle(appTitle + " - " + config.getDbName());
        ProgressUtil.processOn(new FeatureTask(o -> {
            try {
                this.reloadData(config);
                return TaskResult.silence("");
            } catch (Exception e) {
                return TaskResult.error(e.getMessage());
            }
        }), primaryStage);
    }

    private void reloadData(DatabaseConfig config) {
        List<DBTable> dbTables = getDatabaseTables(config, false);
        setOriginData(dbTables);

        List<DbTableProperty> tableProperties = covertDbTablePropertyList(dbTables);
        // Add observable list data to the table
        tableData.clear();
        tableData.addAll(tableProperties);
    }

    public List<DbTableProperty> covertDbTablePropertyList(List<DBTable> dbTables) {
        if (dbTables != null && !dbTables.isEmpty()) {
            return dbTables.stream().map(dbTable -> {
                DbTableProperty property = new DbTableProperty();
                property.setName(dbTable.getName());
                property.setComment(dbTable.getComment());

                if (dbTable.getColumns() != null) {
                    List<DbColumnProperty> columnProperties = dbTable.getColumns().stream()
                            .map(DbColumnProperty::new).collect(Collectors.toList());
                    property.setColumns(FXCollections.observableArrayList(columnProperties));
                }
                return property;
            }).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    public List<DBTable> getDatabaseTables(DatabaseConfig config, boolean loadColumns) {
        List<DBTable> tables = null;
        try {
            Connection connection = DatabaseUtil.getConnection(config);
            tables = DatabaseProcessor.processor(connection, config.getDbName(), loadColumns);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CommonException("0", e.getMessage());
        }
        return tables;
    }

    public List<DBTable> getDatabaseTablesWithColumns() {
        DatabaseConfig config = DbConfigUtil.getFromSetting(getDbSetting());
        List<DBTable> dbTables = getDatabaseTables(config, true);
        return dbTables;
    }


    public List<DBColumn> getTableColumns(String tableName) {
        DatabaseConfig config = DbConfigUtil.getFromSetting(getDbSetting());
        List<DBColumn> tables = null;
        try {
            Connection connection = DatabaseUtil.getConnection(config);
            tables = TableProcessor.getTableColumns(connection, config.getDbName(), tableName);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CommonException("1000002", e.getMessage());
        }
        return tables;
    }

    /**
     * Shows the table overview inside the root layout.
     */
    public void showTableOverview() {
        try {
            // Load table overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ResourceLoader.getFxmlResource("DbTableOverview.fxml"));
            AnchorPane personOverview = loader.load();

            // Set table overview into the center of root layout.
            rootLayout.setCenter(personOverview);

            // Give the controller access to the main app.
            DbTableOverviewController controller = loader.getController();
            controller.setMainApp(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens a dialog to edit details for the specified table. If the user
     * clicks OK, the changes are saved into the provided table object and true
     * is returned.
     *
     * @param dbTableProperty the table object to be edited
     * @return true if the user clicked OK, false otherwise.
     */
    public boolean showPersonEditDialog(DbTableProperty dbTableProperty) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ResourceLoader.getFxmlResource("DbTableEditDialog.fxml"));
            AnchorPane page = loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Table");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the table into the controller.
            DbTableEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setPerson(dbTableProperty);

            // Set the dialog icon.
            dialogStage.getIcons().add(new Image(ResourceLoader.getResource("edit.png").toString()));

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Opens a dialog to show birthday statistics.
     */
    public void showBirthdayStatistics() {
        try {
            // Load the fxml file and create a new stage for the popup.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ResourceLoader.getFxmlResource("BirthdayStatistics.fxml"));
            AnchorPane page = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Birthday Statistics");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the persons into the controller.
            BirthdayStatisticsController controller = loader.getController();
            controller.setPersonData(tableData);

            // Set the dialog icon.
            dialogStage.getIcons().add(new Image(ResourceLoader.getResource("calendar.png").toString()));

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the table file preference, i.e. the file that was last opened.
     * The preference is read from the OS specific registry. If no such
     * preference can be found, null is returned.
     *
     * @return
     */
    public File getPersonFilePath() {
        Preferences prefs = Preferences.userNodeForPackage(DocFXApplication.class);
        String filePath = prefs.get("filePath", null);
        if (filePath != null) {
            return new File(filePath);
        } else {
            return null;
        }
    }

    /**
     * Sets the file path of the currently loaded file. The path is persisted in
     * the OS specific registry.
     *
     * @param file the file or null to remove the path
     */
    public void setPersonFilePath(File file) {
        Preferences preferences = Preferences.userNodeForPackage(DocFXApplication.class);
        if (file != null) {
            preferences.put("filePath", file.getPath());

            // Update the stage title.
            primaryStage.setTitle(appTitle + " - " + file.getName());
        } else {
            preferences.remove("filePath");

            // Update the stage title.
            primaryStage.setTitle(appTitle);
        }
    }

    /**
     * Loads table data from the specified file. The current table data will
     * be replaced.
     *
     * @param file
     */
    public void loadTableDataFromFile(File file) {
        try {
/*            JAXBContext context = JAXBContext
                    .newInstance(DbTablePropertyListWrapper.class);
            Unmarshaller um = context.createUnmarshaller();

            // Reading XML from the file and unmarshalling.
            DbTablePropertyListWrapper wrapper = (DbTablePropertyListWrapper) um.unmarshal(file);

            tableData.clear();
            tableData.addAll(wrapper.getPersons());*/

            // Save the file path to the registry.
            setPersonFilePath(file);

        } catch (Exception e) { // catches ANY exception
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not load data");
            alert.setContentText("Could not load data from file:\n" + file.getPath());

            alert.showAndWait();
        }
    }

    /**
     * Saves the current table data to the specified file.
     *
     * @param file
     */
    public void savePersonDataToFile(File file) {
        try {
            JAXBContext context = JAXBContext
                    .newInstance(DbTablePropertyListWrapper.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            // Wrapping our table data.
            DbTablePropertyListWrapper wrapper = new DbTablePropertyListWrapper();
            wrapper.setPersons(tableData);

            // Marshalling and saving XML to the file.
            m.marshal(wrapper, file);

            // Save the file path to the registry.
            setPersonFilePath(file);
        } catch (Exception e) { // catches ANY exception
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not save data");
            alert.setContentText("Could not save data to file:\n" + file.getPath());

            alert.showAndWait();
        }
    }

    /**
     * Returns the main stage.
     *
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }

    public boolean showDatabaseSettingDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ResourceLoader.getFxmlResource("DbSettingDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Database Setting");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the table into the controller.
            DbSettingDialogController controller = loader.getController();
            controller.setMainApp(this);
            controller.setDialogStage(dialogStage);
            controller.setDbSetting(dbSettingProperty);

            // Set the dialog icon.
            dialogStage.getIcons().add(new Image(ResourceLoader.getResource("edit.png").toString()));

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            boolean flag = controller.isOkClicked();
            if (flag) {
                reloadDataTask();
            }
            return flag;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean showDocumentLabelDialog() {
        try {
            // Load table overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ResourceLoader.getFxmlResource("LabelFileDialog.fxml"));
            AnchorPane page = loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("标签文档");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the table into the controller.
            LabelFileDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setMainApp(this);

            // Set the dialog icon.
            dialogStage.getIcons().add(new Image(ResourceLoader.getResource("edit.png").toString()));

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
