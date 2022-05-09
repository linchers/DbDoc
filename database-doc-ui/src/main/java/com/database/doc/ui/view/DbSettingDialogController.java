package com.database.doc.ui.view;

import com.database.doc.config.DatabaseConfig;
import com.database.doc.ui.DocFXApplication;
import com.database.doc.ui.model.DbSettingProperty;
import com.database.doc.ui.task.TaskResult;
import com.database.doc.ui.task.FeatureTask;
import com.database.doc.ui.util.ProgressUtil;
import com.database.doc.util.DatabaseUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.sql.Connection;

/**
 * Dialog to edit details of a table.
 *
 * @author Marco Jakob
 */
public class DbSettingDialogController {

    private DocFXApplication mainApp;

    @FXML
    private TextField hostField;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField portField;
    @FXML
    private TextField dbNameField;


    private Stage dialogStage;
    private DbSettingProperty dbSettingProperty;
    private boolean okClicked = false;

    public DocFXApplication getMainApp() {
        return mainApp;
    }

    public void setMainApp(DocFXApplication mainApp) {
        this.mainApp = mainApp;
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
    }

    /**
     * Sets the stage of this dialog.
     *
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;

        // Set the dialog icon.
        this.dialogStage.getIcons().add(new Image("file:resources/images/edit.png"));
    }

    /**
     * Sets the table to be edited in the dialog.
     *
     * @param dbSettingProperty
     */
    public void setDbSetting(DbSettingProperty dbSettingProperty) {
        this.dbSettingProperty = dbSettingProperty;

        hostField.setText(dbSettingProperty.getHost());
        usernameField.setText(dbSettingProperty.getUsername());
        passwordField.setText(dbSettingProperty.getPassword());
        portField.setText(Integer.toString(dbSettingProperty.getPort()));
        dbNameField.setText(dbSettingProperty.getDbName());
    }

    /**
     * Returns true if the user clicked OK, false otherwise.
     *
     * @return
     */
    public boolean isOkClicked() {
        return okClicked;
    }

    /**
     * Called when the user clicks ok.
     */
    @FXML
    private void handleOk() {
        if (isInputValid()) {
            dbSettingProperty.setHost(hostField.getText());
            dbSettingProperty.setUsername(usernameField.getText());
            dbSettingProperty.setPassword(passwordField.getText());
            dbSettingProperty.setPort(Integer.parseInt(portField.getText()));
            dbSettingProperty.setDbName(dbNameField.getText());

            okClicked = true;
            dialogStage.close();
        }
    }

    /**
     * Called when the user clicks cancel.
     */
    @FXML
    private void handleConnectionTest() {
        if (!isInputValid()) {
            return;
        }
        FeatureTask task = new FeatureTask(obj -> {
            Connection connection = null;
            try {
                DatabaseConfig config = getFormSetting();
                connection = DatabaseUtil.getConnection(config);
                if (connection != null) {
                    // Show the error message.
                    return new TaskResult(1, "Connection Success!");
                }
            } catch (Exception e) {
                return new TaskResult(0, e.getMessage());
            } finally {
                DatabaseUtil.releaseConnection(connection);
            }
            return new TaskResult(0, "Connection Error!");
        });
        ProgressUtil.processOn(task, getMainApp().getPrimaryStage());
    }

    private DatabaseConfig getFormSetting() {
        DatabaseConfig setting = new DatabaseConfig();
        setting.setHost(hostField.getText());
        setting.setUsername(usernameField.getText());
        setting.setPassword(passwordField.getText());
        setting.setPort(Integer.parseInt(portField.getText()));
        setting.setDbName(dbNameField.getText());
        return setting;
    }

    /**
     * Called when the user clicks cancel.
     */
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    /**
     * Validates the user input in the text fields.
     *
     * @return true if the input is valid
     */
    private boolean isInputValid() {
        String errorMessage = "";

        if (hostField.getText() == null || hostField.getText().length() == 0) {
            errorMessage += "No valid host!\n";
        }
        if (usernameField.getText() == null || usernameField.getText().length() == 0) {
            errorMessage += "No valid username!\n";
        }
        if (passwordField.getText() == null || passwordField.getText().length() == 0) {
            errorMessage += "No valid pwd!\n";
        }

        if (portField.getText() == null || portField.getText().length() == 0) {
            errorMessage += "No valid port!\n";
        } else {
            // try to parse the postal code into an int.
            try {
                Integer.parseInt(portField.getText());
            } catch (NumberFormatException e) {
                errorMessage += "No valid port (must be an integer)!\n";
            }
        }

        if (dbNameField.getText() == null || dbNameField.getText().length() == 0) {
            errorMessage += "No valid dababase name!\n";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
            Alert alert = new Alert(AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage);

            alert.showAndWait();

            return false;
        }
    }
}