package au.edu.unimelb.csse.trollsvsgoats.core.view;

import java.util.*;

import static playn.core.PlayN.*;
import playn.core.AssetWatcher;
import playn.core.GroupLayer;
import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.Json;
import playn.core.Json.Array;
import playn.core.Mouse;
import playn.core.Sound;
import playn.core.Mouse.*;
import react.UnitSlot;
import tripleplay.ui.*;
import tripleplay.ui.layout.*;
import au.edu.unimelb.csse.trollsvsgoats.core.TrollsVsGoatsGame;
import au.edu.unimelb.csse.trollsvsgoats.core.model.*;
import au.edu.unimelb.csse.trollsvsgoats.core.model.units.*;
import au.edu.unimelb.csse.trollsvsgoats.core.model.units.Unit.State;

/**
 * Represents the screen of a game level and controls its running.
 */
public class Level extends View {

    // The unit locations before the game starts.
    private Map<Square, Unit> unitsLocations = new HashMap<Square, Unit>();

    // The first unit in a lane.
    // A lane of units is represented as a linked list.
    private Map<Integer, Unit> headTrolls = new HashMap<Integer, Unit>();
    private Map<Integer, Unit> headGoats = new HashMap<Integer, Unit>();

    private Map<String, Integer> availableTrolls = new HashMap<String, Integer>();
    private Map<String, GroupLayer> availableTrollLayers = new HashMap<String, GroupLayer>();
    private GroupLayer availableTrollGroup = graphics().createGroupLayer();

    private GroupLayer momentLayer;

    private final int squareHeight = 40, squareWidth = squareHeight - 2;
    private int laneCount;
    private int pivotLocation, bridgeLocation;
    private Animation introduction, conclusion, counterImages;
    private Label trollInfoLabel;
    private Label goatInfoLabel;
    private String selectedTrollType;
    private int selectedTrollIndex;
    private boolean started = false, paused = false;
    private Json.Object json;
    private Interface iface;

    public Level(TrollsVsGoatsGame game, String levelJson) {
        super(game);
        this.json = json().parse(levelJson);
        laneCount = json.getArray("tiles").length();
    }

    @Override
    public void init() {
        // Adds background image.
        layer.add(graphics().createImageLayer(
                getImage("bg_level" + game.levelIndex())));

        // Initialises tripleplay interface.
        iface = new Interface();
        Styles buttonStyles = Styles.none().addSelected(
                Style.BACKGROUND.is(Background
                        .image(getImage("button_selected"))));
        Stylesheet rootSheet = Stylesheet.builder()
                .add(Button.class, buttonStyles).create();
        Root root = iface.createRoot(AxisLayout.vertical().gap(15), rootSheet);
        root.setSize(graphics().width(), graphics().height());
        layer.add(root.layer);
        Group buttons;
        Group trollInfo;
        Group goatInfo;
        root.add(buttons = new Group(new AbsoluteLayout()));

        // Adds unit info labels.
        root.add(trollInfo = new Group(new AbsoluteLayout()));
        root.add(goatInfo = new Group(new AbsoluteLayout()));
        trollInfo.add(AbsoluteLayout
                .at(trollInfoLabel = new Label(), -250, -20));
        goatInfo.add(AbsoluteLayout.at(goatInfoLabel = new Label(), 500, -60));

        // Adds buttons.
        final Button startButton = new Button()
                .setIcon(getImage("button_start"));
        int y = (int) laneToY(-3);
        buttons.add(AbsoluteLayout.at(startButton, 0, y));
        startButton.clicked().connect(new UnitSlot() {
            @Override
            public void onEmit() {
                if (!started) {
                    startButton.setIcon(getImage("button_restart"));
                    proceed();
                } else {
                    startButton.setIcon(getImage("button_start"));
                    started = false;
                    restart(true);
                }
            }
        });

        Button button = new Button().setIcon(getImage("button_clear"));
        buttons.add(AbsoluteLayout.at(button, 0, y += 30));
        button.clicked().connect(new UnitSlot() {

            @Override
            public void onEmit() {
                if (!started)
                    clearTrolls();
            }
        });

        button = new Button().setIcon(getImage("button_back"));
        buttons.add(AbsoluteLayout.at(button, 0, y + 30));
        button.clicked().connect(new UnitSlot() {

            @Override
            public void onEmit() {
                game.showLevelMenu();
            }
        });

        loadResources();
    }

