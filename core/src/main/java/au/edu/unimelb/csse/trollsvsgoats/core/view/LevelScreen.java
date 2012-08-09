package au.edu.unimelb.csse.trollsvsgoats.core.view;

import java.util.*;

import static playn.core.PlayN.*;
import static tripleplay.ui.layout.TableLayout.COL;
import playn.core.*;
import playn.core.Mouse.*;
import react.UnitSlot;
import tripleplay.game.ScreenStack;
import tripleplay.ui.*;
import tripleplay.ui.Style.Binding;
import tripleplay.ui.layout.*;
import au.edu.unimelb.csse.trollsvsgoats.core.TrollsVsGoatsGame;
import au.edu.unimelb.csse.trollsvsgoats.core.model.*;
import au.edu.unimelb.csse.trollsvsgoats.core.model.units.*;
import au.edu.unimelb.csse.trollsvsgoats.core.model.units.Unit.State;
import au.edu.unimelb.csse.trollsvsgoats.core.view.MessageBox.ChoiceCallBack;
import au.edu.unimelb.csse.trollsvsgoats.core.view.MessageBox.SimpleCallBack;

/**
 * Represents the screen of a game level and controls its running.
 */
public class LevelScreen extends View {

    private static final int SQUARE_HEIGHT = 40,
            SQUARE_WIDTH = SQUARE_HEIGHT - 2;
    private static final int Y_SHIM = 10;
    private static final int LEFT_MARGIN = 5;
    private static final int RIGHT_MARGIN = 5;
    private static final int BUTTON_WIDTH = 110;
    private static final int COST_WIDTH = 100;
    private static final int NUMBER_COLOR = 0xFF0000FF;
    private static final int NUMBER_COLOR2 = 0xFFE5004F;
    private static final int ABILITY_BAR_WIDTH = 16;
    private static final int STRENGTH_BAR_COLOR = 0xFFE5004F;
    private static final int SPEED_BAR_COLOR = 0xFF1D2088;
    private static final float MAX_MOMENT = 20;
    private static final int PUSHING_DISTANCE = 3;
    private static final Binding<Background> selOn = Style.BACKGROUND
            .is(Background.bordered(0, 0xFFF6B37F, 2).inset(3));
    private static final Binding<Background> selOff = Style.BACKGROUND
            .is(Background.bordered(0, 0, 0).inset(3));

    // The unit locations before the game starts.
    private Map<Square, Unit> unitsLocations = new HashMap<Square, Unit>();

    // The first unit in a lane.
    // A lane of units is represented as a linked list.
    private Map<Integer, Unit> headTrolls = new HashMap<Integer, Unit>();
    private Map<Integer, Unit> headGoats = new HashMap<Integer, Unit>();

    private Map<String, Integer> trollCounts = new HashMap<String, Integer>();
    private Map<String, Button> trollIcons = new HashMap<String, Button>();
    private Map<String, Button> goatIcons = new HashMap<String, Button>();
    private Map<String, Label> trollCountLabels = new HashMap<String, Label>();
    private Map<String, Group> trollForceBars = new HashMap<String, Group>();
    private Map<String, Group> trollSpeedBars = new HashMap<String, Group>();
    private Map<String, Group> goatStrengthBars = new HashMap<String, Group>();
    private Map<String, Group> goatSpeedBars = new HashMap<String, Group>();

    private int laneCount;
    private int cost, score, moment;
    private int pivotLocation, bridgeLocation;
    // private Animation introduction, conclusion;
    private Label trollInfoLabel;
    private Label goatInfoLabel;
    private Button preSelGoat;
    private Label costLabel;
    private String selTrollType;
    private boolean started = false, paused = false, hasMovingUnit = false;
    private boolean messageShown = false;
    private boolean showUnitMoment = false;
    private Json.Object json;

    protected Group trollInfoPanel;
    protected Group goatInfoPanel;
    protected Group buttonPanel;
    protected Group middlePanel;
    protected Group bottomPanel;
    protected Button selTrollIcon;
    protected Label momentLabel;

    public LevelScreen(TrollsVsGoatsGame game) {
        super(game);
    }

    public LevelScreen(TrollsVsGoatsGame game, String levelJson) {
        super(game);
        if (height() == 600)
            TOP_MARGIN = -10;
        this.json = json().parse(levelJson);
        laneCount = json.getArray("tiles").length();
        TITLE_FONT = font(Font.Style.BOLD, 22);
        if (model.levelIndex() <= 1)
            showUnitMoment = true;
    }

