package au.edu.unimelb.csse.trollsvsgoats.core.view;

import static playn.core.PlayN.*;

import au.edu.unimelb.csse.trollsvsgoats.core.TrollsVsGoatsGame;
import au.edu.unimelb.csse.trollsvsgoats.core.model.GameModel;
import playn.core.Font;
import playn.core.GroupLayer;
import playn.core.Image;
import playn.core.Mouse;
import playn.core.Mouse.ButtonEvent;
import playn.core.Mouse.MotionEvent;
import react.UnitSlot;
import tripleplay.game.ScreenStack;
import tripleplay.game.UIAnimScreen;
import tripleplay.ui.Background;
import tripleplay.ui.Button;
import tripleplay.ui.Group;
import tripleplay.ui.Icon;
import tripleplay.ui.Label;
import tripleplay.ui.Root;
import tripleplay.ui.Shim;
import tripleplay.ui.SimpleStyles;
import tripleplay.ui.Style;
import tripleplay.ui.Stylesheet;
import tripleplay.ui.layout.AxisLayout;

/**
 * Represents a screen in the game.
 */
public abstract class View extends UIAnimScreen {

    protected final static int bgColor = 0xFF00A0E9;
    protected Font TITLE_FONT = font(Font.Style.BOLD_ITALIC, 24);
    protected Font SUBTITLE_FONT = font(Font.Style.BOLD, 24);
    protected int TOP_MARGIN = 30;
    protected final static int butBgColor = 0xFFFACD89,
            butUlColor = 0xFFEEEEEE, butBrColor = 0xFFAAAAAA,
            butBoColor = 0xFFF6B37F;
    protected final static Background butBg = Background.roundRect(butBgColor,
            5, butUlColor, 2).inset(7, 6, 0, 6);
    protected final static Background butOverBg = Background.roundRect(
            butBgColor, 5, butBoColor, 2).inset(6, 7, 1, 5);
    protected final static Background butSelBg = Background.roundRect(
            butBgColor, 5, butBrColor, 2).inset(8, 5, -1, 7);

    protected TrollsVsGoatsGame game;
    protected GameModel model;
    protected Root root;
    protected Button back;
    protected UnitSlot backSlot;
    protected Group topPanel;
    protected final View _this = this;

    public View(TrollsVsGoatsGame game) {
        this.game = game;
        this.model = game.model();
    }

    @Override
    public void wasAdded() {
        super.wasAdded();
        root = iface.createRoot(AxisLayout.vertical().offStretch(),
                stylesheet(), layer);
        root.addStyles(Style.BACKGROUND.is(Background.solid(bgColor)),
                Style.VALIGN.top);
        root.setSize(width(), height());
        topPanel = new Group(AxisLayout.vertical().offStretch(),
                Style.BACKGROUND.is(Background.solid(bgColor)));
        root.add(topPanel
                .add(new Shim(0, TOP_MARGIN))
                .add(new Group(AxisLayout.horizontal(), Style.HALIGN.left)
                        .add(this.back = new Button(getIcon("back_off"))))
                .add(new Label(title()).addStyles(Style.FONT.is(TITLE_FONT))));
        topPanel.layer.setDepth(1);
        back.setStyles(Style.BACKGROUND.is(Background.blank()));
        back.layer.addListener(new Mouse.LayerAdapter() {
            @Override
            public void onMouseOver(MotionEvent event) {
                back.icon.update(getIcon("back_on"));
            }

            @Override
            public void onMouseOut(MotionEvent event) {
                back.icon.update(getIcon("back_off"));
            }
        });
        back.clicked().connect(backSlot = new UnitSlot() {

            @Override
            public void onEmit() {
                if (noTransition())
                    game.stack().remove(_this, ScreenStack.NOOP);
                else
                    game.stack().remove(_this);
            }
        });
        if (subtitle() != null)
            root.add(new Label(subtitle()));
        Group iface = createIface();
        if (iface != null)
            root.add(iface.setConstraint(AxisLayout.stretched()));

        Group bottom = createButtomPanel();
        if (bottom != null)
            root.add(bottom);
    };

    public GroupLayer layer() {
        return this.layer;
    }

    /**
     * Override this method and return a group that contains your main UI, or
     * null.
     */
    protected abstract Group createIface();

    protected Group createButtomPanel() {
        return null;
    };

    /** Returns the title of this demo. */
    protected abstract String title();

    /** Returns an explanatory subtitle for this screen, or null. */
    protected String subtitle() {
        return null;
    }

    /** Returns the stylesheet to use for this screen. */
    protected Stylesheet stylesheet() {
        return SimpleStyles.newSheet();
    }

    /** Whether to show this view without transition */
    protected boolean noTransition() {
        return false;
    }

    protected Font font(Font.Style style, int size) {
        return graphics().createFont("Helvetica", style, size);
    }

    protected Button button(String name) {
        return button(name, butBg, butOverBg, butSelBg);
    }

    protected Button button(String name, final Background butBg,
            final Background butOverBg, final Background butSelBg) {
        final Button button = new Button(name);
        button.addStyles(Style.BACKGROUND.is(butBg),
                Style.FONT.is(font(Font.Style.PLAIN, 18)));
        button.layer.addListener(new Mouse.LayerAdapter() {
            @Override
            public void onMouseOver(MotionEvent event) {
                button.addStyles(Style.BACKGROUND.is(butOverBg));
            }

            @Override
            public void onMouseOut(MotionEvent event) {
                button.addStyles(Style.BACKGROUND.is(butBg));
            }

            @Override
            public void onMouseDown(ButtonEvent event) {
                if (button.isEnabled())
                    button.addStyles(Style.BACKGROUND.is(butSelBg));
            }
        });
        return button;
    }

    protected Image getImage(String path) {
        return game.getImage(path);
    }
    
    protected Icon getIcon(String path) {
    	return game.getIcon(path);
    }

    protected void playSound(String name) {
        if (model.isSoundEnabled())
            game.getSound(name).play();
    }

    /** Images needed for this screen. */
    public String[] images() {
        return null;
    }

    /** Sounds needed for this screen. */
    public String[] sounds() {
        return null;
    }

    @Override
    public void wasRemoved() {
        // layer.destroy();
        super.wasRemoved();
        iface.destroyRoot(root);
        while (layer.size() > 0)
            layer.get(0).destroy();
    }
}
