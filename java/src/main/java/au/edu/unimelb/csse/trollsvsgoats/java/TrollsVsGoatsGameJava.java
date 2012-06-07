package au.edu.unimelb.csse.trollsvsgoats.java;

import playn.core.PlayN;
import playn.java.JavaPlatform;

import au.edu.unimelb.csse.trollsvsgoats.core.TrollsVsGoatsGame;
import au.edu.unimelb.csse.trollsvsgoats.java.model.LocalPersistenceClient;

public class TrollsVsGoatsGameJava {

    public static void main(String[] args) {
        JavaPlatform platform = JavaPlatform.register();
        platform.assets().setPathPrefix(
                "au/edu/unimelb/csse/trollsvsgoats/resources");
        PlayN.run(new TrollsVsGoatsGame(new LocalPersistenceClient()));
    }
}
