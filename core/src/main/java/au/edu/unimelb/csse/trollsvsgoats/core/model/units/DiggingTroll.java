package au.edu.unimelb.csse.trollsvsgoats.core.model.units;

public class DiggingTroll extends Troll {

    @Override
    void init() {
        setSpeed(0);
        setForce(0);
    }

    @Override
    public void notifyColliedWithBack() {
        this.back.layer().destroy();
        removeBack();
    }

    @Override
    public boolean isOnTrollSide() {
        return false;
    }

}
