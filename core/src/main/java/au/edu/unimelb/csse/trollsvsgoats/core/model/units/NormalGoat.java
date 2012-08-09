package au.edu.unimelb.csse.trollsvsgoats.core.model.units;

public class NormalGoat extends Goat {

    @Override
    void init() {
        setSpeed(1);
        setForce(4);
    }
    
    @Override
    public String type() {
        return "normal";
    }

}
