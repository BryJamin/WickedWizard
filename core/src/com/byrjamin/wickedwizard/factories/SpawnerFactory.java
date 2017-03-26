package com.byrjamin.wickedwizard.factories;

import com.artemis.Component;
import com.artemis.utils.Bag;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.IntMap;
import com.byrjamin.wickedwizard.assets.TextureStrings;
import com.byrjamin.wickedwizard.ecs.components.BlinkComponent;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.EnemyComponent;
import com.byrjamin.wickedwizard.ecs.components.GravityComponent;
import com.byrjamin.wickedwizard.ecs.components.HealthComponent;
import com.byrjamin.wickedwizard.ecs.components.MoveToPlayerComponent;
import com.byrjamin.wickedwizard.ecs.components.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.SpawnerComponent;
import com.byrjamin.wickedwizard.ecs.components.VelocityComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationStateComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.helper.AnimationPacker;
import com.byrjamin.wickedwizard.helper.BagSearch;
import com.byrjamin.wickedwizard.helper.Measure;
import com.byrjamin.wickedwizard.screens.PlayScreen;

/**
 * Created by Home on 25/03/2017.
 */

public class SpawnerFactory {


    public static Bag<Component> spawnerBag(float x, float y){

        Bag<Component> bag = new Bag<Component>();

        bag.add(new PositionComponent(x,y));
        bag.add(new EnemyComponent());
        AnimationStateComponent sc = new AnimationStateComponent();
        sc.setState(0);
        bag.add(sc);
        Animation<TextureRegion> a = AnimationPacker.genAnimation(1.0f / 35f, TextureStrings.CIRCLE, Animation.PlayMode.LOOP);
        IntMap<Animation<TextureRegion>> animMap = new IntMap<Animation<TextureRegion>>();
        animMap.put(0, a);
        bag.add(new AnimationComponent(animMap));

        Bag<Component> enemyBag = EntityFactory.blobBag(x,y);
        TextureRegionComponent trc = BagSearch.getObjectOfTypeClass(TextureRegionComponent.class, enemyBag);
        bag.add(new SpawnerComponent(EntityFactory.blobBag(x,y), 1.0f));
        bag.add(new TextureRegionComponent(a.getKeyFrame(sc.stateTime), trc.width, trc.height));

        return bag;
    }


}
