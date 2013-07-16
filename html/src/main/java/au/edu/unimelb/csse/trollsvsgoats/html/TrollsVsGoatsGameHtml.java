package au.edu.unimelb.csse.trollsvsgoats.html;

import playn.core.PlayN;
import playn.html.HtmlGame;
import playn.html.HtmlPlatform;
import au.edu.unimelb.csse.trollsvsgoats.core.TrollsVsGoatsGame;
import au.edu.unimelb.csse.trollsvsgoats.core.handlers.IPlatformHandler;
import au.edu.unimelb.csse.trollsvsgoats.html.model.MuglePersistenceClient;

public class TrollsVsGoatsGameHtml extends HtmlGame implements IPlatformHandler{

	HtmlPlatform platform;

    @Override
    public void start() {
        platform = HtmlPlatform.register();
        platform.assets().setPathPrefix("trollsvsgoats/");
        PlayN.run(new TrollsVsGoatsGame(new MuglePersistenceClient(), this));
    }

	@Override
	public void setSize(int width, int height) {
		platform.graphics().setSize(width, height);
    }
}