    @Override
    public Group createIface() {
        root.addStyles(Style.BACKGROUND.is(Background
                .image(getImage("bg_level"))));
        topPanel.addStyles(Style.BACKGROUND.is(Background.blank()));
        Group iface = new Group(AxisLayout.vertical().gap(0).offStretch(),
                Style.HALIGN.center).add(momentLabel = new Label("0")
                .addStyles(Style.FONT.is(font(Font.Style.BOLD, 25)),
                        Style.VALIGN.top, Style.COLOR.is(NUMBER_COLOR))
                .setConstraint(Constraints.fixedHeight(20)));

        back.clicked().disconnect(backSlot);
        back.clicked().connect(new UnitSlot() {
            @Override
            public void onEmit() {
                showBadgesAchievement(new SimpleCallBack() {

                    @Override
                    public void onClose() {
                        game.stack().remove(_this, ScreenStack.NOOP);
                    }
                });
            }

        });

        middlePanel = new Group(new AbsoluteLayout());
        iface.add(new Group(AxisLayout.horizontal()).add(middlePanel)).add(
                new Shim(0, -35));

        bottomPanel = new Group(AxisLayout.horizontal(), Style.HALIGN.left);
        iface.add(bottomPanel).add(
                new Group(AxisLayout.horizontal(), Style.HALIGN.left)
                        .add(costLabel = new Label("Cost: 0")
                                .setConstraint(Constraints
                                        .fixedWidth(COST_WIDTH)))
                        .add(trollInfoLabel = new Label().addStyles(
                                Style.HALIGN.left).setConstraint(
                                Constraints.fixedHeight(60)))
                        .add(AxisLayout.stretch(new Shim(1, 1)))
                        .add(goatInfoLabel = new Label().addStyles(
                                Style.HALIGN.right).setConstraint(
                                Constraints.fixedHeight(40)))
                        .add(new Shim(20, 0)));
        trollInfoPanel = new Group(AxisLayout.horizontal().gap(10),
                Style.VALIGN.top, Style.HALIGN.left).setConstraint(
                Constraints.fixedWidth(width() / 2 - BUTTON_WIDTH / 2 - 4))
                .add(new Shim(LEFT_MARGIN, 0));
        buttonPanel = new Group(AxisLayout.vertical().gap(10),
                Style.VALIGN.bottom).add(new Shim(0, 15));
        goatInfoPanel = new Group(AxisLayout.horizontal().gap(10),
                Style.VALIGN.top, Style.HALIGN.right)
                .setConstraint(trollInfoPanel.constraint());
        bottomPanel.add(trollInfoPanel);
        bottomPanel.add(buttonPanel);
        bottomPanel.add(goatInfoPanel);

        final Button start = button("START").setConstraint(
                Constraints.minWidth(BUTTON_WIDTH));
        final Button reset = button("RESET").setConstraint(
                Constraints.minWidth(BUTTON_WIDTH));
        final Button pause = button("PAUSE").setConstraint(
                Constraints.minWidth(BUTTON_WIDTH));

        // Adds buttons.
        buttonPanel.add(start);
        start.clicked().connect(new UnitSlot() {
            @Override
            public void onEmit() {
                if (messageShown)
                    return;
                if (!started && !paused) {
                    start.text.update("RETRY");
                    proceed();
                } else {
                    start.text.update("START");
                    started = false;
                    pause(false);
                    pause.text.update("PAUSE");
                    restart();
                }
            }
        });

        buttonPanel.add(reset);
        reset.clicked().connect(new UnitSlot() {

            @Override
            public void onEmit() {
                if (messageShown)
                    return;
                if (!started && !paused)
                    clearTrolls();
            }
        });

        buttonPanel.add(pause);
        pause.clicked().connect(new UnitSlot() {

            @Override
            public void onEmit() {
                if (messageShown)
                    return;
                if (started) {
                    if (!paused) {
                        pause(true);
                        pause.text.update("CONTINUE");
                    } else {
                        pause(false);
                        pause.text.update("PAUSE");
                    }
                }
            }
        });

        // Creates troll information panel.
        final Json.Object trolls = json.getObject("trolls");
        final Group table = new Group(new TableLayout(COL.alignLeft(),
                COL.stretch(), COL.stretch(), COL.stretch()).gaps(0, 5));
        trollInfoPanel.add(table);

        for (final String type : trolls.keys()) {
            Troll troll = newTroll(type);
            table.add(new Label("$" + (int) troll.cost()));
            trollCounts.put(type, (int) trolls.getNumber(type));

            Label counter;
            table.add(new Group(AxisLayout.horizontal()).add(
                    counter = new Label(String.valueOf((int) trolls
                            .getNumber(type))).addStyles(
                            Style.FONT.is(SUBTITLE_FONT),
                            Style.COLOR.is(NUMBER_COLOR)).setConstraint(
                            Constraints.fixedWidth(20))).add(
                    new Label("X").addStyles(Style.FONT.is(SUBTITLE_FONT))));
            trollCountLabels.put(type, counter);

            final Button trollIcon = new Button(getImage("troll_" + type));
            table.add(trollIcon);
            trollIcons.put(type, trollIcon);

            if (type.equals(trolls.keys().get(0))) {
                selTrollIcon = trollIcon;
                trollIcon.setStyles(selOn);
            } else
                trollIcon.setStyles(selOff);
            trollIcon.layer.addListener(new Mouse.LayerAdapter() {
                @Override
                public void onMouseOver(MotionEvent event) {
                    selTrollIcon.setStyles(selOff);
                    trollIcon.setStyles(selOn);
                    updateTrollInfo(type);
                }

                @Override
                public void onMouseOut(MotionEvent event) {
                    selTrollIcon.setStyles(selOn);
                    if (!trollIcon.equals(selTrollIcon))
                        trollIcon.setStyles(selOff);
                    updateTrollInfo(selTrollType);
                }

                @Override
                public void onMouseDown(ButtonEvent event) {
                    selTrollIcon.setStyles(selOff);
                    selTrollIcon = trollIcon;
                    trollIcon.setStyles(selOn);
                    selTrollType = type;
                    updateTrollInfo(type);
                }
            });

            Group forceMeter, speedMeter;
            table.add(new Group(AxisLayout.vertical().gap(10),
                    Style.HALIGN.left).add(
                    forceMeter = new Group(AxisLayout.horizontal())).add(
                    speedMeter = new Group(AxisLayout.horizontal())));

            trollForceBars.put(type, forceMeter);
            trollSpeedBars.put(type, speedMeter);
            updateUnitAbility(troll);
        }

        selTrollType = trolls.keys().get(0);
        updateTrollInfo(trolls.keys().get(0));

        // Handles mouse wheel event to select troll.
        mouse().setListener(new Mouse.Adapter() {

            @Override
            public void onMouseWheelScroll(WheelEvent event) {
                int index = 0;
                boolean pred;
                if (PlayN.platformType().equals(Platform.Type.JAVA))
                    pred = event.velocity() > 0;
                else
                    pred = event.velocity() < 0;
                // Down.
                if (pred) {
                    for (int j = 0; j < table.childCount(); j++) {
                        if (selTrollIcon.equals(table.childAt(j))) {
                            if (j == 2)
                                index = table.childCount() - 2;
                            else
                                index = j - 4;
                            break;
                        }
                    }
                    // UP.
                } else {
                    for (int j = 0; j < table.childCount(); j++) {
                        if (selTrollIcon.equals(table.childAt(j))) {
                            if (j == table.childCount() - 2)
                                index = 2;
                            else
                                index = j + 4;
                            break;
                        }
                    }
                }
                selTrollIcon.setStyles(selOff);
                selTrollIcon = (Button) table.childAt(index);
                selTrollIcon.setStyles(selOn);
                selTrollType = trolls.keys().get((index - 2) / 4);
                updateTrollInfo(selTrollType);
                return;
            }
        });

        createMiddlePanel();

        return iface;
    }

