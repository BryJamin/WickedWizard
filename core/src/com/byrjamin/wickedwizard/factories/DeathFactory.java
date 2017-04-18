package com.byrjamin.wickedwizard.factories;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.IntMap;
import com.byrjamin.wickedwizard.assets.TextureStrings;
import com.byrjamin.wickedwizard.ecs.components.ExpireComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationStateComponent;
import com.byrjamin.wickedwizard.ecs.components.OnDeathComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.utils.AnimationPacker;
import com.byrjamin.wickedwizard.utils.ComponentBag;

/**
 * Created by Home on 01/04/2017.
 */

public class DeathFactory {

    private final static int aniNumber = 1;

    public static OnDeathComponent basicOnDeathExplosion( OnDeathComponent fillOdc, float width, float height){
        return basicOnDeathExplosion(fillOdc, width, height, 0 ,0);
    }


    public static OnDeathComponent basicOnDeathExplosion(OnDeathComponent fillodc, float width, float height,
                                                         float textureOffsetX, float textureOffsetY){

        ComponentBag bag = new ComponentBag();
        bag.add(new PositionComponent());
        AnimationStateComponent asc = new AnimationStateComponent();
        asc.setState(aniNumber);
        bag.add(asc);
        IntMap<Animation<TextureRegion>> animMap = new IntMap<Animation<TextureRegion>>();
        animMap.put(aniNumber,
                AnimationPacker.genAnimation(0.02f, TextureStrings.EXPLOSION));
        bag.add(new AnimationComponent(animMap));
        bag.add(new TextureRegionComponent(animMap.get(aniNumber).getKeyFrame(0),
                textureOffsetX,
                textureOffsetY,
                width,
                height,
                TextureRegionComponent.ENEMY_LAYER_MIDDLE));
        bag.add(new ExpireComponent(animMap.get(aniNumber).getAnimationDuration()));

        fillodc.getComponentBags().add(bag);

        return fillodc;
    }



}
