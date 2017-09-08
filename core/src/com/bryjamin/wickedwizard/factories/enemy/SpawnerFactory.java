package com.bryjamin.wickedwizard.factories.enemy;

import com.artemis.Component;
import com.artemis.utils.Bag;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;
import com.bryjamin.wickedwizard.assets.SoundFileStrings;
import com.bryjamin.wickedwizard.assets.TextureStrings;
import com.bryjamin.wickedwizard.ecs.components.audio.SoundEmitterComponent;
import com.bryjamin.wickedwizard.ecs.components.identifiers.EnemyComponent;
import com.bryjamin.wickedwizard.ecs.components.identifiers.IntangibleComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.bryjamin.wickedwizard.ecs.components.object.SpawnerComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.AnimationComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.AnimationStateComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.bryjamin.wickedwizard.factories.AbstractFactory;
import com.bryjamin.wickedwizard.factories.arenas.skins.ArenaSkin;
import com.bryjamin.wickedwizard.utils.ComponentBag;
import com.bryjamin.wickedwizard.utils.Measure;

/**
 * Created by Home on 25/03/2017.
 */

public class SpawnerFactory extends AbstractFactory {

    private ArenaSkin arenaSkin;
    public int life;
    public float spawnTime;
    public float resetTime;
    public float scale;
    public Array<Spawner> spawners;

    public SpawnerFactory(AssetManager assetManager, ArenaSkin arenaSkin) {
        super(assetManager);
        this.arenaSkin = arenaSkin;
    }


    public SpawnerFactory(SpawnerBuilder sb) {
        super(sb.assetManager);
        this.arenaSkin = sb.arenaSkin;
        this.spawners = sb.spawners;
        this.life = sb.life;
        this.spawnTime = sb.spawnTime;
        this.resetTime = sb.resetTime;
        this.scale = sb.scale;
    }

    public static class SpawnerBuilder {

        //Required Parameters
        private final AssetManager assetManager;
        private final ArenaSkin arenaSkin;

        //Optional Parameters
        public int life = 1;
        public float spawnTime = 1.0f;
        public float resetTime = 1.0f;
        public float scale = 1;
        public Array<Spawner> spawners = new Array<Spawner>();


        public SpawnerBuilder(AssetManager assetManager, ArenaSkin arenaSkin) {
            this.assetManager = assetManager;
            this.arenaSkin = arenaSkin;
        }


        public SpawnerBuilder spawners(Spawner... val) {
            spawners = new Array<Spawner>();
            spawners.addAll(val);
            return this;
        }

        public SpawnerBuilder life(int val)
        { life = val; return this; }


        public SpawnerBuilder spawnTime(float val)
        { spawnTime = val; return this; }

        public SpawnerBuilder resetTime(float val)
        { resetTime = val; return this; }

        public SpawnerBuilder scale(float val)
        { scale = val; return this; }


        public SpawnerFactory build() {
            return new SpawnerFactory(this);
        }


    }



    private final float width = Measure.units(10f);
    private final float height = Measure.units(10f);

    public ComponentBag spawnerBag(float x, float y, Array<Spawner> spawners) {
        return spawnerBag(x,y, 1, 1.0f, 1.0f, 1, spawners);
    }

    public ComponentBag spawnerBag(float x, float y, int life, float spawnTime, float scale, Array<Spawner> spawners) {
        return spawnerBag(x,y,life,spawnTime,spawnTime, scale, spawners);
    }


    public ComponentBag spawnerBag(float x, float y){

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

        bag.add(new CollisionBoundComponent(new Rectangle(x, y, width, height)));
        bag.add(new IntangibleComponent());

        bag.add(new SoundEmitterComponent(SoundFileStrings.spawningMix, 0.5f));

        SpawnerComponent spawn = new SpawnerComponent(spawners, spawnTime);
        spawn.offsetX = width / 2;
        spawn.offsetY = height / 2;
        spawn.life = life;
        spawn.resetTime = resetTime;

        bag.add(spawn);
        return bag;
    }


    public ComponentBag spawnerBag(float x, float y, int life, float spawnTime, float resetTime, float scale, Array<Spawner> spawners){

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
