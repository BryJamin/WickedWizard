package com.byrjamin.wickedwizard.factories.enemy;

import com.artemis.Component;
import com.artemis.utils.Bag;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.IntMap;
import com.byrjamin.wickedwizard.assets.TextureStrings;
import com.byrjamin.wickedwizard.ecs.components.AccelerantComponent;
import com.byrjamin.wickedwizard.ecs.components.BlinkComponent;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.EnemyComponent;
import com.byrjamin.wickedwizard.ecs.components.FiringAIComponent;
import com.byrjamin.wickedwizard.ecs.components.GravityComponent;
import com.byrjamin.wickedwizard.ecs.components.HealthComponent;
import com.byrjamin.wickedwizard.ecs.components.MoveToPlayerComponent;
import com.byrjamin.wickedwizard.ecs.components.PhaseComponent;
import com.byrjamin.wickedwizard.ecs.components.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.SpawnerComponent;
import com.byrjamin.wickedwizard.ecs.components.VelocityComponent;
import com.byrjamin.wickedwizard.ecs.components.WeaponComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationStateComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.factories.EntityFactory;
import com.byrjamin.wickedwizard.factories.SpawnerFactory;
import com.byrjamin.wickedwizard.helper.AnimationPacker;
import com.byrjamin.wickedwizard.helper.Measure;
import com.byrjamin.wickedwizard.screens.PlayScreen;

/**
 * Created by Home on 26/03/2017.
 */

public class BlobFactory {

    private static final float width = Measure.units(9);
    private static final float height = Measure.units(9);

    private static final float textureWidth = Measure.units(12);
    private static final float textureHeight = Measure.units(12);

    private static final float textureOffsetX = -Measure.units(1f);
    private static final float textureOffsetY = 0;

    public static Bag<Component> blobBag(float x, float y){
        Bag<Component> bag = new Bag<Component>();
        bag.add(new PositionComponent(x,y));
        bag.add(new VelocityComponent(0, 0));
        bag.add(new CollisionBoundComponent(new Rectangle(x,y, width, height)));
        bag.add(new GravityComponent());
        bag.add(new EnemyComponent());
        bag.add(new MoveToPlayerComponent());
        bag.add(new AccelerantComponent(Measure.units(15), 0));
        bag.add(new HealthComponent(10));
        bag.add(new BlinkComponent());
        AnimationStateComponent sc = new AnimationStateComponent();
        sc.setState(0);
        bag.add(sc);
        IntMap<Animation<TextureRegion>> animMap = new IntMap<Animation<TextureRegion>>();
        animMap.put(0, AnimationPacker.genAnimation(0.25f / 1f, TextureStrings.BLOB_STANDING, Animation.PlayMode.LOOP));
        bag.add(new AnimationComponent(animMap));
        bag.add(new TextureRegionComponent(PlayScreen.atlas.findRegion(TextureStrings.BLOB_STANDING),
                textureOffsetX, textureOffsetY, textureWidth, textureHeight));
        return bag;
    }

    public static Bag<Component> smallblobBag(float x, float y){

        float scale = 0.5f;
        Bag<Component> bag = new Bag<Component>();
        bag.add(new PositionComponent(x,y));
        bag.add(new VelocityComponent(0, 0));
        bag.add(new CollisionBoundComponent(new Rectangle(x,y, width * scale, height * scale)));
        bag.add(new GravityComponent());
        bag.add(new EnemyComponent());
        bag.add(new AccelerantComponent(Measure.units(2.5f), 0, Measure.units(30), 0));
        bag.add(new MoveToPlayerComponent());
        bag.add(new HealthComponent(2));
        bag.add(new BlinkComponent());
        AnimationStateComponent sc = new AnimationStateComponent();
        sc.setState(0);
        bag.add(sc);
        IntMap<Animation<TextureRegion>> animMap = new IntMap<Animation<TextureRegion>>();
        animMap.put(0, AnimationPacker.genAnimation(0.25f / 1f, TextureStrings.BLOB_STANDING, Animation.PlayMode.LOOP));
        bag.add(new AnimationComponent(animMap));
        bag.add(new TextureRegionComponent(PlayScreen.atlas.findRegion(TextureStrings.BLOB_STANDING),
                textureOffsetX * scale,
                textureOffsetY * scale,
                textureWidth * scale,
                textureHeight * scale));
        return bag;
    }


    public static Bag<Component> BiggaBlobbaBag(float x, float y){


        Bag<Component> bag = new Bag<Component>();
        bag.add(new PositionComponent(x,y));
        bag.add(new VelocityComponent(0, 0));
        bag.add(new CollisionBoundComponent(new Rectangle(x, y, Measure.units(33), Measure.units(38))));
        bag.add(new GravityComponent());
        bag.add(new EnemyComponent());
        bag.add(new HealthComponent(65));
        bag.add(new BlinkComponent());
        AnimationStateComponent sc = new AnimationStateComponent();
        sc.setState(0);
        bag.add(sc);
        IntMap<Animation<TextureRegion>> animMap = new IntMap<Animation<TextureRegion>>();
        animMap.put(0, AnimationPacker.genAnimation(1f / 20f, TextureStrings.BIGGABLOBBA_STANDING, Animation.PlayMode.LOOP));
        bag.add(new AnimationComponent(animMap));
        bag.add(new TextureRegionComponent(PlayScreen.atlas.findRegion(TextureStrings.BLOB_STANDING),
                -Measure.units(6),
                0,
                Measure.units(45),
                Measure.units(45)));


        WeaponComponent wc = new WeaponComponent(1.5f);
        wc.additionalComponenets.add(new EnemyComponent());

        SpawnerFactory.Spawner s = new SpawnerFactory.Spawner() {
            @Override
            public Bag<Component> spawnBag(float x, float y) {
                return smallblobBag(x,y);
            }
        };

        SpawnerComponent sc2 = new SpawnerComponent(2.0f, s);
        sc2.isEndless = true;

        PhaseComponent pc = new PhaseComponent();
        pc.addPhase(7.0f, new FiringAIComponent(), wc);
        pc.addPhase(5.0f, sc2);
        pc.addPhaseSequence(1,0);

        bag.add(pc);


        return bag;







    }


}
