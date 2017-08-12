package com.byrjamin.wickedwizard.factories.enemy;

import com.artemis.Component;
import com.artemis.utils.Bag;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;
import com.byrjamin.wickedwizard.assets.SoundFileStrings;
import com.byrjamin.wickedwizard.assets.TextureStrings;
import com.byrjamin.wickedwizard.ecs.components.audio.SoundEmitterComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.EnemyComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.object.SpawnerComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationStateComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.factories.arenas.skins.ArenaSkin;
import com.byrjamin.wickedwizard.utils.ComponentBag;
import com.byrjamin.wickedwizard.utils.Measure;

/**
 * Created by Home on 25/03/2017.
 */

public class SpawnerFactory extends EnemyFactory {

    private ArenaSkin arenaSkin;

    public SpawnerFactory(AssetManager assetManager, ArenaSkin arenaSkin) {
        super(assetManager);
        this.arenaSkin = arenaSkin;
    }



    private final float width = Measure.units(10f);
    private final float height = Measure.units(10f);

    public ComponentBag spawnerBag(float x, float y, Array<Spawner> spawners) {
        return spawnerBag(x,y, 1, 1.0f, 1.0f, 1, spawners);
    }

    public ComponentBag spawnerBag(float x, float y,int life, float spawnTime, Array<Spawner> spawners) {
        return spawnerBag(x,y,life,spawnTime,spawnTime, 1, spawners);
    }

    public ComponentBag spawnerBag(float x, float y,int life, float spawnTime,float scale,  Array<Spawner> spawners) {
        return spawnerBag(x,y,life,spawnTime,spawnTime, scale, spawners);
    }


    public ComponentBag spawnerBag(float x, float y,int life,float spawnTime,float resetTime, float scale, Array<Spawner> spawners){

        float width = this.width * scale;
        float height = this.height * scale;


        x = x - width  / 2;
        y = y - height  / 2;


        ComponentBag bag = new ComponentBag();
        bag.add(new PositionComponent(x,y));
        bag.add(new EnemyComponent());
        bag.add(new AnimationStateComponent(AnimationStateComponent.DEFAULT));
        Animation<TextureRegion> a = new Animation<TextureRegion>(1.0f / 35f, atlas.findRegions(TextureStrings.SPAWNER), Animation.PlayMode.LOOP);
        IntMap<Animation<TextureRegion>> animMap = new IntMap<Animation<TextureRegion>>();
        animMap.put(AnimationStateComponent.DEFAULT, a);
        bag.add(new AnimationComponent(animMap));
        bag.add(new TextureRegionComponent(atlas.findRegion(TextureStrings.SPAWNER), width, height,
                TextureRegionComponent.ENEMY_LAYER_MIDDLE, arenaSkin.getWallTint()));

        bag.add(new SoundEmitterComponent(SoundFileStrings.spawningMix, 0.5f));

        SpawnerComponent spawn = new SpawnerComponent(spawners, spawnTime);
        spawn.offsetX = width / 2;
        spawn.offsetY = height / 2;
        spawn.life = life;
        spawn.resetTime = resetTime;

        bag.add(spawn);
        return bag;
    }

    public ComponentBag spawnerBag(float x, float y, Spawner spawner){
        Array<Spawner> s = new Array<Spawner>();
        s.add(spawner);
        return spawnerBag(x,y, s);
    }

    public ComponentBag spawnerBag(float x, float y, Spawner spawner, int life){
        Array<Spawner> s = new Array<Spawner>();
        s.add(spawner);
        return spawnerBag(x,y, life,1.0f,1.0f, 1, s);
    }


    public interface Spawner {
        Bag<Component> spawnBag(float x, float y);
    }





}
