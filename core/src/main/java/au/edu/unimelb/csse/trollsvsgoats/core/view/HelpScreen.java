package au.edu.unimelb.csse.trollsvsgoats.core.view;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.log;

import playn.core.Font;
import playn.core.Mouse;
import playn.core.ResourceCallback;
import playn.core.Mouse.MotionEvent;
import react.UnitSlot;
import au.edu.unimelb.csse.trollsvsgoats.core.TrollsVsGoatsGame;
import tripleplay.ui.Background;
import tripleplay.ui.Button;
import tripleplay.ui.Constraints;
import tripleplay.ui.Group;
import tripleplay.ui.Label;
import tripleplay.ui.Shim;
import tripleplay.ui.Style;
import tripleplay.ui.Style.Binding;
import tripleplay.ui.layout.AxisLayout;

public class HelpScreen extends View {
    private final static int TEXT_BG = 0xFF7ECEF4;
    private final static int TAB_SHOWN_COLOR = 0xFF7ECEF4;
    private final static int TAB_HIDDEN_COLOR = 0xFF88ABDA;
    private final static int NAVI_SHOWN_COLOR = 0xFF0000FF;
    private final static int NAVI_HIDDEN_COLOR = 0xFFFFFFFF;
    private final static int NAVI_OVER_COLOR = butBgColor;
    private final static float WIDTH = 650;
    private final static Binding<Background> TAB_SHOWN_STYLE = Style.BACKGROUND
            .is(Background.bordered(TAB_SHOWN_COLOR, 0xFF1B1B1B, 1));
    private final static Binding<Background> TAB_HIDDEN_STYLE = Style.BACKGROUND
            .is(Background.bordered(TAB_HIDDEN_COLOR, 0xFF1B1B1B, 1));

    private Button gameTab, principleTab, currentTab, currentTitleBut;
    private String currentTitle;
    private Group gameHelp, principles;
    private LevelScreen demo;

    public HelpScreen(TrollsVsGoatsGame game) {
        super(game);

        Button[] tabs = new Button[] { gameTab = new Button("HOW TO PLAY"),
                principleTab = new Button("PRINCIPLES") };
        for (final Button tab : tabs) {
            tab.addStyles(TAB_HIDDEN_STYLE,
                    Style.FONT.is(font(Font.Style.BOLD_ITALIC, 16)))
                    .setConstraint(Constraints.fixedSize(200, 30));
            tab.clicked().connect(new UnitSlot() {

                @Override
                public void onEmit() {
                    if (!currentTab.equals(tab)) {
                        currentTab.addStyles(TAB_HIDDEN_STYLE);
                        currentTab = tab;
                        wasAdded();
                    }
                }
            });
        }
    }

    @Override
    protected Group createIface() {
        Group show = null;
        if (currentTab == null || currentTab.equals(gameTab)) {
            currentTab = gameTab;
            show = gameHelp();
        } else if (currentTab.equals(principleTab)) {
            show = principles();
        }
        currentTab.addStyles(TAB_SHOWN_STYLE);
        show.addStyles(
                Style.BACKGROUND.is(Background.bordered(TEXT_BG, 0xFF1B1B1B, 2)
                        .inset(3)))
                .setConstraint(Constraints.fixedWidth(WIDTH));

        Group iface = new Group(AxisLayout.horizontal(), Style.VALIGN.top)
                .add(new Group(AxisLayout.vertical().gap(0), Style.HALIGN.left)
                        .add(new Group(AxisLayout.horizontal().gap(0)).add(
                                gameTab).add(principleTab)).add(show));
        return iface;
    }