    private void createMiddlePanel() {
        Json.Array tiles = json.getArray("tiles");
        int width = tiles.getString(1).length();
        bridgeLocation = (width - 1) / 2;
        boolean isTrollSide = true;
        boolean hasPivot = false;

        float x, y;
        char tileSymbol;
        Image image;
        HashSet<String> goatTypes = new HashSet<String>();

        for (int distance, lane = laneCount; lane > 0; lane--) {
            isTrollSide = true;
            for (int segment = 0; segment < width; segment++) {
                x = segmentToX(segment);
                y = laneToY(lane);
                distance = Math.abs(bridgeLocation - segment);

                tileSymbol = tiles.getString(laneCount - lane).charAt(segment);
                image = getTileImages(tileSymbol).get(0);
                Button tile = new Button().addStyles(Style.BACKGROUND
                        .is(Background.blank()));
                GroupLayer squareLayer = tile.layer;

                switch (tileSymbol) {
                // A segment of a lane.
                case '.':
                case ' ':
                    final float _x = x - 5;
                    final float _y = y + SQUARE_HEIGHT;
                    final boolean _isTrollSide = isTrollSide;
                    final int _lane = lane;
                    final int _segment = segment;
                    final int _distance = distance;

                    squareLayer.addListener(new Mouse.LayerAdapter() {
                        private Square square = new Square(_lane, _segment);

                        // Deploy a troll at the square where the
                        // mouse clicks.
                        @Override
                        public void onMouseDown(ButtonEvent event) {
                            if (!started && !paused) {
                                square.setDistance(_distance);
                                square.setX(_x);
                                square.setY(_y);

                                if (trollCounts.get(selTrollType) == 0)
                                    return;

                                final Troll troll = newTroll(selTrollType);
                                if ((troll instanceof DiggingTroll || troll instanceof SpittingTroll)
                                        && _distance > 4)
                                    return;
                                if (_isTrollSide ^ troll.isOnTrollSide())
                                    return;

                                GroupLayer layer = troll.widget().layer;
                                troll.widget()
                                        .addStyles(
                                                Style.BACKGROUND.is(Background
                                                        .blank()));

                                Animation moveAnimation = new Animation(
                                        getImage("troll_" + selTrollType
                                                + "_move"), 10, troll
                                                .frameTime());
                                troll.setSquare(square);
                                troll.setMoveAnimation(moveAnimation);
                                troll.setDefaultImage(getImage("troll_"
                                        + selTrollType));
                                if (_distance == 1 && troll.speed() != 0)
                                    troll.setState(State.PUSHING);
                                else
                                    troll.setState(State.MOVING);
                                unitsLocations.put(square, troll);
                                trollCounts.put(troll.type(),
                                        trollCounts.get(troll.type()) - 1);
                                updateTrollCounter(troll.type());

                                layer.setOrigin(0, moveAnimation.frame(0)
                                        .height());
                                layer.setDepth(1);
                                middlePanel.add(AbsoluteLayout.at(
                                        troll.widget(), _x, _y));
                                troll.setParent(middlePanel);
                                layer.addListener(new Mouse.LayerAdapter() {

                                    @Override
                                    public void onMouseDown(ButtonEvent event) {
                                        // Removed the troll if game is not yet
                                        // started.
                                        if (!started && !paused) {
                                            troll.widget().layer.destroy();
                                            unitsLocations.remove(square);
                                            trollCounts.put(troll.type(),
                                                    trollCounts.get(troll
                                                            .type()) + 1);
                                            updateTrollCounter(troll.type());
                                            updateCost(-troll.cost());

                                            selTrollIcon.setStyles(selOff);
                                            selTrollIcon = trollIcons.get(
                                                    troll.type()).setStyles(
                                                    selOn);
                                            selTrollType = troll.type();
                                            updateTrollInfo(troll.type());
                                        }
                                    }

                                    @Override
                                    public void onMouseOver(MotionEvent event) {
                                        trollIcons.get(selTrollType).setStyles(
                                                selOff);
                                        trollIcons.get(troll.type()).setStyles(
                                                selOn);
                                        updateTrollInfo(troll.type());
                                        updateUnitAbility(troll);
                                        if (showUnitMoment)
                                            momentLabel.text.update(String
                                                    .valueOf((int) unitMoment(troll)));
                                    }

                                    @Override
                                    public void onMouseOut(MotionEvent event) {
                                        trollIcons.get(troll.type()).setStyles(
                                                selOff);
                                        trollIcons.get(selTrollType).setStyles(
                                                selOn);
                                        updateTrollInfo(selTrollType);
                                        updateUnitAbility(troll);
                                        momentLabel.text.update(String
                                                .valueOf(moment));
                                    }
                                });
                                updateCost(troll.cost());
                                playSound("sound_unitDeployed");
                            }
                        }
                    });
                    break;
                // Gate.
                case '|':
                    isTrollSide = false;
                    squareLayer.setDepth(1);

                    if (lane == 1 && !hasPivot) {
                        pivotLocation = 0;
                        ImageLayer pivotLayer = graphics().createImageLayer(
                                getImage("pivot"));
                        Label pivot = new Label(getImage("pivot"));
                        pivotLayer.setDepth(2);
                        middlePanel.add(AbsoluteLayout.at(pivot, x, y
                                + SQUARE_HEIGHT));

                    }
                    break;
                // Pivot.
                case 'o':
                    hasPivot = true;
                    pivotLocation = lane;
                    squareLayer.setDepth(2);
                    break;

                case 'g':// Little goat.
                case 'G':// Normal goat.
                case 'B':// Big goat.
                case 'F':// Fast goat.
                case 'T':// Butting goat.
                case 'J':// Jumping goat.
                    // Add the tile layer for this unit.
                    Label goatTile = new Label().addStyles(Style.BACKGROUND
                            .is(Background.blank()));
                    if ((segment + 1) % 5 != 0) {
                        goatTile.icon.update(getImage("segment"));
                    } else
                        goatTile.icon.update(getImage("gap"));

                    middlePanel.add(AbsoluteLayout.at(goatTile, x, y));

                    // Add the unit layer.
                    final Goat goat = newGoat(tileSymbol);
                    goat.setParent(middlePanel);
                    goatTypes.add(String.valueOf(tileSymbol));
                    goat.setLayer(tile);
                    Square square = new Square(lane, segment);
                    square.setDistance(distance);
                    if (distance == 1)
                        goat.setState(State.PUSHING);
                    else
                        goat.setState(State.MOVING);
                    x -= 5;
                    y += SQUARE_HEIGHT;
                    square.setX(x);
                    square.setY(y);
                    goat.setSquare(square);
                    unitsLocations.put(square, goat);

                    Animation moveAnimation = new Animation(getTileImages(
                            tileSymbol).get(1), 10, goat.frameTime());
                    goat.setMoveAnimation(moveAnimation);
                    goat.setDefaultImage(image);

                    squareLayer.setOrigin(0, image.height());
                    squareLayer.setDepth(1);
                    squareLayer.addListener(new Mouse.LayerAdapter() {
                        @Override
                        public void onMouseOver(MotionEvent event) {
                            if (preSelGoat != null)
                                preSelGoat.setStyles(selOff);
                            preSelGoat = goatIcons.get(goat.type()).setStyles(
                                    selOn);
                            updateGoatInfo(goat);
                            if (showUnitMoment)
                                momentLabel.text.update(String
                                        .valueOf((int) unitMoment(goat)));
                        }

                        @Override
                        public void onMouseOut(MotionEvent event) {
                            // goatIcons.get(goat.type()).setStyles(selOff);
                            // goatInfoLabel.text.update(null);
                            momentLabel.text.update(String.valueOf(moment));
                        }
                    });
                    break;

                default:
                    break;
                }

                if (image != null) {
                    tile.icon.update(image);
                    middlePanel.add(AbsoluteLayout.at(tile, x, y));
                }

            }
        }

        initGoatsInfo(goatTypes);
    }

