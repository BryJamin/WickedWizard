package com.byrjamin.wickedwizard.assets;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

/**
 * Created by Home on 03/06/2017.
 */

public class SoundFileStrings {


    public static final String playerFire = "audio/sounds/laser.wav";
    public static final Mix playerFireMix = new Mix(playerFire, 0.05f);


    public static final Mix playerFireMix1 = new Mix.MixMaker(playerFire).volume(0.05f).pitch(0.925f).build();
    public static final Mix playerFireMix2 = new Mix.MixMaker(playerFire).volume(0.05f).pitch(0.95f).build();
    public static final Mix playerFireMix3 = new Mix.MixMaker(playerFire).volume(0.05f).pitch(0.975f).build();
    public static final Mix playerFireMix4 = new Mix.MixMaker(playerFire).volume(0.05f).pitch(1.0f).build();
    public static final Mix playerFireMix5 = new Mix.MixMaker(playerFire).volume(0.05f).pitch(1.025f).build();
    public static final Mix playerFireMix6 = new Mix.MixMaker(playerFire).volume(0.05f).pitch(1.05f).build();
    public static final Mix playerFireMix7 = new Mix.MixMaker(playerFire).volume(0.05f).pitch(1.075f).build();
    public static final Mix playerFireMix8 = new Mix.MixMaker(playerFire).volume(0.05f).pitch(1.1f).build();

    public static final Mix[] playerFireMegaMix = new Mix[]{playerFireMix1, playerFireMix2, playerFireMix3, playerFireMix4,
            playerFireMix5,
            playerFireMix6,
            playerFireMix7,
            playerFireMix8
    };


    public static final String grappleFire = "audio/sounds/laser.wav";
    public static final Mix grappleFireMix = new Mix(playerFire, 0.05f);


    public static final String enemyFire = "audio/sounds/laser2.wav";
    public static final Mix enemyFireMix = new Mix(enemyFire, 0.025f);


    public static final Mix jumpMix = new Mix.MixMaker("audio/sounds/jump/1_jump.ogg").volume(0.2f).build();
    public static final Mix jumpMix2 = new Mix.MixMaker("audio/sounds/jump/2_jump.ogg").volume(0.2f).build();
    public static final Mix jumpMix3 = new Mix.MixMaker("audio/sounds/jump/3_jump.ogg").volume(0.2f).build();
    public static final Mix[] jumpMegaMix = new Mix[]{jumpMix, jumpMix2, jumpMix3};

    public static final String explosion1 = "audio/sounds/explosion-01.ogg";
    public static final String explosion2 = "audio/sounds/explosion-02.ogg";
    public static final String explosion3 = "audio/sounds/explosion-03.ogg";
    public static final String explosion4 = "audio/sounds/explosion-04.ogg";

    public static final String[] explosionStrings = new String[]{explosion1, explosion2, explosion3, explosion4};

    public static final Mix explosionMix1 = new Mix.MixMaker(explosion1).volume(0.1f).build();
    public static final Mix explosionMix2 = new Mix.MixMaker(explosion2).volume(0.1f).build();
    public static final Mix explosionMix3 = new Mix.MixMaker(explosion3).volume(0.1f).build();
    public static final Mix explosionMix4 = new Mix.MixMaker(explosion4).volume(0.1f).build();

    public static final Mix[] explosionMegaMix = new Mix[]{explosionMix1, explosionMix2, explosionMix3, explosionMix4};

    public static final Mix quietExplosionMix1 = new Mix.MixMaker(explosion1).volume(0.005f).build();
    public static final Mix quietExplosionMix2 = new Mix.MixMaker(explosion2).volume(0.005f).build();
    public static final Mix quietExplosionMix3 = new Mix.MixMaker(explosion3).volume(0.005f).build();
    public static final Mix quietExplosionMix4 = new Mix.MixMaker(explosion4).volume(0.005f).build();

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



    public static final String itemPickUp = "audio/sounds/itempickup.ogg";
    public static final Mix itemPickUpMix1 = new Mix.MixMaker(itemPickUp).volume(0.2f).build();

    public static final Mix[] itemPickUpMegaMix = new Mix[]{itemPickUpMix1};


    //Laser
    public static final String quietLaser = "audio/sounds/laser/1_highlaserAud.ogg";
    public static final Mix quietLaserMix = new Mix.MixMaker(quietLaser).volume(0.3f).pitch(0.9f).build();


    public static final String laser = "audio/sounds/laser/1_highlaser.ogg";
    public static final Mix laserMix = new Mix.MixMaker(quietLaser).volume(0.5f).pitch(1.1f).build();


    public static final String spawning = "audio/sounds/spawning/1_spawnSFX.ogg";
    public static final Mix spawningMix = new Mix(spawning, 0.5f);







    public static void loadSoundsToManager(AssetManager assetManager){

        assetManager.load(playerFire, Sound.class);
        assetManager.load(enemyFire, Sound.class);

        for(String s : SoundFileStrings.explosionStrings) assetManager.load(s, Sound.class);
        for(Mix m : SoundFileStrings.hitMegaMix) assetManager.load(m.getFileName(), Sound.class);
        for(Mix m : SoundFileStrings.jumpMegaMix) assetManager.load(m.getFileName(), Sound.class);

        assetManager.load(coinPickUp, Sound.class);
        assetManager.load(jumpMix.getFileName(), Sound.class);

        assetManager.load(quietLaser, Music.class);
        assetManager.load(laser, Music.class);

        assetManager.load(itemPickUp, Sound.class);
        assetManager.load(spawning, Sound.class);



    }



}
