package ir.shayandaneshvar;

public enum Side {
    // x || y
    //ordinals 0 to 5 => Side(x) + Side(y) = 5
    S1, S2, S3, S4, S5, S6;

    public static Side getTheOtherSide(Side s) {
        return Side.values()[5 - s.ordinal()];
    }

    public Side getTheOtherSide() {
        return getTheOtherSide(this);
    }
}