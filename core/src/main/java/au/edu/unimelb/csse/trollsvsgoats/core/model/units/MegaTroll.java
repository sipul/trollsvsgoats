package au.edu.unimelb.csse.trollsvsgoats.core.model.units;

public class MegaTroll extends Troll {

    @Override
    void init() {
        setSpeed(0.5f);
        setForce(8);
        setCost(25);
    }

    @Override
    public String type() {
        return "mega";
    }
    
}
