package com.byrjamin.wickedwizard.factories.enemy;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.utils.Bag;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.IntMap;
import com.byrjamin.wickedwizard.assets.TextureStrings;
import com.byrjamin.wickedwizard.ecs.components.ai.ProximityTriggerAIComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.Task;
import com.byrjamin.wickedwizard.ecs.components.movement.BounceComponent;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.FiringAIComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.byrjamin.wickedwizard.ecs.components.WeaponComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationStateComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.factories.weapons.WeaponFactory;
import com.byrjamin.wickedwizard.factories.weapons.enemy.MultiPistol;
import com.byrjamin.wickedwizard.utils.ComponentBag;
import com.byrjamin.wickedwizard.utils.Measure;

import java.util.Random;

/**
 * Created by Home on 26/03/2017.
 */

public class TurretFactory extends EnemyFactory {

    WeaponFactory wf;

    private final float sentryHealth = 10;
    private final float triSentryHealth = 15;

    public TurretFactory(AssetManager assetManager) {
        super(assetManager);
        wf = new WeaponFactory(assetManager);
    }

    final float width = Measure.units(10f);
    final float height = Measure.units(10f);

    public Bag<Component> fixedLockOnTurret(float x, float y){
        x = x - width / 2;
        y = y - width / 2;

        ComponentBag bag = this.defaultEnemyBag(new ComponentBag(), x , y, width, height, sentryHealth);

        bag.add(new CollisionBoundComponent(new Rectangle(x,y, Measure.units(9), Measure.units(9)), true));
        bag.add(new TextureRegionComponent(atlas.findRegion(TextureStrings.SENTRY),
                0, 0, width, height, TextureRegionComponent.ENEMY_LAYER_MIDDLE
        ));

        bag.add(new AnimationStateComponent(0));
        IntMap<Animation<TextureRegion>> animMap = new IntMap<Animation<TextureRegion>>();
        animMap.put(0, new Animation<TextureRegion>(0.15f / 1f, atlas.findRegions(TextureStrings.SENTRY), Animation.PlayMode.LOOP_RANDOM));
        animMap.put(AnimationStateComponent.FIRING, new Animation<TextureRegion>(0.10f / 1f, atlas.findRegions(TextureStrings.SENTRY_FIRING)));

        bag.add(new AnimationComponent(animMap));

        WeaponComponent wc = new WeaponComponent(wf.enemyWeapon(), 2f);
        bag.add(wc);

        bag.add(defaultTurretTrigger());

        return bag;
    }

    public Bag<Component> movingSentry(float x, float y){

        Bag<Component> bag = fixedLockOnTurret(x,y);

        Random random = new Random();
        if(random.nextBoolean()) {
            bag.add(new VelocityComponent(300, 0));
        } else {
            bag.add(new VelocityComponent(-300, 0));
        }

        bag.add(new BounceComponent());

        return bag;
    }



    public ProximityTriggerAIComponent defaultTurretTrigger(){
        return new ProximityTriggerAIComponent(new Task() {
            @Override
            public void performAction(World world, Entity e) {
                e.edit().add(new FiringAIComponent());
            }

            @Override
            public void cleanUpAction(World world, Entity e) {
                e.edit().remove(FiringAIComponent.class);
            }
        },
                true);
    }




    public ComponentBag fixedMultiSentry(float x, float y){
        x = x - width / 2;
        y = y - width / 2;

        ComponentBag bag = this.defaultEnemyBag(new ComponentBag(), x , y, width, height, triSentryHealth);
        bag.add(new CollisionBoundComponent(new Rectangle(x,y, Measure.units(9), Measure.units(9)), true));
        bag.add(new TextureRegionComponent(atlas.findRegion(TextureStrings.SENTRY_TRI),
                0, 0, width, height, TextureRegionComponent.ENEMY_LAYER_MIDDLE
        ));

        bag.add(new AnimationStateComponent(0));
        IntMap<Animation<TextureRegion>> animMap = new IntMap<Animation<TextureRegion>>();
        animMap.put(0, new Animation<TextureRegion>(0.15f / 1f, atlas.findRegions(TextureStrings.SENTRY_TRI), Animation.PlayMode.LOOP_RANDOM));
        animMap.put(AnimationStateComponent.FIRING, new Animation<TextureRegion>(0.10f / 1f, atlas.findRegions(TextureStrings.SENTRY_FIRING_TRI)));

        bag.add(new AnimationComponent(animMap));

        WeaponComponent wc = new WeaponComponent(new MultiPistol(assetManager, 2f), 2f);
        bag.add(wc);

        bag.add(defaultTurretTrigger());

        return bag;
    }


    public Bag<Component> movingHorizontalMultiSentry(float x, float y){

        ComponentBag bag = fixedMultiSentry(x,y);

        Random random = new Random();
        if(random.nextBoolean()) {
            bag.add(new VelocityComponent(300, 0));
        } else {
            bag.add(new VelocityComponent(-300, 0));
        }

        bag.add(new BounceComponent());

        return bag;
    }





}
