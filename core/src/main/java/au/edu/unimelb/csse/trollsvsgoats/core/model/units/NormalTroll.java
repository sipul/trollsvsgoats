package au.edu.unimelb.csse.trollsvsgoats.core.model.units;

public class NormalTroll extends Troll {

    @Override
    void init() {
        setSpeed(1);
        setForce(2);
        setCost(10);
    }

    @Override
    public String type() {
        return "normal";
    }
    
}
