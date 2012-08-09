package au.edu.unimelb.csse.trollsvsgoats.core.model.units;

public class BarrowTroll extends Troll {

    @Override
    void init() {
        setSpeed(2);
        setForce(2);
        setCost(15);
    }

    @Override
    public void notifyColliedWithFront() {
        this.front().setSpeed(this.speed());
        this.front().setTimer(this.timer());
    }

    @Override
    public String type() {
        return "barrow";
    }

}
