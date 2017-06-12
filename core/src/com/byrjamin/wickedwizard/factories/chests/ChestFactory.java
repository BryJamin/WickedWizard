package com.byrjamin.wickedwizard.factories.chests;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.IntMap;
import com.byrjamin.wickedwizard.ecs.components.BlinkComponent;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.HealthComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.OnDeathActionComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.LootComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.GravityComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationStateComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.factories.AbstractFactory;
import com.byrjamin.wickedwizard.factories.GibletFactory;
import com.byrjamin.wickedwizard.utils.ComponentBag;
import com.byrjamin.wickedwizard.utils.Measure;

/**
 * Created by Home on 22/04/2017.
 */

public class ChestFactory extends AbstractFactory {


    private GibletFactory gibletFactory;

    public ChestFactory(AssetManager assetManager) {

        super(assetManager);
        this.gibletFactory = new GibletFactory(assetManager);
    }

    public final float width = Measure.units(10f);
    public final float height = Measure.units(10f);


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

        animMap.put(1, new Animation<TextureRegion>(0.2f / 1f, atlas.findRegions("chest"), Animation.PlayMode.LOOP));
        bag.add(new AnimationComponent(animMap));

        bag.add(new TextureRegionComponent(atlas.findRegion("chest", 0), width, height,
                TextureRegionComponent.ENEMY_LAYER_NEAR));
        bag.add(new OnDeathActionComponent(gibletFactory.giblets(5,0.4f,
                Measure.units(20f), Measure.units(100f), Measure.units(1f), new Color(Color.WHITE))));

        return bag;
    }

    public ComponentBag centeredChestBag(float x, float y) {
        x = x - width / 2;
        y = y- width / 2;
        return chestBag(x, y);
    }


    public ComponentBag centeredChestBag(float x, float y, OnDeathActionComponent odac) {

        ComponentBag bag = new ComponentBag();

        x = x - width / 2;
        y = y- width / 2;

        bag.add(new PositionComponent(x, y));
        bag.add(new CollisionBoundComponent(new Rectangle(x, y, width, height), true));
        bag.add(new VelocityComponent());
        bag.add(new LootComponent(5, 2));
        bag.add(new GravityComponent());
        bag.add(new HealthComponent(3));
        bag.add(new BlinkComponent());

        bag.add(new AnimationStateComponent(1));
        IntMap<Animation<TextureRegion>> animMap = new IntMap<Animation<TextureRegion>>();

        animMap.put(1, new Animation<TextureRegion>(0.2f / 1f, atlas.findRegions("chest"), Animation.PlayMode.LOOP));
        bag.add(new AnimationComponent(animMap));

        bag.add(new TextureRegionComponent(atlas.findRegion("chest", 0), width, height,
                TextureRegionComponent.ENEMY_LAYER_NEAR));
        bag.add(odac);

        return bag;
    }

}