    private void initGoatsInfo(Set<String> types) {
        final Group table = new Group(new TableLayout(COL.stretch(),
                COL.alignLeft()).gaps(0, 5), Style.HALIGN.right);
        goatInfoPanel.add(table).add(new Shim(RIGHT_MARGIN, 0));
        boolean first = true;
        for (String symbol : types) {
            final Goat goat = newGoat(symbol.charAt(0));

            Group strengthMeter, speedMeter;
            table.add(new Group(AxisLayout.vertical().gap(10),
                    Style.HALIGN.right).add(
                    strengthMeter = new Group(AxisLayout.horizontal())).add(
                    speedMeter = new Group(AxisLayout.horizontal())));

            final Button goatIcon = new Button(getImage("goat_" + goat.type()));
            if (first) {
                first = false;
                preSelGoat = goatIcon.setStyles(selOn);
                updateGoatInfo(goat);
            } else
                goatIcon.setStyles(selOff);
            table.add(goatIcon);
            goatIcons.put(goat.type(), goatIcon);
            goatIcon.layer.addListener(new Mouse.LayerAdapter() {
                @Override
                public void onMouseOver(MotionEvent event) {
                    if (preSelGoat != null)
                        preSelGoat.setStyles(selOff);
                    preSelGoat = goatIcon;
                    goatIcon.setStyles(selOn);
                    updateGoatInfo(goat);
                }

                // @Override
                // public void onMouseOut(MotionEvent event) {
                // goatIcon.setStyles(selOff);
                // goatInfoLabel.text.update(null);
                // }

            });

            goatStrengthBars.put(goat.type(), strengthMeter);
            goatSpeedBars.put(goat.type(), speedMeter);
            updateUnitAbility(goat);
        }
    }

