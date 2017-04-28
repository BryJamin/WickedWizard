package com.byrjamin.wickedwizard.factories;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.IntMap;
import com.byrjamin.wickedwizard.assets.TextureStrings;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.Action;
import com.byrjamin.wickedwizard.ecs.components.ai.ActionOnTouchComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.ExpireComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationStateComponent;
import com.byrjamin.wickedwizard.ecs.components.OnDeathComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.ecs.systems.RoomTransitionSystem;
import com.byrjamin.wickedwizard.screens.PlayScreen;
import com.byrjamin.wickedwizard.utils.AnimationPacker;
import com.byrjamin.wickedwizard.utils.ComponentBag;
import com.byrjamin.wickedwizard.utils.Measure;

/**
 * Created by Home on 01/04/2017.
 */

public class DeathFactory extends AbstractFactory {

    private final static int aniNumber = 1;

    public DeathFactory(AssetManager assetManager) {
        super(assetManager);
    }

    public OnDeathComponent basicOnDeathExplosion( OnDeathComponent fillOdc, float width, float height){
        return basicOnDeathExplosion(fillOdc, width, height, 0 ,0);
    }


    public OnDeathComponent basicOnDeathExplosion(OnDeathComponent fillodc, float width, float height,
                                                         float textureOffsetX, float textureOffsetY){
        ComponentBag bag = new ComponentBag();
        bag.add(new PositionComponent());
        AnimationStateComponent asc = new AnimationStateComponent();
        asc.setState(aniNumber);
        bag.add(asc);
        IntMap<Animation<TextureRegion>> animMap = new IntMap<Animation<TextureRegion>>();
        animMap.put(aniNumber,
                new Animation<TextureRegion>(0.02f, atlas.findRegions(TextureStrings.EXPLOSION)));
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

    public ComponentBag worldPortal (float x, float y){


        ComponentBag bag = new ComponentBag();
        bag.add(new PositionComponent(x, y));
        bag.add(new ActionOnTouchComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {
                world.getSystem(RoomTransitionSystem.class).recreateWorld();
            }

            @Override
            public void cleanUpAction(World world, Entity e) {

            }
        }));
        bag.add(new CollisionBoundComponent(new Rectangle(x,y, Measure.units(10), Measure.units(10))));

        AnimationStateComponent sc = new AnimationStateComponent();
        sc.setState(0);
        bag.add(sc);
        IntMap<Animation<TextureRegion>> animMap = new IntMap<Animation<TextureRegion>>();
        animMap.put(0, new Animation<TextureRegion>(0.02f, atlas.findRegions("squ_dash"), Animation.PlayMode.LOOP));
        bag.add(new AnimationComponent(animMap));

        bag.add(new TextureRegionComponent(atlas.findRegion("squ_dash"),
                Measure.units(10),
                Measure.units(10),
                TextureRegionComponent.ENEMY_LAYER_MIDDLE));


        return bag;

    }



}
