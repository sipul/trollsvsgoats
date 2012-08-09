package au.edu.unimelb.csse.trollsvsgoats.core.view;

import playn.core.Font;
import react.UnitSlot;
import tripleplay.ui.*;
import tripleplay.ui.layout.AxisLayout;
import au.edu.unimelb.csse.trollsvsgoats.core.TrollsVsGoatsGame;

public class MessageBox extends View {

    private final static int WIDTH = 320;
    private final static int HEIGHT = 170;
    final Background bg = Background.roundRect(bgColor, 5, butUlColor, 2);

    private String title;
    private Element<?> body;
    private String leftLabel;
    private String rightLabel;
    private SimpleCallBack simpleCallBack;
    private ChoiceCallBack choiceCallBack;

    public MessageBox(TrollsVsGoatsGame game, String title, String buttonLabel,
            SimpleCallBack callBack) {
        super(game);
        this.title = title;
        this.leftLabel = buttonLabel;
        this.simpleCallBack = callBack;
    }

    public MessageBox(TrollsVsGoatsGame game, String title, String leftLabel,
            String rightLabel, ChoiceCallBack callBack) {
        super(game);
        this.title = title;
        this.leftLabel = leftLabel;
        this.rightLabel = rightLabel;
        this.choiceCallBack = callBack;
    }

    public static abstract class SimpleCallBack {
        public abstract void onClose();
    }

    public static abstract class ChoiceCallBack {
        public void onLeftButton() {
        };

        public void onRightButton() {
        };
    }

    @Override
    protected Group createIface() {
        return null;
    }

    @Override
    public void wasAdded() {
        root = iface
                .createRoot(AxisLayout.vertical().gap(15).offStretch(),
                        stylesheet(), layer).setSize(WIDTH, HEIGHT)
                .addStyles(Style.BACKGROUND.is(bg));
        root.add(new Label(title).setStyles(Style.FONT.is(font(Font.Style.BOLD,
                18))));
        if (body != null)
            root.add(body);
        Group buttons;
        root.add(buttons = new Group(AxisLayout.horizontal().offStretch()
                .gap(20)));

        Button button;
        String name;
        if (leftLabel == null)
            name = "OK";
        else
            name = leftLabel;
        button = button(name);
        buttons.add(button);
        button.clicked().connect(new UnitSlot() {
            @Override
            public void onEmit() {
                if (choiceCallBack != null)
                    choiceCallBack.onLeftButton();
                else if (simpleCallBack != null)
                    simpleCallBack.onClose();
            }
        });

        if (choiceCallBack != null) {
            if (rightLabel == null)
                name = "OK";
            else
                name = rightLabel;
            button = button(name);
            buttons.add(button);
            button.clicked().connect(new UnitSlot() {
                @Override
                public void onEmit() {
                    choiceCallBack.onRightButton();
                }
            });
        }
    }

    public void addBody(Element<?> body) {
        this.body = body;
    }

    public float width() {
        return WIDTH;
    }

    public float height() {
        return HEIGHT;
    }

    @Override
    public void paint(float alpha) {
        iface.paint(alpha);
    }

    @Override
    protected String title() {
        return null;
    }

}
