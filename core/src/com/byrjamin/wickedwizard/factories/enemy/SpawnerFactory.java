package com.byrjamin.wickedwizard.factories.enemy;

import com.artemis.Component;
import com.artemis.utils.Bag;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;
import com.byrjamin.wickedwizard.assets.TextureStrings;
import com.byrjamin.wickedwizard.ecs.components.EnemyComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.SpawnerComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationStateComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.utils.AnimationPacker;
import com.byrjamin.wickedwizard.utils.BagSearch;
import com.byrjamin.wickedwizard.utils.Measure;

/**
 * Created by Home on 25/03/2017.
 */

public class SpawnerFactory {


    private static float width = Measure.units(12f);
    private static float height = Measure.units(12f);

    public static Bag<Component> spawnerBag(float x, float y, Array<Spawner> spawners){

        x = x - width / 2;
        y = y - height / 2;


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
        bag.add(new TextureRegionComponent(a.getKeyFrame(sc.stateTime), width, height,
                TextureRegionComponent.ENEMY_LAYER_MIDDLE));

        SpawnerComponent spawn = new SpawnerComponent(spawners, 1.0f);
        spawn.offsetX = width / 2;
        spawn.offsetY = height / 2;

        bag.add(spawn);
        return bag;
    }


    public interface Spawner {
        Bag<Component> spawnBag(float x, float y);
    }





}
