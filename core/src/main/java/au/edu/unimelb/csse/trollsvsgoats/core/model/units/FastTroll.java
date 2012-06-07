package au.edu.unimelb.csse.trollsvsgoats.core.model.units;

public class FastTroll extends Troll {

    @Override
    void init() {
        setSpeed(2);
        setForce(1);
    }

    @Override
    public void notifyColliedWithFront() {
        // TODO Auto-generated method stub

    }

}
