package au.edu.unimelb.csse.trollsvsgoats.core.view;

import static playn.core.PlayN.*;
import au.edu.unimelb.csse.trollsvsgoats.core.TrollsVsGoatsGame;
import playn.core.GroupLayer;

/**
 * Represents a screen in the game.
 */
public abstract class View {
    TrollsVsGoatsGame game;
    GroupLayer layer;

    public View(TrollsVsGoatsGame game) {
        this.game = game;
        this.layer = graphics().createGroupLayer();
    }

    public GroupLayer layer() {
        return this.layer;
    }

    public abstract void init();

    /**
     * Clears data when this view is removed.
     */
    public void destroy() {
        layer.destroy();
    }

    public void update(float delta) {
    }

    public void paint(float alpha) {
    }
}
