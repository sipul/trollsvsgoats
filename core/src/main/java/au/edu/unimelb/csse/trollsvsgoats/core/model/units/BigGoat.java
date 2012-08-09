package au.edu.unimelb.csse.trollsvsgoats.core.model.units;

public class BigGoat extends Goat {

    @Override
    void init() {
        setSpeed(0.5f);
        setForce(8);
    }

    @Override
    public String type() {
        return "big";
    }

}
