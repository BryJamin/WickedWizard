package com.byrjamin.wickedwizard.factories.chests;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.IntMap;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.CurrencyComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.Action;
import com.byrjamin.wickedwizard.ecs.components.ai.ActionOnTouchComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.LootComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.GravityComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.byrjamin.wickedwizard.ecs.components.object.LockComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationStateComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.ecs.systems.FindPlayerSystem;
import com.byrjamin.wickedwizard.ecs.systems.LuckSystem;
import com.byrjamin.wickedwizard.factories.AbstractFactory;
import com.byrjamin.wickedwizard.utils.ComponentBag;
import com.byrjamin.wickedwizard.utils.Measure;

/**
 * Created by Home on 22/04/2017.
 */

public class ChestFactory extends AbstractFactory {

    public ChestFactory(AssetManager assetManager) {
        super(assetManager);
    }

    public final float width = Measure.units(10f);
    public final float height = Measure.units(10f);

    public ComponentBag chestBag(float x, float y) {

        ComponentBag bag = new ComponentBag();

        x = x - width / 2;
        y = y- width / 2;

        bag.add(new PositionComponent(x, y));
        bag.add(new CollisionBoundComponent(new Rectangle(x, y, width, height)));
        bag.add(new VelocityComponent());
        bag.add(new LootComponent(3));
        bag.add(new GravityComponent());
        bag.add(new ActionOnTouchComponent(generateLoot()));


        bag.add(new TextureRegionComponent(atlas.findRegion("chest", 0), width, height,
                TextureRegionComponent.ENEMY_LAYER_NEAR));

        return bag;
    }


    public ComponentBag lockedChestBag(float x, float y) {

        ComponentBag bag = new ComponentBag();

        x = x - width / 2;
        y = y- width / 2;

        bag.add(new PositionComponent(x, y));
        bag.add(new CollisionBoundComponent(new Rectangle(x, y, width, height)));
        bag.add(new VelocityComponent());
        bag.add(new LockComponent());
        bag.add(new LootComponent(6));
        bag.add(new GravityComponent());
        bag.add(new ActionOnTouchComponent(generateLoot()));


        bag.add(new TextureRegionComponent(atlas.findRegion("locked_chest", 0), width, height,
                TextureRegionComponent.ENEMY_LAYER_NEAR));

        return bag;
    }




    public Action generateLoot() {

        return new Action() {
            @Override
            public void performAction(World world, Entity e) {

                CollisionBoundComponent cbc = e.getComponent(CollisionBoundComponent.class);
                if(!cbc.bound.overlaps(world.getSystem(FindPlayerSystem.class).getPC(CollisionBoundComponent.class).bound)) return;

                if(world.getMapper(LockComponent.class).has(e)) {
                    CurrencyComponent cc =  world.getSystem(FindPlayerSystem.class).getPC(CurrencyComponent.class);
                    if(cc.keys - 1 < 0 ) return;
                    else cc.keys -= 1;

                    IntMap<Animation<TextureRegion>> animMap = new IntMap<Animation<TextureRegion>>();
                    animMap.put(0, new Animation<TextureRegion>(0.05f / 1f, atlas.findRegions("locked_chest")));
                    e.edit().add(new AnimationComponent(animMap));


                } else {

                    IntMap<Animation<TextureRegion>> animMap = new IntMap<Animation<TextureRegion>>();
                    animMap.put(0, new Animation<TextureRegion>(0.05f / 1f, atlas.findRegions("chest")));
                    e.edit().add(new AnimationComponent(animMap));


                }


                for(int i = 0; i < e.getComponent(LootComponent.class).maxDrops; i++) {
                    world.getSystem(LuckSystem.class).spawnPickUp(cbc.getCenterX(), cbc.getCenterY());
                }

                e.edit().remove(ActionOnTouchComponent.class);
                e.edit().add(new AnimationStateComponent());
            }

            @Override
            public void cleanUpAction(World world, Entity e) {

            }
        };

    }




}
