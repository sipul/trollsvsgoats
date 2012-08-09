package au.edu.unimelb.csse.trollsvsgoats.core.model;

public class TrollSaverBadge extends Badge {

    @Override
    public boolean achieved(GameModel game) {
        boolean pred = game.maxCompletedLevel() >= 5;
        for (int i = 1; i <= 5; i++) {
            if (!game.scores().containsKey(i) || game.scores().get(i) != 3)
                pred = false;
        }
        return pred;
    }

    @Override
    public String name() {
        return "trollSaver";
    }

    @Override
    public String displayName() {
        return "Troll Saver";
    }

    @Override
    public String description() {
        return "Complete levels 1-5 with 3 star ratings";
    }

}
