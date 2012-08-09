package au.edu.unimelb.csse.trollsvsgoats.core.model.units;

public class Obstacle extends Unit {

    @Override
    void init() {
        setSpeed(0);
        setForce(0);
    }

    @Override
    public void notifyColliedWithBack() {
        this.back().setState(State.BLOCKED);
    }

    @Override
    public void update(float delta) {
        // TODO Auto-generated method stub

    }

    @Override
    public String type() {
        return "obstacle";
    }

}
