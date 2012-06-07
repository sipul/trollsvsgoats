package au.edu.unimelb.csse.trollsvsgoats.core;

import static playn.core.PlayN.*;

import java.util.HashMap;
import java.util.Map;

import au.edu.unimelb.csse.trollsvsgoats.core.model.PersistenceClient;
import au.edu.unimelb.csse.trollsvsgoats.core.view.Level;
import au.edu.unimelb.csse.trollsvsgoats.core.view.LevelMenu;
import au.edu.unimelb.csse.trollsvsgoats.core.view.MainMenu;
import au.edu.unimelb.csse.trollsvsgoats.core.view.View;

import playn.core.Game;
import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.ResourceCallback;
import playn.core.Sound;

/**
 * Main game interface which updates the game states and views.
 */
public class TrollsVsGoatsGame implements Game {

    private PersistenceClient persistence;
    private Level currentLevel;
    private View currentView;
    private int levelIndex;
    private int maxCompletedLevel = 1;

    private Map<String, Image> images = new HashMap<String, Image>();
    private Map<String, Sound> sounds = new HashMap<String, Sound>();

    // Views
    private MainMenu mainMenu = new MainMenu(this);
    private LevelMenu levelMenu = new LevelMenu(this);

    public TrollsVsGoatsGame(PersistenceClient persistence) {
        this.persistence = persistence;
    }

    @Override
    public void init() {
        graphics().setSize(800, 480);
        showMainMenu();
    }

    public void showMainMenu() {
        if (currentView != null) {
            currentView.destroy();
        }
        mainMenu.init();
        graphics().rootLayer().add(mainMenu.layer());
        currentView = mainMenu;
    }

    public void showLevelMenu() {
        if (currentView != null) {
            currentView.destroy();
        }
        levelMenu.init();
        graphics().rootLayer().add(levelMenu.layer());
        currentView = levelMenu;
    }

    public void loadLevel(final int index) {
        String levelPath = "levels/level_" + String.valueOf(index)
                + "_demo.txt";
        final TrollsVsGoatsGame game = this;
        getImage("numbers");
        assets().getText(levelPath, new ResourceCallback<String>() {

            @Override
            public void error(Throwable err) {
                log().error(err.getMessage());
            }

            @Override
            public void done(String resource) {
                if (currentView != null) {
                    currentView.destroy();
                }
                Level level = new Level(game, resource);
                levelIndex = index;
                level.init();
                graphics().rootLayer().add(level.layer());
                currentLevel = level;
                currentView = level;
            }
        });
    }

    public void loadNextLevel() {
        loadLevel(++levelIndex);
    }

    // Called when completed the current level, persists the level index.
    public void levelCompleted() {
        // TODO
    }

    public int levelIndex() {
        return this.levelIndex;
    }

    public int maxCompletedLevel() {
        return this.maxCompletedLevel;
    }

    /**
     * Retrieves and caches images in the case of assets service for Java
     * platform does not do caching. Images should be type of png.
     **/
    public Image getImage(String name) {
        Image image = null;
        if ((image = images.get(name)) == null) {
            image = assets().getImage("images/" + name + ".png");
            images.put(name, image);
        }
        return image;
    }

    /**
     * Retrieves and caches sounds.
     **/
    public Sound getSound(String name) {
        Sound sound = null;
        if ((sound = sounds.get(name)) == null) {
            sound = assets().getSound("sounds/" + name);
            sounds.put(name, sound);
        }
        return sound;
    }

    @Override
    public void paint(float alpha) {
        currentView.paint(alpha);
    }

    @Override
    public void update(float delta) {
        currentView.update(delta);
    }

    @Override
    public int updateRate() {
        return 25;
    }
}
