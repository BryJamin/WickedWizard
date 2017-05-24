package com.byrjamin.wickedwizard.factories;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.IntMap;
import com.byrjamin.wickedwizard.assets.TextureStrings;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.ExplosionComponent;
import com.byrjamin.wickedwizard.ecs.components.OnDeathComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.Action;
import com.byrjamin.wickedwizard.ecs.components.ai.Condition;
import com.byrjamin.wickedwizard.ecs.components.ai.ConditionalActionComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.ExpireComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.GravityComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.MoveToComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationStateComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.FadeComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.ecs.systems.FindPlayerSystem;
import com.byrjamin.wickedwizard.ecs.systems.input.GrapplePointSystem;
import com.byrjamin.wickedwizard.ecs.systems.input.GrappleSystem;
import com.byrjamin.wickedwizard.utils.BulletMath;
import com.byrjamin.wickedwizard.utils.ComponentBag;
import com.byrjamin.wickedwizard.utils.Measure;

/**
 * Created by Home on 23/05/2017.
 */

public class BombFactory extends  AbstractFactory{


    public BombFactory(AssetManager assetManager) {
        super(assetManager);
    }


    public ComponentBag bomb(float x, float y){


        float width = Measure.units(5);
        float height = Measure.units(5);

        x = x - width / 2;
        y = y - width / 2;

        ComponentBag bag = new ComponentBag();

        bag.add(new PositionComponent(x, y));
        bag.add(new CollisionBoundComponent(new Rectangle(x,y,width,height)));

        bag.add(new GravityComponent());
        bag.add(new VelocityComponent());
        bag.add(new ExpireComponent(3f));

        bag.add(new TextureRegionComponent(atlas.findRegion(TextureStrings.BOMB),
                width, height,
                TextureRegionComponent.ENEMY_LAYER_MIDDLE));


        bag.add(new AnimationStateComponent(0));
        IntMap<Animation<TextureRegion>> animMap = new IntMap<Animation<TextureRegion>>();
        animMap.put(0, new Animation<TextureRegion>(0.25f / 1f, atlas.findRegions(TextureStrings.BOMB), Animation.PlayMode.LOOP));

        bag.add(new AnimationComponent(animMap));

        ConditionalActionComponent cac = new ConditionalActionComponent();
        cac.condition = new Condition() {
            @Override
            public boolean condition(World world, Entity entity) {
                return entity.getComponent(ExpireComponent.class).expiryTime < 0.75f;
            }
        };


        cac.action = new Action() {
            @Override
            public void performAction(World world, Entity e) {
                e.getComponent(AnimationComponent.class).animations.get(0).setFrameDuration(0.05f / 1f);
            }

            @Override
            public void cleanUpAction(World world, Entity e) {

            }
        };

        bag.add(cac);

        OnDeathComponent odc = new OnDeathComponent();
        odc.getComponentBags().add(bombExplosion(x,y,width*4,height*4));
/*        odc.addComponenetsToBag(new ExplosionComponent(1),
                new CollisionBoundComponent(new Rectangle(x,y,width * 2,height * 2)),
                new PositionComponent(),
                new ExpireComponent(2));*/
        bag.add(odc);


        return bag;

    }


    public ComponentBag bombExplosion(float x, float y, float width, float height){

        ComponentBag bag = new ComponentBag();

        bag.add(new ExplosionComponent(1));
        bag.add(new CollisionBoundComponent(new Rectangle(x,y,width,height)));
        bag.add(new PositionComponent(x,y));

        bag.add(new TextureRegionComponent(atlas.findRegion(TextureStrings.EXPLOSION),
                width, height,
                TextureRegionComponent.ENEMY_LAYER_NEAR));


        bag.add(new AnimationStateComponent(0));
        IntMap<Animation<TextureRegion>> animMap = new IntMap<Animation<TextureRegion>>();
        animMap.put(0, new Animation<TextureRegion>(0.05f / 1f, atlas.findRegions(TextureStrings.EXPLOSION)));

        bag.add(new AnimationComponent(animMap));

        bag.add(new ExpireComponent(4f));


        return bag;

    }



}
