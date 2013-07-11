package au.edu.unimelb.csse.trollsvsgoats.core;

import static playn.core.PlayN.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import playn.core.*;
import playn.core.util.Callback;
import playn.core.util.Clock;
import tripleplay.game.ScreenStack;
import tripleplay.ui.Background;
import tripleplay.ui.Icon;
import tripleplay.ui.Icons;
import tripleplay.ui.Interface;
import tripleplay.ui.Root;
import tripleplay.ui.SimpleStyles;
import tripleplay.ui.Style;
import tripleplay.ui.layout.AxisLayout;
import au.edu.unimelb.csse.trollsvsgoats.core.model.*;
import au.edu.unimelb.csse.trollsvsgoats.core.view.*;

/**
 * Main game interface which updates the game states and views.
 */
public class TrollsVsGoatsGame extends Game.Default implements Game {

    private final View[] screens;
    private PersistenceClient persistence;
    private GameModel model;
    private String userName;

    private Map<String, Image> images = new HashMap<String, Image>();
    private Map<String, Sound> sounds = new HashMap<String, Sound>();
    private Map<String, Icon>  icons = new HashMap<String, Icon>();

    // Views
    private MainScreen mainScreen;
    private LoadingScreen loadScreen;
    private ThemeSelScreen themeSelScreen;
    private LevelSelScreen levelSelScreen;
    private MessageBox messageBox;
    private BadgesScreen badgesScreen;
    private OptionScreen optionScreen;
    private HelpScreen helpScreen;
    
    //tripleplay 1.7.2
    private static final int UPDATE_RATE = 30; // FPS
    private static final int UPDATE_PERIOD = 1000 / UPDATE_RATE;
    private final Clock.Source _clock = new Clock.Source(UPDATE_RATE);
    protected float _lastTime;
    //

    public TrollsVsGoatsGame(PersistenceClient persistence) {
    	super(UPDATE_PERIOD); // call update every 33ms (30 times per second)
        this.persistence = persistence;
        this.model = new GameModel();
        screens = new View[] { mainScreen = new MainScreen(this),
                loadScreen = new LoadingScreen(this),
                themeSelScreen = new ThemeSelScreen(this),
                levelSelScreen = new LevelSelScreen(this),
                new LevelScreen(this), badgesScreen = new BadgesScreen(this),
                optionScreen = new OptionScreen(this),
                helpScreen = new HelpScreen(this) };
    }

    @Override
    public void init() {
        userName = "LocalTest";
        populate();
        stack.push(loadScreen);
        loadResources();
    }

    /** Load all the images and sounds. */
    private void loadResources() {
        AssetWatcher asset = new AssetWatcher(new AssetWatcher.Listener() {

            @Override
            public void error(Throwable e) {
                log().error("Error loading asset: " + e.getMessage());
            }

            @Override
            public void done() {
                //TODO graphics().setSize(model.width, model.height);
            	//http://code.google.com/p/playn/source/detail?spec=svn02ad17652134fc1a47d647a9fec2e72bd7a2134b&r=2ee14ffb17a4935e3db3079fd1bcab45831d9105
            	//issue with html - graphics().ctx().setSize(model.width, model.height);
            	
                stack.replace(mainScreen, ScreenStack.NOOP);
            }
        });
        for (View screen : screens) {
            if (screen.images() != null) {
                for (String path : screen.images()) {
                    Image image = assets().getImage("images/" + path + ".png");
                    asset.add(image);
                    images.put(path, image);
                    
                    Icon icon = Icons.image(PlayN.assets().getImage("images/" + path + ".png"));
                    icons.put(path, icon);
                }
            }
            if (screen.sounds() != null) {
                for (String path : screen.sounds()) {
                    Sound sound = assets().getSound("sounds/" + path);
                    asset.add(sound);
                    sounds.put(path, sound);
                }
            }
        }

        asset.start();
    }

    public void setScreenSize(int width, int height) {
        //TODO graphics().setSize(width, height);
    	//issue with html - graphics().ctx().setSize(width, height);

        persistence.persist(model);
    }

    /** Grab data from persistence source */
    public void populate() {
        persistence.getUserName(new PersistenceClient.Callback<String>() {

            @Override
            public void onSuccess(String result) {
                if (result != null)
                    userName = result;
            }

            @Override
            public void onFailure(Throwable caught) {
                log().error(caught.getMessage());
            }
        });
        persistence.populate(model);
    }