    public Group gameHelp() {
        if (gameHelp == null)
            gameHelp = new Group(AxisLayout.vertical().offStretch());
        else
            return gameHelp;
        final Label label;
        final Group texts = new Group(AxisLayout.vertical(), Style.HALIGN.left,
                Style.VALIGN.top).add(
                label = new Label().addStyles(Style.FONT.is(font(
                        Font.Style.BOLD, 20)))).setConstraint(
                Constraints.fixedHeight(118));
        Group navi = new Group(AxisLayout.vertical(), Style.HALIGN.left);
        String titles[] = { "Introduction", "Deployment", "Running" };
        if (currentTitle == null)
            currentTitle = "Introduction";
        for (final String title : titles) {
            int color;
            final Button button = new Button(title);
            if (currentTitle.equals(title)) {
                color = NAVI_SHOWN_COLOR;
                currentTitleBut = button;
                label.text.update(title);
                for (String text : titleToText(title))
                    texts.add(new Label(text).addStyles(Style.HALIGN.left));
            } else
                color = NAVI_HIDDEN_COLOR;
            navi.add(button.addStyles(Style.COLOR.is(color),
                    Style.FONT.is(font(Font.Style.BOLD, 16)),
                    Style.BACKGROUND.is(Background.blank())));
            button.clicked().connect(new UnitSlot() {
                @Override
                public void onEmit() {
                    currentTitle = title;
                    currentTitleBut.addStyles(Style.COLOR.is(NAVI_HIDDEN_COLOR));
                    currentTitleBut = button;
                    button.addStyles(Style.COLOR.is(NAVI_SHOWN_COLOR));
                    label.text.update(title);

                    demo.restart();
                    ((Button) demo.buttonPanel.childAt(1)).text.update("START");
                    if (title.equals("Running"))
                        demo.buttonPanel.childAt(1).setEnabled(true);
                    else
                        demo.buttonPanel.childAt(1).setEnabled(false);

                    texts.removeAll();
                    texts.add(label);
                    for (String text : titleToText(title))
                        texts.add(new Label(text).addStyles(Style.HALIGN.left));
                }
            });
            button.layer.addListener(new Mouse.LayerAdapter() {
                @Override
                public void onMouseOver(MotionEvent event) {
                    if (currentTitle != title)
                        button.addStyles(Style.COLOR.is(NAVI_OVER_COLOR));
                }

                @Override
                public void onMouseOut(MotionEvent event) {
                    if (currentTitle != title)
                        button.addStyles(Style.COLOR.is(NAVI_HIDDEN_COLOR));

                }
            });
        }

        gameHelp.add(new Group(AxisLayout.horizontal().offStretch()).add(texts)
                .add(AxisLayout.stretch(new Shim(1, 1))).add(navi));

        assets().getText("levels/demo.txt", new ResourceCallback<String>() {

            @Override
            public void error(Throwable err) {
                log().error(err.getMessage());
            }

            @Override
            public void done(String resource) {
                model.levelStart(0);
                demo = new LevelScreen(game, resource);
                demo.wasAdded();
                gameHelp.add(demo.root);
                demo.root.addStyles(Style.BACKGROUND.is(Background.blank()));
                demo.topPanel.destroyAll();
                demo.buttonPanel.remove(demo.buttonPanel.childAt(3));
                demo.buttonPanel.childAt(1).setEnabled(false);
                demo.trollInfoPanel.setConstraint(Constraints.fixedWidth(263));
                demo.goatInfoPanel.setConstraint(Constraints.fixedWidth(200));
            }
        });
        return gameHelp;
    }

    public Group principles() {
        if (principles == null)
            principles = new Group(AxisLayout.vertical().gap(10),
                    Style.HALIGN.left);
        else
            return principles;
        for (String text : principlesText()) {
            Font font = font(Font.Style.PLAIN, 18);
            if (text.endsWith(":"))
                font = font(Font.Style.BOLD, 18);
            principles.add(new Label(text).addStyles(Style.FONT.is(font)));
        }
        return principles;
    }

    @Override
    public void wasRemoved() {
    }

    @Override
    protected String title() {
        return "HELP";
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        if (demo != null) {
            demo.update(delta);
        }
    }

    private String[] titleToText(String title) {
        if (title.equals("Introduction"))
            return introductionText();
        else if (title.equals("Deployment"))
            return deploymentText();
        else if (title.equals("Running"))
            return gameRunText();
        return null;
    }

    private String[] principlesText() {
        return new String[] {
                "Moment = Force x Distance.",
                "Cross Product rule:",
                "Moment = Force x distance x sin(angle)",
                "Varignon's Theorem:",
                "The Total moment about a point = ",
                "The sum of the components of the force about the same point.",
                "Couples:",
                "The moment generated by two equal, opposite and noncollinear forces.",
                "The magnitude of the couple is Force x the distance between them.",
                "Principle of moments:",
                "The moment experienced by a body is equal to the sum of",
                "all the moment acting upon it." };
    }

    private String[] introductionText() {
        return new String[] {
                "Each level has a gate in the middle which can be rotated around the pivot.",
                "Trolls push from the left while goats push from the right.",
                "Each troll and goat exerts a particular force according to its type.",
                "The goal is to maintain the equilibrium of the moments caused by both sides." };
    }

    private String[] deploymentText() {
        return new String[] {
                "Trolls and goats walk forward at a constant speed once start.",
                "Before that, you need to deploy trolls on the left side to counter goats.",
                "The force of a unit is denoted as red bars (blue for speed).",
                "Try to spend as few trolls as possible to achieve higher score." };
    }

    private String[] gameRunText() {
        return new String[] {
                "Trolls and Goats will start pushing when they are adjacent to the gate",
                "or a pushing unit in front.",
                "Game is lost when final state is not 0 or once the overall moment going",
                "over 20 N/m, e.g. a 4N Troll at 5 lanes from the pivot." };
    }

}
