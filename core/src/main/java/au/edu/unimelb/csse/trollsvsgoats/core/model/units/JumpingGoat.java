package au.edu.unimelb.csse.trollsvsgoats.core.model.units;

public class JumpingGoat extends Goat {

    @Override
    void init() {
        setSpeed(1);
        setForce(4);
    }

    @Override
    public void notifyColliedWithFront() {
        if (this.front() instanceof DiggingTroll) {
            Unit front = this.front();
            Unit back = this.back();
            if (front.front() != null) {
                if (front.square().distance()
                        - front.front().square().distance() != 1) {
                    setFront(front.front());
                    setBack(front);
                    front.setBack(back);
                    setState(State.JUMPING);
                }
            } else {
                setFront(null);
                setBack(front);
                front.setBack(back);
                setState(State.JUMPING);
            }
        }
    }
    
    @Override
    public String ability() {
        return "can jump over digging trolls";
    }
    
    @Override
    public String type() {
        return "jumping";
    }

}
