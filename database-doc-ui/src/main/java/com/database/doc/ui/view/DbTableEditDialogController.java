package com.database.doc.ui.view;

import com.database.doc.ui.model.DbTableProperty;
import com.database.doc.ui.util.DateUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Dialog to edit details of a table.
 * 
 * @author Marco Jakob
 */
public class DbTableEditDialogController {

    @FXML
    private TextField tableNameField;
    @FXML
    private TextField tableCommentField;

    private Stage dialogStage;
    private DbTableProperty dbTableProperty;
    private boolean okClicked = false;

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
     * @param dbTableProperty
     */
    public void setPerson(DbTableProperty dbTableProperty) {
        this.dbTableProperty = dbTableProperty;

        tableNameField.setText(dbTableProperty.getName());
        tableCommentField.setText(dbTableProperty.getComment());
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
            dbTableProperty.setName(tableNameField.getText());
            dbTableProperty.setComment(tableCommentField.getText());

            okClicked = true;
            dialogStage.close();
        }
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

        if (tableNameField.getText() == null || tableNameField.getText().length() == 0) {
            errorMessage += "No valid first name!\n"; 
        }
        if (tableCommentField.getText() == null || tableCommentField.getText().length() == 0) {
            errorMessage += "No valid last name!\n"; 
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