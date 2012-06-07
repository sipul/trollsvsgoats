package au.edu.unimelb.csse.trollsvsgoats.core.model;

public class Square {
    private int lane;
    private int segment;
    private int distance;
    private float x;
    private float y;

    public Square(int lane, int segment) {
        this.lane = lane;
        this.segment = segment;
    }

    public int lane() {
        return this.lane;
    }

    public int segment() {
        return this.segment;
    }

    public int distance() {
        return this.distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return String.valueOf(lane) + String.valueOf(segment);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Square)) {
            return false;
        }

        Square that = (Square) obj;
        if (this.toString().equals(that.toString())) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }

}
