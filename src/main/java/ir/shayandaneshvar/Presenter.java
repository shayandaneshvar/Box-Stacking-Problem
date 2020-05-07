package ir.shayandaneshvar;

import com.jfoenix.controls.JFXColorPicker;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.AnimationTimer;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import java.awt.*;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.*;
import java.util.function.Function;

public class Presenter implements Initializable {
    @FXML private AnchorPane root;
    @FXML private JFXColorPicker s1Color;
    @FXML private JFXColorPicker s2Color;
    @FXML private JFXColorPicker s3Color;
    @FXML private JFXColorPicker s4Color;
    @FXML private JFXColorPicker s5Color;
    @FXML private JFXColorPicker s6Color;
    @FXML private JFXTextField weightField;
    private List<Box> boxList;
    private Group view;
    private Stage stage;

    @FXML void addBox() {
        try {
            show3DView();
        } catch (URISyntaxException | MalformedURLException e) {
            e.printStackTrace();
        }
        double weight;
        try {
            weight = Double.parseDouble(weightField.getText().trim());
        } catch (NumberFormatException e) {
            weightField.setText("?");
            Toolkit.getDefaultToolkit().beep();
            return;
        }
        Box box = new Box(weight);
        box.addSides(s1Color.getValue(), s2Color.getValue(),
                s3Color.getValue(), s4Color.getValue(),
                s5Color.getValue(), s6Color.getValue());
        boxList.add(box);
        update3DView();
    }
    private void update3DView() {
        clearWindow();
        for (int i = 0; i < boxList.size(); i++) {
            var cube = Cube.getGraphics(boxList.get(i));
            cube.enableRotationAnimation();
            var x = 50 + (i % 8) * 100;
            var y = 50 + (i / 8) * 100;
            cube.setTranslateX(x);
            cube.setTranslateY(y);
            Label label = new Label("Weight:" + boxList.get(i).getWeight());
            label.setTranslateX(x - 25);
            label.setTranslateY(y + 40);
            view.getChildren().addAll(cube, label);
        }
    }
    @FXML void reset() {
        boxList.clear();
        clearWindow();
    }
    private void clearWindow() {
        view.getChildren().clear();
    }
    @FXML void startStacking() {
        if (boxList.isEmpty()) {
            return;
        }
        drawStack(getTallestBoxStack.apply(boxList));
    }
    private void drawStack(List<Box> boxes) {
        clearWindow();
        for (int i = 0; i < boxes.size(); i++) {
            var cube = Cube.getGraphics(boxes.get(i));
            var y = 50 + (i % 8) * 80;
            cube.setTranslateY(y);
            cube.setTranslateX(100);
            Label label = new Label("Weight:" + boxes.get(i).getWeight());
            label.setTranslateY(y + 25);
            label.setTranslateX(200);
            view.getChildren().addAll(cube, label);
            new AnimationTimer() {
                int value = 1;
                int sign = 1;

                @Override
                public void handle(long l) {
                    cube.setRotationAxis(new Point3D(1, 0, 0));
                    cube.setRotate(value);
                    value += sign;
                    if (value == 37 || value == -37) {
                        sign *= -1;
                    }
                }
            }.start();
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        boxList = new ArrayList<>();
        view = new Group();
        stage = new Stage();
        Scene scene = new Scene(view, 800, 610, true,
                SceneAntialiasing.BALANCED);
        stage.setScene(scene);
        Camera camera = new ParallelCamera();
        scene.setCamera(camera);
        stage.initModality(Modality.NONE);
        stage.initStyle(StageStyle.DECORATED);
        stage.getScene().getWindow().addEventFilter(
                WindowEvent.WINDOW_CLOSE_REQUEST, Event::consume);
        stage.setTitle("Colored Box Stacking Problem");
    }
    private void show3DView() throws URISyntaxException, MalformedURLException {
        if (stage.isShowing()) {
            return;
        }
        stage.getIcons().add(new Image(getClass().getResource(
                "/images/cube.png").toURI().toURL().toString()));
        stage.setX(root.getScene().getWindow().getX() - 810);
        stage.initOwner(root.getScene().getWindow());
        stage.show();
    }
    private Function<List<Box>, List<Box>> getTallestBoxStack
            = (list) -> {
        list.sort(Comparator.comparingDouble(Box::getWeight));
        HashMap<Side, Integer>[] valuesList = new HashMap[list.size()];
        for (int i = 0; i < list.size(); i++) {
            valuesList[i] = new HashMap<>();
            for (int j = 0; j < 6; j++) {
                valuesList[i].put(Side.values()[j], 1);
            }
        }

        for (int i = 1; i < list.size(); i++) {
            for (int k = 0; k < i; k++) {
                for (int j = 0; j < 6; j++) {
                    int curValue = valuesList[i].get(Side.values()[j]);
                    Color color = list.get(i).getSides().get(Side.values()[j]);
                    List<Side> sides = list.get(k).getSide_s(color);
                    for (Side s : sides) {
                        int value = valuesList[k].get(s);
                        if (/*curValue <= value &&*/ value <= valuesList[k]
                                .get(s.getTheOtherSide())) {
                            curValue++;
                            break;
                        }
                    }
                    valuesList[i].put(Side.values()[j], curValue);
                }
            }
        }
        int maxIndex = 0;
        int maxValue = 0;
        Side topSide = null;
        for (int i = 0; i < list.size(); i++) {
            int temp = valuesList[i].values()
                    .stream()
                    .mapToInt(Integer::intValue)
                    .max().orElse(0);
            if (temp > maxValue) {
                topSide = valuesList[i].entrySet()
                        .stream()
                        .filter(z -> z.getValue().equals(temp))
                        .map(Map.Entry::getKey)
                        .findAny()
                        .get();
                maxIndex = i;
                maxValue = temp;
            }
        }
        List<Box> result = new ArrayList<>();
        Box box = list.get(maxIndex).setSideOnTop(topSide);
        Color color = box.getTop();
        result.add(box);
        extractAnswer(list, valuesList, maxIndex, result, color);
        Collections.reverse(result);
        return result;
    };
    private void extractAnswer(List<Box> list, HashMap<Side, Integer>[]
            valuesList, int maxIndex, List<Box> result, Color color) {
        if (maxIndex == 0) return;
        int index = 0;
        int maxVal = 0;
        Side top = null;
        for (int i = 0; i < maxIndex; i++) {
            if (!list.get(i).containsColor(color)) continue;
            int temp = valuesList[i].values()
                    .stream().mapToInt(Integer::intValue).max().getAsInt();
            if (temp > maxVal) {
                int finalI = i;
                top = valuesList[i].entrySet()
                        .stream()
                        .filter(z -> z.getValue().equals(temp))
                        .map(Map.Entry::getKey)
                        .filter(x -> list.get(finalI).getSides()
                                .get(x.getTheOtherSide()).equals(color))
                        .findFirst().get();
                index = i;
                maxVal = temp;
            }
        }
        Box wantedBox = list.get(index).setSideOnTop(top);
        Color topColor = wantedBox.getTop();
        result.add(wantedBox);
        if (maxVal == 1) return;
        extractAnswer(list, valuesList, index, result, topColor);
    }

}