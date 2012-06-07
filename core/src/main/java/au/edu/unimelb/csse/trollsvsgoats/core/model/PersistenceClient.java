package au.edu.unimelb.csse.trollsvsgoats.core.model;

/**
 * Handles game state persistence.
 */
public interface PersistenceClient {
    public void persist(GameModel model);

    public void populate(final GameModel model);
}
