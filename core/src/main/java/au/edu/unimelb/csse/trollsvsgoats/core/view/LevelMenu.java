package au.edu.unimelb.csse.trollsvsgoats.core.view;

import static playn.core.PlayN.*;
import react.UnitSlot;
import tripleplay.ui.*;
import tripleplay.ui.layout.*;
import au.edu.unimelb.csse.trollsvsgoats.core.TrollsVsGoatsGame;

public class LevelMenu extends View {

    private Interface iface;

    public LevelMenu(TrollsVsGoatsGame game) {
        super(game);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void init() {
        iface = new Interface();

        // Define root stylesheet.
        Styles buttonStyles = Styles.none()

        .addSelected(Style.BACKGROUND.is(Background.solid(0xFFCCCCCC)));
        Stylesheet rootSheet = Stylesheet.builder()
                .add(Button.class, buttonStyles).create();

        // Creates interface.
        Root root = iface.createRoot(AxisLayout.vertical().gap(15), rootSheet);
        root.setSize(graphics().width(), graphics().height());

        // Adds background image.
        root.addStyles(Styles.make(Style.BACKGROUND.is(Background.image(game
                .getImage("bg")))));
        layer.add(root.layer);

        Group buttons;
        root.add(new Label("Levels"), buttons = new Group(AxisLayout.vertical()
                .offStretch()));

        // Adds level buttons.
        for (int i = 1; i <= game.maxCompletedLevel(); i++) {
            Button button = new Button();
            button.setIcon(game.getImage("button_level" + i));
            buttons.add(button);
            final int _i = i;
            button.clicked().connect(new UnitSlot() {

                @Override
                public void onEmit() {
                    game.loadLevel(_i);
                }
            });
        }

    }

    @Override
    public void paint(float alpha) {
        iface.paint(alpha);
    }

}
