package au.edu.unimelb.csse.trollsvsgoats.core.model.units;

public class HungryTroll extends Troll {

    private boolean hasEaten;

    @Override
    void init() {
        setSpeed(1);
        setForce(2);
        setCost(15);
    }

    public void setEaten() {
        this.hasEaten = true;
    }

    public boolean hasEaten() {
        return this.hasEaten;
    }

    @Override
    public void reset() {
        super.reset();
        this.hasEaten = false;
    }

    @Override
    public String ability() {
        return "grab the first goat across the gate"
                + "\nfrom it and eat it removing it from play";
    }

    @Override
    public String type() {
        return "hungry";
    }

}
