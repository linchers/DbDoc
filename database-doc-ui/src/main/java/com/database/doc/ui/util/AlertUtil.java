package com.database.doc.ui.util;

import com.database.doc.ui.task.TaskResult;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;

public class AlertUtil {

    public static Alert alert(Stage dialogStage, String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initOwner(dialogStage);
        alert.setTitle("提示");
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
        return alert;
    }

    public static Alert information(Stage dialogStage, String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initOwner(dialogStage);
        alert.setTitle("提示");
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
        return alert;
    }

    public static Alert error(Stage dialogStage, String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initOwner(dialogStage);
        alert.setTitle("提示");
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
        return alert;
    }

    public static Alert alert(Stage dialogStage, Integer level, String headerText, String contentText) {
        if (level == TaskResult.SILENCE) {
            return null;
        }
        Alert.AlertType type = Alert.AlertType.INFORMATION;
        if (level == TaskResult.ERROR) {
            type = Alert.AlertType.ERROR;
        }

        Alert alert = new Alert(type);
        alert.initOwner(dialogStage);
        alert.setTitle("提示");
        alert.setHeaderText(getDefaultHeaderText(level, headerText));
        alert.setContentText(contentText);
        alert.showAndWait();
        return alert;
    }

    private static String getDefaultHeaderText(Integer level, String text) {
        String headerTitle = "Success";
        if (StringUtils.isNotBlank(headerTitle)) {
            headerTitle = text;
        } else {
            if (level == TaskResult.ERROR) {
                headerTitle = "Error";
            }
        }
        return headerTitle;
    }

}
