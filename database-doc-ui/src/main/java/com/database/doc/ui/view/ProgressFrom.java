package com.database.doc.ui.view;

import com.database.doc.ui.task.FeatureTask;
import com.database.doc.ui.task.TaskResult;
import com.database.doc.ui.util.AlertUtil;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.SneakyThrows;

public class ProgressFrom {

    private Stage dialogStage;
    private ProgressIndicator progressIndicator;
    private FeatureTask task;
    private TaskResult result;

    @SneakyThrows
    public ProgressFrom(final FeatureTask task, Stage primaryStage) {
        this.task = task;

        dialogStage = new Stage();
        progressIndicator = new ProgressIndicator();
        // 窗口父子关系
        dialogStage.initOwner(primaryStage);
        dialogStage.initStyle(StageStyle.UNDECORATED);
        dialogStage.initStyle(StageStyle.TRANSPARENT);
        dialogStage.initModality(Modality.APPLICATION_MODAL);

        // progress bar
        Label label = new Label("加载中, 请稍后...");
        label.setTextFill(Color.BLUE);
        progressIndicator.setProgress(-1F);
        progressIndicator.progressProperty().bind(task.progressProperty());

        VBox vBox = new VBox();
        vBox.setSpacing(10);
        vBox.setBackground(Background.EMPTY);
        vBox.getChildren().addAll(progressIndicator, label);

        Scene scene = new Scene(vBox);
        scene.setFill(null);
        dialogStage.setScene(scene);
    }

    @SneakyThrows
    public void activateProgressBar() {
        Thread inner = new Thread(task);
        inner.start();

        task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @SneakyThrows
            public void handle(WorkerStateEvent event) {
                dialogStage.close();

                result = task.get();
                AlertUtil.alert(dialogStage, result.getCode(), "", result.getMessage());
            }
        });
        dialogStage.show();
    }

    public TaskResult getResult() {
        return this.result;
    }

    public Stage getDialogStage() {
        return dialogStage;
    }

    public void cancelProgressBar() {
        dialogStage.close();
    }
}