package au.edu.unimelb.csse.trollsvsgoats.java;

import playn.core.PlayN;
import playn.java.JavaPlatform;

import au.edu.unimelb.csse.trollsvsgoats.core.TrollsVsGoatsGame;
import au.edu.unimelb.csse.trollsvsgoats.java.model.LocalPersistenceClient;

public class TrollsVsGoatsGameJava {

    public static void main(String[] args) {
        JavaPlatform platform = JavaPlatform.register();
        
        //tripleplay 1.7.2
        JavaPlatform.Config config = new JavaPlatform.Config();
        config.width = 1024;
        config.height = 720;
        JavaPlatform.register(config);
        //
        
        PlayN.run(new TrollsVsGoatsGame(new LocalPersistenceClient()));
    }
}
