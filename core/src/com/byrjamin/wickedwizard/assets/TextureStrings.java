package com.byrjamin.wickedwizard.assets;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.byrjamin.wickedwizard.helper.AnimationPacker;

/**
 * Created by Home on 01/01/2017.
 */
public class TextureStrings {

    //PLAYER
    public static Animation<TextureRegion> SQU_WALK = AnimationPacker.genAnimation(1 / 10f, "squ_walk", Animation.PlayMode.LOOP);
    public static Animation<TextureRegion> SQU_FIRING = AnimationPacker.genAnimation(0.15f / 10, "squ_firing");

    //ENEMIES
    public static String EXPLOSION = "explosion";

    //BLOB
    public static String BLOB_STANDING = "blob";
    public static String BLOB_ATTACKING = "blob_attack";
    public static String BLOB_DYING = "blob_dying";

    //SILVERHEAD
    public static String SILVERHEAD_ST = "silverhead_standing";
    public static String SILVERHEAD_HIDING = "silverhead_hiding";
    public static String SILVERHEAD_CHARGING = "silverhead_charging";





    //BOSSES

    //BIGGABLOBBA
    public static String BIGGABLOBBA_STANDING = "biggablobba";


    //PLAYER



}
