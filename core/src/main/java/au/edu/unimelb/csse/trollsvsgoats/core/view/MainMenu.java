package au.edu.unimelb.csse.trollsvsgoats.core.view;

import static playn.core.PlayN.graphics;
import react.UnitSlot;
import tripleplay.ui.*;
import tripleplay.ui.layout.AxisLayout;
import au.edu.unimelb.csse.trollsvsgoats.core.TrollsVsGoatsGame;

public class MainMenu extends View {

    private Interface iface;

    public MainMenu(TrollsVsGoatsGame game) {
        super(game);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void init() {
        iface = new Interface();

        // Adds buttons.
        Styles buttonStyles = Styles.none()

        .addSelected(
                Style.BACKGROUND.is(Background.image(game
                        .getImage("button_selected"))));
        Stylesheet rootSheet = Stylesheet.builder()
                .add(Button.class, buttonStyles).create();
        Root root = iface.createRoot(AxisLayout.vertical().gap(15), rootSheet);
        root.setSize(graphics().width(), graphics().height());
        root.addStyles(Styles.make(Style.BACKGROUND.is(Background.image(game
                .getImage("bg")))));
        layer.add(root.layer);
        Group buttons;
        root.add(new Label("Trolls Vs Goats Demo"), buttons = new Group(
                AxisLayout.vertical().offStretch()));
        Button button = new Button();
        button.setIcon(game.getImage("button_play"));
        buttons.add(button);
        button.clicked().connect(new UnitSlot() {
            @Override
            public void onEmit() {
                game.showLevelMenu();
            }
        });

    }

    @Override
    public void paint(float alpha) {
        iface.paint(alpha);
    }

}
