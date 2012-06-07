package au.edu.unimelb.csse.trollsvsgoats.html;

import playn.core.PlayN;
import playn.html.HtmlGame;
import playn.html.HtmlPlatform;

import au.edu.unimelb.csse.trollsvsgoats.core.TrollsVsGoatsGame;
import au.edu.unimelb.csse.trollsvsgoats.html.model.MuglePersistenceClient;

public class TrollsVsGoatsGameHtml extends HtmlGame {

    @Override
    public void start() {
        HtmlPlatform platform = HtmlPlatform.register();
        platform.assets().setPathPrefix("trollsvsgoats/");
        PlayN.run(new TrollsVsGoatsGame(new MuglePersistenceClient()));
    }
}
