package com.database.doc.ui.view;

import com.database.doc.output.ExcelOutPut;
import com.database.doc.ui.DocFXApplication;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;

import java.io.File;

/**
 * The controller for the root layout. The root layout provides the basic
 * application layout containing a menu bar and space where other JavaFX
 * elements can be placed.
 *
 * @author Marco Jakob
 */
public class RootLayoutController {

    // Reference to the main application
    private DocFXApplication mainApp;

    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp
     */
    public void setMainApp(DocFXApplication mainApp) {
        this.mainApp = mainApp;
    }

    /**
     * Creates an empty address book.
     */
    @FXML
    private void handleNew() {
        // mainApp.getPersonData().clear();
        // mainApp.setPersonFilePath(null);
        mainApp.showDatabaseSettingDialog();
    }

    /**
     * Opens a FileChooser to let the user select an address book to load.
     */
    @FXML
    private void handleOpen() {
        FileChooser fileChooser = new FileChooser();

        // Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show save file dialog
        File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());

        if (file != null) {
            mainApp.loadTableDataFromFile(file);
        }
    }

    /**
     * Saves the file to the table file that is currently open. If there is no
     * open file, the "save as" dialog is shown.
     */
    @FXML
    private void handleSave() {
        FileChooser fileChooser = new FileChooser();
        String fileName = "数据库表结构" + ExcelOutPut.getFileTimeName() + ".xlsx";
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel File(*.xlsx)", ".xlsx"));
        fileChooser.setInitialFileName(fileName);
        File file = fileChooser.showSaveDialog(mainApp.getPrimaryStage());
        if (file != null) {
            //将要写入的内容写到file文件中
            ExcelOutPut.exportXLSXFile(this.mainApp.getDatabaseTablesWithColumns(), file);
        }
    }

    /**
     * Opens a FileChooser to let the user select a file to save to.
     */
    @FXML
    private void handleSaveAs() {
        FileChooser fileChooser = new FileChooser();

        // Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show save file dialog
        File file = fileChooser.showSaveDialog(mainApp.getPrimaryStage());

        if (file != null) {
            // Make sure it has the correct extension
            if (!file.getPath().endsWith(".xml")) {
                file = new File(file.getPath() + ".xml");
            }
            mainApp.savePersonDataToFile(file);
        }
    }

    /**
     * Opens an about dialog.
     */
    @FXML
    private void handleAbout() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(mainApp.getAppTitle());
        alert.setHeaderText("About");
        alert.setContentText("Author: LiChengQi " +
                "\nEmail: 1024020320@qq.com" +
                "\nWebsite: http://lichengqi.com.ch");

        alert.showAndWait();
    }

    /**
     * Opens an about dialog.
     */
    @FXML
    private void handleSetting() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(mainApp.getAppTitle());
        alert.setHeaderText("About");
        alert.setContentText("Author: LiChengQi " +
                "\nEmail: 1024020320@qq.com" +
                "\nWebsite: http://lichengqi.com.ch");

        alert.showAndWait();
    }

    /**
     * Opens an about dialog.
     */
    @FXML
    private void handleDocumentLabel() {
        mainApp.showDocumentLabelDialog();
    }

    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
        System.exit(0);
    }

    /**
     * Opens the birthday statistics.
     */
    @FXML
    private void handleShowBirthdayStatistics() {
        mainApp.showBirthdayStatistics();
    }
}