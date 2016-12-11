package com.byrjamin.wickedwizard.helper;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.screens.PlayScreen;

/**
 * Since I will end up repeating the same code a lot in order to create an Animation
 * It requiring first getting an atlasregion, grabbing the texture and then putting it in
 * the animation class. This class does it all, so in my other classes making an animation is just
 * a single line of code.
 *
 * I may expand it in future to take a string instead of an array of atlasregions and then class the
 * atlasregion here. In fact I'll do that now and then convert it back after if it turn out
 * to be a bad idea.
 *
 */
public class AnimationPacker {

    public static Animation genLoopedAnimation(float animationSpeed, String atlasRegionsName){
        Array<TextureRegion> textureRegions = new Array<TextureRegion>();

        for(TextureAtlas.AtlasRegion a : PlayScreen.atlas.findRegions(atlasRegionsName)){
            textureRegions.add(a);
        }

        return new Animation(animationSpeed, textureRegions, Animation.PlayMode.LOOP);

    }

    public static Animation genAnimation(float animationSpeed, String atlasRegionsName){

        Array<TextureRegion> textureRegions = new Array<TextureRegion>();

        for(TextureAtlas.AtlasRegion a : PlayScreen.atlas.findRegions(atlasRegionsName)){
            textureRegions.add(a);
        }

        return new Animation(animationSpeed, textureRegions);

    }

}
