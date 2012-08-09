package au.edu.unimelb.csse.trollsvsgoats.core.model;

public abstract class Badge {

    protected boolean isAchieved = false;
    protected boolean isShown = false;

    /**
     * Whether this badge can be achieved.
     */
    public abstract boolean achieved(GameModel game);

    /**
     * Set the badge is achieved.
     */
    public void setAchieved() {
        this.isAchieved = true;
    };

    /**
     * Whether this badge has been shown to player.
     */
    public boolean isShown() {
        return this.isShown;
    };

    /**
     * Set the badge is shown.
     */
    public void setShown() {
        this.isShown = true;
    };

    /**
     * Whether this badge has been achieved.
     */
    public boolean isAchieved() {
        return this.isAchieved;
    };

    /** The short name used to identify a badge. */
    public abstract String name();

    /** The long "friendly" string which names the badge for users. */
    public abstract String displayName();

    public String iconName() {
        return "badge_" + name();
    }

    /**
     * The even longer text which describes how to earn the badge. This should
     * be in the form of an instruction, such as "rescue the princess."
     */
    public abstract String description();
}
