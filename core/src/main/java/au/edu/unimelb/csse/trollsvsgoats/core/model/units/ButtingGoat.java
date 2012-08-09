package au.edu.unimelb.csse.trollsvsgoats.core.model.units;

public class ButtingGoat extends Goat {

    @Override
    void init() {
        setSpeed(1);
        setForce(2);
    }

    @Override
    public void notifyColliedWithFront() {
        if (this.front() instanceof DiggingTroll
                || this.front() instanceof SpittingTroll) {
            this.front().widget().layer.destroy();
            removeFront();
        }
    }

    @Override
    public String ability() {
        return "will remove digging and spitting trolls";
    }

    @Override
    public String type() {
        return "butting";
    }

}
