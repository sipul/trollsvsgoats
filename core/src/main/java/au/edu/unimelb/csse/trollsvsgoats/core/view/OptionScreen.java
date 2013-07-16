package au.edu.unimelb.csse.trollsvsgoats.core.view;

import static tripleplay.ui.layout.TableLayout.*;

import playn.core.Font;
import au.edu.unimelb.csse.trollsvsgoats.core.TrollsVsGoatsGame;
import react.Function;
import react.UnitSlot;
import tripleplay.ui.*;
import tripleplay.ui.layout.AxisLayout;
import tripleplay.ui.layout.TableLayout;

public class OptionScreen extends View {

    private static final float MAX_SPEED = 6;
    private Label speedValue;

    public OptionScreen(TrollsVsGoatsGame game) {
        super(game);
    }

    @Override
    protected Group createIface() {
        Group iface = new Group(new TableLayout(COL.alignLeft().fixed(), COL
                .alignLeft().fixed()).gaps(20, 10));
        String title;
        if (model.isSoundEnabled())
            title = "ON";
        else
            title = "OFF";

        final ToggleButton sound = newToggle(title, 60, title.equals("ON"));
        sound.clicked().connect(new UnitSlot() {

            @Override
            public void onEmit() {
                if (sound.selected.get()) {
                    sound.addStyles(Style.BACKGROUND.is(butSelBg));
                    sound.text.update("ON");
                    model.setSoundEnabled(true);
                    game.persist();
                } else {
                    sound.addStyles(Style.BACKGROUND.is(butBg));
                    sound.text.update("OFF");
                    model.setSoundEnabled(false);
                    game.persist();
                }
            }
        });
        if (title.equals("ON")) {
        	sound.selected.update(true);
            model.setSoundEnabled(true);
        }
        iface.add(new Label("Sound").addStyles(Style.FONT.is(SUBTITLE_FONT)))
                .add(sound);

        int currentSpeed = (int) (MAX_SPEED + 1 - (int) (model.movementTime() * 2));
        Slider speed = new Slider(currentSpeed, 1, MAX_SPEED).setIncrement(1);
        speedValue = new Label(String.valueOf(currentSpeed)).setConstraint(
                Constraints.minSize("0")).setStyles(Style.HALIGN.right);
        speed.value.map(new Function<Float, String>() {

            @Override
            public String apply(Float value) {
                return String.valueOf(value.intValue());
            }
        }).connect(speedValue.text.slot());
        iface.add(new Label("Speed").addStyles(Style.FONT.is(SUBTITLE_FONT)))
                .add(new Group(AxisLayout.vertical()).add(speed)
                        .add(speedValue));

        Group screenSizes;
        iface.add(
                new Label("Screen Size").addStyles(Style.FONT.is(SUBTITLE_FONT)))
                .add(screenSizes = new Group(AxisLayout.horizontal()));
        ToggleButton[] toggles = { newToggle("800 X 600", 120, width() == 800),
                newToggle("1024 X 720", 120, width() == 1024) };
        for (final ToggleButton toggle : toggles) {
            final String size = toggle.text.get();
            
            toggle.clicked().connect(new UnitSlot() {
                @Override
                public void onEmit() {
                    if (toggle.selected.get()) {
                        toggle.addStyles(Style.BACKGROUND.is(butSelBg));
                        if (width() != stringToWidth(size)) {
                            game.setScreenSize(stringToWidth(size), stringToHeight(size));
                            game.refreshMainScreen();
                            game.persist();
                            wasAdded();
                        }
                    } else {
                        toggle.addStyles(Style.BACKGROUND.is(butSelBg));
                        toggle.selected.update(true);
                    }
                }

                private int stringToWidth(String string) {
                    return Integer.valueOf(string.split("X")[0].trim());
                }

                private int stringToHeight(String string) {
                    return Integer.valueOf(string.split("X")[1].trim());
                }
            });
            screenSizes.add(toggle);
        }

        return iface.add(new Shim(0, 100));
    }

    private ToggleButton newToggle(String title, int width, boolean selected) {
        final ToggleButton toggle = new ToggleButton(title)
                .setConstraint(Constraints.fixedWidth(width));
        toggle.addStyles(Style.BACKGROUND.is(butBg),
                Style.FONT.is(font(Font.Style.PLAIN, 18)));
        if (selected) {
        	toggle.selected.update(true);
            toggle.addStyles(Style.BACKGROUND.is(butSelBg));
        }
        return toggle;
    }

    @Override
    protected String title() {
        return "OPTIONS";
    }

    @Override
    public void wasHidden() {
        game.setMovementTime((MAX_SPEED + 1 - Float.valueOf(speedValue.text
                .get())) / 2);
    }

}
