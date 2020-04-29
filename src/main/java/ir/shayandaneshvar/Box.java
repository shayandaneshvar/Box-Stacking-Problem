package ir.shayandaneshvar;

import javafx.scene.paint.Color;

import java.util.*;

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
public class Box {
    private final double weight;
    private final HashMap<Side, Color> sides = new HashMap<>();

    public boolean containsColor(Color color) {
        return sides.containsValue(color);
    }

    // get all useful non duplicate boxes 24 possible boxes 3 duplicate for
    // each side so 24 - 6*3 = 6 - or think of how many possible sides could
    // you put on top of the box which is 6 also
    public List<Box> getAllRotations() {
        Box box1 = new Box(weight).addSides(
                sides.get(Side.S5), sides.get(Side.S1), sides.get(Side.S3),
                sides.get(Side.S4), sides.get(Side.S6), sides.get(Side.S2));
        Box box2 = new Box(weight).addSides(
                sides.get(Side.S6), sides.get(Side.S5), sides.get(Side.S3),
                sides.get(Side.S4), sides.get(Side.S2), sides.get(Side.S1));
        Box box3 = new Box(weight).addSides(
                sides.get(Side.S2), sides.get(Side.S6), sides.get(Side.S3),
                sides.get(Side.S4), sides.get(Side.S1), sides.get(Side.S5));
        Box box4 = new Box(weight).addSides(
                sides.get(Side.S1), sides.get(Side.S3), sides.get(Side.S5),
                sides.get(Side.S2), sides.get(Side.S4), sides.get(Side.S6));
        Box box5 = new Box(weight).addSides(
                sides.get(Side.S1), sides.get(Side.S4), sides.get(Side.S2),
                sides.get(Side.S5), sides.get(Side.S3), sides.get(Side.S6));
        return new ArrayList<>(Arrays.asList(box1, box2, box3, box4, box5, this));
    }

    public Box(double weight) {
        this.weight = weight;
    }

    public boolean areParallel(Color side, Color otherSide) {
        if (!(containsColor(side) && containsColor(otherSide))) {
            return false;
        }
        return sides.entrySet()
                .stream()
                .filter(x -> x.getValue().equals(side))
                .map(Map.Entry::getKey)
                .anyMatch(z -> Side.values()[5 - z.ordinal()]
                        .equals(otherSide));
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

    public enum Side {
        // x || y
        //ordinals 0 to 5 => Side(x) + Side(y) = 5
        S1, S2, S3, S4, S5, S6
    }
}