    @Override
    public String[] images() {
        String[] tiles = new String[] { "segment", "gap", "gate", "pivot" };
        String[] trolls = new String[] { "normal", "little", "fast", "digging",
                "barrow", "cheerLeader", "hungry", "mega", "spitting" };
        String[] goats = new String[] { "little", "normal", "big", "fast",
                "butting", "jumping" };
        List<String> names = new ArrayList<String>(Arrays.asList(tiles));
        for (String name : trolls) {
            names.add("troll_" + name);
            names.add("troll_" + name + "_move");
        }
        for (String name : goats) {
            names.add("goat_" + name);
            names.add("goat_" + name + "_move");
        }
        names.add("bg_level");
        return names.toArray(new String[names.size()]);
    }

    @Override
    public String[] sounds() {
        return new String[] { "sound_unitDeployed" };
    }

    private List<Image> getTileImages(char symbol) {
        List<String> names = new ArrayList<String>();
        List<Image> images = new ArrayList<Image>();
        switch (symbol) {
        case '.':
            names.add("segment");
            break;
        case ' ':
            names.add("gap");
            break;
        case '|':
            names.add("gate");
            break;
        case 'o':
            names.add("pivot");
            break;
        case 'g':// Little goat.
        case 'G':// Normal goat.
        case 'B':// Big goat.
        case 'F':// Fast goat.
        case 'T':// Butting goat.
        case 'J':// Jumping goat.
            names.add("goat_" + newGoat(symbol).type());
            names.add("goat_" + newGoat(symbol).type() + "_move");
            break;
        default:
            log().error("Unsupported square type character '" + symbol + "'");
            return null;
        }

        for (String name : names) {
            images.add(getImage(name));
        }
        return images;
    }

    private void updateTrollCounter(String type) {
        trollCountLabels.get(type).text.update(String.valueOf(trollCounts
                .get(type)));
    }

    private void updateTrollInfo(String type) {
        Troll troll = newTroll(type);
        String text = troll.type().substring(0, 1).toUpperCase()
                + troll.type().substring(1) + " troll " + troll.ability()
                + "\nStrength: " + (int) troll.force();
        if (troll.speed() != Math.round(troll.speed()))
            text += "  Speed: " + troll.speed();
        else
            text += "  Speed: " + (int) troll.speed();
        trollInfoLabel.text.update(text);
    }

    private void updateGoatInfo(Goat goat) {
        String text = goat.type().substring(0, 1).toUpperCase()
                + goat.type().substring(1) + " goat " + goat.ability()
                + "\nStrength: " + (int) goat.force();
        if (goat.speed() != Math.round(goat.speed()))
            text += "  Speed: " + goat.speed();
        else
            text += "  Speed: " + (int) goat.speed();
        goatInfoLabel.text.update(text);
    }

    private void updateUnitAbility(Unit unit) {
        Group strength, speed;
        if (unit instanceof Troll) {
            strength = trollForceBars.get(unit.type());
            speed = trollSpeedBars.get(unit.type());
        } else {
            strength = goatStrengthBars.get(unit.type());
            speed = goatSpeedBars.get(unit.type());
        }
        strength.removeAll();
        speed.removeAll();

        for (int j = 0; j < unit.force(); j++) {
            strength.add(new Shim(ABILITY_BAR_WIDTH, 5)
                    .addStyles(Style.BACKGROUND.is(Background
                            .solid(STRENGTH_BAR_COLOR))));
        }
        if (unit.force() > (int) unit.force()) {
            int div;
            if (unit instanceof Goat)
                div = 1;
            else
                div = 2;
            strength.add(new Shim(ABILITY_BAR_WIDTH / div, 5)
                    .addStyles(Style.BACKGROUND.is(Background
                            .solid(STRENGTH_BAR_COLOR))));
            if (unit instanceof Goat)
                strength.childAt(0).setConstraint(
                        Constraints.fixedWidth(ABILITY_BAR_WIDTH / 2));
        }

        for (int j = 0; j < (int) unit.speed(); j++) {
            speed.add(new Shim(ABILITY_BAR_WIDTH, 5).addStyles(Style.BACKGROUND
                    .is(Background.solid(SPEED_BAR_COLOR))));
        }
        if (unit.speed() > (int) unit.speed()) {
            int div;
            if (unit instanceof Goat)
                div = 1;
            else
                div = 2;
            speed.add(new Shim(ABILITY_BAR_WIDTH / div, 5)
                    .addStyles(Style.BACKGROUND.is(Background
                            .solid(SPEED_BAR_COLOR))));
            if (unit instanceof Goat)
                speed.childAt(0).setConstraint(
                        Constraints.fixedWidth(ABILITY_BAR_WIDTH / 2));
        }
    }