    private void loadResources() {
        final Json.Array tiles = json.getArray("tiles");
        AssetWatcher assetWatcher = new AssetWatcher(
                new AssetWatcher.Listener() {

                    @Override
                    public void error(Throwable e) {
                        log().error("Error loading images: " + e.getMessage());
                    }

                    @Override
                    public void done() {
                        // Sets up squares once all images all loaded.
                        loadTiles(tiles);
                    }
                });

        // Loads tile images.
        int distance = tiles.getString(1).length();
        int lane = tiles.length();
        getImage("pivot");
        for (int l = 0; l < lane; l++) {
            for (int d = 0; d < distance; d++) {
                for (Image image : getTileImages(tiles.getString(l).charAt(d))) {
                    if (image != null)
                        assetWatcher.add(image);
                }
            }
        }

        // Loads troll images and creates available troll layers.
        final Json.Object trolls = json.getObject("trolls");
        updateTrollInfo(trolls.keys().get(0), 0);
        final ImageLayer selectedLayer = graphics().createImageLayer(
                getImage("square"));
        assetWatcher.add(getImage("numbers"));
        counterImages = new Animation(getImage("numbers"), 10, 0);
        int i = 0;
        for (final String type : trolls.keys()) {
            assetWatcher.add(getImage("troll_" + type));
            assetWatcher.add(getImage("troll_" + type + "_move"));
            availableTrolls.put(type, (int) trolls.getNumber(type));

            // Creates a layer to show available troll.
            final GroupLayer group = graphics().createGroupLayer();
            availableTrollLayers.put(type, group);
            ImageLayer layer = graphics().createImageLayer(
                    getImage("troll_" + type));
            final int _i = i;
            final int y = i % 2 == 0 ? i / 2 * 52 : (i - 1) * 52;
            final int x = i % 2 == 0 ? 0 : 120;
            layer.addListener(new Mouse.Adapter() {
                @Override
                public void onMouseDown(ButtonEvent event) {
                    group.add(selectedLayer);
                    selectedLayer.setTranslation(25 + x, 330 + y);
                    selectedTrollType = type;
                    updateTrollInfo(type, _i);
                }
            });
            group.addAt(layer, 25 + x, 330 + y);
            if (i == 0)
                group.addAt(selectedLayer, 25 + x, 330);
            layer = graphics().createImageLayer(getImage("cross"));
            group.addAt(layer, 80 + x, 340 + y);
            layer = graphics().createImageLayer(
                    counterImages.frame((int) trolls.getNumber(type)));
            layer.setDepth(3);
            group.addAt(layer, 105 + x, 344 + y);
            group.setDepth(i);
            availableTrollGroup.add(group);
            i++;
        }
        layer.add(availableTrollGroup);

        // Adds layers for the moment meter.
        momentLayer = graphics().createGroupLayer();
        momentLayer.setVisible(false);
        ImageLayer layer = graphics().createImageLayer(counterImages.frame(0));
        layer.setTranslation(graphics().width() / 2 - 10, 53);
        momentLayer.add(layer);
        layer = graphics().createImageLayer(counterImages.frame(0));
        layer.setTranslation(graphics().width() / 2 + 10, 53);
        momentLayer.add(layer);
        layer = graphics().createImageLayer(getImage("minus"));
        layer.setTranslation(graphics().width() / 2 - 29, 53);
        momentLayer.add(layer);
        this.layer.add(momentLayer);

        // Handles mouse wheel event to select troll.
        mouse().setListener(new Mouse.Adapter() {

            @Override
            public void onMouseWheelScroll(WheelEvent event) {
                if (event.velocity() > 0) {
                    selectedTrollIndex = selectedTrollIndex != availableTrollGroup
                            .size() - 1 ? selectedTrollIndex + 1 : 0;
                } else {
                    selectedTrollIndex = selectedTrollIndex != 0 ? selectedTrollIndex - 1
                            : availableTrollGroup.size() - 1;
                }
                ((GroupLayer) availableTrollGroup.get(selectedTrollIndex))
                        .add(selectedLayer);
                int x = selectedTrollIndex % 2 == 0 ? 0 : 120;
                int y = selectedTrollIndex % 2 == 0 ? selectedTrollIndex / 2 * 52
                        : (selectedTrollIndex - 1) * 52;
                selectedLayer.setTranslation(25 + x, 330 + y);
                updateTrollInfo(trolls.keys().get(selectedTrollIndex),
                        selectedTrollIndex);
            }

        });

        // Loads sounds.
        assetWatcher.add(getSound("sound_unitDeployed"));

        assetWatcher.start();
    }

