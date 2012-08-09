package au.edu.unimelb.csse.trollsvsgoats.core.model;

public class ALevelBadge extends Badge {

    @Override
    public boolean achieved(GameModel game) {
        boolean achieved;
        if (achieved = game.isLevelCompleted() && !game.momentOverZero())
            setAchieved();
        return achieved;
    }

    @Override
    public String name() {
        return "aLevel";
    }

    @Override
    public String displayName() {
        return "A Level";
    }

    @Override
    public String description() {
        return "First level that doesn't leave 0 N/m";
    }

}
