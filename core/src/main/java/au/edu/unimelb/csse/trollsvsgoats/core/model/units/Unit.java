package au.edu.unimelb.csse.trollsvsgoats.core.model.units;

import au.edu.unimelb.csse.trollsvsgoats.core.model.Animation;
import au.edu.unimelb.csse.trollsvsgoats.core.model.Square;
import playn.core.Image;
import playn.core.ImageLayer;
import static playn.core.PlayN.*;

public abstract class Unit {
    public static enum State {
        MOVING, PUSHING, REMOVED, GRABBED, BLOCKED
    };

    // An unit has a reference to its front unit and back unit.
    Unit front;
    Unit back;

    // How many seconds for a unit at normal speed to cover a segment.
    final static int SEGMENT_TIME = 2;

    float speed;
    float force;

    // Controls the moving of the unit.
    float moveDelay;
    float timer;

    Animation moveAnimation;
    Animation pushAnimation;
    Image defaultImage;

    ImageLayer layer;
    Square square;
    State state;
    boolean moved;

    public Unit() {
        init();
        timer = moveDelay;
    }

    /**
     * Sets the speed and force of this unit.
     */
    abstract void init();

    public float speed() {
        return this.speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
        moveDelay = SEGMENT_TIME * 1000 / speed;
        if (moveAnimation != null)
            moveAnimation.setFrameTime(frameTime());
    }

    public void setTimer(float timer) {
        this.timer = timer;
    }

    public float force() {
        return this.force;
    }

    public void setForce(float force) {
        this.force = force;
    }

    public ImageLayer layer() {
        if (this.layer == null)
            layer = graphics().createImageLayer();
        return this.layer;
    }

    public void setLayer(ImageLayer layer) {
        this.layer = layer;
    }

    public void setSquare(Square square) {
        this.square = square;
    }

    public void move(Square square) {
        this.moved = true;
        setSquare(square);
    }

    public Square square() {
        return this.square;
    }

    public float timer() {
        return this.timer;
    }

    public float frameTime() {
        return 100 / this.speed;
    }

    public void reset() {
        init();
        this.moved = false;
        this.timer = moveDelay;
        this.front = null;
        this.back = null;
        layer.setImage(defaultImage != null ? defaultImage : moveAnimation
                .frame(0));
    }

    public float updateTimer(float delta) {
        if (timer <= 0 && moved) {
            timer = moveDelay;
            moved = false;
        }
        timer -= delta;
        return timer;
    }

    public void setMoveAnimation(Animation animation) {
        this.moveAnimation = animation;
        layer().setImage(animation.frame(0));
    }

    public void setPushAnimation(Animation animaiton) {
        this.pushAnimation = animaiton;
    }

    public Unit front() {
        return this.front;
    }

    public void setFront(Unit unit) {
        this.front = unit;
    }

    public Unit back() {
        return this.back;
    }

    public void setBack(Unit unit) {
        this.back = unit;
        if (unit != null)
            unit.setFront(this);
    }

    public void removeFront() {
        Unit front = this.front;
        if (front.front != null) {
            front.front.setBack(this);
        }
        this.setFront(front.front);
    }

    public void removeBack() {
        Unit back = this.back;
        if (back.back != null) {
            back.back.setFront(this);
        }
        this.setBack(back.back);
    }

    public State state() {
        return this.state;
    }

    public void setState(State state) {
        this.state = state;
    }

    /**
     * Sets the image to show when the unit is deployed. If it's not set, then
     * it's the first frame of the move animation.
     */
    public void setDefaultImage(Image image) {
        this.defaultImage = image;
        layer.setImage(image);
    }

    public void notifyColliedWithFront() {
    };

    public void notifyColliedWithBack() {
    };

    public abstract void update(float delta);

    public abstract String type();

}
