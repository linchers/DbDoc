package com.database.doc.ui.view;

import com.database.doc.domain.DBColumn;
import com.database.doc.ui.DocFXApplication;
import com.database.doc.ui.cell.IDCell;
import com.database.doc.ui.model.DbColumnProperty;
import com.database.doc.ui.model.DbTableProperty;
import com.database.doc.ui.model.LabelFileProperty;
import com.database.doc.ui.task.FeatureTask;
import com.database.doc.ui.task.TaskResult;
import com.database.doc.ui.util.ProgressUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DbTableOverviewController {
    @FXML
    private TableView<DbTableProperty> itemTable;
    @FXML
    private CheckBox checkAllBox;
    @FXML
    private TableColumn<DbTableProperty, CheckBox> checkBox;
/*    @FXML
    private TableColumn<LabelFileProperty, String> tableNumberColumn;*/
    @FXML
    private TableColumn<DbTableProperty, String> tableNameColumn;
    @FXML
    private TableColumn<DbTableProperty, String> tableCommentColumn;
    @FXML
    private TableColumn<DbColumnProperty, String> columnNameColumn;
    @FXML
    private TableColumn<DbColumnProperty, String> columnCommentColumn;
    @FXML
    private TableColumn<DbColumnProperty, String> columnTypeColumn;
    @FXML
    private TableColumn<DbColumnProperty, Integer> columnSizeColumn;
    @FXML
    private TableColumn<DbColumnProperty, String> columnNullAbleColumn;
    @FXML
    private TableView<DbColumnProperty> columnTable;
    @FXML
    private Label tableNameLabel;
    @FXML
    private Label tableCommentLabel;

    // Reference to the main application.
    private DocFXApplication mainApp;
    private ObservableList<DbColumnProperty> columnsData = FXCollections.observableArrayList();

    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public DbTableOverviewController() {
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        // Initialize the table table with the two columns.
        checkBox.setSortable(false);
        checkBox.setCellValueFactory(new PropertyValueFactory<>("checkBox"));
//        tableNumberColumn.setCellFactory(new IDCell<>()); //通过这个类实现自动增长的序号

        tableNameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        tableCommentColumn.setCellValueFactory(cellData -> cellData.getValue().commentProperty());

        columnNameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        columnCommentColumn.setCellValueFactory(cellData -> cellData.getValue().commentProperty());
        columnTypeColumn.setCellValueFactory(cellData -> cellData.getValue().typeProperty());
        columnSizeColumn.setCellValueFactory(cellData -> cellData.getValue().sizeProperty().asObject());
        columnNullAbleColumn.setCellValueFactory(cellData -> cellData.getValue().nullAbleProperty());

        // Clear table details.
        showPersonDetails(null);

        // Listen for selection changes and show the table details when changed.
        itemTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showPersonDetails(newValue));
    }

    /**
     * Is called by the main application to give a reference back to itself.
     * 
     * @param mainApp
     */
    public void setMainApp(DocFXApplication mainApp) {
        this.mainApp = mainApp;
        itemTable.setItems(mainApp.getTableData());

        mainApp.reloadDataTask();
    }

    /**
     * Fills all text fields to show details about the table.
     * If the specified table is null, all text fields are cleared.
     * 
     * @param dbTableProperty the table or null
     */
    private void showPersonDetails(DbTableProperty dbTableProperty) {
        if (dbTableProperty != null) {
            // Fill the labels with info from the table object.
            tableNameLabel.setText(dbTableProperty.getName());
            tableCommentLabel.setText(dbTableProperty.getComment());

            columnTable.setItems(columnsData);

            columnsData.clear();
            if (dbTableProperty.getColumns() != null) {
                columnsData.addAll(dbTableProperty.getColumns());
            } else {
                ProgressUtil.processOn(new FeatureTask(new Function<Object, TaskResult>() {
                    @Override
                    public TaskResult apply(Object o) {
                        try {
                            List<DBColumn> columns = mainApp.getTableColumns(dbTableProperty.getName());
                            List<DbColumnProperty> columnProperties = columns.stream()
                                    .map(DbColumnProperty::new).collect(Collectors.toList());
                            columnsData.addAll(columnProperties);
                            return TaskResult.silence("");
                        } catch (Exception e) {
                            return TaskResult.error(e.getMessage());
                        }
                    }
                }), mainApp.getPrimaryStage());
            }
        } else {
            // Table is null, remove all the text.
            tableNameLabel.setText("");
            tableCommentLabel.setText("");
        }
    }

    /**
     * Called when the user clicks on the delete button.
     */
    @FXML
    private void handleDeletePerson() {
        int selectedIndex = itemTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            itemTable.getItems().remove(selectedIndex);
        } else {
            // Nothing selected.
            Alert alert = new Alert(AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("No Selection");
            alert.setHeaderText("No Table Selected");
            alert.setContentText("Please select a table in the table.");
            
            alert.showAndWait();
        }
    }
    
    /**
     * Called when the user clicks the new button. Opens a dialog to edit
     * details for a new table.
     */
    @FXML
    private void handleCheckAll() {
        ObservableList<DbTableProperty> items = itemTable.getItems();
        for (DbTableProperty property : items) {
            property.getCheckBox().setSelected(checkAllBox.isSelected());
        }
    }

    /**
     * Called when the user clicks the new button. Opens a dialog to edit
     * details for a new table.
     */
    @FXML
    private void handleNewPerson() {
        DbTableProperty tempDbTableProperty = new DbTableProperty();
        boolean okClicked = mainApp.showPersonEditDialog(tempDbTableProperty);
        if (okClicked) {
            mainApp.getTableData().add(tempDbTableProperty);
        }
    }

    /**
     * Called when the user clicks the edit button. Opens a dialog to edit
     * details for the selected table.
     */
    @FXML
    private void handleEditPerson() {
        DbTableProperty selectedDbTableProperty = itemTable.getSelectionModel().getSelectedItem();
        if (selectedDbTableProperty != null) {
            boolean okClicked = mainApp.showPersonEditDialog(selectedDbTableProperty);
            if (okClicked) {
                showPersonDetails(selectedDbTableProperty);
            }

        } else {
            // Nothing selected.
            Alert alert = new Alert(AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("No Selection");
            alert.setHeaderText("No Table Selected");
            alert.setContentText("Please select a table in the table.");
            
            alert.showAndWait();
        }
    }
}