package au.edu.unimelb.csse.trollsvsgoats.core.model.units;

import tripleplay.ui.Icons;
import tripleplay.ui.layout.AbsoluteLayout;

public abstract class Troll extends Unit {

    private float cost;

    public float cost() {
        return this.cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    @Override
    public void update(float delta) {
        if (state() == null || state().equals(State.REMOVED)
                || this.speed() == 0)
            return;
        if (timer() <= 0)
            parent.add(AbsoluteLayout.at(widget(), square().getX(), square()
                    .getY()));
        if (state().equals(State.MOVING))
        	widget().icon.update(Icons.image(moveAnimation.nextFrame(delta)));
        else if (state().equals(State.PUSHING)) {
            if (pushAnimation != null)
            	widget().icon.update(Icons.image(pushAnimation.nextFrame(delta)));
        }
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
