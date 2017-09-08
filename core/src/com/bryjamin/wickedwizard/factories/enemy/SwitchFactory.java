package com.bryjamin.wickedwizard.factories.enemy;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.IntMap;
import com.bryjamin.wickedwizard.assets.TextureStrings;
import com.bryjamin.wickedwizard.ecs.components.identifiers.ArenaLockComponent;
import com.bryjamin.wickedwizard.ecs.components.identifiers.EnemyComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.AnimationComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.AnimationStateComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.FadeComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.bryjamin.wickedwizard.utils.BagSearch;
import com.bryjamin.wickedwizard.utils.ComponentBag;
import com.bryjamin.wickedwizard.utils.Measure;

/**
 * Created by Home on 23/06/2017.
 */

public class SwitchFactory extends EnemyFactory {



    private final float width = Measure.units(5);
    private final float height = Measure.units(5);
    private static final float health = 2;

    public SwitchFactory(AssetManager assetManager) {
        super(assetManager);
    }


    public ComponentBag switchBag(float x , float y, float rotation){

        ComponentBag bag = this.defaultEnemyBagNoLoot(new ComponentBag(), x, y, health);
        BagSearch.removeObjectOfTypeClass(EnemyComponent.class,bag);
        bag.add(new ArenaLockComponent());

        bag.add(new CollisionBoundComponent(new Rectangle(x,y, width, height), true));

        bag.add(new AnimationStateComponent(0));
        IntMap<Animation<TextureRegion>> animMap = new IntMap<Animation<TextureRegion>>();
        animMap.put(0, new Animation<TextureRegion>(0.15f / 1f, atlas.findRegions(TextureStrings.SWITCH), Animation.PlayMode.LOOP_PINGPONG));

        bag.add(new AnimationComponent(animMap));
        TextureRegionComponent textureRegionComponent = new TextureRegionComponent(atlas.findRegion(TextureStrings.SWITCH),
                 width, height,
                TextureRegionComponent.ENEMY_LAYER_MIDDLE);

        textureRegionComponent.rotation = rotation;

        bag.add(textureRegionComponent);

        return bag;

    }

    public ComponentBag fadeInswitchBag(float x , float y, float rotation){

        ComponentBag bag = switchBag(x,y,rotation);

        FadeComponent fc = new FadeComponent(true, 0.5f, false);
        fc.flicker = true;
        bag.add(fc);

        return bag;

    }











}
