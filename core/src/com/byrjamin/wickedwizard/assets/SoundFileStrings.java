package com.byrjamin.wickedwizard.assets;

/**
 * Created by Home on 03/06/2017.
 */

public class SoundFileStrings {


    public static final String playerFire = "audio/sounds/laser.wav";
    public static final Mix playerFireMix = new Mix(playerFire, 0.05f);


    public static final String enemyFire = "audio/sounds/laser2.wav";
    public static final Mix enemyFireMix = new Mix(enemyFire, 0.05f);


    public static final Mix jumpMix = new Mix.MixMaker("audio/sounds/Jump55.wav").build();

    public static final String explosion1 = "audio/sounds/explosion-01.ogg";
    public static final String explosion2 = "audio/sounds/explosion-02.ogg";
    public static final String explosion3 = "audio/sounds/explosion-03.ogg";
    public static final String explosion4 = "audio/sounds/explosion-04.ogg";

    public static final String[] explosionStrings = new String[]{explosion1, explosion2, explosion3, explosion4};

    public static final Mix explosionMix1 = new Mix.MixMaker(explosion1).volume(0.2f).build();
    public static final Mix explosionMix2 = new Mix.MixMaker(explosion2).volume(0.2f).build();
    public static final Mix explosionMix3 = new Mix.MixMaker(explosion3).volume(0.2f).build();
    public static final Mix explosionMix4 = new Mix.MixMaker(explosion4).volume(0.2f).build();

    public static final Mix[] explosionMegaMix = new Mix[]{explosionMix1, explosionMix2, explosionMix3, explosionMix4};

    public static final Mix quietExplosionMix1 = new Mix.MixMaker(explosion1).volume(0.01f).build();
    public static final Mix quietExplosionMix2 = new Mix.MixMaker(explosion2).volume(0.01f).build();
    public static final Mix quietExplosionMix3 = new Mix.MixMaker(explosion3).volume(0.01f).build();
    public static final Mix quietExplosionMix4 = new Mix.MixMaker(explosion4).volume(0.01f).build();

    public static final Mix[] queitExplosionMegaMix = new Mix[]{quietExplosionMix1, quietExplosionMix2, quietExplosionMix3, quietExplosionMix4};


    ////Hit

    public static final Mix hitMix1 = new Mix.MixMaker("audio/sounds/Hit_Hurt118.wav").volume(0.1f).build();
    public static final Mix hitMix2 = new Mix.MixMaker("audio/sounds/Hit_Hurt119.wav").volume(0.1f).build();
    public static final Mix hitMix3 = new Mix.MixMaker("audio/sounds/Hit_Hurt120.wav").volume(0.1f).build();
    public static final Mix hitMix4 = new Mix.MixMaker("audio/sounds/Hit_Hurt121.wav").volume(0.1f).build();
    public static final Mix hitMix5 = new Mix.MixMaker("audio/sounds/Hit_Hurt122.wav").volume(0.1f).build();
    public static final Mix hitMix6 = new Mix.MixMaker("audio/sounds/Hit_Hurt123.wav").volume(0.1f).build();

    public static final Mix[] hitMegaMix = new Mix[]{hitMix1, hitMix2, hitMix3, hitMix4, hitMix5, hitMix6};



    public static final String coinPickUp = "audio/sounds/diing-01.ogg";
    public static final Mix coinPickUpMix = new Mix(coinPickUp, 0.1f);


}
