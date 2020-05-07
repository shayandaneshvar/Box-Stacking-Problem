package ir.shayandaneshvar;

import javafx.animation.AnimationTimer;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
//For Visualization
//JavaFX 3D Coordinate => y & z axis are reverse
public class Cube extends Group {
    private Box box1;
    private Box box2;
    private Box box3;
    private Box box4;
    private Box box5;
    private Box box6;

    public Cube(double length) {
        box1 = new Box(length, length, 0.5);
        box2 = new Box(length, 0.5, length);
        box3 = new Box(0.5, length, length);
        box4 = new Box(0.5, length, length);
        box5 = new Box(length, 0.5, length);
        box6 = new Box(length, length, 0.5);
        box1.setTranslateZ(-length / 2);
        box2.setTranslateY(-length / 2);
        box3.setTranslateX(length / 2);
        box4.setTranslateX(-length / 2);
        box5.setTranslateY(length / 2);
        box6.setTranslateZ(length / 2);
        getChildren().addAll(box1, box2, box3, box4, box5, box6);
    }
    public void enableRotationAnimation() {
        new AnimationTimer() {
            public void handle(long l) {
                setRotationAxis(new Point3D(1, 1, 1));
                setRotate(getRotate() + 3);
            }
        }.start();
    }

    public void setColorSide1(Color color) {
        box1.setMaterial(new PhongMaterial(color));
    }

    public void setColorSide2(Color color) {
        box2.setMaterial(new PhongMaterial(color));
    }

    public void setColorSide3(Color color) {
        box3.setMaterial(new PhongMaterial(color));
    }

    public void setColorSide4(Color color) {
        box4.setMaterial(new PhongMaterial(color));
    }

    public void setColorSide5(Color color) {
        box5.setMaterial(new PhongMaterial(color));
    }

    public void setColorSide6(Color color) {
        box6.setMaterial(new PhongMaterial(color));
    }

    public static Cube getGraphics(ir.shayandaneshvar.Box box, int length) {
        var cube = new Cube(length);
        cube.setColorSide1(box.getSides().get(Side.S1));
        cube.setColorSide2(box.getSides().get(Side.S2));
        cube.setColorSide3(box.getSides().get(Side.S3));
        cube.setColorSide4(box.getSides().get(Side.S4));
        cube.setColorSide5(box.getSides().get(Side.S5));
        cube.setColorSide6(box.getSides().get(Side.S6));
        return cube;
    }

    public static Cube getGraphics(ir.shayandaneshvar.Box box) {
        return getGraphics(box, 50);
    }
}