    private void loadTiles(Array tiles) {
        int width = tiles.getString(1).length();
        bridgeLocation = (width - 1) / 2;
        boolean isTrollSide = true;
        boolean hasPivot = false;

        float x, y;
        char tileSymbol;
        Image image;

        for (int distance, lane = laneCount; lane > 0; lane--) {
            isTrollSide = true;
            for (int segment = 0; segment < width; segment++) {
                x = segmentToX(segment);
                y = laneToY(lane);
                distance = Math.abs(bridgeLocation - segment);

                tileSymbol = tiles.getString(laneCount - lane).charAt(segment);
                image = getTileImages(tileSymbol).get(0);
                ImageLayer squareLayer = graphics().createImageLayer();

                switch (tileSymbol) {
                // A segment of a lane.
                case '.':
                case ' ':
                    final float _x = x - 5;
                    final float _y = y + squareHeight;
                    final boolean _isTrollSide = isTrollSide;
                    final int _lane = lane;
                    final int _segment = segment;
                    final int _distance = distance;
                    final GroupLayer _layer = layer;

                    squareLayer.addListener(new Mouse.Adapter() {

                        // Deploy a troll at the square where the
                        // mouse clicks.
                        @Override
                        public void onMouseDown(ButtonEvent event) {
                            if (!started) {
                                Square square = new Square(_lane, _segment);
                                square.setDistance(_distance);
                                square.setX(_x);
                                square.setY(_y);
                                // Removed the troll if the square is occupied.
                                if (unitsLocations.containsKey(square)) {
                                    if (unitsLocations.get(square) instanceof Goat)
                                        return;
                                    Troll troll = (Troll) unitsLocations
                                            .get(square);
                                    troll.layer().destroy();
                                    unitsLocations.remove(square);
                                    availableTrolls.put(
                                            troll.type(),
                                            availableTrolls.get(troll.type()) + 1);
                                    updateTrollCounter(troll.type());
                                    return;
                                }

                                if (availableTrolls.get(selectedTrollType) == 0)
                                    return;

                                Troll troll = newTroll(selectedTrollType);
                                if (_isTrollSide ^ troll.isOnTrollSide())
                                    return;

                                Animation moveAnimation = new Animation(
                                        getImage("troll_" + selectedTrollType
                                                + "_move"), 10, troll
                                                .frameTime());
                                troll.setMoveAnimation(moveAnimation);
                                troll.setSquare(square);
                                troll.setDefaultImage(getImage("troll_"
                                        + selectedTrollType));
                                if (_distance == 1 && troll.speed() != 0)
                                    troll.setState(State.PUSHING);
                                else
                                    troll.setState(State.MOVING);
                                unitsLocations.put(square, troll);
                                availableTrolls.put(troll.type(),
                                        availableTrolls.get(troll.type()) - 1);
                                updateTrollCounter(troll.type());

                                ImageLayer layer = troll.layer();
                                layer.setOrigin(0, moveAnimation.frame(0)
                                        .height());
                                layer.setTranslation(_x, _y);
                                layer.setDepth(1);
                                _layer.add(layer);

                                getSound("sound_unitDeployed").play();
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
                        pivotLayer.setTranslation(x, y + squareHeight);
                        pivotLayer.setDepth(2);
                        layer.add(pivotLayer);
                    }
                    break;
                // Pivot.
                case 'o':
                    hasPivot = true;
                    pivotLocation = lane;
                    squareLayer.setDepth(2);
                    break;
                // Little goat.
                case 'g':
                    // Add the tile layer for this unit.
                    ImageLayer tileLayer = graphics().createImageLayer();
                    if ((segment - 1) % 5 != 0) {
                        tileLayer.setImage(getImage("segment"));
                    } else
                        tileLayer.setImage(getImage("gap"));

                    tileLayer.setTranslation(x, y);
                    layer.add(tileLayer);

                    // Add the unit layer.
                    final Goat goat = newGoat(tileSymbol);
                    goat.setLayer(squareLayer);
                    Square square = new Square(lane, segment);
                    square.setDistance(distance);
                    if (distance == 1)
                        goat.setState(State.PUSHING);
                    else
                        goat.setState(State.MOVING);
                    x -= 5;
                    y += squareHeight;
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
                    squareLayer.addListener(new Mouse.Adapter() {
                        @Override
                        public void onMouseDown(ButtonEvent event) {
                            updateGoatInfo(goat);
                        }
                    });
                    break;

                default:
                    break;
                }

                if (image != null) {
                    squareLayer.setImage(image);
                    squareLayer.setTranslation(x, y);
                    layer.add(squareLayer);
                }

            }
        }
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
        case 'g':
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

    private Image getImage(String name) {
        return game.getImage(name);
    }

    private Sound getSound(String name) {
        return game.getSound(name);
    }

    private void updateMoment(Integer moment) {
        momentLayer.setVisible(true);
        ((ImageLayer) momentLayer.get(0)).setImage(counterImages.frame(Math
                .abs(moment) / 10));
        ((ImageLayer) momentLayer.get(1)).setImage(counterImages.frame(Math
                .abs(moment) % 10));
        if (moment < 0)
            ((ImageLayer) momentLayer.get(2)).setVisible(true);
        else
            ((ImageLayer) momentLayer.get(2)).setVisible(false);
    }

    private void updateTrollCounter(String type) {
        int i = type.equals(selectedTrollType) ? 3 : 2;
        ImageLayer layer = (ImageLayer) availableTrollLayers.get(type).get(i);
        layer.setImage(counterImages.frame(availableTrolls.get(type)));
        layer.setDepth(i);
    }

    private void updateTrollInfo(String selectedType, int selectedIndex) {
        selectedTrollType = selectedType;
        selectedTrollIndex = selectedIndex;
        Troll troll = newTroll(selectedType);
        trollInfoLabel.text.update(troll.type() + " troll: " + troll.force()
                + "N force, " + troll.speed() + " speed");
    }

    private void updateGoatInfo(Goat goat) {
        goatInfoLabel.text.update(goat.type() + " goat: " + goat.force()
                + "N force, " + goat.speed() + " speed");
    }

    private Troll newTroll(String type) {
        if (type.equals("normal"))
            return new NormalTroll();
        else if (type.equals("fast"))
            return new FastTroll();
        else if (type.equals("digging"))
            return new DiggingTroll();
        else if (type.equals("barrow"))
            return new BarrowTroll();
        else if (type.equals("cheerleader"))
            return new CheerleaderTroll();
        else if (type.equals("hungry"))
            return new HungryTroll();
        else if (type.equals("mega"))
            return new MegaTroll();
        else if (type.equals("spitting"))
            return new SpittingTroll();

        return null;
    }

    private Goat newGoat(char type) {
        switch (type) {
        case 'g':
            return new LittleGoat();
        default:
            return null;
        }
    }

    private Unit tailUnit(Unit unit) {
        Unit next = unit;
        while (next.back() != null) {
            next = next.back();
        }
        return next;
    }

    private float segmentToX(float segment) {
        return squareWidth * segment;
    }

    private float laneToY(float lane) {
        return squareHeight * (laneCount - lane + 2);
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

    private void pause() {
        paused = true;
    }

    public void restart(boolean keepTrollsDeployment) {
        started = false;
        paused = false;
        headTrolls.clear();
        headGoats.clear();
        momentLayer.setVisible(false);

        Unit unit = null;
        for (Square square : unitsLocations.keySet()) {
            unit = unitsLocations.get(square);
            if (!keepTrollsDeployment && unit instanceof Troll)
                continue;
            unit.setSquare(square);
            if (square.distance() == 1)
                unit.setState(State.PUSHING);
            else
                unit.setState(State.MOVING);
            unit.update(0);
            unit.reset();
            layer.add(unit.layer());
        }

        if (!keepTrollsDeployment) {
            availableTrolls.clear();
            Json.Object trolls = json.getObject("trolls");
            for (String type : trolls.keys()) {
                availableTrolls.put(type, (int) trolls.getNumber(type));
            }
        }
    }

    private void placeUnit(Unit unit, Square square) {
        // TODO
    }

    private void removeUnit(Unit unit) {
        // TODO
    }

    private void clearTrolls() {
        Set<Unit> removes = new HashSet<Unit>();
        for (Unit unit : unitsLocations.values()) {
            if (unit instanceof Troll) {
                unit.layer().destroy();
                availableTrolls.put(unit.type(),
                        availableTrolls.get(unit.type()) + 1);
                updateTrollCounter(unit.type());
                removes.add(unit);
            }
        }
        for (Unit unit : removes) {
            unitsLocations.remove(unit.square());
        }
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
            if (!unit.state().equals(State.PUSHING)) {
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
                        unit.front().notifyColliedWithBack();
                        updateCollidedUnit(unit);
                        updateCollidedUnit(unit.front());
                        // Tries to move the previous unit after collision.
                        if (!retryUnits.contains(unit.front())
                                && unit.front().timer() <= 0) {
                            unit = unit.front();
                            retryUnits.add(unit);
                            continue;
                        }
                    } else {
                        Square s1 = unit.square();
                        // Initialises the front square.
                        Square s2 = new Square(
                                s1.lane(),
                                s1.segment() < bridgeLocation ? s1.segment() + 1
                                        : s1.segment() - 1);
                        s2.setX(segmentToX(s2.segment()));
                        s2.setY(s1.getY());
                        s2.setDistance(s1.distance() - 1);
                        unit.move(s2);
                        if (s2.distance() == 1 || adjacent(unit, unit.front())
                                && unit.front().state().equals(State.PUSHING))
                            pushing = true;
                    }
                }
            } else if (unit.speed() != 0)
                pushing = true;

            if (pushing) {
                unit.setState(State.PUSHING);
                moments += unit.square().lane() * unit.force();
            }
            unit.update(delta);
            unit = unit.back();
        }

        return moments;
    }

    private void updateCollidedUnit(Unit unit) {
        if (unit.state().equals(State.GRABBED)) {
            headGoats.get(unit.square().lane());
            // TODO
        }
    }

    private boolean adjacent(Unit u1, Unit u2) {
        if (u1 == null || u2 == null)
            return false;
        Square s1 = u1.square();
        Square s2 = u2.square();
        return s1.segment() == s2.segment() + 1
                || s1.segment() == s2.segment() - 1;
    }

    @Override
    public void update(float delta) {
        if (!started || paused)
            return;
        float trollsMoments = 0;
        float goatsMoments = 0;
        for (Unit troll : headTrolls.values()) {
            trollsMoments += updateUnits(troll, delta);
        }
        for (Unit goat : headGoats.values()) {
            goatsMoments += updateUnits(goat, delta);
        }
        updateMoment((int) (trollsMoments - goatsMoments));
    }

    @Override
    public void paint(float alpha) {
        iface.paint(alpha);
    }

}
