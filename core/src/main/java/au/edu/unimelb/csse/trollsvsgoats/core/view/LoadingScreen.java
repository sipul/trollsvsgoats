package au.edu.unimelb.csse.trollsvsgoats.core.view;

import playn.core.Font;
import au.edu.unimelb.csse.trollsvsgoats.core.TrollsVsGoatsGame;
import tripleplay.ui.Group;
import tripleplay.ui.Label;
import tripleplay.ui.Style;

public class LoadingScreen extends View {

    public LoadingScreen(TrollsVsGoatsGame game) {
        super(game);
    }

    @Override
    protected Group createIface() {
        root.add(
                new Label("LOADING...").addStyles(
                        Style.FONT.is(font(Font.Style.BOLD, 40)),
                        Style.COLOR.is(0xFFFFFFFF))).addStyles(
                Style.VALIGN.center);
        return null;
    }

    @Override
    protected String title() {
        return null;
    }

}
