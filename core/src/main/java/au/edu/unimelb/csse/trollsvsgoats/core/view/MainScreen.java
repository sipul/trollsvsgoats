package au.edu.unimelb.csse.trollsvsgoats.core.view;

import react.UnitSlot;
import tripleplay.ui.Button;
import tripleplay.ui.Constraints;
import tripleplay.ui.Group;
import tripleplay.ui.layout.AxisLayout;
import au.edu.unimelb.csse.trollsvsgoats.core.TrollsVsGoatsGame;

public class MainScreen extends View {

    public MainScreen(TrollsVsGoatsGame game) {
        super(game);
        TOP_MARGIN = 70;
    }

    @Override
    protected Group createIface() {
        back.setVisible(false);
        Group buttons;
        buttons = new Group(AxisLayout.vertical().gap(10));
        Button button = button("START").setConstraint(
                Constraints.fixedSize(150, 50));
        buttons.add(button);
        button.clicked().connect(new UnitSlot() {
            @Override
            public void onEmit() {
                game.showLevelSelScreen(0);
            }
        });

        button = button("BADGES").setConstraint(Constraints.fixedSize(150, 50));
        buttons.add(button);
        button.clicked().connect(new UnitSlot() {
            @Override
            public void onEmit() {
                game.showBadgesScreen();
            }
        });

        button = button("OPTIONS")
                .setConstraint(Constraints.fixedSize(150, 50));
        buttons.add(button);
        button.clicked().connect(new UnitSlot() {
            @Override
            public void onEmit() {
                game.showOptionScreen();
            }
        });

        button = button("HELP").setConstraint(Constraints.fixedSize(150, 50));
        buttons.add(button);
        button.clicked().connect(new UnitSlot() {
            @Override
            public void onEmit() {
                game.showHelpScreen();
            }
        });

        return buttons;
    }

    @Override
    public String[] images() {
        return new String[] { "back_on", "back_off" };
    }

    @Override
    protected String title() {
        return "WELCOME, " + game.userName();
    }

}
