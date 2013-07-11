package au.edu.unimelb.csse.trollsvsgoats.core.view;

import static tripleplay.ui.layout.TableLayout.COL;
import au.edu.unimelb.csse.trollsvsgoats.core.TrollsVsGoatsGame;
import au.edu.unimelb.csse.trollsvsgoats.core.model.GameModel;
import react.UnitSlot;
import tripleplay.ui.*;
import tripleplay.ui.layout.AxisLayout;
import tripleplay.ui.layout.TableLayout;

public class ThemeSelScreen extends View {

    private static final int BUTTON_WIDTH = 200;
    private static final int BUTTON_HEIGHT = 140;

    public ThemeSelScreen(TrollsVsGoatsGame game) {
        super(game);
    }

    @Override
    protected Group createIface() {
        Group themes = new Group(new TableLayout(COL.alignRight(), COL,
                COL.alignLeft()).alignTop().gaps(30, 0));

        // Adds themes buttons.
        for (int i = 1; i <= model.themes().length; i++) {
            final int _i = i - 1;
            final Button theme = button(model.themes()[_i]).setConstraint(
                    Constraints.fixedSize(BUTTON_WIDTH, BUTTON_HEIGHT));
            theme.setStyles(Style.BACKGROUND.is(butBg), Style.ICON_POS.below,
                    Style.FONT.is(SUBTITLE_FONT));

            if ((i - 1) * GameModel.levelsPerTheme > game.model()
                    .maxCompletedLevel() + 1) {
                themes.add(new Group(AxisLayout.vertical()).add(theme));
                theme.text.update(null);
                theme.icon.update(getIcon("lock"));
                theme.setEnabled(false);
            } else {
                themes.add(new Group(AxisLayout.vertical()).add(theme));
            }
            theme.clicked().connect(new UnitSlot() {

                @Override
                public void onEmit() {
                    game.showLevelSelScreen(_i);
                }
            });
        }
        return themes;
    }

    @Override
    public void wasShown() {
        wasRemoved();
        wasAdded();
    }

    @Override
    protected String title() {
        return "SELETE THEME";
    }

}
