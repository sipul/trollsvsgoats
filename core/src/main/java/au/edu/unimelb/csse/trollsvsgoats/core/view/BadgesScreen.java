package au.edu.unimelb.csse.trollsvsgoats.core.view;

import java.util.ArrayList;

import playn.core.Font;
import playn.core.Key;
import playn.core.Mouse;
import playn.core.PlayN;
import playn.core.Keyboard.Event;
import playn.core.Keyboard.Listener;
import playn.core.Keyboard.TypedEvent;
import playn.core.Mouse.ButtonEvent;
import tripleplay.ui.*;
import tripleplay.ui.Style.Binding;
import tripleplay.ui.layout.AxisLayout;
import au.edu.unimelb.csse.trollsvsgoats.core.TrollsVsGoatsGame;
import au.edu.unimelb.csse.trollsvsgoats.core.model.Badge;

public class BadgesScreen extends View {

    private final static int TILE_BG = 0xFF7ECEF4;
    private final static int TILE_GAP = 10;
    private final static int TILE_WIDTH = 400;
    private final static int TILE_HEIGHT = 70;
    public final static int ICON_WIDTH = 60;
    public final static int DESCRIPTION_WIDTH = TILE_WIDTH - ICON_WIDTH;
    private final static int SCROLL_HEIGHT = 300;
    private final static int SCROLL_WIDTH = 30;
    private final static int BUTTON_SCROLL_DISTANCE = 10;
    private final static int BOTTOM_MARGIN = 40;
    final Binding<Background> selOn = Style.BACKGROUND.is(Background.blank()
            .inset(2, 1, -3, 3));
    final Binding<Background> selOff = Style.BACKGROUND.is(Background.blank()
            .inset(1, 2, -2, 2));

    private int tileCount = 10;
    private Group tiles;
    private ScrollBar scroll;

    public BadgesScreen(TrollsVsGoatsGame game) {
        super(game);
    }

    private void addCheatKeys() {
        PlayN.keyboard().setListener(new Listener() {

            @Override
            public void onKeyUp(Event event) {
                if (event.key().equals(Key.ENTER)) {
                    for (Badge badge : model.badges())
                        badge.setAchieved();
                    wasAdded();
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
        topPanel.add(new Shim(0, 20));

        tiles = new Group(AxisLayout.vertical().gap(TILE_GAP), Style.VALIGN.top);
        Group iface = new Group(AxisLayout.horizontal()).add(tiles);

        for (Badge badge : model.badges()) {
            String icon = null;
            if (badge.isAchieved())
                icon = badge.iconName();
            else
                icon = "badge_lock";
            tiles.add(new Group(AxisLayout.horizontal(), Style.HALIGN.left)
                    .add(new Label(getImage(icon)).setConstraint(Constraints
                            .fixedWidth(ICON_WIDTH)))
                    .add(new Group(AxisLayout.vertical())
                            .add(new Label(badge.displayName())
                                    .addStyles(Style.FONT.is(font(
                                            Font.Style.BOLD, 16))))
                            .add(new Label(badge.description()))
                            .setConstraint(
                                    Constraints.fixedWidth(DESCRIPTION_WIDTH)))
                    .addStyles(Style.BACKGROUND.is(Background.solid(TILE_BG)))
                    .setConstraint(
                            Constraints.fixedSize(TILE_WIDTH, TILE_HEIGHT)));
        }

        for (int i = 0; i < tileCount - model.badges().length; i++) {
            tiles.add(new Group(AxisLayout.horizontal(), Style.HALIGN.left)
                    .add(new Label(getImage("badge_lock"))
                            .setConstraint(Constraints.fixedWidth(ICON_WIDTH)))
                    .add(new Group(AxisLayout.vertical()).add(
                            new Label("?").addStyles(Style.FONT.is(font(
                                    Font.Style.BOLD, 16)))).setConstraint(
                            Constraints.fixedWidth(DESCRIPTION_WIDTH)))
                    .addStyles(Style.BACKGROUND.is(Background.solid(TILE_BG)))
                    .setConstraint(
                            Constraints.fixedSize(TILE_WIDTH, TILE_HEIGHT)));
        }

        int scrollRange = tileCount * (TILE_HEIGHT + TILE_GAP);
        float PAGE_SIZE = (height() - SCROLL_HEIGHT) / (TILE_HEIGHT + TILE_GAP)
                + 1;
        float PAGE_RANGE = PAGE_SIZE * (TILE_HEIGHT + TILE_GAP);
        scroll = new ScrollBar(tiles, scrollRange, PAGE_RANGE).setBarSize(
                SCROLL_WIDTH, height() - SCROLL_HEIGHT);

        final Button up = scroll.upButton();
        final Button down = scroll.downButton();
        up.addStyles(selOff).icon.update(getImage("scroll_up"));
        down.addStyles(selOff).icon.update(getImage("scroll_down"));
        addButtonListener(up);
        addButtonListener(down);
        if (scrollRange > PAGE_RANGE)
            iface.add(scroll);

        return iface;
    }

    protected void addButtonListener(final Button button) {
        button.layer.addListener(new Mouse.LayerAdapter() {
            @Override
            public void onMouseDown(ButtonEvent event) {
                button.addStyles(selOn);
            }

            @Override
            public void onMouseUp(ButtonEvent event) {
                button.addStyles(selOff);
            }
        });
    }

    @Override
    protected Group createButtomPanel() {
        Group bottom = new Group(AxisLayout.vertical(), Style.HALIGN.left,
                Style.BACKGROUND.is(Background.solid(bgColor)));
        bottom.add(new Shim(0, BOTTOM_MARGIN).addStyles()).add(
                new Label("<ENTER>: Unlock all badges"));
        bottom.layer.setDepth(1);
        return bottom;
    }

    @Override
    public String[] images() {
        ArrayList<String> names = new ArrayList<String>();
        for (Badge badge : model.badges()) {
            names.add("badge_" + badge.name());
        }
        names.add("scroll_up");
        names.add("scroll_down");
        names.add("badge_lock");

        return names.toArray(new String[names.size()]);
    }

    @Override
    protected String title() {
        return "BADGES";
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
    public void update(float delta) {
        if (scroll.isUpButtonDown())
            scroll.scrollUp(BUTTON_SCROLL_DISTANCE);
        else if (scroll.isDownButtonDown())
            scroll.scrollDown(BUTTON_SCROLL_DISTANCE);
        super.update(delta);
    }

}
