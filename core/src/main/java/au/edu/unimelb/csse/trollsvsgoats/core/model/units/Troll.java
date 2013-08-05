package au.edu.unimelb.csse.trollsvsgoats.core.model.units;


public abstract class Troll extends Unit {

    private float cost;

    public float cost() {
        return this.cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public boolean isOnTrollSide() {
        return true;
    }

    // @Override
    // public String type() {
    // String name = this.getClass().getSimpleName();
    // return name.substring(0, name.indexOf("Troll")).toLowerCase();
    // }

}
