package au.edu.unimelb.csse.trollsvsgoats.core.model.units;

public class SpittingTroll extends Troll {

    @Override
    void init() {
        setSpeed(0);
        setForce(0);
    }

    @Override
    public void notifyColliedWithBack() {
        this.back.setSpeed(this.back.speed() / 2);
    }

    @Override
    public boolean isOnTrollSide() {
        return false;
    }

}
