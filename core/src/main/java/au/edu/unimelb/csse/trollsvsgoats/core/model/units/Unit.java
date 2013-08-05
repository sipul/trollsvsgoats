package au.edu.unimelb.csse.trollsvsgoats.core.model.units;

import au.edu.unimelb.csse.trollsvsgoats.core.model.Animation;
import au.edu.unimelb.csse.trollsvsgoats.core.model.Square;
import au.edu.unimelb.csse.trollsvsgoats.core.view.LevelScreen;
import playn.core.Image;
import tripleplay.ui.Button;
import tripleplay.ui.Group;
import tripleplay.ui.Icons;
import tripleplay.ui.layout.AbsoluteLayout;

public abstract class Unit {
    public static enum State {
        MOVING, PUSHING, REMOVED, BLOCKED, JUMPING
    };

    // An unit has a reference to its front and back unit.
    private Unit front;
    private Unit back;

    // How many seconds for a unit at normal speed to cover a segment.
    private float movementTime = 2;

    private float speed;
    private float force;

    // Controls the moving of the unit.
    private float moveDelay;

    Animation moveAnimation;
    Animation pushAnimation;
    private Image icon;

    private Button widget;
    private Square square;
    private State state;
    private float tempNewX;
    
    protected Group parent;
    protected boolean canMove = false;
    protected float moveDelta = -1;

    public Unit() {
        init();
    }

    public void setParent(Group parent) {
        this.parent = parent;
    }

    /**
     * Sets the speed, force and cost of this unit.
     */
    abstract void init();

    public float speed() {
        return this.speed;
    }

    public void setMovementTime(float seconds) {
        this.movementTime = seconds;
        setSpeed(speed);
    }

    public void setSpeed(float speed) {
        this.speed = speed;
        moveDelay = movementTime * 1000 / speed;
        this.moveDelta = -1;
        if (moveAnimation != null)
            moveAnimation.setFrameTime(frameTime());
    }

    public float force() {
        return this.force;
    }

    public void setForce(float force) {
        this.force = force;
    }

    /** The widget contains this unit. */
    public Button widget() {
        if (this.widget == null)
            widget = new Button(Icons.image(icon));
        return this.widget;
    }

    public void setLayer(Button layer) {
        this.widget = layer;
    }

    public void setSquare(Square square) {
        this.square = square;
        tempNewX = square.getX();
        canMove = false;
    }

    public void move(Square square) {
        setSquare(square);
    }
    
    public boolean canMove() {
        return this.canMove;
    }

    public Square square() {
        return this.square;
    }

    public float frameTime() {
        return 100 / this.speed;
    }

    /** Reset this unit to initial state. */
    public void reset() {
        init();
        this.moveDelta = -1;
        this.front = null;
        this.back = null;
        widget.layer.setVisible(true);
        widget.icon.update(icon != null ? Icons.image(icon) : Icons.image(moveAnimation.frame(0)));
        parent.add(AbsoluteLayout
                .at(widget(), square().getX(), square().getY()));
    }
    
    public boolean updatePosition(float timeDelta) {
        if (this.moveDelta == -1) {
            this.moveDelta = LevelScreen.SQUARE_WIDTH / (this.moveDelay / timeDelta);
        }
        tempNewX = tempNewX + (this instanceof Troll ? this.moveDelta : - this.moveDelta);
        float previousX = square.getX();
        if (Math.abs(tempNewX - previousX) >= LevelScreen.SQUARE_WIDTH) {
            canMove = true;
        } else {
            parent.add(AbsoluteLayout.at(widget(), tempNewX, square().getY()));
        }
        
        return canMove;
    }

    public void setMoveAnimation(Animation animation) {
        this.moveAnimation = animation;
        widget.icon.update(Icons.image(animation.frame(0)));
    }

    public void setPushAnimation(Animation animaiton) {
        this.pushAnimation = animaiton;
    }

    public Unit front() {
        return this.front;
    }

    public void setFront(Unit unit) {
        this.front = unit;
        if (unit != null)
            unit.back = this;
    }

    public Unit back() {
        return this.back;
    }

    public void setBack(Unit unit) {
        this.back = unit;
        if (unit != null)
            unit.front = this;
    }

    public void removeFront() {
        this.setFront(this.front.front);
    }

    public void removeBack() {
        this.setBack(this.back.back);
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
        this.icon = image;
        widget.icon.update(Icons.image(image));
    }

    /** Handles collision with the front unit. */
    public void notifyColliedWithFront() {
    };

    /** Handles collision with the back unit. */
    public void notifyColliedWithBack() {
    };

    /** Special ability of this unit. */
    public String ability() {
        return "";
    }

    public void update(float delta) {
        if (state() == null || state().equals(State.REMOVED)
                || this.speed() == 0)
            return;
        if (canMove)
            parent.add(AbsoluteLayout.at(widget(), square().getX(), square()
                    .getY()));
        if (state().equals(State.MOVING))
            widget().icon.update(Icons.image(moveAnimation.nextFrame(delta)));
        else if (state().equals(State.PUSHING)) {
            if (pushAnimation != null)
                widget().icon.update(Icons.image(pushAnimation.nextFrame(delta)));
        }
    }

    public abstract String type();

}
