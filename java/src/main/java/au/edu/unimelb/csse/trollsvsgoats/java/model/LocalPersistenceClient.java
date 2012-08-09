package au.edu.unimelb.csse.trollsvsgoats.java.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import au.edu.unimelb.csse.trollsvsgoats.core.model.Badge;
import au.edu.unimelb.csse.trollsvsgoats.core.model.GameModel;
import au.edu.unimelb.csse.trollsvsgoats.core.model.PersistenceClient;

public class LocalPersistenceClient implements PersistenceClient {

    private int maxCompletedLevel = 0;
    private Map<Integer, Integer> levelScores = new HashMap<Integer, Integer>();
    private List<String> achievedBadges = new ArrayList<String>();

    @Override
    public void persist(GameModel model) {
        if (model.isLevelDataDirty()) {
            this.maxCompletedLevel = model.maxCompletedLevel();
            this.levelScores.putAll(model.scores());
        }
    }

    @Override
    public void populate(GameModel model) {
        model.setMaxCompletedLevel(this.maxCompletedLevel);
        model.setScores(this.levelScores);
        for (String name : achievedBadges) {
            model.setBadgeAchieved(name);
        }
    }

    @Override
    public void getUserName(Callback<String> callBack) {
        callBack.onSuccess("LocalTest");
    }

    @Override
    public void achieveBadge(Badge badge) {
        achievedBadges.add(badge.name());
        badge.setAchieved();
    }

    @Override
    public void logTrollsDeployment(int level, List<String> lanes) {

    }

}
