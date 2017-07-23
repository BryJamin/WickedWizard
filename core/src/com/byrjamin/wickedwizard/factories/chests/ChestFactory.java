package com.byrjamin.wickedwizard.factories.chests;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.IntMap;
import com.byrjamin.wickedwizard.assets.ColorResource;
import com.byrjamin.wickedwizard.ecs.components.BlinkComponent;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.HealthComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.OnDeathActionComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.Task;
import com.byrjamin.wickedwizard.ecs.components.identifiers.LootComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.GravityComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationStateComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.ecs.systems.level.RoomTransitionSystem;
import com.byrjamin.wickedwizard.factories.AbstractFactory;
import com.byrjamin.wickedwizard.factories.GibletFactory;
import com.byrjamin.wickedwizard.factories.arenas.Arena;
import com.byrjamin.wickedwizard.factories.weapons.Giblets;
import com.byrjamin.wickedwizard.utils.BagSearch;
import com.byrjamin.wickedwizard.utils.ComponentBag;
import com.byrjamin.wickedwizard.utils.Measure;

/**
 * Created by Home on 22/04/2017.
 */

public class ChestFactory extends AbstractFactory {

    private Giblets giblets;

    public ChestFactory(AssetManager assetManager) {
        super(assetManager);

        this.giblets = new Giblets.GibletBuilder(assetManager)
                .numberOfGibletPairs(3)
                .expiryTime(0.4f)
                .minSpeed(20f)
                .maxSpeed(Measure.units(100f))
                .size(Measure.units(1f))
                .colors(new Color(Color.BROWN)) //Maybe change the color of this?
                .build();


    }

    public final float width = Measure.units(10f);
    public final float height = Measure.units(7f);

    public final float texWidth = Measure.units(10f);
    public final float texHeight = Measure.units(10f);


    public ComponentBag chestBag(float x, float y){

        ComponentBag bag = new ComponentBag();

        bag.add(new PositionComponent(x, y));
        bag.add(new CollisionBoundComponent(new Rectangle(x, y, width, height), true));
        bag.add(new VelocityComponent());
        bag.add(new LootComponent(5, 2));
        bag.add(new GravityComponent());
        bag.add(new HealthComponent(3));
        bag.add(new BlinkComponent());

        bag.add(new AnimationStateComponent(1));
        IntMap<Animation<TextureRegion>> animMap = new IntMap<Animation<TextureRegion>>();

        animMap.put(1, new Animation<TextureRegion>(1f / 7.5f, atlas.findRegions("chest"), Animation.PlayMode.LOOP));
        bag.add(new AnimationComponent(animMap));

        bag.add(new TextureRegionComponent(atlas.findRegion("chest", 0), texWidth, texHeight,
                TextureRegionComponent.BACKGROUND_LAYER_NEAR));

        bag.add(new OnDeathActionComponent(giblets));

        return bag;
    }


    public ComponentBag centeredChestBag(float x, float y) {
        x = x - width / 2;
        y = y- height / 2;
        return chestBag(x, y);
    }

    public ComponentBag centeredChestBag(float x, float y, OnDeathActionComponent odac){
        x = x - width / 2;
        y = y- height / 2;
        return chestBag(x,y,odac);
    }



    public ComponentBag chestBag(float x, float y, OnDeathActionComponent odac) {
        ComponentBag bag = chestBag(x, y);
        BagSearch.removeObjectOfTypeClass(OnDeathActionComponent.class, bag);
        bag.add(odac);
        return bag;
    }


    public OnDeathActionComponent trapODAC(){
        return new OnDeathActionComponent(new Task() {
            @Override
            public void performAction(World world, Entity e) {
                giblets.performAction(world, e);
                Arena arena = world.getSystem(RoomTransitionSystem.class).getCurrentArena();
                arena.roomType = Arena.RoomType.TRAP;

            }

            @Override
            public void cleanUpAction(World world, Entity e) {

            }
        });
    }

}
