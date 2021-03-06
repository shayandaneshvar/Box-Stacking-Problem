package ir.shayandaneshvar;

import javafx.scene.paint.Color;

import java.util.*;
import java.util.stream.Collectors;

/**
 * ----______
 * --/   2  /|
 * -/______/ |
 * |       |3|
 * |   1   | |
 * |_______|/
 * <p>
 * x || y : Side(x) + Side(y) = 7
 *
 * @author shayan daneshvar
 *
 */
public class Box  {
    private final double weight;
    private final HashMap<Side, Color> sides = new HashMap<>();

    public boolean containsColor(Color color) {
        return sides.containsValue(color);
    }

    private Box getS1OnTop() {
        return new Box(weight).addSides(
                sides.get(Side.S5), sides.get(Side.S1), sides.get(Side.S3),
                sides.get(Side.S4), sides.get(Side.S6), sides.get(Side.S2));
    }
    private Box getS5OnTop() {
        return new Box(weight).addSides(
                sides.get(Side.S6), sides.get(Side.S5), sides.get(Side.S3),
                sides.get(Side.S4), sides.get(Side.S2), sides.get(Side.S1));
    }
    private Box getS6OnTop() {
        return new Box(weight).addSides(
                sides.get(Side.S2), sides.get(Side.S6), sides.get(Side.S3),
                sides.get(Side.S4), sides.get(Side.S1), sides.get(Side.S5));
    }
    private Box getS3OnTop() {
        return new Box(weight).addSides(
                sides.get(Side.S1), sides.get(Side.S3), sides.get(Side.S5),
                sides.get(Side.S2), sides.get(Side.S4), sides.get(Side.S6));
    }
    private Box getS4OnTop() {
        return new Box(weight).addSides(
                sides.get(Side.S1), sides.get(Side.S4), sides.get(Side.S2),
                sides.get(Side.S5), sides.get(Side.S3), sides.get(Side.S6));
    }

    public Box(double weight) {
        this.weight = weight;
    }

    public Box setSideOnTop(Side side) {
        Box box = null;
        switch (side) {
            case S1:
                box = getS1OnTop();
                break;
            case S2:
                box = this;
                break;
            case S3:
                box = getS3OnTop();
                break;
            case S4:
                box = getS4OnTop();
                break;
            case S5:
                box = getS5OnTop();
                break;
            case S6:
                box = getS6OnTop();
                break;
        }
        return box;
    }

    public List<Side> getSide_s(Color color) {
        return sides.entrySet()
                .stream()
                .filter(x -> x.getValue().equals(color))
                .map(Map.Entry::getKey).collect(Collectors.toList());
    }

    public double getWeight() {
        return weight;
    }

    public Color getFront() {
        return sides.get(Side.S1);
    }

    public Color getRear() {
        return sides.get(Side.S6);
    }

    public Color getTop() {
        return sides.get(Side.S2);
    }

    public Color getBottom() {
        return sides.get(Side.S5);
    }

    public Color getRight() {
        return sides.get(Side.S3);
    }

    public Color getLeft() {
        return sides.get(Side.S4);
    }

    public Map<Side, Color> getSides() {
        return sides;
    }

    public void addSide(Side side, Color color) {
        sides.put(side, color);
    }

    public Box addSides(Color... colors) {
        if (sides.size() != 0) {
            throw new IllegalStateException("Sides Has Been Added Already!");
        } else if (colors.length != 6) {
            throw new IllegalArgumentException(colors.length + " != 6");
        }
        for (int i = 0; i < 6; i++) {
            addSide(Side.values()[i], colors[i]);
        }
        return this;
    }

    public String toString() {
        return "Box(weight=" + this.getWeight() + ", sides=" + this.getSides() + ")";
    }
}
