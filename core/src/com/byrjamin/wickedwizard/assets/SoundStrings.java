package com.byrjamin.wickedwizard.assets;

/**
 * Created by Home on 03/06/2017.
 */

public class SoundStrings {


    public static final String playerFire = "audio/sounds/laser.wav";
    public static final Mix playerFireMix = new Mix(playerFire, 0.05f);


    public static final String enemyFire = "audio/sounds/laser2.wav";
    public static final Mix enemyFireMix = new Mix(enemyFire, 0.05f);


    public static final Mix jumpMix = new Mix.MixMaker("audio/sounds/Jump55.wav").build();

    public static final Mix explosionMix1 = new Mix.MixMaker("audio/sounds/explosion-01.ogg")
            .volume(0.3f)
            .build();

    public static final Mix explosionMix2 = new Mix.MixMaker("audio/sounds/explosion-02.ogg")
            .volume(0.3f)
            .build();

    public static final Mix explosionMix3 = new Mix.MixMaker("audio/sounds/explosion-03.ogg")
            .volume(0.3f)
            .build();

    public static final Mix explosionMix4 = new Mix.MixMaker("audio/sounds/explosion-04.ogg")
            .volume(0.3f)
            .build();

    public static final Mix[] explosionMegaMix = new Mix[]{explosionMix1, explosionMix2, explosionMix3, explosionMix4};


    public static final String coinPickUp = "audio/sounds/diing-01.ogg";
    public static final Mix coinPickUpMix = new Mix(coinPickUp, 0.3f);


}
