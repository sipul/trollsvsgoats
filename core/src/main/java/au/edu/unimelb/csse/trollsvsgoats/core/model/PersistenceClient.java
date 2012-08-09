package au.edu.unimelb.csse.trollsvsgoats.core.model;

import java.util.List;

/**
 * Handles game state persistence.
 */
public interface PersistenceClient {

    public interface Callback<T> {
        public void onSuccess(T result);

        public void onFailure(Throwable caught);
    }

    public void persist(GameModel model);

    public void populate(final GameModel model);

    public void achieveBadge(Badge badge);

    public void getUserName(Callback<String> callBack);

    public void logTrollsDeployment(int level, List<String> lanes);
}
