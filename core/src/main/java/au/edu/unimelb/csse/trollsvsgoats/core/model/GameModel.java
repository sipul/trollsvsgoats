package au.edu.unimelb.csse.trollsvsgoats.core.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import playn.core.PlayN;

public class GameModel {
    public int width = 1024, height = 720;
    public final static int levelsPerTheme = 9;
    private float movementTime = 2;

    private int levelIndex;
    private int themeIndex;
    private int maxCompletedLevel;
    private boolean isDirty;
    private boolean levelCompleted;
    private final Badge[] badges;
    private final String[] themes;
    private boolean soundEnabled = true;
    private HashMap<Integer, Integer> scores = new HashMap<Integer, Integer>();

    // Badges related fields.
    private boolean momentOverZero;
    private boolean goatEaten;

    public GameModel() {
        badges = new Badge[] { new ALevelBadge(), new FirstBloodBadge(),
                new BeginnerGraduateBadge(), new TrollSaverBadge() };
        themes = new String[] { "BRIDGE", "COMING SOON", "COMING SOON" };
    }

    public String[] themes() {
        return this.themes;
    }

    public Badge[] badges() {
        return this.badges;
    }

    public List<Badge> newAchievedBadges() {
        List<Badge> badges = new ArrayList<Badge>();
        for (Badge badge : this.badges) {
            if (!badge.isAchieved() || !badge.isShown())
                if (badge.achieved(this))
                    badges.add(badge);
        }
        return badges;
    }

    public void setBadgeAchieved(String name) {
        for (Badge badge : badges) {
            if (badge.name().equals(name)) {
                badge.setAchieved();
                badge.setShown();
            }
        }
    }

    /** All badges have been shown to player */
    public void setBadgesShown() {
        for (Badge badge : badges) {
            badge.setShown();
        }
    }

    public boolean isSoundEnabled() {
        return this.soundEnabled;
    }

    public void setSoundEnabled(boolean enabled) {
        this.soundEnabled = enabled;
    }

    public void setScreenWidth(int width) {
        this.width = width;
        if (width == 800)
            this.height = 600;
        else if (width == 1024) {
            this.height = 720;
        }
    }

    public float movementTime() {
        return this.movementTime;
    }

    public void setMovementTime(float seconds) {
        this.movementTime = seconds;
    }

    public int levelIndex() {
        return levelIndex;
    }

    public int screenWidth() {
        return PlayN.graphics().width();
    }

    public String currentTheme() {
        return themes[themeIndex].toLowerCase();
    }

    public void setThemeIndex(int index) {
        this.themeIndex = index;
    }

    public int levelScore(int level) {
        if (scores.containsKey(level))
            return this.scores.get(level);
        else
            return 0;
    }

    public boolean momentOverZero() {
        return this.momentOverZero;
    }

    public boolean goatEaten() {
        return this.goatEaten;
    }

    public void setMomentOverZero() {
        this.momentOverZero = true;
    }

    public void setGoatEaten() {
        this.goatEaten = true;
    }

    public HashMap<Integer, Integer> scores() {
        return this.scores;
    }

    public void setScores(Map<Integer, Integer> scores) {
        scores.putAll(scores);
    }

    public int nextLevelIndex() {
        return ++levelIndex;
    }

    public int maxCompletedLevel() {
        return maxCompletedLevel;
    }

    public int maxThemeCompletedLevel() {
        return maxCompletedLevel % (themeIndex * levelsPerTheme);
    }

    /** Reset level data. */
    public void levelStart(int levelIndex) {
        this.levelIndex = levelIndex;
        levelCompleted = false;
        momentOverZero = false;
        goatEaten = false;
    }

    public void levelRestart() {
        levelStart(levelIndex);
    }

    public boolean isLevelCompleted() {
        return this.levelCompleted;
    }

    public void levelCompleted(int score) {
        levelCompleted = true;
        if (maxCompletedLevel < levelIndex) {
            isDirty = true;
            maxCompletedLevel = levelIndex;
        }
        if (scores.containsKey(levelIndex)) {
            if (scores.get(levelIndex) < score) {
                scores.put(levelIndex, score);
                isDirty = true;
            }
        } else {
            scores.put(levelIndex, score);
            isDirty = true;
        }
    }

    public void setLevelScores(HashMap<Integer, Integer> scores) {
        this.scores.putAll(scores);
    }

    // TODO Just for cheating
    public void setLevelScore(int score) {
        if (scores.containsKey(maxCompletedLevel + 1)) {
            if (score != scores.get(maxCompletedLevel + 1)) {
                scores.put(maxCompletedLevel + 1, score);
                isDirty = true;
            }
        } else {
            scores.put(maxCompletedLevel + 1, score);
            isDirty = true;
        }

    }

    public void setMaxCompletedLevel(int levelIndex) {
        maxCompletedLevel = levelIndex;
    }

    // TODO
    public void setLevelDataDirty() {
        this.isDirty = true;
    }

    // TODO
    public void clearLevelScores() {
        this.scores.clear();
    }

    public boolean isLevelDataDirty() {
        boolean _isDirty = isDirty;
        if (isDirty)
            isDirty = false;
        return _isDirty;
    }

}
