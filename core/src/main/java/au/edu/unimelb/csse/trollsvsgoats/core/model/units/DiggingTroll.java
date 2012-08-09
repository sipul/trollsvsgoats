package au.edu.unimelb.csse.trollsvsgoats.core.model.units;

public class DiggingTroll extends Troll {

    @Override
    void init() {
        setSpeed(0);
        setForce(0);
        setCost(20);
    }

    @Override
    public void notifyColliedWithBack() {
        if (!(this.back() instanceof JumpingGoat)) {
            this.back().widget().layer.setVisible(false);
            this.back().setState(State.REMOVED);
            removeBack();
        }
    }

    @Override
    public boolean isOnTrollSide() {
        return false;
    }

    @Override
    public String ability() {
        return "deployed on the goat side,digs a hole that"
                + "\ngoats fall into, can only be placed in the first 4 segments";
    }

    @Override
    public String type() {
        return "digging";
    }

}
