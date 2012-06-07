package au.edu.unimelb.csse.trollsvsgoats.core.model.units;

public class HungryTroll extends Troll {

    @Override
    void init() {
        setSpeed(1);
        setForce(2);
    }

    @Override
    public void notifyColliedWithFront() {
        if (!(this.front instanceof Obstacle))
            this.front.setState(State.GRABBED);
    }

}
