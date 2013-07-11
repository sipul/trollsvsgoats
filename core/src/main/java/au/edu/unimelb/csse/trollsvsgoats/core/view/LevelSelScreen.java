package au.edu.unimelb.csse.trollsvsgoats.core.view;

import static tripleplay.ui.layout.TableLayout.COL;

import playn.core.Keyboard.Listener;
import playn.core.Key;
import playn.core.Keyboard.Event;
import playn.core.Keyboard.TypedEvent;
import playn.core.PlayN;
import react.UnitSlot;
import tripleplay.ui.Button;
import tripleplay.ui.Constraints;
import tripleplay.ui.Group;
import tripleplay.ui.Label;
import tripleplay.ui.Shim;
import tripleplay.ui.Style;
import tripleplay.ui.layout.AxisLayout;
import tripleplay.ui.layout.TableLayout;
import au.edu.unimelb.csse.trollsvsgoats.core.TrollsVsGoatsGame;

public class LevelSelScreen extends View {

    private Group stars;
    private int score = 0;

    private static final int BUTTON_WIDTH = 140;
    private static final int BUTTON_HEIGHT = 80;

    public LevelSelScreen(TrollsVsGoatsGame game) {
        super(game);
    }

    // Cheating keys for demo.
    private void addCheatKeys() {
        PlayN.keyboard().setListener(new Listener() {

            @Override
            public void onKeyUp(Event event) {
                if (event.key().equals(Key.PERIOD)) {
                    game.increaseMaxLevel();
                    wasRemoved();
                    wasAdded();
                } else if (event.key().equals(Key.COMMA)) {
                    game.decreaseMaxLevel();
                    wasRemoved();
                    wasAdded();
                } else if (event.key().equals(Key.RIGHT_BRACKET)) {
                    score++;
                    updateStars(stars, score);
                    game.setLevelScore(score);
                } else if (event.key().equals(Key.LEFT_BRACKET)) {
                    score--;
                    updateStars(stars, score);
                    game.setLevelScore(score);
                }
            }

            @Override
            public void onKeyTyped(TypedEvent event) {
            }

            @Override
            public void onKeyDown(Event event) {
            }
        });
    }

    @Override
    protected Group createIface() {
        Group levels = new Group(new TableLayout(COL.alignRight(), COL,
                COL.alignLeft()).alignTop().gaps(30, 0));

        // Adds level buttons.
        for (int i = 1; i <= 9; i++) {
            final Button level = button(String.valueOf(i)).setConstraint(
                    Constraints.fixedSize(BUTTON_WIDTH, BUTTON_HEIGHT));
            level.setStyles(Style.BACKGROUND.is(butBg), Style.ICON_POS.below,
                    Style.FONT.is(SUBTITLE_FONT));
            Group stars = new Group(AxisLayout.horizontal());

            if (i > model.maxCompletedLevel() + 1) {
                levels.add(new Group(AxisLayout.vertical()).add(level).add(
                        new Shim(0, getImage("star_solid").height())));
                level.text.update(null);
                level.icon.update(getIcon("lock"));
                level.setEnabled(false);
            } else {
                levels.add(new Group(AxisLayout.vertical()).add(level).add(
                        stars));
                updateStars(stars, model.levelScore(i));
                this.stars = stars;
            }
            final int _i = i;
            level.clicked().connect(new UnitSlot() {

                @Override
                public void onEmit() {
                    game.loadLevel(_i, false);
                }
            });
        }
        return levels;
    }

    @Override
    protected Group createButtomPanel() {
        Group iface = new Group(new TableLayout(COL.alignLeft().fixed(), COL
                .alignLeft().fixed()).gaps(0, 10), Style.HALIGN.left)
                .add(new Label("<PERIOD>: + max level"))
                .add(new Label("']': + score"))
                .add(new Label("<COMMA>: - max level"))
                .add(new Label("'[': - score"));
        return iface;
    }

    private void updateStars(Group stars, int score) {
        if (score > 3)
            score = 3;
        else if (score < 0)
            score = 0;
        this.score = score;
        stars.removeAll();
        for (int i = 1; i <= 3; i++) {
            if (i <= score)
                stars.add(new Label(getIcon("star_solid")));
            else
                stars.add(new Label(getIcon("star_empty")));
        }
    }

    @Override
    public void wasShown() {
        addCheatKeys();
        wasAdded();
    }

    @Override
    public void wasHidden() {
        PlayN.keyboard().setListener(null);
    }

    @Override
    public String[] images() {
        return new String[] { "star_solid", "star_empty", "lock" };
    }

    @Override
    protected String title() {
        return "SELECT LEVEL";
    }

}