    private void updateCost(float cost) {
        this.cost += cost;
        costLabel.text.update("Cost: " + this.cost);
    }

    private Troll newTroll(String type) {
        Troll troll = null;
        if (type.equals("normal"))
            troll = new NormalTroll();
        else if (type.equals("little"))
            troll = new LittleTroll();
        else if (type.equals("fast"))
            troll = new FastTroll();
        else if (type.equals("digging"))
            troll = new DiggingTroll();
        else if (type.equals("barrow"))
            troll = new BarrowTroll();
        else if (type.equals("cheerLeader"))
            troll = new CheerleaderTroll();
        else if (type.equals("hungry"))
            troll = new HungryTroll();
        else if (type.equals("mega"))
            troll = new MegaTroll();
        else if (type.equals("spitting"))
            troll = new SpittingTroll();
        troll.setMovementTime(model.movementTime());

        return troll;
    }

    private Goat newGoat(char type) {
        Goat goat = null;
        switch (type) {
        case 'g':
            goat = new LittleGoat();
            break;
        case 'G':
            goat = new NormalGoat();
            break;
        case 'B':
            goat = new BigGoat();
            break;
        case 'F':
            goat = new FastGoat();
            break;
        case 'T':
            goat = new ButtingGoat();
            break;
        case 'J':
            goat = new JumpingGoat();
            break;
        default:
            goat = null;
        }
        goat.setMovementTime(model.movementTime());
        return goat;
    }

    private Unit tailUnit(Unit unit) {
        Unit next = unit;
        while (next.back() != null) {
            next = next.back();
        }
        return next;
    }

    private float segmentToX(float segment) {
        return SQUARE_WIDTH * segment;
    }

    private float laneToY(float lane) {
        return SQUARE_HEIGHT * (laneCount - lane) + Y_SHIM;
    }

    /**
     * Start or continue the game.
     */
    private void proceed() {
        if (paused) {
            paused = false;
            return;
        }
        started = true;
        creatUnitLinklists();
    }

    private void creatUnitLinklists() {
        List<Square> squares = new ArrayList<Square>(unitsLocations.keySet());
        // Sorts the squares.
        Collections.sort(squares, new Comparator<Square>() {
            @Override
            public int compare(Square s1, Square s2) {
                if (s1.lane() > s2.lane())
                    return 1;
                else if (s1.lane() == s2.lane()) {
                    if (s1.distance() > s2.distance())
                        return 1;
                }
                return -1;
            }
        });

        // Constructs the unit linked lists.
        Map<Integer, Unit> headUnits = null;
        Unit unit;
        for (Square square : squares) {
            unit = unitsLocations.get(square);
            if (unit instanceof Goat || unit instanceof Troll
                    && !((Troll) unit).isOnTrollSide()) {
                headUnits = headGoats;
            } else if (unitsLocations.get(square) instanceof Troll) {
                headUnits = headTrolls;
            } else
                continue;
            if (headUnits.get(square.lane()) != null) {
                tailUnit(headUnits.get(square.lane())).setBack(
                        unitsLocations.get(square));
            } else
                headUnits.put(square.lane(), unitsLocations.get(square));
        }
    }

    private void pause(boolean paused) {
        this.paused = paused;
    }

    public void restart() {
        started = false;
        paused = false;
        messageShown = false;
        score = 0;
        moment = 0;
        headTrolls.clear();
        headGoats.clear();
        momentLabel.text.update("0");
        momentLabel.addStyles(Style.COLOR.is(NUMBER_COLOR));
        model.levelRestart();

        // Reset units.
        Unit unit = null;
        for (Square square : unitsLocations.keySet()) {
            unit = unitsLocations.get(square);
            unit.setSquare(square);
            if (square.distance() == 1 && unit.speed() != 0)
                unit.setState(State.PUSHING);
            else
                unit.setState(State.MOVING);
            unit.update(0);
            unit.reset();
        }
    }

    // Returns a list of symbol string representing trolls deployment.
    private List<String> trollsDeployment() {
        Unit unit = null;
        for (Square square : unitsLocations.keySet()) {
            unit = unitsLocations.get(square);
            unit.setSquare(square);
            unit.reset();
        }
        headTrolls.clear();
        headGoats.clear();
        creatUnitLinklists();
        ArrayList<String> lanes = new ArrayList<String>();
        for (int l = laneCount; l > 0; l--) {
            String laneString = "";
            Unit troll = headTrolls.get(l);
            for (int i = bridgeLocation - 1; i >= 0; i--) {
                if (troll != null && troll.square().segment() == i) {
                    laneString = troll.type().substring(0, 1) + laneString;
                    troll = troll.back();
                } else {
                    if ((bridgeLocation - 1 - i) % 5 == 0)
                        laneString = " " + laneString;
                    else
                        laneString = "-" + laneString;
                }
            }
            lanes.add(laneString);
        }
        return lanes;
    }

