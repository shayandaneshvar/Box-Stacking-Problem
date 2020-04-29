package ir.shayandaneshvar;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URISyntaxException;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @FXML
    @Override
    public void start(Stage stage) throws URISyntaxException, IOException {
        AnchorPane root = FXMLLoader
                .load(getClass()
                        .getResource("/Main.fxml")
                        .toURI()
                        .toURL());
        Scene scene = new Scene(root, root.getPrefWidth(), root.getPrefHeight(),
                false, SceneAntialiasing.BALANCED);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.initStyle(StageStyle.UTILITY);
        stage.setTitle("Box Toolkit");
        stage.setX(Screen.getPrimary().getBounds().getMaxX() - 470);
        stage.show();
    }
}
