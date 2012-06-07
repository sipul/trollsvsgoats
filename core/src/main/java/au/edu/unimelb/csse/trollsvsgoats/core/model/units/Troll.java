package au.edu.unimelb.csse.trollsvsgoats.core.model.units;

public abstract class Troll extends Unit {

    @Override
    public void update(float delta) {
        layer.setTranslation(square.getX(), square.getY());
        if (state == null || this.speed == 0)
            return;
        if (state.equals(State.MOVING))
            layer.setImage(moveAnimation.nextFrame(delta));
        else if (state.equals(State.PUSHING)) {
            if (pushAnimation != null)
                layer.setImage(pushAnimation.nextFrame(delta));
        }
    }

    public boolean isOnTrollSide() {
        return true;
    }

    @Override
    public String type() {
        String name = this.getClass().getSimpleName();
        return name.substring(0, name.indexOf("Troll")).toLowerCase();
    }

}
