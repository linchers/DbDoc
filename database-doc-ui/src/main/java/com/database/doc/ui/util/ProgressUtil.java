package com.database.doc.ui.util;

import com.database.doc.ui.task.FeatureTask;
import com.database.doc.ui.task.TaskResult;
import com.database.doc.ui.view.ProgressFrom;
import javafx.stage.Stage;

public class ProgressUtil {
    public static TaskResult processOn(FeatureTask task, Stage primaryStage) {
        ProgressFrom progressFrom = new ProgressFrom(task, primaryStage);
        progressFrom.activateProgressBar();
        return progressFrom.getResult();
    }
}
