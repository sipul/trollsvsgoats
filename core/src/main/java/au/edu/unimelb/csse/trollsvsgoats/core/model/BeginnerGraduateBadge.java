package au.edu.unimelb.csse.trollsvsgoats.core.model;

public class BeginnerGraduateBadge extends Badge {

    @Override
    public boolean achieved(GameModel game) {
        return game.maxCompletedLevel() >= 5;
    }

    @Override
    public String name() {
        return "beginnerGraduate";
    }

    @Override
    public String displayName() {
        return "Beginner Graduate";
    }

    @Override
    public String description() {
        return "Complete Level 1-5";
    }

}
