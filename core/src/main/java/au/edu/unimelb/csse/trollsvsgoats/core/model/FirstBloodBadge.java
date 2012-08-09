package au.edu.unimelb.csse.trollsvsgoats.core.model;

public class FirstBloodBadge extends Badge {

    @Override
    public boolean achieved(GameModel game) {
        if (game.goatEaten())
            setAchieved();
        return game.goatEaten();
    }

    @Override
    public String name() {
        return "firstBlood";
    }

    @Override
    public String displayName() {
        return "First Blood";
    }

    @Override
    public String description() {
        return "First Goat eaten";
    }
}