    /** Save game data to persistence source */
    public void persist() {
        persistence.persist(model);
    }

    public void logTrollsDeployment(List<String> lanes) {
        persistence.logTrollsDeployment(model.levelIndex(), lanes);
    }

    public void refreshMainScreen() {
        mainScreen.wasAdded();
    }

    public void showThemeSelScreen() {
        stack.push(themeSelScreen);
    }

    public void showLevelSelScreen(int index) {
        model.setThemeIndex(index);
        stack.push(levelSelScreen);
    }

    public void showBadgesScreen() {
        stack.push(badgesScreen);
    }

    public void showOptionScreen() {
        stack.push(optionScreen);
    }

    public void showHelpScreen() {
        stack.push(helpScreen);
    }

    public void showMessageBox(View currentScreen, MessageBox messageBox) {
        closeMessageBox();
        this.messageBox = messageBox;
        messageBox.wasAdded();
        currentScreen.layer.addAt(messageBox.layer(), graphics().width() / 2
                - messageBox.width() / 2,
                graphics().height() / 2 - messageBox.height() / 2);
        messageBox.layer().setDepth(3);
    }

    public void closeMessageBox() {
        if (messageBox != null) {
            messageBox.wasRemoved();
            messageBox = null;
        }
    }

    public String userName() {
        return this.userName;
    }

    public GameModel model() {
        return this.model;
    }

    public void loadLevel(final int index, final boolean replace) {
        String levelPath = "levels/" + model.currentTheme() + "_level_"
                + String.valueOf(index) + ".txt";
        final TrollsVsGoatsGame game = this;
        assets().getText(levelPath, new Callback<String>() {
			@Override
			public void onSuccess(String result) {
				game.model().levelStart(index);
                LevelScreen level = new LevelScreen(game, result);
                if (replace)
                    stack.replace(level, ScreenStack.NOOP);
                else
                    stack.push(level, ScreenStack.NOOP);
			}

			@Override
			public void onFailure(Throwable cause) {
				log().error(cause.getMessage());
				
			}
        });
    }

    public void loadNextLevel() {
        loadLevel(model.nextLevelIndex(), true);
    }

    /** Called when completed the current level, persists the level index. */
    public void levelCompleted(int score) {
        model.levelCompleted(score);
        persistence.persist(model);
    }

    public void setBadgeAchieve(Badge badge) {
        persistence.achieveBadge(badge);
    }

    /** How long for a unit to cover a segment. */
    public void setMovementTime(float seconds) {
        model.setMovementTime(seconds);
        persistence.persist(model);
    }

    // TODO Just for cheating
    public void setLevelScore(int score) {
        model.setLevelScore(score);
        persistence.persist(model);
    }

    // TODO Just for cheating
    public void increaseMaxLevel() {
        if (model.maxCompletedLevel() < 6) {
            model.setMaxCompletedLevel(model.maxCompletedLevel() + 1);
            model.setLevelDataDirty();
            persistence.persist(model);
        }
    }

    public void decreaseMaxLevel() {
        if (model.maxCompletedLevel() > 0) {
            model.setMaxCompletedLevel(model.maxCompletedLevel() - 1);
            model.setLevelDataDirty();
            persistence.persist(model);
        }
    }

    /**
     * Retrieves images which should be type of png.
     **/
    public Image getImage(String path) {
        return images.get(path);
    }
    
    public Icon getIcon(String path) {
    	return icons.get(path);
    }

    /**
     * Retrieves and caches sounds.
     **/
    public Sound getSound(String path) {
        return sounds.get(path);
    }

    @Override
    public void paint(float alpha) {
    	_clock.paint(alpha);
        stack.paint(_clock);
        if (messageBox != null)
            messageBox.paint(_clock);
    }

    @Override
    public void update(int delta) {
    	_clock.update(delta);
        stack.update(delta);
    }

    public ScreenStack stack() {
        return this.stack;
    }

    private final ScreenStack stack = new ScreenStack() {
        @Override
        protected void handleError(RuntimeException error) {
            PlayN.log().warn("Screen failure", error);
        }

        @Override
        protected Transition defaultPushTransition() {
            return slide();
        }

        @Override
        protected Transition defaultPopTransition() {
            return slide().right();
        }
    };
}
