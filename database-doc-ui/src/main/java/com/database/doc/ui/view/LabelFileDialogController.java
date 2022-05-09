package com.database.doc.ui.view;

import com.database.doc.ui.DocFXApplication;
import com.database.doc.ui.cell.IDCell;
import com.database.doc.ui.model.LabelFileProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LabelFileDialogController {
    private DocFXApplication mainApp;

    @FXML
    private TableView<LabelFileProperty> itemTable;
    @FXML
    private TableColumn<LabelFileProperty, String> tableNumberColumn;
    @FXML
    private TableColumn<LabelFileProperty, String> tableNameColumn;
    @FXML
    private TableColumn<LabelFileProperty, String> tableDateColumn;

    private ObservableList<LabelFileProperty> fileList = FXCollections.observableArrayList();

    private Stage dialogStage;
    private boolean okClicked = false;

    public void setMainApp(DocFXApplication mainApp) {
        this.mainApp = mainApp;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        tableNumberColumn.setCellFactory(new IDCell<>()); //通过这个类实现自动增长的序号
        // Initialize the table table with the two columns.
        tableNameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        tableDateColumn.setCellValueFactory(cellData -> cellData.getValue().dateStrProperty());

        LabelFileProperty fileProperty = new LabelFileProperty();
        fileProperty.setName("这是一个label文档");
        fileList.add(fileProperty);
        itemTable.setItems(fileList);
    }

    //handleUpload
    /**
     * Called when the user clicks the new button. Opens a dialog to edit
     * details for a new table.
     */
    @FXML
    private void handleUpload() {
        FileChooser fileChooser = new FileChooser();
        // Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "Excel files (*.xlsx)", "*.xlsx");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show save file dialog
        File file = fileChooser.showOpenDialog(dialogStage);

        if(file != null) {
            LabelFileProperty fileProperty = new LabelFileProperty();
            fileProperty.setName(file.getName());
            fileProperty.setDateStr(SimpleDateFormat.getDateInstance().format(new Date()));
            fileList.add(fileProperty);
        }
    }

}
