package au.edu.unimelb.csse.trollsvsgoats.core.model.units;

public class CheerleaderTroll extends Troll {

    @Override
    void init() {
        setSpeed(1);
        setForce(2);
        setCost(20);
    }

    @Override
    public void notifyColliedWithFront() {
        Unit unit = this.front();
        if (unit.state().equals(State.PUSHING)) {
            while (unit != null) {
                unit.setForce(2 * unit.force());
                unit = unit.front();
            }
        }
    }

    @Override
    public String type() {
        return "cheerleader";
    }

}
