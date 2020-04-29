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
import java.util.stream.Collectors;

public class Presenter implements Initializable {
    @FXML
    private AnchorPane root;
    @FXML
    private JFXColorPicker s1Color;
    @FXML
    private JFXColorPicker s2Color;
    @FXML
    private JFXColorPicker s3Color;
    @FXML
    private JFXColorPicker s4Color;
    @FXML
    private JFXColorPicker s5Color;
    @FXML
    private JFXColorPicker s6Color;
    @FXML
    private JFXTextField weightField;

    private List<Box> boxList;

    private Group view;
    private Stage stage;

    @FXML
    void addBox(MouseEvent event) {
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

    @FXML
    void reset(MouseEvent event) {
        boxList.clear();
        clearWindow();
    }

    private void clearWindow() {
        view.getChildren().clear();
    }

    @FXML
    void startStacking(MouseEvent event) {
        drawStack(calculateAllIncreasingSubsequences
                .andThen(getTheHighestPossibleStack)
                .apply(boxList)
                .stream()
                .peek(z -> System.out.println("Bottom:" + z.getBottom() + " " +
                        "Top:" + z.getTop() + " Weight:" + z.getWeight()))
                .collect(Collectors.toList()));

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

    private Function<List<Box>, List<List<Box>>> calculateAllIncreasingSubsequences
            = (list) -> {
        int[] lis = new int[list.size()];
        ArrayList<Box>[] lists = new ArrayList[list.size()];
        for (int i = 0; i < list.size(); i++) {
            lis[i] = 1;
            lists[i] = new ArrayList<>();
            lists[i].add(list.get(i));
        }
        for (int i = 1; i < list.size(); i++) {
            for (int j = 0; j < i; j++) {
                if (list.get(i).getWeight() > list.get(j).getWeight()
                        && lis[i] < lis[j] + 1) {
                    lis[i]++;
                    lists[i].add(lists[i].size() - 1, list.get(j));
                }
            }
        }
        return Arrays.asList(lists);
    };

    private Function<List<List<Box>>, List<Box>> getTheHighestPossibleStack =
            (lists) -> {
                lists.sort(Comparator.comparingInt(List::size));
                List<Box> answer = null;
                for (int i = lists.size() - 1; i >= 0; i--) {
                    answer = getAnswer(lists.get(i));
                    if (answer != null && answer.isEmpty()) {
                        break;
                    }
                }
                return answer;
            };

    // Find whether the answer is acceptable or not
    private List<Box> getAnswer(List<Box> boxes) {
        //all variations of each cube - n cubes each having a set of 6 rotations
        Set<Box>[] initial = new HashSet[boxes.size()];
        Set<ArrayList<Box>> answer = new HashSet<>();
        // solving for bottom - O(6n)
        for (int i = 0; i < boxes.size(); i++) {
            initial[i] = new HashSet<>();
            for (Box box : boxes.get(i).getAllRotations()) {
                initial[i].add(box);
                if (i == 0) {
                    answer.add(new ArrayList<>(List.of(box)));
                }
            }
        }
        for (int i = 1; i < boxes.size(); i++) {
            answer = findCompatibles(answer, initial[i]);
        }
        return answer//get any possible solution with max Height
                .stream()
                .reduce((x, y) -> x.size() > y.size() ? x : y)
                .orElse(new ArrayList<>());
    }

    private Set<ArrayList<Box>> findCompatibles(Set<ArrayList<Box>> answer,
                                                Set<Box> boxes) {
        Set<ArrayList<Box>> result = new HashSet<>();
        for (ArrayList<Box> seq : answer) {
            for (Box box : boxes) {
                if (seq.get(seq.size() - 1).getBottom().equals(box.getTop())) {
                    ArrayList<Box> acceptableSeq = (ArrayList<Box>) seq.clone();
                    acceptableSeq.add(box);
                    result.add(acceptableSeq);
                }
            }
        }
        return result;
    }
}
