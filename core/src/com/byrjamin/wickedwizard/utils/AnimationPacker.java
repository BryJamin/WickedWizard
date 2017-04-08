package com.byrjamin.wickedwizard.utils;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.screens.PlayScreen;

/**
 * Takes a string and then packs it into an animation.
 * Currently uses the PlayScreen atlas
 *
 * //TODO expand to have an atlas set to the Animationpacker so it isn't only reliant on the PlayScreen
 *
 */
public class AnimationPacker {

    /**
     * Generates an animation using the frames per second, names of the regions and the Playmode.
     * @param animationSpeed - speed of animation
     * @param atlasRegionsName - names of regions
     * @param playmode - Animation playmode
     * @return - Returns a new Animaion
     */
    public static Animation<TextureRegion> genAnimation(float animationSpeed, String atlasRegionsName, Animation.PlayMode playmode){
        Array<TextureRegion> textureRegions = new Array<TextureRegion>();

        for(TextureAtlas.AtlasRegion a : PlayScreen.atlas.findRegions(atlasRegionsName)){
            textureRegions.add(a);
        }

        return new Animation<TextureRegion>(animationSpeed, textureRegions, playmode);

    }

    /**
     * Generates an animation using the default playmode using the frames per second and names of the regions.
     * @param animationSpeed - speed of animation
     * @param atlasRegionsName - names of regions
     * @return
     */
    public static Animation<TextureRegion> genAnimation(float animationSpeed, String atlasRegionsName){

        Array<TextureRegion> textureRegions = new Array<TextureRegion>();

        for(TextureAtlas.AtlasRegion a : PlayScreen.atlas.findRegions(atlasRegionsName)){
            textureRegions.add(a);
        }

        return new Animation<TextureRegion>(animationSpeed, textureRegions);

    }

}