    private void removeUnit(Unit unit) {
        if (unit == null)
            return;
        if (unit.front() != null)
            unit.front().removeBack();
        else {
            if (unit.back() != null) {
                unit.back().removeFront();
                if (unit instanceof Troll)
                    headTrolls.put(unit.square().lane(), unit.back());
                else
                    headGoats.put(unit.square().lane(), unit.back());
            } else {
                if (unit instanceof Troll)
                    headTrolls.remove(unit.square().lane());
                else {
                    headGoats.remove(unit.square().lane());
                }
            }
        }
        unit.widget().layer.setVisible(false);
    }

    private void clearTrolls() {
        Set<Unit> removes = new HashSet<Unit>();
        for (Unit unit : unitsLocations.values()) {
            if (unit instanceof Troll) {
                unit.widget().layer.destroy();
                trollCounts.put(unit.type(), trollCounts.get(unit.type()) + 1);
                updateTrollCounter(unit.type());
                removes.add(unit);
            }
        }
        for (Unit unit : removes) {
            unitsLocations.remove(unit.square());
        }
        cost = 0;
        updateCost(0);
    }

    /**
     * Moves all the units of a lane forward if it's possible, handles collision
     * with the front unit. Returns the overall moments generated by this lane
     * of units.
     */
    private float updateUnits(Unit unit, float delta) {
        float moments = 0;
        Set<Unit> retryUnits = new HashSet<Unit>();
        while (unit != null) {
            boolean pushing = false;
            if (!unit.state().equals(State.PUSHING)
                    && !unit.state().equals(State.BLOCKED) && unit.speed() != 0) {
                hasMovingUnit = true;
                if (adjacent(unit, unit.front())) {
                    State state = unit.front().state();
                    if (state.equals(State.PUSHING))
                        pushing = true;
                    else if (state.equals(State.BLOCKED))
                        unit.setState(State.BLOCKED);
                }
                // If a unit can moves.
                if (unit.updateTimer(delta) <= 0
                        && !unit.state().equals(State.BLOCKED)) {
                    // Handles collision.
                    if (adjacent(unit, unit.front())) {
                        unit.notifyColliedWithFront();
                        if (unit.front() != null)
                            unit.front().notifyColliedWithBack();
                        // Jumping goat becomes the head of a lane.
                        else if (unit instanceof JumpingGoat)
                            headGoats.put(unit.square().lane(), unit);

                        if (unit instanceof FastTroll) {
                            removeUnit(unit.front());
                            removeUnit(unit);
                        }
                        // Tries to move the previous unit after collision.
                        else if (!retryUnits.contains(unit.front())
                                && unit.front() != null
                                && unit.front().timer() <= 0) {
                            unit = unit.front();
                            retryUnits.add(unit);
                            continue;
                        }
                    }
                    if (!adjacent(unit, unit.front())
                            || unit.state().equals(State.JUMPING)) {
                        Square s1 = unit.square();
                        // Initialises the front square.
                        int moveDistance = unit.state().equals(State.JUMPING) ? 2
                                : 1;
                        Square s2 = new Square(s1.lane(),
                                s1.segment() < bridgeLocation ? s1.segment()
                                        + moveDistance : s1.segment()
                                        - moveDistance);
                        s2.setX(segmentToX(s2.segment()));
                        s2.setY(s1.getY());
                        s2.setDistance(s1.distance() - moveDistance);
                        unit.move(s2);
                        unit.setState(State.MOVING);
                        if (s2.distance() == 1 || adjacent(unit, unit.front())
                                && unit.front().state().equals(State.PUSHING))
                            pushing = true;
                    }
                }
            } else if (unit.speed() != 0)
                pushing = true;

            if (pushing) {
                // Handles HungryTroll.
                if (unit.square().distance() == 1
                        && unit instanceof HungryTroll
                        && !((HungryTroll) unit).hasEaten()) {
                    Unit head = headGoats.get(unit.square().lane());
                    if (head != null && head.square().distance() == 1
                            && head instanceof Goat) {
                        removeUnit(headGoats.get(unit.square().lane()));
                        ((HungryTroll) unit).setEaten();
                        model.setGoatEaten();
                        moments += unitMoment(head);
                    }
                }
                if (unit.square().distance() <= PUSHING_DISTANCE) {
                    unit.setState(State.PUSHING);
                    moments += unitMoment(unit);
                } else
                    unit.setState(State.BLOCKED);
            }
            unit.update(delta);
            unit = unit.back();
        }

        return moments;
    }

    private float unitMoment(Unit unit) {
        if (unit.square().lane() > pivotLocation)
            return (unit.square().lane() - pivotLocation) * unit.force();
        else if (unit.square().lane() < pivotLocation)
            return -(pivotLocation - unit.square().lane()) * unit.force();
        else
            return 0;
    }

    private boolean adjacent(Unit u1, Unit u2) {
        if (u1 == null || u2 == null)
            return false;
        return Math.abs(u1.square().distance() - u2.square().distance()) == 1;
    }

    private void showBadgesAchievement(SimpleCallBack callBack) {
        final List<Badge> badges = model.newAchievedBadges();
        for (Badge badge : badges) {
            if (!badge.isAchieved())
                game.setBadgeAchieve(badge);
        }

        if (!badges.isEmpty()) {
            messageShown = true;
            paused = true;
            showBadgesMessage(badges, callBack);
        } else if (callBack != null)
            callBack.onClose();
    }

