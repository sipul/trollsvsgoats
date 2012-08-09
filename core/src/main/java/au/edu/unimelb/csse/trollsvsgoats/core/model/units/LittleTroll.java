package au.edu.unimelb.csse.trollsvsgoats.core.model.units;

public class LittleTroll extends Troll {

    @Override
    void init() {
        setSpeed(1);
        setForce(1);
        setCost(5);
    }
    
    @Override
    public String type() {
        return "little";
    }

}
