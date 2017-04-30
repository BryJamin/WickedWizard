package com.byrjamin.wickedwizard.factories.enemy;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.IntMap;
import com.byrjamin.wickedwizard.assets.TextureStrings;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.OnDeathComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.BounceComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationStateComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.utils.BagSearch;
import com.byrjamin.wickedwizard.utils.ComponentBag;
import com.byrjamin.wickedwizard.utils.Measure;

import java.util.Random;

/**
 * Created by Home on 29/03/2017.
 */

public class BouncerFactory extends EnemyFactory {

    public BouncerFactory(AssetManager assetManager) {
        super(assetManager);
    }

    private static final float width = Measure.units(5);
    private static final float height = Measure.units(5);

    private static final Random random = new Random();

    private static final float speed = Measure.units(35f);

    public ComponentBag smallBouncer(float x, float y){

        x = x - width / 2;
        y = y - height / 2;
        ComponentBag bag = basicBouncer(x, y, width, height, speed, random.nextBoolean());

        return bag;
    }

    public ComponentBag smallBouncerTargeted(float x, float y, boolean startsLeft){

        x = x - width / 2;
        y = y - height / 2;
        ComponentBag bag = basicBouncer(x, y, width, height, speed, startsLeft);

        return bag;
    }

    public ComponentBag largeBouncer(float x, float y){

        x = x - width * 2 / 2;
        y = y - height * 2 / 2;
        ComponentBag bag = basicBouncer(x, y, width * 2, height * 2, speed / 2, random.nextBoolean());

        OnDeathComponent odc = BagSearch.getObjectOfTypeClass(OnDeathComponent.class, bag);
        odc.getComponentBags().add(smallBouncerTargeted(0,0, true));
        odc.getComponentBags().add(smallBouncerTargeted(0,0, false));

        return bag;
    }

    private ComponentBag basicBouncer(float x, float y, float width, float height, float speed, boolean startsLeft) {

        ComponentBag bag = new ComponentBag();
        bag.add(new CollisionBoundComponent(new Rectangle(x, y, width, height), true));

        this.defaultEnemyBag(bag, x, y, width, height, 3);

        bag.add(new AnimationStateComponent(0));
        IntMap<Animation<TextureRegion>> animMap = new IntMap<Animation<TextureRegion>>();
        animMap.put(0, new Animation<TextureRegion>(0.25f / 1f,
                atlas.findRegions(TextureStrings.BOUNCER_DEFAULT), Animation.PlayMode.LOOP));

        bag.add(new AnimationComponent(animMap));
        bag.add(new TextureRegionComponent(atlas.findRegion(TextureStrings.BOUNCER_DEFAULT),
                0, 0, width, height,
                TextureRegionComponent.ENEMY_LAYER_MIDDLE));

        bag.add(new BounceComponent());
        bag.add(new VelocityComponent(startsLeft ? -speed : speed,speed));

        return bag;
    }



}