    private void showBadgesMessage(final List<Badge> badges,
            final SimpleCallBack callBack) {
        Badge badge = badges.get(0);
        MessageBox messageBox = new MessageBox(game, "NEW BADGE", "OK",
                new SimpleCallBack() {

                    @Override
                    public void onClose() {
                        badges.get(0).setShown();
                        game.closeMessageBox();
                        badges.remove(0);
                        if (!badges.isEmpty())
                            showBadgesMessage(badges, callBack);
                        else if (callBack != null) {
                            callBack.onClose();
                        }
                    }

                });
        messageBox
                .addBody(new Group(AxisLayout.horizontal(), Style.HALIGN.left)
                        .addStyles(
                                Style.BACKGROUND.is(Background
                                        .solid(0xFF7ECEF4)))
                        .add(new Label(getImage(badge.iconName()))
                                .setConstraint(Constraints
                                        .fixedWidth(BadgesScreen.ICON_WIDTH)))
                        .add(new Group(AxisLayout.vertical())
                                .add(new Label(badge.displayName())
                                        .addStyles(Style.FONT.is(font(
                                                Font.Style.BOLD, 16))))
                                .add(new Label(badge.description()))
                                .setConstraint(
                                        Constraints
                                                .fixedWidth(BadgesScreen.DESCRIPTION_WIDTH - 100))));
        game.showMessageBox(this, messageBox);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        if (!started || paused)
            return;
        hasMovingUnit = false;
        float trollsMoments = 0;
        float goatsMoments = 0;
        for (Unit goat : headGoats.values()) {
            goatsMoments += updateUnits(goat, delta);
        }
        for (Unit troll : headTrolls.values()) {
            trollsMoments += updateUnits(troll, delta);
        }
        if (trollsMoments != goatsMoments)
            model.setMomentOverZero();

        this.moment = (int) (trollsMoments - goatsMoments);
        momentLabel.text.update(String
                .valueOf((int) (trollsMoments - goatsMoments)));

        if (model.levelIndex() == 0)
            return;

        // To check whether the game should end.
        if (!hasMovingUnit
                || Math.abs(trollsMoments - goatsMoments) > MAX_MOMENT) {
            paused = true;
            messageShown = true;

            if (trollsMoments - goatsMoments != 0) {
                momentLabel.addStyles(Style.COLOR.is(NUMBER_COLOR2));
                MessageBox messageBox = new MessageBox(game, "LEVEL FAILED",
                        "Retry", "Level", new ChoiceCallBack() {

                            @Override
                            public void onLeftButton() {
                                model.setBadgesShown();
                                game.closeMessageBox();
                                showBadgesAchievement(new SimpleCallBack() {
                                    @Override
                                    public void onClose() {
                                        ((Button) buttonPanel.childAt(1)).text
                                                .update("START");
                                        restart();
                                    }
                                });
                            }

                            @Override
                            public void onRightButton() {
                                model.setBadgesShown();
                                game.closeMessageBox();
                                showBadgesAchievement(new SimpleCallBack() {
                                    @Override
                                    public void onClose() {
                                        game.stack().remove(_this,
                                                ScreenStack.NOOP);
                                    }
                                });
                            }

                        });
                game.showMessageBox(this, messageBox);
            } else {
                Json.Object scores = json.getObject("scores");
                for (String cost : scores.keys()) {
                    if (this.cost <= Integer.valueOf(cost)) {
                        if (score < scores.getInt(cost))
                            score = scores.getInt(cost);
                    }
                }
                game.levelCompleted(score);
                // Log trolls deployment when complete the level.
                game.logTrollsDeployment(trollsDeployment());
                MessageBox messageBox = new MessageBox(game, "LEVEL COMPLETED",
                        "Next", "Retry", new ChoiceCallBack() {

                            @Override
                            public void onLeftButton() {
                                model.setBadgesShown();
                                game.closeMessageBox();
                                showBadgesAchievement(new SimpleCallBack() {
                                    @Override
                                    public void onClose() {
                                        game.loadNextLevel();
                                    }
                                });
                            }

                            @Override
                            public void onRightButton() {
                                model.setBadgesShown();
                                game.closeMessageBox();
                                showBadgesAchievement(new SimpleCallBack() {
                                    @Override
                                    public void onClose() {
                                        ((Button) buttonPanel.childAt(1)).text
                                                .update("START");
                                        restart();
                                    }
                                });
                            }

                        });
                Group stars = new Group(AxisLayout.horizontal());
                Group group = new Group(AxisLayout.vertical()).add(stars);
                for (int i = 0; i < 3; i++) {
                    if (i < score)
                        stars.add(new Label(getImage("star_solid")));
                    else
                        stars.add(new Label(getImage("star_empty")));
                }
                if (score < 3)
                    for (String cost : scores.keys()) {
                        if (scores.getInt(cost) == score + 1)
                            group.add(new Label("Lower cost to " + cost
                                    + " for next star"));
                    }
                messageBox.addBody(group);
                game.showMessageBox(this, messageBox);
            }
        }
        // TODO To show the new badge immediately?
        // else
        // showBadgesAchievement(new SimpleCallBack() {
        //
        // @Override
        // public void onOk() {
        // messageShown = false;
        // paused = false;
        // }
        // });
    }

    @Override
    protected String title() {
        return "LEVEL " + model.levelIndex();
    }

    @Override
    protected boolean noTransition() {
        return true;
    }

}
