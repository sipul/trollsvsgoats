package au.edu.unimelb.csse.trollsvsgoats.java;

import playn.core.PlayN;
import playn.java.JavaPlatform;

import au.edu.unimelb.csse.trollsvsgoats.core.TrollsVsGoatsGame;
import au.edu.unimelb.csse.trollsvsgoats.core.handlers.IPlatformHandler;
import au.edu.unimelb.csse.trollsvsgoats.java.model.LocalPersistenceClient;

public class TrollsVsGoatsGameJava implements IPlatformHandler {

	JavaPlatform platform;
	
	public TrollsVsGoatsGameJava() {
		platform = JavaPlatform.register();
        PlayN.run(new TrollsVsGoatsGame(new LocalPersistenceClient(), this));
	}
        
    public static void main(String[] args) {
    	new TrollsVsGoatsGameJava();
    }
        
	@Override
	public void setSize(int width, int height) {
		platform.graphics().setSize(width, height);
    }
}
